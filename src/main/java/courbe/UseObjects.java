package courbe;


import com.datastax.driver.core.Session;

public class UseObjects {

	public Cassandra_acces_Object conc; 
	public Influx_acces_Object  coni;
	
	
	
	public UseObjects(Connexion_Cassandra conca){
		this.conc= new Cassandra_acces_Object(conca);
	}
	
	public UseObjects(Connexion_influxDB coninf){
		this.coni= new Influx_acces_Object(coninf);
	}
	
	 public class Cassandra_acces_Object{
	    	Session session;
	    	String KeyS;
	    	String TABLE_N;
	    	int thre;
	    	int clients;
	    	Connexion_Cassandra co;
	    	
	    	public Cassandra_acces_Object(Connexion_Cassandra c){
	    		this.co=c;
	    	}
	    	
	    	
	    	public void set_All( String keyS, String tABLE_N, int thre, int clients,
					Connexion_Cassandra co) {
				KeyS = keyS;
				TABLE_N = tABLE_N;
				this.thre = thre;
				this.clients = clients;
				this.co = co;
			}
	    	
	    	
			public int getClients() {
				return clients;
			}
			
			public Session getSession() {
				return session;
			}
			
			public String getKeyS() {
				return KeyS;
			}
			
			public String getTABLE_N() {
				return TABLE_N;
			}
			
			public int getThre() {
				return thre;
			}
			public Connexion_Cassandra getCo() {
				return co;
			}
			public void setCo(Connexion_Cassandra co) {
				this.co = co;
			}
	    	
	 }
	 
	//InfluxAccesObject
	 public class Influx_acces_Object{
		 String Database_N;
	    	int thre;
	    	int clients;
	    	Connexion_influxDB co;
	    	
	    	public Influx_acces_Object(Connexion_influxDB c){
	    		this.co=c;
	    	}
	    	
	    	
	    	public void set_All( String Database_N, int thre, int clients,
	    			Connexion_influxDB co) {
				this.Database_N=Database_N;
				this.thre = thre;
				this.clients = clients;
				this.co = co;
			}
	    	
	    	
			public int getClients() {
				return clients;
			}
			
			
			public String getDatabase_N() {
				return Database_N;
			}
			
			public int getThre() {
				return thre;
			}
			public Connexion_influxDB getCo() {
				return co;
			}
			public void setCo(Connexion_influxDB co) {
				this.co = co;
			}
	    	
	 }
	 
	    
}
