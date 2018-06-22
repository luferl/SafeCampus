//获取参数
var courseid=0;
		var reg = new RegExp("(^|&)courseid=([^&]*)(&|$)", "i");  
	    var r = window.location.search.substr(1).match(reg);  
	    if (r != null) 
	    	$.get("GetCourse",
	    			{courseid:r[2]},
						function(data){
		    				var text=data.text;
		    				var url=data.url;
		    				var time=data.time;
		    				var temp=url.split('/');
		    				temp=temp[temp.length-1];
		    				temp=temp.split('.')[0];
		    				temp="http://v.qq.com/iframe/player.html?vid="+temp+"&tiny=0&auto=0";
		    				document.getElementById("videopreview").src =temp;
		    				$('#coursename').html(text);
		    				loadquestions(r[2]);
		    				courseid=r[2];
					}
			);
	    //加载课后题
		function loadquestions(courseid)
		{
			console.log(courseid);
			$("#questionlist").empty();
			$.post("GetCourseQuestions",{
				courseid:courseid
				},
				function(data){
					var html="";
					var opt="";
					var questions=data;
					for(i=0;i<questions.length;i++)
					{
						var question = document.createElement('fieldset'); //创建元素
						question.id=questions[i].id;
						var id=questions[i].id;
						var text=questions[i].text;
						var choices=questions[i].choices;
						var type=questions[i].type;
						var answer=questions[i].answer;
						var html="<div class='row'>";
						if(type=="check")
							{
								html=html+"<section class='col col-md-2'><label class='label'>[判断]"+text+"</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle'><i data-swchon-text='正确' data-swchoff-text='错误'></i>选项：</label></div></div></section></div>";
							}
						else
						if(type=="single")
							{
								html=html+"<section class='col col-md-2'><label class='label'>[单选]"+text+"</label></section></div>";
								html=html+"<section class='singlechoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
								var optarr=choices.split('|');
								opt="";
								for(j=0;j<optarr.length;j++)
								{
									opt=opt+"<label class='radio'><input type='radio' name='single"+id+"'><i></i>"+optarr[j]+"</label>";
								}
								html=html+opt;
								html=html+"</div></div></section>";
							}
						else
							{
								html=html+"<section class='col col-md-2'><label class='label'>[多选]"+text+"</label></section></div>";	
								html=html+"<section class='multychoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
								var optarr=choices.split('|');
								opt="";
								for(j=0;j<optarr.length;j++)
								{
									opt=opt+"<div class='checkbox'><label><input type='checkbox' class='checkbox style-0' name='multy"+id+"'><span>"+optarr[j]+"</span></label></div>";
								}
								html=html+opt;
								html=html+"</div></div></section>";
							}
						question.innerHTML=html;
						var field = document.getElementById('questionlist'); //2、找到父级元素
						field.appendChild(question);//插入到最左边
					}
					
				}
			);
		}
		//提交
		function submit()
		{
			var fieldlist=$("#questionlist").find("fieldset");
			console.log(fieldlist);
			var answers=[];
			
			var json="[";
			for(i=0;i<fieldlist.length;i++)
			{
				var answer={};
				var field=fieldlist[i];	
				var temp=field.innerText;
				var type=temp.slice(1,3);
				if(i>0)
					json=json+",";
				answer.id=field.id;
				if(type=="判断")
				{
					var temp=field.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].checked;
					if(temp)
						answer.answer="1";
					else
						answer.answer="0";
				}
				else
					if(type=="单选")
						{
							if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[0].childNodes[0].checked)
								answer.answer="A";
							else
								if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[1].childNodes[0].checked)
									answer.answer="B";
								else
									if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[2].childNodes[0].checked)
										answer.answer="C";
									else
										answer.answer="D";
						}
					else
					{
						answer.answer="";
						if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].checked)
							answer.answer=answer.answer+"A";
						if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].checked)
							answer.answer=answer.answer+"B";
						if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[0].checked)
							answer.answer=answer.answer+"C";
						if(field.childNodes[1].childNodes[1].childNodes[0].childNodes[3].childNodes[0].childNodes[0].checked)
							answer.answer=answer.answer+"D";
					}
				console.log(answer);
				answers.push(answer);
			}
			var st = JSON.stringify(answers);
			$.post("SubmitCourseQuestions",
					{
						courseid:courseid,
						answers:st
					},
					function(data){
						if(data=="wrong")
							alert("答案有误，请检查");
						else
							if(data=="success")
								{
									alert("恭喜你完成本节课程");
									window.location.href="courselist.html";
								}
							else
								console.log(data);
					}
				);			
		}