import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
	
	public static void write(List<String> list) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File("test.csv"));
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<list.size()/2;i++){
			sb.append(list.get(i*3)+';'+list.get(3*i+1)+list.get(3*i+2)+'\n');
		}
		pw.write(sb.toString());
        pw.close();
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		List<String> list = new LinkedList<String>();
		for (int i=0;i<60;i++){
			list.add(randomnb());
		}
		
		write(list);
		
	}
	
}
