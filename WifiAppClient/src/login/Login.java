package login;

import client.ClientForm;

import java.awt.EventQueue;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import java.awt.Color;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField user;
	private JPasswordField pass;
	public static String userPublic = "";
	public static String passPublic = "";
	public static String macPublic = "";
	public static String user1stPublic = "";
	private JTextField user1st;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 539, 376);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 153, 51));
		contentPane.setBackground(new Color(204, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setForeground(new Color(0, 51, 153));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 26));
		lblNewLabel.setBounds(10, 11, 304, 35);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_1.setForeground(new Color(0, 51, 153));
		lblNewLabel_1.setBounds(20, 57, 102, 35);
		contentPane.add(lblNewLabel_1);
		
		user = new JTextField();
		user.setForeground(new Color(0, 51, 153));
		user.setBackground(new Color(204, 204, 255));
		user.setBounds(10, 90, 203, 35);
		contentPane.add(user);
		user.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setForeground(new Color(0, 51, 153));
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(20, 141, 102, 35);
		contentPane.add(lblNewLabel_1_1);
		
		pass = new JPasswordField();
		pass.setForeground(new Color(0, 51, 153));
		pass.setBackground(new Color(204, 204, 255));
		pass.setBounds(10, 172, 203, 35);
		contentPane.add(pass);
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnNewButton.setForeground(new Color(0, 51, 153));
		btnNewButton.setBackground(new Color(204, 204, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					userPublic = user.getText();
					passPublic = pass.getText().toString();
				}catch(Exception ex) {
					
				}
	
				
				//

				InetAddress localhost = null;
				try {
					localhost = InetAddress.getLocalHost();
				} catch (UnknownHostException e2) {
					e2.printStackTrace();
				}
				
				//generate mac address
				String address = null;
		        try {

		            NetworkInterface network = NetworkInterface.getByInetAddress(localhost);
		            byte[] mac = network.getHardwareAddress();

		            StringBuilder sb = new StringBuilder();
		            for (int i = 0; i < mac.length; i++) {
		                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		            }
		            address = sb.toString();

		        } catch (SocketException ex) {
		            ex.printStackTrace();
		        }

		        macPublic = address;
				user1stPublic = user1st.getText();
				
				
					ClientForm client = new ClientForm();
					try {
						ClientForm.main(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
			}
		});
		btnNewButton.setBounds(103, 242, 110, 35);
		contentPane.add(btnNewButton);  
		
		user1st = new JTextField();
		user1st.setBounds(330, 97, 86, 20);
		contentPane.add(user1st);
		user1st.setColumns(10);

		JLabel test = new JLabel("Mac");
		test.setBounds(285, 230, 203, 35);
		contentPane.add(test);

		
		JButton btnNewButton_1 = new JButton("New login");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//

//				InetAddress localhost = null;
//				try {
//					localhost = InetAddress.getLocalHost();
//				} catch (UnknownHostException e2) {
//					e2.printStackTrace();
//				}
//				
//				//generate mac address
//				String address = null;
//		        try {
//
//		            NetworkInterface network = NetworkInterface.getByInetAddress(localhost);
//		            byte[] mac = network.getHardwareAddress();
//
//		            StringBuilder sb = new StringBuilder();
//		            for (int i = 0; i < mac.length; i++) {
//		                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//		            }
//		            address = sb.toString();
//
//		        } catch (SocketException ex) {
//		            ex.printStackTrace();
//		        }
//				macPublic = address;
//				user1stPublic = user1st.getText();
//				
//				test.setText(macPublic);
//				
//				ClientForm c = new ClientForm();
//				try {
//					ClientForm.main(null);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
		});
		btnNewButton_1.setBounds(330, 148, 89, 23);
		contentPane.add(btnNewButton_1);
	}
}
