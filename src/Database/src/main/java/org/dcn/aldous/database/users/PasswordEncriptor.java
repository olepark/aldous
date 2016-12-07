package org.dcn.aldous.database.users;

public class PasswordEncriptor {

  public String encrypt(String password) {
    return password.toLowerCase();
  }
}
