package PublicClass;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	static String dbip = java.util.ResourceBundle.getBundle("db").getString("dbip");    
	static String dbname=java.util.ResourceBundle.getBundle("db").getString("dbname");
	static String username=java.util.ResourceBundle.getBundle("db").getString("username");
	static String password=java.util.ResourceBundle.getBundle("db").getString("password");
	
	public static Connection getConnnection() {
	    // �������ӵĶ���
	    Connection connection = null;
	    try {
	        // ����ȡ������Ϣ�洢�����ӵĶ�������
	    	Class.forName("com.mysql.jdbc.Driver");
	    	String url = "jdbc:mysql://"+dbip+"/"+dbname;
			connection = DriverManager.getConnection(url, username, password);
	    }
	    catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
		} 
	    catch (SQLException e) {
	        System.out.println("��ȡ����ʧ��" + e.getMessage());
	    }
	    // �������ӵĶ���
	    return connection;
	}
}
