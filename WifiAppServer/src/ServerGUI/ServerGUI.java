package ServerGUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import java.awt.Font;

import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

import ServerFile.ServerFile;

public class ServerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserName;
	private JTextField txtPass;
	private static JTable table;
	Connection con;
	static Statement st;
	static ResultSet rs;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		try {
			ServerFile.main(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerGUI() {
		initialise();
		connect();
		table_load();
	}
		
	public void initialise() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 685, 434);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Attendance Report");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(262, 11, 216, 29);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Insert Login Data", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 51, 258, 136);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(10, 51, 82, 14);
		panel.add(lblNewLabel_1);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(116, 49, 112, 20);
		panel.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_2.setForeground(new Color(0, 0, 0));
		lblNewLabel_2.setBounds(10, 105, 66, 14);
		panel.add(lblNewLabel_2);
		
		txtPass = new JTextField();
		txtPass.setBounds(116, 103, 112, 20);
		panel.add(txtPass);
		txtPass.setColumns(10);
		
		JButton addLoginBtn = new JButton("Add Login");
		addLoginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				String userName = txtUserName.getText();
				String pass = txtPass.getText();
				try {
//					String macAddr = ServerFile.getMac();
//					JOptionPane.showMessageDialog(null, "Mac address: " + macAddr);
					st = con.createStatement();
					String sql = "insert into login(UserName, Password) values('"+userName.toString()+"','"+pass.toString()+"')";
					st.executeUpdate(sql);
					sql = "INSERT INTO presentCnt(UserName, AttendCnt) VALUES('"+userName.toString()+"','"+0+"')";
					st.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "Data inserted");
					txtUserName.setText("");
					txtPass.setText("");
					txtUserName.requestFocus();
				}
				catch(Exception ex) {
					System.out.println(ex);
				}
			}
		});
		addLoginBtn.setBounds(76, 197, 114, 23);
		contentPane.add(addLoginBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(307, 51, 352, 318);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
	
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifidb","root","");
			st = con.createStatement();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
		
	public static void table_load() {
		try {
			String sql = "select UserName, EntryTime from attendData";
			rs = st.executeQuery(sql);
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception e) {
		}
	}	
}
