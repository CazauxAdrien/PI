package Interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import courbe.Connexion_Cassandra;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Scrollbar;
import javax.swing.ScrollPaneConstants;

public class Cassandra extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Cassandra frame = new Cassandra();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Cassandra(Connexion_Cassandra q) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 1600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestAuto = new JLabel("Test Auto");
		lblTestAuto.setBounds(173, 34, 70, 15);
		contentPane.add(lblTestAuto);
		
		JLabel lblKeyspace = new JLabel("Keyspace");
		lblKeyspace.setBounds(28, 101, 70, 15);
		contentPane.add(lblKeyspace);
		
		textField = new JTextField();
		textField.setBounds(259, 101, 114, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblTable = new JLabel("Table");
		lblTable.setBounds(28, 409, 70, 15);
		contentPane.add(lblTable);
		
		textField_1 = new JTextField();
		textField_1.setBounds(259, 409, 114, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNbClients = new JLabel("Nb Clients");
		lblNbClients.setBounds(28, 482, 114, 15);
		contentPane.add(lblNbClients);
		
		textField_2 = new JTextField();
		textField_2.setBounds(259, 482, 114, 19);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nb Connexion");
		lblNewLabel.setBounds(29, 549, 137, 15);
		contentPane.add(lblNewLabel);
		
		textField_3 = new JTextField();
		textField_3.setBounds(259, 549, 114, 19);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnLancer = new JButton("Lancer");
		btnLancer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int clients = Integer.parseInt(textField_2.getText());
				int connex = Integer.parseInt(textField_3.getText());
				System.out.println(clients);
				
				q.createKeyspace(textField.getText(), textField_4.getText(), Integer.parseInt(textField_5.getText()));
				
				q.createTable(textField.getText(),textField_1.getText(),"ID","Time","Value");
				
				q.insertMasse(textField.getText(),textField_1.getText(),"ID","Time","Value", clients);
				
				q.acces_concurrent(textField.getText(),textField_1.getText(),connex,clients);
				
				System.out.println("End of proces");
				
				SwingUtilities.invokeLater(() -> {
				      Cassandra_plot example = new Cassandra_plot("Line Chart Example",q.DatasetTest);
				      example.setAlwaysOnTop(true);
				      example.pack();
				      example.setSize(1600, 900);
				      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				      example.setVisible(true);
				});
			dispose();
			}
		});
		btnLancer.setBounds(173, 590, 117, 25);
		contentPane.add(btnLancer);
		
		JLabel lblReplication = new JLabel("Replication  Stratégie");
		lblReplication.setBounds(28, 211, 168, 15);
		contentPane.add(lblReplication);
		
		JLabel lblReplicationFactor = new JLabel("Replication Factor");
		lblReplicationFactor.setBounds(28, 295, 168, 15);
		contentPane.add(lblReplicationFactor);
		
		textField_4 = new JTextField();
		textField_4.setBounds(259, 209, 114, 19);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(259, 293, 114, 19);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		System.out.println(q.getCluster().getMetadata().getKeyspaces().toString());
		System.out.println(q.getCluster().getMetadata().exportSchemaAsString());
		System.out.println(q.getCluster().getMetadata().getAllHosts());

		JTextPane textPane = new JTextPane();
		textPane.setBounds(420, 43, 828, 704);
		textPane.setText(q.getCluster().getMetadata().exportSchemaAsString()+
				q.getCluster().getMetadata().getAllHosts());
		
		
		JScrollPane jsp = new JScrollPane(textPane);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(420, 43, 1028, 704);
		
		contentPane.add(jsp);
		
		
		
		
	}
}
