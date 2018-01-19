package courbe;

import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class TestThread extends Thread{
	
	UseObjects u;
	int nbTh;
	
	public TestThread(UseObjects use,int i){
		this.u=use;
		this.nbTh=i;
	}
	

	  public void run(){

		 
		  if (u.coni == null){
			  int nb = (int) (Math.random()*(u.conc.clients-1));
			  long bfquer = System.currentTimeMillis();	  
			  Session session = u.conc.co.getCluster().newSession();
			  //ResultSet result =this.session.execute("SELECT * FROM "+KeyS+"."+TABLE_N+" WHERE ID="+nb+" ALLOW FILTERING;");
			  ResultSet result =session.execute("SELECT * FROM "+u.conc.KeyS+"."+u.conc.TABLE_N+" WHERE ID="+nb+" ALLOW FILTERING;");
			  long afquer = System.currentTimeMillis()-bfquer;
			  
			  u.conc.co.series1.add(nbTh,afquer);
			  u.conc.co.getSession().close();
		  }
		  else{	 
			  long bfquer = System.currentTimeMillis();	  
			  Query query = new Query("SELECT * FROM consommation", u.coni.Database_N);
	          QueryResult resultinf = u.coni.co.influxDB.query(query);
	          long afquer = System.currentTimeMillis()-bfquer;
			  
			  u.coni.co.series1.add(nbTh,afquer);
			  u.coni.co.influxDB.close();
	          
		  }
		   
	  }  
}