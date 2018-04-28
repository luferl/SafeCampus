package PublicClass;

import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

import com.mysql.jdbc.Statement;

public class DBConnection {
	 private static DataSource ds = null;
	    
	    static{
	        try{

	            Context initCtx = new InitialContext();
	            Context envCtx = (Context)initCtx.lookup("java:comp/env");
	            ds = (DataSource) envCtx.lookup("safacampusdbpool");    
	//����<Resource>Ԫ�ص�name����ֵ��JNDI�����м������ӳض���        
	        }catch (Exception e) {
	            throw new ExceptionInInitializerError(e);
	        }
	    }
	    
	    public static Connection getConnection() throws SQLException {
	        return ds.getConnection();  //��������Դ��ȡ����
	    }
	    
	    public static void CloseConnection(Connection conn) {
	    	/*
	        if(rs!=null) {
	            try{
	                rs.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        if(st!=null) {
	            try{
	                st.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        */
	        if(conn!=null) {
	            try{
	                conn.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
	
	
	/*
	static String dbip = java.util.ResourceBundle.getBundle("db").getString("dbip");    
	static String dbname=java.util.ResourceBundle.getBundle("db").getString("dbname");
	static String username=java.util.ResourceBundle.getBundle("db").getString("username");
	static String password=java.util.ResourceBundle.getBundle("db").getString("password");
	private static DataSource ds = null; 
	static{  

        try{  

            //����dbcp.properties�����ļ�  

            InputStream in =DBConnection.class.getClassLoader().getResourceAsStream("dbcp.properties");  

            Properties prop = new Properties();  

            prop.load(in);  

            //��������Դ  

            ds =BasicDataSourceFactory.createDataSource(prop);  

        }catch (Exception e) {  

           e.printStackTrace();

        }  

    } 
	
	
	
	
	
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
	}*/
