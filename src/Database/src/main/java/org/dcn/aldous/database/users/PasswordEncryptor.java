package org.dcn.aldous.database.users;

public class PasswordEncryptor {

  public String encrypt(String password) {
    return new StringBuilder(password).reverse().toString();
  }
}
