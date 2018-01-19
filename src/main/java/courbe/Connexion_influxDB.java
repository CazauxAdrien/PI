package courbe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;



public class Connexion_influxDB {
	public InfluxDB influxDB;
	
	public XYSeries series;
    public XYSeries series1;
    
    public XYSeriesCollection DatasetTest;
    
    DateTime t = new DateTime(2017,1,1,0,0);
    long timelong = t.getMillis();
    double max = 0.23;
    double min = 0.03;
    
    
    public void connect_influx(String IP, int port){
    	 influxDB = InfluxDBFactory.connect("http://" + IP + ":" + port, "root", "root");
    	 Pong response = this.influxDB.ping();
    	 System.out.println(response);
    	 DatasetTest = new XYSeriesCollection();
     	 series = new XYSeries("Import");
     	 series1 = new XYSeries("Query");
    }
    public void createDatabase(String dbName, String rpName){
    	
		influxDB.deleteDatabase(dbName);
		influxDB.dropRetentionPolicy(rpName, dbName);
		
    	influxDB.createDatabase(dbName);
		influxDB.setDatabase(dbName);
		
		influxDB.createRetentionPolicy(rpName, dbName, "1825d", "30m", 2, true);
		influxDB.setRetentionPolicy(rpName);
    }
    
	 public void insertMasse(String dbName, String rpName,String Grandeur_ref, String valeur, int n){
		 BatchPoints batchPoints = BatchPoints.database(dbName).retentionPolicy(rpName).build();
	        for(int i=0;i<n;i++){
	        	for(int j=0;j<12;j++){
	        		for(int a=0;a<1440;a++){ // BatchPoint contient la consommation d'un mois 
	        			double random = min + Math.random() * (max - min);
	        			Point point1 = Point
	        					.measurement("consommation")
	        					.tag(Grandeur_ref, "352447")
	        					.time(timelong+a*1000*60*30, TimeUnit.MILLISECONDS)
	        					.addField(valeur, random)
	        					.build(); 
	        			batchPoints.point(point1);
	        			System.out.println(point1.getTags());        
	        		}
	        		long bfins = System.currentTimeMillis();
		            this.influxDB.write(batchPoints);
				    long afins = System.currentTimeMillis()-bfins;

				    series.add(n*i,afins+0.0001);
	            }
	            
	            
	        }
	        DatasetTest.addSeries(series);
	    }
	 
	
	
	public class TestThread extends Thread{
	    	int nbclients;
	    	int nb ;
	    	int trac;
	    	int nbconnexions;
	    	
	    	public TestThread(int clients, int trace, int nbconnex){
	    		nbclients=clients;
	    		nb= (int) (Math.random()*(clients-1));
	    		trac = trace;
	    		nbconnexions=nbconnex;
	    		
	    	  }
	 }
	 
	 public void acces_concurrent(int n, int clients){	
	    	int trace = (int) (clients*17280)/(n-1);
	    	List<TestThread> ThreadList = new ArrayList<TestThread>();
	    	for (int i=0;i<n;i++){
	    		ThreadList.add(new TestThread(clients, trace , i ));
			}
	    	for (int i=0;i<ThreadList.size();i++){
	    		ThreadList.get(i).start();
			}
	    	System.out.println(series1);
	    	DatasetTest.addSeries(series1);
	    }
 
}
