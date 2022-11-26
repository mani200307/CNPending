package ServerFile;
import java.awt.HeadlessException;
import java.io.*;
import java.sql.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import javax.swing.JOptionPane;

import ServerGUI.ServerGUI;

//ClientHandler class
class ClientHandler extends Thread {
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	static String userNameVal;
	static String passVal;
	static String dateInVal;
	static String onlyDate;
	static String macAddr;
	String res;
	String userArray;
	String passArray;
	Statement st;
	Connection con;
	
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, String res) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		this.res = res;
	}
	
	@Override
	public void run() {
	
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
	    System.out.println("System IP Address : " + (localhost.getHostAddress()).trim());
		
	    String systemipaddress =  (localhost.getHostAddress()).trim();
	
		int cCnt = 0;
		String res = "";
		for(int i=0;i<systemipaddress.length();i++)
		{
			if(systemipaddress.charAt(i) == '.')
				cCnt++;
			if(cCnt == 3)
				break;
			res += systemipaddress.charAt(i);
		}

		//get mac from user

		String origMac = null;
		String user1st = null;
		String macAd = null;
		try {
			origMac = dis.readUTF();
			user1st = dis.readUTF();
			macAd = dis.readUTF();
			JOptionPane.showMessageDialog(null, macAd);
			JOptionPane.showMessageDialog(null, "Line 76");

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Line 76");

//			e2.printStackTrace();
		}

//		JOptionPane.showMessageDialog(null, "orig: " + origMac + "\n" + "user: " + user1st + "\n macA: " + macA);
		
	    systemipaddress = res;
	    
		String iprcv = "";
		try {
			iprcv = dis.readUTF(); // receive data from client
		} catch (Exception e) {
			System.out.println(e);
		}

		if(!user1st.equals(null))
		{
			try {
				if(!isMacAlready(user1st, macAd))
				{
					String r = "Mac already there !" + macAd + "!";
					JOptionPane.showMessageDialog(null, r);
				}
			} catch (SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			return;
		}
			
		try 
		{
			
			if(systemipaddress.equals(iprcv))
			{
				if(isMacMatched(user1st, origMac))
					System.out.println("matched");
				else {
					JOptionPane.showMessageDialog(null, "Unable to connect! (Try login desired machine)");
					return;
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Unable to connect! (Try login desired wifi)");
				return;
			}			
		} catch (HeadlessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//Message testing. Getting it
		
		userArray = "";
		passArray = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connectAttend();
			String sql = "Select UserName from login";
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				userArray += rs.getString(1) + " ";
			}
			
			sql = "Select Password from login";
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				passArray += rs.getString(1) + " ";
			}
			
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		String strUser[] = userArray.split(" ");
		String strPass[] = passArray.split(" ");		
		
		userNameVal = "";
		passVal = "";
		dateInVal = "";
		onlyDate = "";
		try {
			userNameVal = dis.readUTF();
			passVal = dis.readUTF();
			dateInVal = dis.readUTF();
			onlyDate = dis.readUTF();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//user credentials check
		String flagMatch = "false";
		String cnt = "";
		boolean val = false;
		for(int i=0;i<strUser.length;i++)
		{
			if(strUser[i].equals(userNameVal))
			{
				if(strPass[i].equals(passVal))
				{
					try {
						val = checkedAlready(userNameVal);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					flagMatch = "true";
//					if()
					try {
						if(!val)
							cnt = incrCnt(strUser[i]);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		try {
			dos.writeUTF(flagMatch);
			dos.writeUTF(cnt);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			removeDates(onlyDate);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			dos.writeUTF(val+"");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(flagMatch.equals("true"))
		{
			//already entered
			try {
				if(val)
				{
					return;
				}				
			} catch (Exception e) {
			}
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connectAttend();
				String sql = "INSERT INTO `attendData`(`UserName`, `EntryTime`, `DateIn`) VALUES ('"+userNameVal.toString()+"','"+dateInVal.toString()+"','"+onlyDate.toString()+"')";
				st.executeUpdate(sql);
				res += "User Name : "+ userNameVal + "Time : "+ dateInVal + "\n";
				con.close();
				ServerGUI.table_load();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		
		try {
			// closing resources
			this.dis.close();
			this.dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean isMacAlready(String user, String mac) throws SQLException {
		if(user.equals(null))
			return false;
		connectAttend();
		Statement st = con.createStatement();
		String sql = "Select MacAddr from macTable";
		ResultSet rs = st.executeQuery(sql);		
		String macArray = "";
		while(rs.next()) {
			macArray += rs.getString(1) + " ";
		}
		String strMac[] = macArray.split(" ");
		
		int flag = 0;
		for(int i=0;i<strMac.length;i++)
		{
			//already registered
			if(strMac[i].equals(mac))
			{
				flag = 1;
				break;
			}
		}

		//if new mac? Then, add into database
		if(flag == 0)
		{
			sql = "INSERT INTO `macTable`(`UserName`, `MacAddr`) VALUES ('"+user.toString()+"','"+mac.toString()+"')";
			st.executeUpdate(sql);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Machine already registered!");
//			return;
		}
		
		return false;
	}

	private boolean isMacMatched(String user, String mac) throws SQLException {
				
		connectAttend();
		Statement st = con.createStatement();
		String sql = "Select MacAddr from macTable";
		ResultSet rs = st.executeQuery(sql);		
		String macArray = "";
		while(rs.next()) {
			macArray += rs.getString(1) + " ";
		}
		String strMac[] = macArray.split(" ");

		sql = "Select UserName from macTable";
		rs = st.executeQuery(sql);		
		String usrArray = "";
		while(rs.next()) {
			usrArray += rs.getString(1) + " ";
		}
		String strUser[] = usrArray.split(" ");
		
		for(int i=0;i<strMac.length;i++)
		{
			//already registered
			if(strMac[i].equals(mac))
			{
				if(strUser[i].equals(user))
					return true;
				return false;
			}
		}
		
		return false;
	}

	private void removeDates(String dateVal) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		connectAttend();
		String sql = "DELETE FROM `attendData` WHERE NOT `DateIn` = '"+dateVal+"'";
		st.executeUpdate(sql);
	}

	public void connectAttend() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wifidb","root","");
		st = con.createStatement();
	}
	
	private String incrCnt(String userName) throws SQLException {
		connectAttend();
		Statement st = con.createStatement();
		String sql = "UPDATE presentCnt SET AttendCnt = AttendCnt+1 WHERE UserName='"+userName+"'";
		st.executeUpdate(sql);
		sql = "SELECT AttendCnt FROM presentCnt WHERE UserName='"+userName+"'";
		ResultSet rs = st.executeQuery(sql);
		int cnt = 0;
		while(rs.next()) {
			cnt = rs.getInt(1);
		}
		
		String resCnt = cnt + "";
		
		return resCnt;
	}
	
	private boolean checkedAlready(String userName) throws SQLException {
		connectAttend();
		Statement st = con.createStatement();
		String sql = "Select UserName, EntryTime from attendData";
		ResultSet rs = st.executeQuery(sql);
		String uName = new String();
		String entryT = new String();
		while(rs.next()) {
			uName = rs.getString(1);
			entryT = rs.getString(2);		
			if(uName.equals(userName) && dateCheckEqual(dateInVal, entryT)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean dateCheckEqual(String d1, String d2) {
		String[] dt1 = d1.split(" "); 
		String[] dt2 = d2.split(" ");
		if(dt1[0].equals(dt2[0]))
			return true;
		return false;
	}
	
//	public static String retMac()
//	{
//		JOptionPane.showMessageDialog(null, "Mac address: " + macAddr);
//		System.out.println("Called");
//		return macAddr;
//	}
}


public class ServerFile extends JFrame {

	public ServerFile() {
	}		

	
	public static void main(String[] args) throws IOException {
				
		// server is listening on port 5056
		ServerSocket ss = new ServerSocket(5056);
		
		// running infinite loop for getting
		// client request
		while (true) {
			Socket s = null;

			try {
				// socket object to receive incoming client requests
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
				
				// obtaining input and out streams
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				System.out.println("Assigning new thread for this client");				
				
				// create a new thread object
				
				ClientHandler t = new ClientHandler(s, dis, dos, "");
				
				// Invoking the start() method
				t.start();
				t.join();
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}