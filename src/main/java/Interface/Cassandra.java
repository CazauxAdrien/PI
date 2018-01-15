package Interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import courbe.Connexion_Cassandra;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Cassandra extends JFrame {

	private JPanel contentPane;
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
		setBounds(200, 200, 500, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTestAuto = new JLabel("Test Auto");
		lblTestAuto.setBounds(96, 44, 70, 15);
		contentPane.add(lblTestAuto);
		
		JLabel lblKeyspace = new JLabel("Keyspace");
		lblKeyspace.setBounds(28, 101, 70, 15);
		contentPane.add(lblKeyspace);
		
		textField = new JTextField();
		textField.setBounds(155, 99, 114, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblTable = new JLabel("Table");
		lblTable.setBounds(28, 173, 70, 15);
		contentPane.add(lblTable);
		
		textField_1 = new JTextField();
		textField_1.setBounds(155, 171, 114, 19);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
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
				q.createKeyspace(textField.getText(), "SimpleStrategy", 1);
				
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
		btnLancer.setBounds(96, 377, 117, 25);
		contentPane.add(btnLancer);
		
		
		
		
		
	}
}
