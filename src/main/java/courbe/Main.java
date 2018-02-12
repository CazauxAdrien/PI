package courbe;

import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Scanner;
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

	/*public static void main(String[] args) throws InterruptedException {
		
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the name of the bdd: Cassandra or InfluxDB ");

	    String BDD = scanner.next();
	    if(!BDD.isEmpty() & (BDD.matches("Cassandra") | BDD.matches("InfluxDB"))){
	    	
	    }
	    System.out.print("Enter the IP: ");

	    String IP = scanner.next();
		
		
		
		}*/

}
