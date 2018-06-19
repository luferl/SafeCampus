package wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PublicClass.DBConnection;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class GetDirectories
 */
@WebServlet("/wechat/CallBack")
public class CallBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallBack() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String code=request.getParameter("code");
		DBConnection dbc=new DBConnection();
		String access_token = ""; 
		String line="";
	    String openid = "";  
		try {
			Connection connection = dbc.getConnection();
			String sql="SELECT AppID,AppSecret FROM wx_config WHERE ID=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			if(re.next()){ 
				String AppID=re.getString("AppID");
				String AppSecret=re.getString("AppSecret");
				String reurl ="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AppID+"&secret="+AppSecret+"&code="+code+"&grant_type=authorization_code";
				URL url = new URL(reurl);  //创建url连接  
		        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //打开连接  
		        urlConnection.setDoOutput(true);  
		        urlConnection.setDoInput(true);  
		        urlConnection.setRequestMethod("GET");  
		        urlConnection.setUseCaches(false);  
		        urlConnection.connect();  
		        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));  
		        StringBuffer buffer = new StringBuffer();//存储服务器返回的信息</span>  
		        //用来接收用户的appid  
		        while ((line = reader.readLine()) != null) {  
		            buffer.append(line);  
		        }  
		        urlConnection.disconnect();
		        String result = buffer.toString();  
		        System.out.println(result);  
		        JSONObject resultObject = JSONObject.fromObject(result);//将服务器返回的字符串转换成json格式  
		        openid = resultObject.getString("openid");  //获取得到
		        access_token=resultObject.getString("access_token");
		        sql="SELECT ID from users WHERE openid='"+openid+"'";
				preparedStatement = connection.prepareStatement(sql);
				ResultSet re2 = preparedStatement.executeQuery();
				if(re2.next())
				{
					String id=re2.getString("ID");
					HttpSession session=request.getSession();
					session.setAttribute("Username", id);
					response.sendRedirect(request.getContextPath()+"/wechat/index.html");
				}
				else
				{
					reurl="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
					url = new URL(reurl);  //创建url连接  
			        urlConnection = (HttpURLConnection) url.openConnection(); //打开连接  
			        urlConnection.setDoOutput(true);  
			        urlConnection.setDoInput(true);  
			        urlConnection.setRequestMethod("GET");  
			        urlConnection.setUseCaches(false);  
			        urlConnection.connect();  
			        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));  
			        buffer = new StringBuffer();//存储服务器返回的信息</span>  
			        //用来接收用户的appid  
			        line="";
			        while ((line = reader.readLine()) != null) {  
			            buffer.append(line);  
			        }  
			        result = buffer.toString();  
			        resultObject = JSONObject.fromObject(result);//将服务器返回的字符串转换成json格式  
			        String nickname = resultObject.getString("nickname");  //获取得到
			        //nickname=new String(nickname.getBytes("GB2312"),"8859_1"); 
			        //System.out.println(nickname);
			        urlConnection.disconnect();
					response.sendRedirect(request.getContextPath()+"/wechat/register.html?openid="+openid+"&nickname="+URLEncoder.encode(nickname, "UTF-8"));
				}
			}
            preparedStatement.close();
			re.close();
			dbc.CloseConnection(connection);
		}
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("获取用户信息成功！！");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String getsubinfo(String topid)
	{
		String json="";
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="SELECT * FROM directories where topid="+topid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			while(re.next()){ 
				String id=re.getString("id");
				String text=re.getString("text");
				String iscourse=re.getString("iscourse");
				String vurl=re.getString("url");
				if(count>0)
					json=json+",";
				//json="[{id:1,text:\"test\",nodes:[{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}],topid:1,url:\"fadf\",time:200},{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}]";
				json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"topid\":"+topid+",\"iscourse\":"+iscourse+",\"url\":\""+vurl+"\"";
				String res=getsubinfo(id);
				if(res=="null")
				{
					json=json+"}";
				}
				else
					json=json+",\"nodes\":["+res+"]}";
				count++;
			 }
			if(count==0)
				json="null";
		}
		catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
		} 
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("目录成功获取！！");
		}
		return json;
	}

}
