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
	    // 创建连接的对象
	    Connection connection = null;
	    try {
	        // 将获取到的信息存储到链接的对象里面
	    	Class.forName("com.mysql.jdbc.Driver");
	    	String url = "jdbc:mysql://"+dbip+"/"+dbname;
			connection = DriverManager.getConnection(url, username, password);
	    }
	    catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
		} 
	    catch (SQLException e) {
	        System.out.println("获取连接失败" + e.getMessage());
	    }
	    // 返回连接的对象
	    return connection;
	}
}
