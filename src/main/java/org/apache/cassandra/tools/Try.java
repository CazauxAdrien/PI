package org.apache.cassandra.tools;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.cassandra.dht.Murmur3Partitioner;
import org.apache.cassandra.exceptions.InvalidRequestException;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class Try {

	 public  void SST(String KeySpace, String TABLE_NAME, String PrimaryKey, String column1, String column2,int n) throws IOException, BulkLoadException{
	    	
	    	StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
	  	          .append(KeySpace+"."+TABLE_NAME).append("(")
	  	          .append("N text PRIMARY KEY, ")
	  	          .append(PrimaryKey+" int, ")
	  	          .append(column1+" text,")
	  	          .append(column2+" text);");
	  	
	  	File DataDir = new File("/home/adrien/Output");
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
	  	
	      BufferedReader reader = new BufferedReader(new FileReader("/home/adrien/Test.csv")); 
	      CsvListReader csvReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
	  	 
	  	List<String> line;
			try {
				while ((line = csvReader.read()) != null)
 	{
				// We use Java types here based on 
				// https://www.datastax.com/drivers/java/2.0/com/datastax/driver/core/DataType.Name.html#asJavaClass%28%29 
				writer.addRow(line.get(0),
						Integer.parseInt(line.get(1)),
						line.get(2),
						line.get(3));
				
 	}
			} catch (InvalidRequestException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	writer.close();
	  	csvReader.close();
	  	
	  	
	  	InetAddress addr = InetAddress.getByName("127.0.0.1");
	  	
	  	//LoaderOptions z = LoaderOptions.builder().parseArgs(args).build();
	  	LoaderOptions LO = LoaderOptions.builder().directory(DataDir).host(addr).connectionsPerHost(2).nativePort(9042).build();

	  	BulkLoader.load(LO);
	 }
	  	
	  	
}
