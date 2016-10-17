package org.dcn.aldous.query.services.query;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DescriptionParserTest {

  @Test
  public void testParse() {
    Description description1 = new DescriptionParser().parse("notebook Samsung 8GB");
    assertThat(description1.tags()).contains("notebook");
    assertThat(description1.vendor()).isEqualTo("Samsung");
    assertThat(description1.properties()).containsExactly("8GB");

    Description description2 = new DescriptionParser().parse("red socks size 42");
    assertThat(description2.tags()).containsExactly("socks");
    assertThat(description2.properties()).contains("size 42", "red");

    Description description3 = new DescriptionParser().parse("Casio CDP 120");
    assertThat(description3.name()).isEqualTo("CDP 120");
    assertThat(description3.vendor()).isEqualTo("Casio");
  }

}