package courbe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
    public XYSeries series;
    public XYSeries series1;
    
    public XYSeriesCollection DatasetTest;
   
    public void connect(String node, Integer port) {
    	DatasetTest = new XYSeriesCollection();
    	series = new XYSeries("Import");
    	series1 = new XYSeries("Query");
    	PoolingOptions poolingOptions = new PoolingOptions();
    	poolingOptions
        .setConnectionsPerHost(HostDistance.LOCAL,  4, 21)
        .setConnectionsPerHost(HostDistance.REMOTE, 2, 21);

        Builder b = Cluster.builder().addContactPoint(node).withPoolingOptions(poolingOptions);
        if (port != null) {
            b.withPort(port);
        }
        
        cluster = b.build();
        //System.out.println(cluster.getMetadata().getKeyspaces());
        //System.out.println(cluster.getMetadata().exportSchemaAsString());
        session = cluster.connect();
        //System.out.println(session.toString());
        
        
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
    
    public void createTable(String KeySpace, String TABLE_NAME, String PrimaryKey, String column1, String column2) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
          .append(KeySpace+"."+TABLE_NAME).append("(")
          .append("N uuid PRIMARY KEY, ")
          .append(PrimaryKey+" int, ")
          .append(column1+" text,")
          .append(column2+" text);");
     
        String query = sb.toString();
        System.out.println(query);
        session.execute(query);
        
        
    }
    
    public void insertMasse(String KeySpace, String TABLE_NAME, String PrimaryKey, String column1, String column2,int n){
    	
    	for(int i=0;i<n;i++){
    		for(int a=0;a<17280;a++){
	    		StringBuilder sb = new StringBuilder("INSERT INTO "+KeySpace+"."+TABLE_NAME+ "( N ,"+PrimaryKey+ ","+ column1+","+ column2+") ")
	    				.append("VALUES ( ")
	    				.append(UUID.randomUUID()+" , ")
	    				.append(i+" , '")
	    				.append(generatedate(a)+"' , '")
	    				.append(randomConso()+"'")
	    				.append(" );");
	    		String query = sb.toString();
	    		
	    		long bfins = System.currentTimeMillis();
			    session.execute(query);
			    long afins = System.currentTimeMillis()-bfins;

			    series.add(n*a,afins+0.0001);
    		}
    	}
    	DatasetTest.addSeries(series);

    }
    public String generatedate(int n){
    	DateTime t = new DateTime(2017,1,1,0,0);
    	t=t.plusMinutes(30*n);
    	return t.toString(DateTimeFormat.shortDateTime());
		
    	
    }
    
    public String randomConso(){
    	char[] chars = "2345789".toCharArray();
    	StringBuilder sb = new StringBuilder();
    	sb.append("0,0");
    	Random random = new Random();
    	for (int i = 0; i < 5; i++) {
    	    char c = chars[random.nextInt(chars.length)];
    	    sb.append(c);
    	}
    	String output = sb.toString();
    	return output;
    }
    
    
    
    public class TestThread extends Thread{
    	Session session;
    	String KeyS;
    	String TABLE_N;
    	int nbclients;
    	int nb ;
    	int trac;
    	int nbconnexions;
    	public TestThread(String KeySpace, String TABLE_NAME,Session name,int clients, int trace, int nbconnex){
    		KeyS=KeySpace;
        	TABLE_N=TABLE_NAME;
    		session=name;
    		nbclients=clients;
    		nb= (int) (Math.random()*(clients-1));
    		trac = trace;
    		nbconnexions=nbconnex;
    		
    	  }

    	  public void run(){
    		  long bfquer = System.currentTimeMillis();
    		  ResultSet result =this.session.execute("SELECT * FROM "+KeyS+"."+TABLE_N+" WHERE ID="+nb+" ALLOW FILTERING;");
    		  
			  
			  long afquer = System.currentTimeMillis()-bfquer;
			  series1.add(nbconnexions*trac,afquer);
    		   List<Row> matchedKeyspaces = result.all();
    		      
    		 
    		   
    	  }  
    }
    
    public void acces_concurrent(String KeySpace, String TABLE_NAME,int n, int clients){
    	Cluster cluster = connectbis("127.0.0.1",9042);
    	Session session=cluster.connect();
    	int trace = (int) (clients*17280)/(n-1);
    	List<TestThread> ThreadList = new ArrayList<TestThread>();
    	for (int i=0;i<n;i++){
    		ThreadList.add(new TestThread(KeySpace, TABLE_NAME,session, clients, trace , i ));
		}
    	for (int i=0;i<ThreadList.size();i++){
    		ThreadList.get(i).start();
		}
    	System.out.println(series1);
    	DatasetTest.addSeries(series1);
    }
    
    
    
    
    
    
    

    
}
