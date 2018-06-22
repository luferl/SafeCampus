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

/*
 * ���ݿ����ӳ��࣬����ά�����ݿ�����
 */
public class DBConnection {
	 private static DataSource ds = null;
	    
	    static{
	        try{
	        	//����<Resource>Ԫ�ص�name����ֵ��JNDI�����м������ӳض���       
	            Context initCtx = new InitialContext();
	            Context envCtx = (Context)initCtx.lookup("java:comp/env");
	            ds = (DataSource) envCtx.lookup("safacampusdbpool");    
	        }catch (Exception e) {
	            throw new ExceptionInInitializerError(e);
	        }
	    }
	    //���ڻ�ȡ���ݿ�����
	    public static Connection getConnection() throws SQLException {
	        return ds.getConnection();  //��������Դ��ȡ����
	    }
	    //�黹���ݿ�����
	    public static void CloseConnection(Connection conn) {
	    	//�ر�����
	        if(conn!=null) {
	            try{
	                conn.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
