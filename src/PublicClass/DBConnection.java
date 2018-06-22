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
 * 数据库连接池类，用于维护数据库连接
 */
public class DBConnection {
	 private static DataSource ds = null;
	    
	    static{
	        try{
	        	//根据<Resource>元素的name属性值到JNDI容器中检索连接池对象       
	            Context initCtx = new InitialContext();
	            Context envCtx = (Context)initCtx.lookup("java:comp/env");
	            ds = (DataSource) envCtx.lookup("safacampusdbpool");    
	        }catch (Exception e) {
	            throw new ExceptionInInitializerError(e);
	        }
	    }
	    //用于获取数据库连接
	    public static Connection getConnection() throws SQLException {
	        return ds.getConnection();  //利用数据源获取连接
	    }
	    //归还数据库连接
	    public static void CloseConnection(Connection conn) {
	    	//关闭连接
	        if(conn!=null) {
	            try{
	                conn.close();
	            }catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
}
