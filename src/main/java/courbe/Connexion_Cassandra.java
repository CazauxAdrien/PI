package courbe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class Connexion_Cassandra {

	private Cluster cluster;
	 
    private Session session;
 
    
    public void connect(String node, Integer port) {
    	PoolingOptions poolingOptions = new PoolingOptions();
    	poolingOptions
        .setConnectionsPerHost(HostDistance.LOCAL,  4, 21)
        .setConnectionsPerHost(HostDistance.REMOTE, 2, 21);

        Builder b = Cluster.builder().addContactPoint(node).withPoolingOptions(poolingOptions);
        if (port != null) {
            b.withPort(port);
        }
        
        cluster = b.build();
        
        session = cluster.connect();
        
    }
    
    public Cluster connectbis(String node, Integer port) {
    	PoolingOptions poolingOptions = new PoolingOptions();
    	poolingOptions
        .setConnectionsPerHost(HostDistance.LOCAL,  4, 21)
        .setConnectionsPerHost(HostDistance.REMOTE, 2, 21);

        Builder b = Cluster.builder().addContactPoint(node).withPoolingOptions(poolingOptions);
        if (port != null) {
            b.withPort(port);
        }
        
        cluster = b.build();
        return cluster;
        
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
    
    public void insertMasse(int n){
    	
    	for(int i=0;i<n;i++){
    		StringBuilder sb = new StringBuilder("INSERT INTO library.books ( id , subject , title ) ")
    				.append("VALUES ( ")
    				.append(UUID.randomUUID().toString()+" , '")
    				.append(randomString()+"' , '")
    				.append(randomString()+"'")
    				.append(" );");
    		String query = sb.toString();
		    session.execute(query);
    	}
    }
    
    public String randomString(){
    	char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    	StringBuilder sb = new StringBuilder();
    	Random random = new Random();
    	for (int i = 0; i < 20; i++) {
    	    char c = chars[random.nextInt(chars.length)];
    	    sb.append(c);
    	}
    	String output = sb.toString();
    	return output;
    }
    
    
    
    public class TestThread extends Thread{
    	Session session;
    	public TestThread(Session name){
    		
    		session=name;
    	  }

    	  public void run(){

    		  ResultSet result =this.session.execute("SELECT * FROM library.books;");
    			
    		   List<Row> matchedKeyspaces = result.all();
    		      
    		 
    		   //System.out.println(matchedKeyspaces);
    		   
    	  }  
    }
    
    public void acces_concurrent(int n){
    	Cluster cluster = connectbis("127.0.0.1",9042);
    	Session session=cluster.connect();
    	List<TestThread> ThreadList = new ArrayList<TestThread>();
    	for (int i=0;i<n;i++){
    		ThreadList.add(new TestThread(session));
		}
    	for (int i=0;i<ThreadList.size();i++){
    		ThreadList.get(i).start();
		}
    }
    
}
