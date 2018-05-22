$(document).ready(function() {
				pageSetUp();
				// PAGE RELATED SCRIPTS
				$.get("GetDirectories",
							function(data){
								$('#tree').treeview({
									data: data,
									onNodeSelected: function(event, node) {
										changedirect(node);
									}
								});
							}
						);	
			});
		var id=0;
		var addid=-1;
		function changedirect(node)
		{
			console.log(node.id);
			iscourse=node.iscourse;
			$('#dircetid').val(node.id);
			$('#directname').val(node.text);
			$("#directlist").val(node.topid); 
			if(iscourse==0)
			{
				$("#directtype").val("direct");
				$('#directurl').val("");
				$('#directtime').val("");
				$('#directurl').attr("disabled","disabled");
				$('#directtime').attr("disabled","disabled");
				document.getElementById("videopreview").src ="";
			}
			else
			{
				$("#directtype").val("course");
				$('#directurl').removeAttr("disabled");
				$('#directtime').removeAttr("disabled");
				$('#directurl').val(node.url);
				$('#directtime').val(node.time);
				var temp=node.url.split('/');
				temp=temp[temp.length-1];
				temp=temp.split('.')[0];
				temp="http://v.qq.com/iframe/player.html?vid="+temp+"&tiny=0&auto=0";
				document.getElementById("videopreview").src =temp;
				loadquestions(node.id);
			}
		}
		$.get("GetDirectlist",
				function(data){
					for(i=0;i<data.length;i++)
					{
						obj=data[i];
						document.getElementById("directlist").options.add(new Option(obj.text,obj.id));
					}
				}
			);	
		function changedirecttype(x)
		{
			if($("#directtype").val()=="direct")
			{
				$('#directurl').val("");
				$('#directtime').val("");
				$('#directurl').attr("disabled","disabled");
				$('#directtime').attr("disabled","disabled");
			}
			else
				{
					$('#directurl').removeAttr("disabled");
					$('#directtime').removeAttr("disabled");
					$('#directurl').val(node.url);
					$('#directtime').val(node.time);
				}
		}
		function createnewdirect()
		{
			var name=$('#directname').val();
			var topid=$('#directlist').val();
			var type;
			if($('#directtype').val()=="direct")
				type=0;
			else
				type=1;
			var vurl=$('#directurl').val();
			var vtime=$('#directtime').val();
			$.post("CreateDirect",
					{
						text:name,
						topid:topid,
						iscourse:type,
						vurl:vurl,
						time:vtime
					},
					function(data){
						if(data=="success")
							{
								alert("创建成功");
								location.reload();
							}
						else
							{
								alert(data);
							}
					}
				);	
		}
		function savedirect()
		{
			var name=$('#directname').val();
			var topid=$('#directlist').val();
			var id=$('#dircetid').val();
			console.log(id);
			var type;
			if($('#directtype').val()=="direct")
				type=0;
			else
				type=1;
			var vurl=$('#directurl').val();
			var vtime=$('#directtime').val();
			$.post("SaveDirect",
					{
						id:id,
						text:name,
						topid:topid,
						iscourse:type,
						vurl:vurl,
						time:vtime
					},
					function(data){
						if(data=="success")
							{
								alert("修改成功");
								location.reload();
							}
						else
							{
								alert(data);
							}
					}
				);	
		}
		function deletedirect()
		{
			var id=$('#dircetid').val();
			$.post("DeleteDirect",
					{
						id:id
					},
					function(data){
						if(data=="success")
							{
								alert("删除成功");
								location.reload();
							}
						else
							{
								alert(data);
							}
					}
				);	
		}
		function addtrigger()
		{
			$(".typeselect").change(function(){  
				var choice=$(this).find('option:selected').val();
				var fieldset=$(this).parent().parent().parent().parent();
				var singlesection=fieldset.find(".singlechoice");
				var multysection=fieldset.find(".multychoice");
				var checksection=fieldset.find(".checkchoice");
				if(choice=="check")
				{
					singlesection[0].style.display="none";
					multysection[0].style.display="none";
					checksection[0].style.display="";
				}
				else
					if(choice=="single")
					{
						singlesection[0].style.display="";
						multysection[0].style.display="none";
						checksection[0].style.display="none";
					}
				else
					{
						singlesection[0].style.display="none";
						multysection[0].style.display="";
						checksection[0].style.display="none";
					}
			});
		}
		function addcoursequestion()
		{
			var question = document.createElement('fieldset'); //创建元素
			question.id=addid;
			var id=addid;
			addid--;
			var text="";
			var choices="A、|B、|C、|D、";
			var type="check";
			var html="<div class='row'>";
			var answer="0";
			if(type=="check")
				{
				html=html+"<section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'>单选</option><option value='multy'>多选</option><option value='check' selected='selected'>判断</option></select></div></section></div>";
				}
			else
				if(type=="single")
					{
						html=html+"<section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'  selected='selected'>单选</option><option value='multy'>多选</option><option value='check'>判断</option></select></div></section></div>";
					}
				else
					html=html+"<section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'>单选</option><option value='multy'  selected='selected'>多选</option><option value='check'>判断</option></select></div></section></div>";
			html=html+"<section><label class='label'>题干：</label><label class='textarea'> <i class='icon-append fa fa-comment'></i><textarea rows='4' name='comment'>"+text+"</textarea></label></section>";
			if(type=="check")
				{
					if(answer=="0")
						html=html+"<section class='checkchoice'><label class='label'>答案：</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle'><i data-swchon-text='正确' data-swchoff-text='错误'></i>正误：</label></div></div></section>";
					else
						html=html+"<section class='checkchoice'><label class='label'>答案：</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle' checked='checked'><i data-swchon-text='正确' data-swchoff-text='错误'></i>正误：</label></div></div></section>";
				}
			else
			{
				html=html+"<section class='checkchoice' style='display:none'><label class='label'>答案：</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle' checked='checked'><i data-swchon-text='正确' data-swchoff-text='错误'></i>正误：</label></div></div></section>";
			}
			if(type=="single")
				html=html+"<section class='singlechoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
			else
				html=html+"<section class='singlechoice' style='display:none'><label class='label'>选项：</label><div class='col col-6'>";
			var optarr=choices.split('|');
			opt="";
			for(j=0;j<optarr.length;j++)
			{
				if(answer.indexOf(String.fromCharCode(65+j)) != -1)
					opt=opt+"<div class=‘row’><label class='radio col col-2'><input type='radio' name='single"+id+"' checked='checked'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
				else
					opt=opt+"<div class=‘row’><label class='radio col col-2'><input type='radio' name='single"+id+"'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
			}
			html=html+opt;
			html=html+"</div></div></section>";
			if(type=="multy")
				html=html+"<section class='multychoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
			else
				html=html+"<section class='multychoice' style='display:none'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
			opt="";
			for(j=0;j<optarr.length;j++)
			{
				if(answer.indexOf(String.fromCharCode(65+j)) != -1)
					opt=opt+"<div class=‘row’><label class='checkbox col col-2'><input type='checkbox' name='multy"+id+"' checked='checked'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
				else
					opt=opt+"<div class=‘row’><label class='checkbox col col-2'><input type='checkbox' name='multy"+id+"'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
			}
			html=html+opt;
			html=html+"</div></div></section>";
			question.innerHTML=html;
			var field = document.getElementById('questionlist'); //2、找到父级元素
			field.prepend(question);//插入到最前边
			addtrigger();
		}
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
							html=html+"<section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'>单选</option><option value='multy'>多选</option><option value='check' selected='selected'>判断</option></select></div></section></div>";
							}
						else
							if(type=="single")
								{
									html=html+"<section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'  selected='selected'>单选</option><option value='multy'>多选</option><option value='check'>判断</option></select></div></section></div>";
								}
							else
								html=html+"<section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'>单选</option><option value='multy'  selected='selected'>多选</option><option value='check'>判断</option></select></div></section></div>";
						html=html+"<section><label class='label'>题干：</label><label class='textarea'> <i class='icon-append fa fa-comment'></i><textarea rows='4' name='comment'>"+text+"</textarea></label></section>";
						if(type=="check")
							{
								if(answer=="0")
									html=html+"<section class='checkchoice'><label class='label'>答案：</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle'><i data-swchon-text='正确' data-swchoff-text='错误'></i>正误：</label></div></div></section>";
								else
									html=html+"<section class='checkchoice'><label class='label'>答案：</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle' checked='checked'><i data-swchon-text='正确' data-swchoff-text='错误'></i>正误：</label></div></div></section>";
							}
						else
						{
							html=html+"<section class='checkchoice' style='display:none'><label class='label'>答案：</label><div class='row'><div class='col col-4'><label class='toggle'><input type='checkbox' name='checkbox-toggle' checked='checked'><i data-swchon-text='正确' data-swchoff-text='错误'></i>正误：</label></div></div></section>";
						}
						if(type=="single")
							html=html+"<section class='singlechoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
						else
							html=html+"<section class='singlechoice' style='display:none'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
						var optarr=choices.split('|');
						opt="";
						for(j=0;j<optarr.length;j++)
						{
							if(answer.indexOf(String.fromCharCode(65+j)) != -1)
								opt=opt+"<div class=‘row’><label class='radio col col-2'><input type='radio' name='single"+id+"' checked='checked'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
							else
								opt=opt+"<div class=‘row’><label class='radio col col-2'><input type='radio' name='single"+id+"'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
						}
						html=html+opt;
						html=html+"</div></div></section>";
						if(type=="multy")
							html=html+"<section class='multychoice'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
						else
							html=html+"<section class='multychoice' style='display:none'><label class='label'>选项：</label><div class='row'><div class='col col-4'>";
						opt="";
						for(j=0;j<optarr.length;j++)
						{
							if(answer.indexOf(String.fromCharCode(65+j)) != -1)
								opt=opt+"<div class=‘row’><label class='checkbox col col-2'><input type='checkbox' name='multy"+id+"' checked='checked'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
							else
								opt=opt+"<div class=‘row’><label class='checkbox col col-2'><input type='checkbox' name='multy"+id+"'><i></i></label><section class='col col-10'><label class='input'><input type='text' name='"+String.fromCharCode(65+j)+"' value='"+optarr[j]+"'></label></section></div>";
						}
						html=html+opt;
						html=html+"</div></div></section>";
						question.innerHTML=html;
						var field = document.getElementById('questionlist'); //2、找到父级元素
						field.appendChild(question);//插入到最左边
					}
					addtrigger();
				}
			);
		}
		function savecoursequestions()
		{
			var fieldlist=$("#questionlist").find("fieldset");
			console.log(fieldlist);
			var json="[";
			for(i=0;i<fieldlist.length;i++)
				{
					if(i>0)
						json=json+",";
					var field=fieldlist[i];
					var id=field.id;
					var type=field.childNodes[0].childNodes[0].childNodes[1].childNodes[0].value;
					var text=field.childNodes[1].childNodes[1].childNodes[2].value;
					var choices="A、|B、|C、|D、";
					var answer="";
					
					if(id>0)
						{
							if(type=="check")
							{
								ischeck=field.childNodes[2].childNodes[1].childNodes[0].childNodes[0].childNodes[0].checked;
								if(ischeck)
									answer="1";
								else
									answer="0";
							}
							else
								if(type=="single")
									{
										var choiceA=field.childNodes[3].childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].value;
										var choiceB=field.childNodes[3].childNodes[1].childNodes[0].childNodes[1].childNodes[1].childNodes[0].childNodes[0].value;
										var choiceC=field.childNodes[3].childNodes[1].childNodes[0].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
										var choiceD=field.childNodes[3].childNodes[1].childNodes[0].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
										choices=choiceA+"|"+choiceB+"|"+choiceC+"|"+choiceD;
										//var answer=field.childNodes[3].find('option:selected').val();
										if(field.childNodes[3].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].checked)
											answer="A";
										else
											if(field.childNodes[3].childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].checked)
												answer="B";
											else
												if(field.childNodes[3].childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[0].checked)
													answer="C";
												else
													answer="D";
									}
							else
								{
									var choiceA=field.childNodes[4].childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].value;
									var choiceB=field.childNodes[4].childNodes[1].childNodes[0].childNodes[1].childNodes[1].childNodes[0].childNodes[0].value;
									var choiceC=field.childNodes[4].childNodes[1].childNodes[0].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
									var choiceD=field.childNodes[4].childNodes[1].childNodes[0].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
									choices=choiceA+"|"+choiceB+"|"+choiceC+"|"+choiceD;
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].checked)
										answer=answer+"A";
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].checked)
										answer=answer+"B";
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[0].checked)
										answer=answer+"C";
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[3].childNodes[0].childNodes[0].checked)
										answer=answer+"D";
								}
							json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"answer\":\""+answer+"\"}";
						}
					else
						{
							
							if(type=="check")
							{
								ischeck=field.childNodes[2].childNodes[1].childNodes[0].childNodes[0].childNodes[0].checked;
								if(ischeck)
									answer="1";
								else
									answer="0";
							}
							else
								if(type=="single")
									{
										var choiceA=field.childNodes[3].childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].value;
										var choiceB=field.childNodes[3].childNodes[1].childNodes[1].childNodes[1].childNodes[0].childNodes[0].value;
										var choiceC=field.childNodes[3].childNodes[1].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
										var choiceD=field.childNodes[3].childNodes[1].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
										choices=choiceA+"|"+choiceB+"|"+choiceC+"|"+choiceD;
										//var answer=field.childNodes[3].find('option:selected').val();
										if(field.childNodes[3].childNodes[1].childNodes[0].childNodes[0].childNodes[0].checked)
											answer="A";
										else
											if(field.childNodes[3].childNodes[1].childNodes[1].childNodes[0].childNodes[0].checked)
												answer="B";
											else
												if(field.childNodes[3].childNodes[1].childNodes[2].childNodes[0].childNodes[0].checked)
													answer="C";
												else
													answer="D";
									}
							else
								{
									var choiceA=field.childNodes[4].childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].value;
									var choiceB=field.childNodes[4].childNodes[1].childNodes[0].childNodes[1].childNodes[1].childNodes[0].childNodes[0].value;
									var choiceC=field.childNodes[4].childNodes[1].childNodes[0].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
									var choiceD=field.childNodes[4].childNodes[1].childNodes[0].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
									choices=choiceA+"|"+choiceB+"|"+choiceC+"|"+choiceD;
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].checked)
										answer=answer+"A";
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[0].checked)
										answer=answer+"B";
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[0].checked)
										answer=answer+"C";
									if(field.childNodes[4].childNodes[1].childNodes[0].childNodes[3].childNodes[0].childNodes[0].checked)
										answer=answer+"D";
								}
							json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"answer\":\""+answer+"\"}";
							}
					}
			json=json+"]";
			$.post("SaveCourseQuestions",{
					courseid:$('#dircetid').val(),
					questions:json
				},
				function(data){
					if(data=="success")
					{
						alert("保存成功！");
						location.reload();
					}
						
				}
			);
			//console.log(json);
		}