package courbe;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

public class Connexion_Cassandra {

	private Cluster cluster;
	 
    private Session session;
 
    public void connect(String node, Integer port) {
        Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
        System.out.println(cluster);
        session = cluster.connect();
        System.out.println(session);
    }
 
    public Session getSession() {
        return this.session;
    }
 
    public void close() {
        session.close();
        cluster.close();
    }
    
    public void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
    		  StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
    		      .append(keyspaceName).append(" WITH replication = {")
    		      .append("'class':'").append(replicationStrategy)
    		      .append("','replication_factor':").append(replicationFactor)
    		      .append("};");
    		         
    		    String query = sb.toString();
    		    session.execute(query);
    		}
    public void createTable(String TABLE_NAME) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
          .append("library."+TABLE_NAME).append("(")
          .append("id uuid PRIMARY KEY, ")
          .append("title text,")
          .append("subject text);");
     
        String query = sb.toString();
        System.out.println(query);
        session.execute(query);
    }
    
}
