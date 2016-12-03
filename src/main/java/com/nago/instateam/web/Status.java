package com.nago.instateam.web;

public enum Status {
  NOT_STARTED("Not started"),
  ACTIVE("Active"),
  ARCHIVED("Archived");

  private String name;

  Status(String name){
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
