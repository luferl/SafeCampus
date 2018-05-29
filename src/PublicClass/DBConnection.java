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
	//根据<Resource>元素的name属性值到JNDI容器中检索连接池对象        
	        }catch (Exception e) {
	            throw new ExceptionInInitializerError(e);
	        }
	    }
	    
	    public static Connection getConnection() throws SQLException {
	        return ds.getConnection();  //利用数据源获取连接
	    }
	    
	    public static void CloseConnection(Connection conn) {
	    	
	        if(conn!=null) {
	            try{
	                conn.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
