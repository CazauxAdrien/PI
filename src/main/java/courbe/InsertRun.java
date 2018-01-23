package courbe;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;

public class InsertRun implements Runnable{

	int users;
	ValuesConso conso;
	PreparedStatement ps;
	int us;
	Cluster clu;
	
	public InsertRun(Cluster cluster, int cli, int nb,int i, PreparedStatement pst){
		this.users=cli;
		this.conso= new ValuesConso(nb);
		this.ps=pst;
		this.us=i;
		this.clu=cluster;
	}
	@Override
	public void run() {
		clu.newSession().execute(ps.bind(UUID.randomUUID(),us+users, conso.getDate(), conso.getConso()));
		System.out.println("COucou");
	}

}
