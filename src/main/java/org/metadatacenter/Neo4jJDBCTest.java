package org.metadatacenter;

import org.junit.Test;

import java.sql.*;

public class Neo4jJDBCTest {


  @Test
  public void test1() {
    try {
      Class.forName("org.neo4j.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    Connection con = null;
    try {
      con = DriverManager.getConnection("jdbc:neo4j:root//localhost:7474/");
      try (Statement stmt = con.createStatement()) {
        ResultSet rs = stmt.executeQuery("MATCH (n:User) RETURN n.name");
        while (rs.next()) {
          System.out.println(rs.getString("n.name"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

}
