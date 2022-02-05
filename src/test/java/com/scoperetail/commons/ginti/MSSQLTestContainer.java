package com.scoperetail.commons.ginti;

import org.testcontainers.containers.MSSQLServerContainer;

public class MSSQLTestContainer extends MSSQLServerContainer {

  private static final String IMAGE_VERSION = "mcr.microsoft.com/mssql/server:2019-latest";
  private static MSSQLTestContainer container;

  private MSSQLTestContainer() {
    super(IMAGE_VERSION);
  }

  public static MSSQLTestContainer getInstance() {
    if (container == null) {
      container = new MSSQLTestContainer();
      container.acceptLicense();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
    System.setProperty("DB_URL", container.getJdbcUrl());
    System.setProperty("DB_USERNAME", container.getUsername());
    System.setProperty("DB_PASSWORD", container.getPassword());
    System.out.println("*********" + container.getJdbcUrl());
  }

  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
