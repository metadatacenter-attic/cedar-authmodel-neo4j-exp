package org.metadatacenter;

import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.RelationshipIndex;
import org.neo4j.jdbc.Neo4jConnection;
import org.neo4j.test.ImpermanentGraphDatabase;

import java.io.File;
import java.sql.*;

public class Neo4jEmbeddedTest {


  @Test
  public void test1() {


    File neo4jdatabase = new File("neo4jdatabase");
    System.out.println(neo4jdatabase.getAbsolutePath());
    RelationshipType SUBFOLDER_OF = DynamicRelationshipType.withName("SUBFOLDER_OF");
    GraphDatabaseService service = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(neo4jdatabase)
        .newGraphDatabase();
    Transaction transaction = service.beginTx();
    try {
      Index<Node> folders = service.index().forNodes("Folders");
      RelationshipIndex relations = service.index().forRelationships("relations");


      String stanfordUid = "folder:stanford";
      Node stanford = service.createNode();
      stanford.setProperty("unique_id", stanfordUid);
      stanford.setProperty("name", "Stanford");
      folders.add(stanford, "unique_id", stanfordUid);

      String bmirUid = "folder:bmir";
      Node bmir = service.createNode();
      bmir.setProperty("unique_id", bmirUid);
      bmir.setProperty("name", "BMIR");
      folders.add(bmir, "unique_id", bmirUid);

      Relationship bmirIsSubfolderOfStanford = bmir.createRelationshipTo(stanford, SUBFOLDER_OF);
      bmirIsSubfolderOfStanford.setProperty("foo", "bar");

      String relId = "bmir:so:stanford";
      relations.add(bmirIsSubfolderOfStanford, "type", bmirIsSubfolderOfStanford.getType().name());

      IndexHits<Relationship> hits1 = relations.get("type", null, bmir, stanford);
      System.out.println(hits1.size());
      IndexHits<Relationship> hits2 = relations.get("type", null, stanford, bmir);
      System.out.println(hits2.size());

      transaction.success();
    } finally {
      transaction.close();
    }
    service.shutdown();
  }


}
