package courbe;

import java.util.ArrayList;
import java.util.LinkedList;
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
    public String IP;
    public int Port;
    public XYSeriesCollection DatasetTest;
   
    public void connect(String node, Integer port) {
    	DatasetTest = new XYSeriesCollection();
    	series = new XYSeries("Import");
    	series1 = new XYSeries("Query");
    	IP=node;
    	Port=port;
    	PoolingOptions poolingOptions = new PoolingOptions();
    	poolingOptions
        .setConnectionsPerHost(HostDistance.LOCAL,  4, 21)
        .setConnectionsPerHost(HostDistance.REMOTE, 2, 21);

        Builder b = Cluster.builder().addContactPoint(node).withPoolingOptions(poolingOptions);
        if (port != null) {
            b.withPort(port);
        }
        
        setCluster(b.build());
        session = getCluster().connect();
        
        
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
    	
    	long sum;
    	for(int i=0;i<n;i++){
    		sum=0;
    		for(int a=0;a<17280;a++){
    			ValuesConso conso= new ValuesConso(a);
	    		StringBuilder sb = new StringBuilder("INSERT INTO "+KeySpace+"."+TABLE_NAME+ "( N ,"+PrimaryKey+ ","+ column1+","+ column2+") ")
	    				.append("VALUES ( ")
	    				.append(UUID.randomUUID()+" , ")
	    				.append(i+" , '")
	    				.append(conso.getDate()+"' , '")
	    				.append(conso.getConso()+"'")
	    				.append(" );");
	    		String query = sb.toString();
	    		
	    		long bfins = System.currentTimeMillis();
			    session.execute(query);
			    long afins = System.currentTimeMillis()-bfins;
			    sum=sum+afins;
			     
    		}
    		double mean = sum*1.0/17280L;
    		series.add(i, mean);
    		System.out.println(mean);
    	}
    	DatasetTest.addSeries(series);

    }
    
    
    
    
    public void acces_concurrent(String KeySpace, String TABLE_NAME,int n, int clients){
    	List<TestThread> ThreadList = new ArrayList<TestThread>();
    	UseObjects u = new UseObjects(this);
    	
    	u.conc.set_All(KeySpace, TABLE_NAME, n, clients, this);
    	
    	
    	for (int i=0;i<n;i++){
    		ThreadList.add(new TestThread(u,i));
    		//ThreadList.add(new TestThread(KeySpace, TABLE_NAME,cluster, i,clients,this));
		}
    	for (int i=0;i<ThreadList.size();i++){
    		ThreadList.get(i).start();
		}
    	DatasetTest.addSeries(series1);
    }

    
   
	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
    
    
	public Session getSession() {
        return this.session;
    }
 
    public void close() {
        session.close();
        getCluster().close();
    }
    
    
    
    
    

    
}
