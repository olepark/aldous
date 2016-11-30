package org.dcn.aldous.crawler.services.site;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.Item;

import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Subscription {

  private final List<Consumer<Item>> subscribers;

  public void broadcast(Item item) {
    subscribers.forEach(consumer -> consumer.accept(item));
  }

  public void addSubscriber(Consumer<Item> subscriber) {
    subscribers.add(subscriber);
  }

  static Subscription create() {
    return new Subscription(newArrayList());
  }
}
