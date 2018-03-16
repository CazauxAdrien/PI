package Interface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import org.influxdb.InfluxDB;

import courbe.Connexion_Cassandra;
import courbe.Connexion_influxDB;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow(args);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow(String[] args) {

		frame = new JFrame();
		frame.setBounds(200, 200, 750, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCassandra = new JLabel("Cassandra:");
		lblCassandra.setBounds(12, 29, 110, 27);
		frame.getContentPane().add(lblCassandra);
		
		textField = new JTextField();
		textField.setBounds(138, 26, 254, 34);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(459, 26, 248, 33);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Connexion_Cassandra q = new Connexion_Cassandra();
				try{
					String IP=textField.getText();
					int port = Integer.parseInt(textField_1.getText());
					
					long a = System.currentTimeMillis();
					q.connect(IP, port);
					a=System.currentTimeMillis() - a;
					System.out.println("Connexion time:" + a);
					
					
					frame.dispose();
					
					
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, "Erreur de connexion");
				}
				Cassandra cassandra = new Cassandra(q);
				cassandra.setVisible(true);
				
				
			}
		});
		btnConnexion.setBounds(331, 72, 117, 25);
		frame.getContentPane().add(btnConnexion);
		
		JLabel lblInfluxdb = new JLabel("InfluxDB");
		lblInfluxdb.setBounds(12, 179, 110, 27);
		frame.getContentPane().add(lblInfluxdb);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(138, 176, 254, 34);
		frame.getContentPane().add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(459, 176, 248, 33);
		frame.getContentPane().add(textField_3);
		
		JButton button = new JButton("Connexion");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Connexion_influxDB inf = new Connexion_influxDB();
				try{
					String IP=textField_2.getText();
					int port = Integer.parseInt(textField_3.getText());	
					inf.connect_influx(IP, port);
					frame.dispose();
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, "Erreur de connexion");
				}
				
				InfluxTest influxtest = new InfluxTest(inf);
				influxtest.setVisible(true);
			}
		});
		button.setBounds(331, 222, 117, 25);
		frame.getContentPane().add(button);
	}
}
