package courbe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.cassandra.dht.Murmur3Partitioner;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;
import org.apache.cassandra.tools.Try;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;
import org.joda.time.base.AbstractDateTime;
import org.joda.time.format.DateTimeFormat;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;

public class Connexion_Cassandra {

	private Cluster cluster;
    private Session session;
    public XYSeries series;
    public XYSeries seriesbatch;
    public XYSeries seriesAsync;
    public XYSeries series1; 
    public String IP;
    public int Port;
    public XYSeriesCollection DatasetTest;
   
    public void connect(String node, Integer port) {
    	DatasetTest = new XYSeriesCollection();
    	series = new XYSeries("Import");
    	seriesbatch=new XYSeries("ImportBatch");
    	seriesAsync=new XYSeries("ImportAsync");
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
    
    public synchronized void insertMasse(String KeySpace, String TABLE_NAME, String PrimaryKey, String column1, String column2,int n) throws Exception{
    	
    	long sum;
    	long oneyear = System.currentTimeMillis();
    	
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
			    session.executeAsync(query);
			    long afins = System.currentTimeMillis()-bfins;
			    sum=sum+afins;
			     
    		}
    		double mean = sum*1.0/17280L;
    		series.add(i, mean);
    	}
    	oneyear = System.currentTimeMillis()-oneyear;
    	System.out.println("Normal "+oneyear);
    	
    	DatasetTest.addSeries(series);
    	/*
    	//2nd method
    	oneyear = System.currentTimeMillis();
    	TestCassandraSerialization t = new TestCassandraSerialization(this.cluster,this.session);
    	for(int i=0;i<n;i++){
    		long afins = 0;
    		long sum1 = 0;
    		for(int a=0;a<17280;a++){
    			ValuesConso conso= new ValuesConso(a);
    			t.insertIntoTable(KeySpace, TABLE_NAME, PrimaryKey, column1, column2, conso, i);
    			
    		}
		    
			seriesbatch.add(i+n, sum1*1.0/17280L);
	        //System.out.println(afins*1.0/17280L);
	    	}
    	oneyear = System.currentTimeMillis()-oneyear;
    	System.out.println("Serial"+oneyear);
    	DatasetTest.addSeries(seriesbatch);
    	*/
    	//3rd method
    	oneyear = System.currentTimeMillis();
    	
    	double M=0;
    	for(int i=0;i<n;i++){
    		sum=0;
    		for(int a=0;a<17280;a++){
    			ValuesConso conso= new ValuesConso(a);
	    		Statement sb = new SimpleStatement("INSERT INTO "+KeySpace+"."+TABLE_NAME+ "( N ,"+PrimaryKey+ ","+ column1+","+ column2+") VALUES ("
	    				+ " "+UUID.randomUUID()+" , "+i+2*n+" , '"+conso.getDate()+"' , '"+conso.getConso()+"'"+" );");
	    				
	    		
	    		long bfins = System.currentTimeMillis();
			    session.executeAsync(sb);
			    long afins = System.currentTimeMillis()-bfins;
			    sum=sum+afins;
			     
    		}
    		double mean = sum*1.0/17280L;
    		//System.out.println(mean);
    		M=M+mean;
    		seriesAsync.add(i+n, mean);
    	}
    	//System.out.println("Async"+M/n);
    	oneyear = System.currentTimeMillis()-oneyear;
    	System.out.println("3rd "+oneyear);
    	DatasetTest.addSeries(seriesAsync);
    	/*
    	//4th Thread Async
    	
    	oneyear = System.currentTimeMillis();
    	
    	com.datastax.driver.core.PreparedStatement pst =
	    session.prepare("INSERT INTO "+KeySpace+"."+TABLE_NAME+ "( N ,"+PrimaryKey+ ","+ column1+","+ column2+") VALUES (?, ?, ?, ?)");
	    ThreadPool TP = new ThreadPool(4,500);
    	for(int i=0;i<n;i++){
    		
    		//sum=0;
    		for(int a=0;a<17280;a++){
    			
	    		InsertRun IR= new InsertRun(cluster,n,a,i,pst);
    			TP.execute(IR);
    		}
    	}
    	
    	oneyear = System.currentTimeMillis()-oneyear;
    	System.out.println("thread "+oneyear);
    	DatasetTest.addSeries(seriesAsync);
    	*/
    	
    	
    	//Try a = new Try();
    	//a.SST(KeySpace, TABLE_NAME, PrimaryKey, column1, column2, n);
    	
    	/*
    	StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
    	          .append(KeySpace+"."+TABLE_NAME).append("(")
    	          .append("N uuid PRIMARY KEY, ")
    	          .append(PrimaryKey+" int, ")
    	          .append(column1+" text,")
    	          .append(column2+" text);");
    	
    	File DataDir = new File("Output");
    	String InsertStatement = "INSERT INTO "+KeySpace+"."+TABLE_NAME+ "( N ,"+PrimaryKey+ ","+ column1+","+ column2+") VALUES (?, ?, ?, ?)";
    	
    	// Prepare SSTable writer 
    	CQLSSTableWriter.Builder builder = CQLSSTableWriter.builder();
    	
		// set output directory 
    	builder.inDirectory(DataDir)
    	       // set target schema 
    	       .forTable(sb.toString())
    	       // set CQL statement to put data 
    	       .using(InsertStatement)
    	       // set partitioner if needed 
    	       // default is Murmur3Partitioner so set if you use different one. 
    	       .withPartitioner(new Murmur3Partitioner());
    	CQLSSTableWriter writer = builder.build();
    	
        BufferedReader reader = new BufferedReader(new FileReader(CSV_URL)); 
        CsvListReader csvReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
    	 
    	List<String> line;
		while ((line = csvReader.read()) != null)
    	{
    	    // We use Java types here based on 
    	    // https://www.datastax.com/drivers/java/2.0/com/datastax/driver/core/DataType.Name.html#asJavaClass%28%29 
    	    writer.addRow();
    	    
    	}
    	writer.close();
    	*/
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	  	
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
