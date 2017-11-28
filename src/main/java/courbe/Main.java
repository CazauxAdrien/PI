package courbe;

import java.util.List;
import java.util.stream.Collectors;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Connexion_Cassandra q = new Connexion_Cassandra();
		q.connect("127.0.0.1",9042);
		Session session=q.getSession();
		String keyspaceName = "library";
		//q.createKeyspace(keyspaceName, "SimpleStrategy", 1);
		//q.createTable("books");
		ResultSet result =session.execute("SELECT * FROM library.books;");
			 
			   List<String> matchedKeyspaces = result.all()
			      .stream()
			      .filter(r -> r.getString(0).equals(keyspaceName.toLowerCase()))
			      .map(r -> r.getString(0))
			      .collect(Collectors.toList());
			 
			   System.out.println(matchedKeyspaces);
		
		List<String> columnNames = 
			      result.getColumnDefinitions().asList().stream()
			      .map(cl -> cl.getName())
			      .collect(Collectors.toList());
		System.out.println(columnNames);
		
		
		
		q.close();*/
		
		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
		System.out.println(influxDB.ping());
		
	}

}
