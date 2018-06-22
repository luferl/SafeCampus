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
 * Servlet implementation class CallBack
 * ��¼�ص����������ڻ�ȡ�û���Ϣ
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
		//��ȡCodeֵ
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String code=request.getParameter("code");
		DBConnection dbc=new DBConnection();
		String access_token = ""; 
		String line="";
	    String openid = "";  
		try {
			//��ȡ���ݿ⣬���΢�ŵ�APPID��AppSecret
			Connection connection = dbc.getConnection();
			String sql="SELECT AppID,AppSecret FROM wx_config WHERE ID=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			if(re.next()){ 
				String AppID=re.getString("AppID");
				String AppSecret=re.getString("AppSecret");
				//��ȡ��ѶAPI
				String reurl ="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AppID+"&secret="+AppSecret+"&code="+code+"&grant_type=authorization_code";
				URL url = new URL(reurl);  //����url����  
		        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //������  
		        urlConnection.setDoOutput(true);  
		        urlConnection.setDoInput(true);  
		        urlConnection.setRequestMethod("GET");  
		        urlConnection.setUseCaches(false);  
		        urlConnection.connect();  
		        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));  
		        StringBuffer buffer = new StringBuffer();//�洢���������ص���Ϣ</span>  
		        //���������û���appid  
		        while ((line = reader.readLine()) != null) {  
		            buffer.append(line);  
		        }  
		        urlConnection.disconnect();
		        String result = buffer.toString();  
		        System.out.println(result);  
		        //�ӽ���л�ȡ�û���Openid
		        JSONObject resultObject = JSONObject.fromObject(result);//�����������ص��ַ���ת����json��ʽ  
		        openid = resultObject.getString("openid");  //��ȡ�õ�
		        //�������ݿ⣬���û��Ƿ���ע��
		        access_token=resultObject.getString("access_token");
		        sql="SELECT ID from users WHERE openid='"+openid+"'";
				preparedStatement = connection.prepareStatement(sql);
				ResultSet re2 = preparedStatement.executeQuery();
				//��ע�ᣬд��session����ת��ҳ
				if(re2.next())
				{
					String id=re2.getString("ID");
					HttpSession session=request.getSession();
					session.setAttribute("Username", id);
					response.sendRedirect(request.getContextPath()+"/wechat/index.html");
				}
				//δע�ᣬ������Ѷapi
				else
				{
					reurl="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
					url = new URL(reurl);  //����url����  
			        urlConnection = (HttpURLConnection) url.openConnection(); //������  
			        urlConnection.setDoOutput(true);  
			        urlConnection.setDoInput(true);  
			        urlConnection.setRequestMethod("GET");  
			        urlConnection.setUseCaches(false);  
			        urlConnection.connect();  
			        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));  
			        buffer = new StringBuffer();//�洢���������ص���Ϣ</span>  
			        //�ӽ���л���û����ǳ� 
			        line="";
			        while ((line = reader.readLine()) != null) {  
			            buffer.append(line);  
			        }  
			        result = buffer.toString();  
			        resultObject = JSONObject.fromObject(result);//�����������ص��ַ���ת����json��ʽ  
			        String nickname = resultObject.getString("nickname");  
			        urlConnection.disconnect();
			        //��תע��ҳ���������ǳƺ�OpenID
					response.sendRedirect(request.getContextPath()+"/wechat/register.html?openid="+openid+"&nickname="+URLEncoder.encode(nickname, "UTF-8"));
				}
			}
            preparedStatement.close();
			re.close();
			dbc.CloseConnection(connection);
		}
		catch(SQLException e) {
			//���ݿ�����ʧ���쳣����
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("��ȡ�û���Ϣ�ɹ�����");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
