package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import PublicClass.Questions;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Questions2Turn {

	public void main(String[] args)
	{
		Connection connection = null;
		String json="";
		int totalcount=0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="select * from questions where type='single' or type='multy'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			String id="";
			while(re.next()){ 
				id=re.getString("ID");
				String choices=re.getString("choices");
				JSONArray jsonarray=JSONArray.fromObject(choices);
				String temp="";
				String answer="";
				if(jsonarray.size()>0)
				{
					temp="";
					answer="";
					for(int i=0;i<jsonarray.size();i++)
					{
						if(i>0)
							temp=temp+"|";
						JSONObject job = jsonarray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
						String text=job.get("text").toString();
						String value=job.get("isRight").toString();
						temp=temp+text;
						if(value.equals("true"))
						{
							if(i==0)
								answer=answer+"A";
							if(i==1)
								answer=answer+"B";
							if(i==2)
								answer=answer+"C";
							if(i==3)
								answer=answer+"D";
						}
					}
				}
				String sql2="UPDATE questions set choices='"+temp+"',answer='"+answer+"' where ID="+id;
				preparedStatement = connection.prepareStatement(sql2);
				int re2 = preparedStatement.executeUpdate();
			}
			
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
			System.out.println("Get Course Questions Successfully");
		}
	}
}
