import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;

import courbe.ValuesConso;

public class Création_csv {

	
	public static String randomnb(){
    	char[] chars = "789".toCharArray();
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
	
	public static void write(List<Object> list) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File("test.csv"));
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<list.size()/3;i++){
			pw.print(list.get(i*3));
			pw.print(';');
			pw.print(list.get(3*i+1));
			pw.print(';');
			pw.print(list.get(3*i+2));
			pw.println();
		}
		//pw.write(sb.toString());
		
        pw.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream storeObject = new ObjectOutputStream(stream);
		List<Object> list = new LinkedList<Object>();
		for (int i=0;i<(17280);i++){
			ValuesConso v = new ValuesConso(i);
			v.writeExternal(storeObject);
	        storeObject.flush();
	        storeObject.close();
	        list.add(UUID.randomUUID());
	        list.add(1);
			list.add(stream.toByteArray());
		}
		
		write(list);*/
		 Builder b = Cluster.builder().addContactPoint("127.0.0.1");
		 Integer port =9042;
	        if (port != null) {
	            b.withPort(port);
	        }
	        Cluster cluster = b.build();
	        Session session = cluster.connect();
	        Statement smt = new SimpleStatement("COPY test.tabtest(N,ID,Time,Value) FROM 'Export.csv'");
	        ResultSetFuture e = session.executeAsync(smt);
	        System.out.println(e.getUninterruptibly().getAllExecutionInfo().toString());
	}
	
}
