package courbe;
import java.nio.ByteBuffer;
import java.lang.Object;
import org.apache.commons.lang3.SerializationUtils;
import com.datastax.driver.core.utils.Bytes;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class TestCassandraSerialization {
	private Cluster cluster;
    private Session session;

    public TestCassandraSerialization(String node) {
        connect(node);
    }

    private void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to %s\n", metadata.getClusterName());
        for (Host host: metadata.getAllHosts()) {
              System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                         host.getDatacenter(), host.getAddress(), host.getRack());
        }
        session = cluster.connect();
    }

    public void setUp() {
        session.execute("CREATE KEYSPACE test_serialization WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");

        session.execute("CREATE TABLE test_serialization.test_table (id text PRIMARY KEY, data blob)");
    }

    public void tearDown() {
        session.execute("DROP KEYSPACE test_serialization");
    }

    public void insertIntoTable(String key, byte[] data) {
        PreparedStatement statement = session.prepare("INSERT INTO test_serialization.test_table (id,data) VALUES (?, ?)");
        BoundStatement boundStatement = new BoundStatement(statement);
        session.execute(boundStatement.bind(key,ByteBuffer.wrap(data)));
    }

    public byte[] readFromTable(String key) {
        String q1 = "SELECT * FROM test_serialization.test_table WHERE id = '"+key+"';";

        ResultSet results = session.execute(q1);
        for (Row row : results) {
            ByteBuffer data = row.getBytes("data");
            byte[] result = new byte[data.remaining()];
            
            System.out.println(result);
            return data.array();
        }
        return null;
    }


    public static boolean compareByteArrays(byte[] one, byte[] two) {
        if (one.length > two.length) {
            byte[] foo = one;
            one = two;
            two = foo;
        }

        // so now two is definitely the longer array    
        for (int i=0; i<one.length; i++) {
            //System.out.printf("%d: %s\t%s\n", i, one[i], two[i]);
            if (one[i] != two[i]) {
                return false;
            }
        }
        return true;
    }


    /*public static void main(String[] args) {
        TestCassandraSerialization tester = new TestCassandraSerialization("localhost");

        try {
            tester.setUp();
            byte[] dataIn = new byte[]{1,2,3};
            tester.insertIntoTable("123", dataIn);
            byte[] dataOut = tester.readFromTable("123");

            //System.out.println(com.datastax.driver.core.utils.Bytes.toHexString(dataIn));
            //System.out.println(dataOut);

            //System.out.println(dataIn.length); 
            //System.out.println(dataOut.length); 

            //System.out.println(compareByteArrays(dataIn, dataOut));        

            String toSave = "Hello, world!";
            dataIn = SerializationUtils.serialize(toSave);
            tester.insertIntoTable("toSave", dataIn);
            dataOut = tester.readFromTable("toSave");

            //System.out.println(dataIn.length); 
            //System.out.println(dataOut.length);


            // The below throws org.apache.commons.lang.SerializationException: java.io.StreamCorruptedException: invalid stream header: 81000008
            String hasLoaded = (String) SerializationUtils.deserialize(dataOut); 
            System.out.println(hasLoaded);

        } finally {
            //tester.tearDown();
        }
    }*/
}
