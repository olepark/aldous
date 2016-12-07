package org.dcn.aldous.database.users;

import lombok.Data;

import java.security.Principal;

@Data
public class AldousUser implements Principal {
  private final String name;
}
