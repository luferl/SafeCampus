package PublicClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculateGrades {
	public void Calculate(String gid)
	{
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnnection();
			String sql="";
			sql="SELECT type,answer,u_answer,score FROM questions_answer WHERE quiz_gid="+gid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re=preparedStatement.executeQuery();
			int totalscore=0;
			while(re.next()) 
			{
				 String type=re.getString("type");
				 String answer=re.getString("answer");
				 String uanswer=re.getString("u_answer");
				 String score=re.getString("score");
				 if(type.equals("check")||type.equals("single"))
				 {
					 if(answer.equals(uanswer))
						 totalscore=totalscore+Integer.parseInt(score);
				 }
				 else
					 {
						 if(answer.equals(uanswer))
							 totalscore=totalscore+Integer.parseInt(score);
						 else
							 if(answer.indexOf(uanswer)!=-1)
								 totalscore=totalscore+Integer.parseInt(score)-1;
					 }
			}
			sql="UPDATE quiz_grades SET grades="+totalscore+",issubmitted=1 WHERE ID="+gid;
			preparedStatement = connection.prepareStatement(sql);
			int re2=preparedStatement.executeUpdate();
			connection.close();
		}
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("Operation Finished:CalculateGrades");
		}
	}
}
