package Interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import courbe.Connexion_Cassandra;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Scrollbar;
import javax.swing.ScrollPaneConstants;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;

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
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestAuto = new JLabel("Test Auto");
		lblTestAuto.setBounds(173, 34, 70, 15);
		contentPane.add(lblTestAuto);
		
		JLabel lblKeyspace = new JLabel("Keyspace");
		lblKeyspace.setBounds(52, 101, 70, 15);
		contentPane.add(lblKeyspace);
		
		textField = new JTextField();
		textField.setBounds(28, 128, 114, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblTable = new JLabel("Table");
		lblTable.setBounds(285, 173, 70, 15);
		contentPane.add(lblTable);
		
		textField_1 = new JTextField();
		textField_1.setBounds(259, 200, 114, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNbClients = new JLabel("Nb Clients");
		lblNbClients.setBounds(28, 249, 114, 15);
		contentPane.add(lblNbClients);
		
		textField_2 = new JTextField();
		textField_2.setBounds(28, 292, 114, 19);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nb Connexion");
		lblNewLabel.setBounds(259, 249, 137, 15);
		contentPane.add(lblNewLabel);
		
		textField_3 = new JTextField();
		textField_3.setBounds(259, 292, 114, 19);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblReplication = new JLabel("Replication  Stratégie");
		lblReplication.setBounds(234, 101, 168, 15);
		contentPane.add(lblReplication);
		
		JLabel lblReplicationFactor = new JLabel("Replication Factor");
		lblReplicationFactor.setBounds(27, 173, 168, 15);
		contentPane.add(lblReplicationFactor);
		
		textField_4 = new JTextField();
		textField_4.setBounds(259, 128, 114, 19);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(28, 200, 114, 19);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		

		JTextArea textArea = new JTextArea();
		textArea.setRows(5);
		textArea.setBounds(22, 388, 374, 113);
		textArea.setLineWrap(true);
		contentPane.add(textArea);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setBounds(420, 43, 828, 704);
		
		
		
		JScrollPane jsp_1 = new JScrollPane(textPane_1);
		jsp_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp_1.setBounds(420, 430, 1103, 370);
		
		contentPane.add(jsp_1);
		
		
		JButton btnExecuter = new JButton("Executer");
		btnExecuter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ResultSet result=q.getCluster().newSession().execute(textArea.getText());
				List<Row> matchedKeyspaces = result.all();
				textPane_1.setText(matchedKeyspaces.toString());
			}
		});
		

		JTextPane textPane = new JTextPane();
		textPane.setBounds(420, 43, 828, 704);
		textPane.setText(q.getCluster().getMetadata().exportSchemaAsString()+
				q.getCluster().getMetadata().getAllHosts());
		
		
		JScrollPane jsp = new JScrollPane(textPane);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setBounds(420, 43, 1103, 370);
		
		contentPane.add(jsp);
		btnExecuter.setBounds(146, 548, 117, 25);
		contentPane.add(btnExecuter);
		
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
				
				System.out.println("End of process");
				
				SwingUtilities.invokeLater(() -> {
				      Cassandra_plot example = new Cassandra_plot("Line Chart Example",q.DatasetTest);
				      example.setAlwaysOnTop(true);
				      example.pack();
				      example.setSize(1600, 900);
				      example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				      example.setVisible(true);
				});
				textPane.setText(q.getCluster().getMetadata().exportSchemaAsString()+
						q.getCluster().getMetadata().getAllHosts());
			}
		});
		btnLancer.setBounds(146, 338, 117, 25);
		contentPane.add(btnLancer);
		
		
		
		
		
		
		
	}
}
