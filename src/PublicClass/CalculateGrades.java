package PublicClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * *�ɼ������࣬�������Ϊ���Լ�¼��id������ôο��Եĳɼ�
 */
public class CalculateGrades {
	public void Calculate(String gid)
	{
		try {
			//���ݴ����¼id(gid)����ȡ�û��Ĵ����¼
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			sql="SELECT type,answer,u_answer,score FROM questions_answer WHERE quiz_gid="+gid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re=preparedStatement.executeQuery();
			int totalscore=0;
			while(re.next()) 
			{
				//���������¼����ȡ�û��������
				 String type=re.getString("type");
				 String answer=re.getString("answer");
				 String uanswer=re.getString("u_answer");
				 String score=re.getString("score");
				 //�û������˸���
				 if(uanswer!=null)
				 {
					 //��ѡ���ж���
					 if(type.equals("check")||type.equals("single"))
					 {
						 //������ȷ��һ��
						 if(answer.equals(uanswer))
							 //�ɼ��ۺ�
							 totalscore=totalscore+Integer.parseInt(score);
					 }
					 //��ѡ��
					 else
						 {
						 	//����ͬ���ɼ��ۼ�
							 if(answer.equals(uanswer))
								 totalscore=totalscore+Integer.parseInt(score);
							 //��©ѡ������÷�-1���ɼ��ۼ�
							 else
								 if(answer.indexOf(uanswer)!=-1)
									 totalscore=totalscore+Integer.parseInt(score)-1;
						 }
				 }
			}
			//�������ݿ⣬�ѳɼ�д��ôδ����¼
			sql="UPDATE quiz_grades SET grades="+totalscore+",issubmitted=1 WHERE ID="+gid;
			preparedStatement = connection.prepareStatement(sql);
			int re2=preparedStatement.executeUpdate();
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
			System.out.println("Operation Finished:CalculateGrades");
		}
	}
}
