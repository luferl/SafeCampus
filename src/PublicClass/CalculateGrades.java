package PublicClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * *成绩计算类，传入参数为考试记录的id，计算该次考试的成绩
 */
public class CalculateGrades {
	public void Calculate(String gid)
	{
		try {
			//根据答题记录id(gid)，获取用户的答题记录
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			sql="SELECT type,answer,u_answer,score FROM questions_answer WHERE quiz_gid="+gid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re=preparedStatement.executeQuery();
			int totalscore=0;
			while(re.next()) 
			{
				//遍历答题记录，获取用户答题参数
				 String type=re.getString("type");
				 String answer=re.getString("answer");
				 String uanswer=re.getString("u_answer");
				 String score=re.getString("score");
				 //用户作答了该题
				 if(uanswer!=null)
				 {
					 //单选或判断题
					 if(type.equals("check")||type.equals("single"))
					 {
						 //答案与正确答案一致
						 if(answer.equals(uanswer))
							 //成绩累和
							 totalscore=totalscore+Integer.parseInt(score);
					 }
					 //多选题
					 else
						 {
						 	//答案相同，成绩累加
							 if(answer.equals(uanswer))
								 totalscore=totalscore+Integer.parseInt(score);
							 //答案漏选，该题得分-1，成绩累加
							 else
								 if(answer.indexOf(uanswer)!=-1)
									 totalscore=totalscore+Integer.parseInt(score)-1;
						 }
				 }
			}
			//更新数据库，把成绩写入该次答题记录
			sql="UPDATE quiz_grades SET grades="+totalscore+",issubmitted=1 WHERE ID="+gid;
			preparedStatement = connection.prepareStatement(sql);
			int re2=preparedStatement.executeUpdate();
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
			System.out.println("Operation Finished:CalculateGrades");
		}
	}
}
