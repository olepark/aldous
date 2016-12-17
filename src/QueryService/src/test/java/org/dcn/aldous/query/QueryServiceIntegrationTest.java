package org.dcn.aldous.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.net.HostAndPort;
import io.dropwizard.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.dcn.aldous.database.items.Item;
import org.dcn.aldous.database.lists.ItemList;
import org.dcn.aldous.query.services.rest.AuthenticationRest;
import org.dcn.aldous.query.services.rest.ListsRest;
import org.dcn.aldous.query.services.rest.MatchingItemsRest;
import org.dcn.aldous.query.services.rest.UsersRest;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.Path;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class QueryServiceIntegrationTest {

  private final HostAndPort hostAndPort = HostAndPort.fromParts("localhost", 8074);

  @ClassRule
  public static final DropwizardAppRule<Configuration> RULE =
      new DropwizardAppRule<>(QueryServiceApplication.class, ResourceHelpers.resourceFilePath("rest.yml"));

  @Test
  public void testAppMainScenario() throws IOException {
    String username = "olepark";
    String password = "pass";
    Form newUser = new Form()
        .param("name", "Oleg Parkhomenko")
        .param("username", username)
        .param("password", password);
    Response add = newRequest(UsersRest.class, "add")
        .post(Entity.form(newUser));
    assertThat(add.getStatus()).isEqualTo(200);
    Integer userId = add.readEntity(Integer.class);

    Form authForm = new Form()
        .param("login", username)
        .param("password", password);
    Response auth = newRequest(AuthenticationRest.class, "byPassword")
        .post(Entity.form(authForm));
    assertThat(auth.getStatus()).isEqualTo(200);
    NewCookie session = auth.getCookies().getOrDefault(AuthenticationRest.ALDOUS_SESSION, null);
    assertThat(session).isNotNull();

    String sessionId = session.getValue();
    String listName = String.format("List %.0f", Math.random() * 100);
    Integer listId = newAuthorizedRequest(ListsRest.class, "addList",
        params("name", listName), sessionId)
        .get(Integer.class);

    String jsonBody = newAuthorizedRequest(MatchingItemsRest.class, "getMatchingItems",
        params("query", "Samsung"), sessionId)
        .get(String.class);
    ObjectMapper mapper = new ObjectMapper();
    TypeFactory typeFactory = mapper.getTypeFactory();
    ArrayType type = typeFactory.constructArrayType(Item.class);
    List<Item> items = Arrays.asList(mapper.readValue(jsonBody, type));

    Integer itemId1 = addRandomItem(sessionId, listId, items);
    Integer itemId2 = addRandomItem(sessionId, listId, items);
    Integer itemId3 = addRandomItem(sessionId, listId, items);

    ItemList list = newAuthorizedRequest(ListsRest.class, "getList",
        params("listId", listId), sessionId)
        .get(ItemList.class);

    assertThat(list.getId()).isEqualTo(listId);
    assertThat(list.getName()).isEqualTo(listName);
    assertThat(list.getOwnerId()).isEqualTo(userId);
    assertThat(list.getItemIds()).contains(itemId1, itemId2, itemId3);
  }

  protected Integer addRandomItem(String sessionId, Integer listId, List<Item> items) {
    Integer itemId = items.get((int) (Math.random() * items.size())).id();
    newAuthorizedRequest(ListsRest.class, "addItem",
        params("listId", listId, "itemId", itemId), sessionId)
        .get(Integer.class);
    return itemId;
  }

  private Map<String, Object> params(String name, Object o) {
    Map<String, Object> map = newHashMap();
    map.put(name, o);
    return map;
  }

  private Map<String, Object> params(String name1, Object o1, String name2, Object o2) {
    Map<String, Object> map = newHashMap();
    map.put(name1, o1);
    map.put(name2, o2);
    return map;
  }

  private Invocation.Builder newAuthorizedRequest(Class<?> restClass,
                                                  String method,
                                                  Map<String, Object> params,
                                                  String sessionId) {
    return newRequest(restClass, method, params).header(HttpHeaders.AUTHORIZATION, "Bearer " + sessionId);
  }

  private Invocation.Builder newRequest(Class<?> restClass, String method) {
    return newRequest(restClass, method, newHashMap());
  }

  private Invocation.Builder newRequest(Class<?> restClass, String method, Map<String, Object> params) {
    Client client = ClientBuilder.newBuilder()
        .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE)
        .build();
    WebTarget target = client.target("http://" + hostAndPort.toString())
        .path(path(restClass, method));
    for (Map.Entry<String, Object> param : params.entrySet()) {
      target = target.queryParam(param.getKey(), param.getValue());
    }
    return target.request();
  }

  private String path(Class<?> restClass, String method) {
    return format("%s/%s", path(restClass), method);
  }

  private String path(Class<?> restClass) {
    return restClass.getAnnotation(Path.class).value();
  }

}
