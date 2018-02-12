package courbe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;


public class ValuesConso implements SerializationExterne{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6715701598812397844L;
	private String conso;
	private String date;
	
	public ValuesConso(int n){
		this.conso=randomConso();
		this.date=generatedate(n);
	}

	public String getConso() {
		return conso;
	}

	public void setConso(String conso) {
		this.conso = conso;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String generatedate(int n){
    	DateTime t = new DateTime(2017,1,1,0,0);
    	t=t.plusMinutes(30*n);
    	return t.toString(DateTimeFormat.shortDateTime());
		
    	
    }
    
    public String randomConso(){
    	char[] chars = "2345789".toCharArray();
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

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(date);
		out.writeObject(conso);
		
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		date =(String) in.readObject();
		conso =(String) in.readObject();
		
		
	}
}
