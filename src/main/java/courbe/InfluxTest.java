package courbe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


import Interface.Influxdb_plot;


public class InfluxTest extends JFrame{
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_3;

	/*Connexion_influxDB influxDB;
	String rpName = "aRetentionPolicy";
	String dbName = "Efluid";

	public static void main(String[] args) throws InterruptedException {
		
		
                InfluxDB influxDB = InfluxDBFactory.connect("http://" + TestUtils.getInfluxIP() + ":" + TestUtils.getInfluxPORT(true), "root", "root");
				System.out.println(TestUtils.getInfluxIP()+":"+TestUtils.getInfluxPORT(true));
                String dbName = "Efluid";
				String rpName = "aRetentionPolicy";
				
		        
				//influxDB.createDatabase(dbName);
				//influxDB.setDatabase(dbName);
				
				//influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
				//influxDB.setRetentionPolicy(rpName);
			    Connexion_influxDB inf = new Connexion_influxDB();
			    inf.influxDB=influxDB;
			    inf.insertMasse(dbName,rpName,"Grandeur_ref", "date","valeur",1);

				// Flush every 2000 Points, at least every 100ms
				//influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
				Query query = new Query("SELECT * FROM consommation", dbName);
	           
	            QueryResult result = influxDB.query(query);
	            System.out.println(result);
	           // influxDB.dropRetentionPolicy(rpName, dbName);
	           // influxDB.deleteDatabase(dbName);
	}*/
	
	public InfluxTest(Connexion_influxDB c) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 500, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestAuto = new JLabel("Test Auto");
		lblTestAuto.setBounds(96, 44, 70, 15);
		contentPane.add(lblTestAuto);
		
		JLabel lblKeyspace = new JLabel("Database");
		lblKeyspace.setBounds(28, 101, 70, 15);
		contentPane.add(lblKeyspace);
		
		textField = new JTextField();
		textField.setBounds(155, 99, 114, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		JLabel lblNbClients = new JLabel("Nb Clients");
		lblNbClients.setBounds(28, 242, 114, 15);
		contentPane.add(lblNbClients);
		
		textField_2 = new JTextField();
		textField_2.setBounds(155, 240, 114, 19);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nb Connexion");
		lblNewLabel.setBounds(28, 299, 137, 15);
		contentPane.add(lblNewLabel);
		
		textField_3 = new JTextField();
		textField_3.setBounds(155, 297, 114, 19);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnLancer = new JButton("Lancer");
		btnLancer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int clients = Integer.parseInt(textField_2.getText());
				int connex = Integer.parseInt(textField_3.getText());
				System.out.println(clients);
				
				
				
				c.createDatabase(textField.getText(),"aRetentionPolicy");
				
				c.insertMasse(textField.getText(),"aRetentionPolicy","Grandeur_ref","Valeur", clients);
				
				c.acces_concurrent(connex,clients);
				
				System.out.println("End of proces");
				
				SwingUtilities.invokeLater(() -> {
				      Influxdb_plot plot = new Influxdb_plot("Line Chart Example",c.DatasetTest);
				      plot.setAlwaysOnTop(true);
				      plot.pack();
				      plot.setSize(1600, 900);
				      plot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				      plot.setVisible(true);
				});
			dispose();
			}
		});
		btnLancer.setBounds(96, 377, 117, 25);
		contentPane.add(btnLancer);
		
		
		
		
		
	}
			




}
