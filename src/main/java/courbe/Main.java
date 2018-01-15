package courbe;

import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Connexion_Cassandra q = new Connexion_Cassandra();
		q.connect("127.0.0.1",9042);
		Session session=q.getSession();
		
		String keyspaceName = "library";
		//q.createKeyspace(keyspaceName, "SimpleStrategy", 1);
		//q.createTable("books");
		
		//q.insertMasse(100);
		
		/*ResultSet result =session.execute("SELECT * FROM library.books;");
		
			   List<Row> matchedKeyspaces = result.all();
			      ;
			 
			   System.out.println(matchedKeyspaces);*/
		long a =System.currentTimeMillis();
		//q.acces_concurrent(90);
		long b =System.currentTimeMillis()-a;
		System.out.println(b);
		
		System.out.println(q.generatedate(10));
		q.close();
		
		/*InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086");
		System.out.println(influxDB.ping());
		//influxDB.createDatabase("library");
		influxDB.setDatabase("library");
		Query query1 = new Query("ALTER RETENTION POLICY T ON library DURATION 2m REPLICATION 1", "library");
		influxDB.query(query1);
		influxDB.setRetentionPolicy("Test");
		BatchPoints batchPoints = BatchPoints
						.database("library")
						.tag("async", "true")
						.retentionPolicy("Test")
						.consistency(InfluxDB.ConsistencyLevel.ALL)
						.build();
		Point point1 = Point.measurement("cpu")
							.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
							.addField("idle", 90L)
							.addField("user", 9L)
							.addField("system", 1L)
							.build();
		Point point2 = Point.measurement("disk")
							.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
							.addField("used", 80L)
							.addField("free", 1L)
							.build();
		batchPoints.point(point1);
		batchPoints.point(point2);
		influxDB.write("library", "autogen", point1);
		//influxDB.write(batchPoints);
		Query query = new Query("SELECT idle FROM cpu", "library");
		influxDB.query(query);*/
		System.exit(0);
		}

}
