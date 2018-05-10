// DO NOT REMOVE : GLOBAL FUNCTIONS!
		var reg = new RegExp("(^|&)quizid=([^&]*)(&|$)", "i");  
	    var r = window.location.search.substr(1).match(reg);
	    var quizid=r[2];
	    reg = new RegExp("(^|&)gid=([^&]*)(&|$)", "i"); 
	    r = window.location.search.substr(1).match(reg);
	    var gid=r[2];
	    var questions;
	    var curpages=0;
	    $.post("GetQuiz",
	    			{
	    				quizid:quizid,
	    				gid:gid
					},
						function(data){
		    				questions=data.questions;
		    				gid=data.gid;
		    				loadquestions();
					}
			);
		function loadquestions()
		{
			$("#questionlist").empty();
			var html="";
			var opt="";
			for(i=curpages*5;(i<questions.length)&&(i<(curpages*5+5));i++)
			{
				var question = document.createElement('fieldset'); //创建元素
				question.id=questions[i].id;
				var id=questions[i].id;
				var text=questions[i].text;
				var choices=questions[i].choices;
				var type=questions[i].type;
				var answer=questions[i].uanswer;
				var html="<div class='row'>";
				if(type=="check")
				{
					if(answer=="1")
						html=html+"<section class='col col-md-2'><label class='label'>"+(i+1)+"、 [判断]"+text+"</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle' checked='checked'><i data-swchon-text='正确' data-swchoff-text='错误'></i>选项：</label></div></div></section></div>";
					else
						html=html+"<section class='col col-md-2'><label class='label'>"+(i+1)+"、 [判断]"+text+"</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle'><i data-swchon-text='正确' data-swchoff-text='错误'></i>选项：</label></div></div></section></div>";
				}
				else
				if(type=="single")
					{
						html=html+"<section class='col col-md-2'><label class='label'>"+(i+1)+"、[单选]"+text+"</label></section></div>";
						html=html+"<section class='singlechoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
						var optarr=choices.split('|');
						opt="";
						for(j=0;j<optarr.length;j++)
						{
							if(answer.indexOf(String.fromCharCode(65+j)) != -1)
								opt=opt+"<label class='radio'><input type='radio' name='single"+id+"' checked='checked'><i></i>"+optarr[j]+"</label>";
							else
								opt=opt+"<label class='radio'><input type='radio' name='single"+id+"'><i></i>"+optarr[j]+"</label>";
						}
						html=html+opt;
						html=html+"</div></div></section>";
					}
				else
					{
						html=html+"<section class='col col-md-2'><label class='label'>"+(i+1)+"、[多选]"+text+"</label></section></div>";	
						html=html+"<section class='multychoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
						var optarr=choices.split('|');
						opt="";
						for(j=0;j<optarr.length;j++)
						{
							if(answer.indexOf(String.fromCharCode(65+j)) != -1)
								opt=opt+"<div class='checkbox'><label><input type='checkbox' class='checkbox style-0' name='multy"+id+"' checked='checked'><span>"+optarr[j]+"</span></label></div>";
							else
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
		function frontpage()
		{
			savecurpage();			
			if(curpages==0)
				{
					return;
				}
			else
				{
					curpages--;
					loadquestions();
				}
		}
		function nextpage()
		{
			savecurpage();	
			var tpages=0;
			if(questions.length%5==0)
				tpages=parseInt(questions.length/5)-1;
			else
				tpages=parseInt(questions.length/5);
			if(curpages<tpages)
				{
					curpages++;
					loadquestions()
				}
			else
				return;
		}
		function savecurpage()
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
				var type=questions[curpages*5+i].type;
				if(i>0)
					json=json+",";
				answer.id=field.id;
				if(type=="check")
				{
					var temp=field.childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].checked;
					if(temp)
						answer.answer="1";
					else
						answer.answer="0";
				}
				else
					if(type=="single")
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
				questions[curpages*5+i].uanswer=answer.answer;
				answers.push(answer);
			}
			var st = JSON.stringify(answers);
			$.post("SaveCPage",
	    			{
	    				qa:st
					},
						function(data){
					}
			);
		}
		function submit()
		{
			var msg = "提交后将不能继续作答，确认要提交吗？"; 
			if (confirm(msg)==true){ 
				 savecurpage();
					$.post("SubmitQuiz",
			    			{
			    				gid:gid
							},
								function(data){
								 alert("交卷成功！");
								 self.location=document.referrer;
							}
					);
			 }else{ 
			  return ; 
			 } 
			
		}