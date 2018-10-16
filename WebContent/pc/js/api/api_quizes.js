			var id=0;
			var node_g=null;
			var knowledgelist;
			$(document).ready(function() {
				//初始化时间选择空间
				$('#starttime').editable({
				       placement: 'right',
				       combodate: {
				           firstItem: 'name'
				       }
				   });
				$('#endtime').editable({
			        placement: 'right',
			        combodate: {
			            firstItem: 'name'
			        }
			    });
			})
			//获取试卷列表
			$.get("GetQuizlist",
				function(data){
					$('#tree').treeview({
						data: data,
						onNodeSelected: function(event, node) {
							changechoices(node);
						}
					});
				}
			);
			//获取学院列表
			$.get("GetCollegelist",
					function(data){
						for(i=0;i<data.length;i++)
							$("#collegelist").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
					}
				);
			//获取部门列表
			$.get("GetRolelist",
					function(data){
						for(i=0;i<data.length;i++)
							$("#rolelist").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
					}
				);
			//获取年级列表
			$.get("GetYearlist",
					function(data){
						for(i=0;i<data.length;i++)
							$("#yearlist").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
					}
				);
			//获取知识点列表
			$.ajax({  
		          type:"POST", 
		          url:"GetKnowledge", 
		          dataType:"json", 
		          async : true,
	   			  success:function(data){   //function1()
	   				knowledgelist=data;
		        }
		  	});
			//重新加载
			function reload()
			{
				if(node_g!=null)
					changechoices(node_g);
			}
			//选择不同试卷时触发
			function changechoices(node)
			{
				//console.log(node.id);
				node_g=node;
				issimulate=node.issimulate;
				$('#quizid').val(node.id);
				$('#quizname').val(node.text);
				$("#starttime")[0].innerHTML=node.starttime; 
				$("#endtime")[0].innerHTML=node.endtime; 
				$("#timelast").val(node.time);
				$("#totalscore").val(node.totalsc);
				$("#passscore").val(node.passsc);
				$("#timelimits").val(node.times);
				if(issimulate==0)
				{
					$("#quiztype").val("formal");
				}
				else
				{
					$("#quiztype").val("simulate");
				}
				loadconfig(node.id);
			}
			//重新计算总分
			function recalculate(){
				var fieldlist=$("#quizconfiglist").find("fieldset");
				var totalscore=0;
				for(i=0;i<fieldlist.length;i++)
				{
					var field=fieldlist[i];
					var count=field.childNodes[0].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
					var score=field.childNodes[0].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
					if(count!=""&&score!="")
						{
							if(isNaN(count)||isNaN(score))
							{
								alert("请输入正确数字");
								return;
							}
							else
								totalscore=totalscore+parseInt(count)*parseInt(score);
						}
				}
				$("#totalscore").val(totalscore);
			}
			//添加配置项
			function addconfig(){
				var config = document.createElement('fieldset');
				var html="<div class='row'><section class='col col-md-2'><label class='label'>知识点:</label><div class='col-md-6'><select class='form-control'>";
				for(j=0;j<knowledgelist.length;j++)
					html=html+"<option value='"+knowledgelist[j].id+"'>"+knowledgelist[j].text+"</option>";
				html=html+"</select></div></section><section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'><option value='single'>单选</option><option value='multy'>多选</option><option value='check' selected='selected'>判断</option></select></div></section><section class='col col-md-2'><label class='label'>题目数量:</label><div class='col-md-6'><label class='input state-disabled'><input type='text' class='input-sm q-count'  onchange='recalculate();'></label></div></section><section class='col col-md-2'><label class='label'>每题分数:</label><div class='col-md-6'><label class='input state-disabled'><input type='text' class='input-sm q-score' onchange='recalculate();'></label></div></section><section class='col col-md-4'><label class='label'>删除:</label><div class='col-md-6'><a href='#' class='delete btn btn-labeled btn-danger col-md-5'> <span class='btn-label'><i class='glyphicon glyphicon-trash'></i></span>删除 </a></div></section></div>";
				config.innerHTML=html;
				var form = document.getElementById('quizconfiglist'); //2、找到父级元素
				form.appendChild(config);//插入到最左边
				$('.delete').click(function(){
					$(this).parent().parent().parent().parent().remove();
					recalculate();
				})
			}
			//重新加载配置项
			function loadconfig(id){
				$("#quizconfiglist").empty();
				$.post("GetQuizconfig",
						{id:id},
						function(data){
							for(i=0;i<data.length;i++)
								{
									var knowledgeid=data[i].knowledgeid;
									var type=data[i].type;
									var count=data[i].count;
									var score=data[i].score;
									var config = document.createElement('fieldset');
									var html="<div class='row'><section class='col col-md-2'><label class='label'>知识点:</label><div class='col-md-6'><select class='form-control'>";
									for(j=0;j<knowledgelist.length;j++)
									{
										if(knowledgelist[j].id==knowledgeid)
										{
											html=html+"<option value='"+knowledgelist[j].id+"' selected='selected'>"+knowledgelist[j].text+"</option>";
										}
										else
											html=html+"<option value='"+knowledgelist[j].id+"'>"+knowledgelist[j].text+"</option>";
									}
									html=html+"</select></div></section><section class='col col-md-2'><label class='label'>题型:</label><div class='col-md-6'><select class='form-control typeselect'>";
									if(type=="check")
										html=html+"<option value='single'>单选</option><option value='multy'>多选</option><option value='check' selected='selected'>判断</option>";
										else
											if(type=="single")
												html=html+"<option value='single' selected='selected'>单选</option><option value='multy'>多选</option><option value='check'>判断</option>";
												else
													html=html+"<option value='single'>单选</option><option value='multy' selected='selected'>多选</option><option value='check'>判断</option>";
									html=html+"</select></div></section><section class='col col-md-2'><label class='label'>题目数量:</label><div class='col-md-6'><label class='input state-disabled'><input type='text' class='input-sm q-count'  onchange='recalculate();' value='"+count+"'></label></div></section><section class='col col-md-2'><label class='label'>每题分数:</label><div class='col-md-6'><label class='input state-disabled'><input type='text' class='input-sm q-score' onchange='recalculate();' value='"+score+"''></label></div></section><section class='col col-md-4'><label class='label'>删除:</label><div class='col-md-6'><a href='#' class='delete btn btn-labeled btn-danger col-md-5'> <span class='btn-label'><i class='glyphicon glyphicon-trash'></i></span>删除 </a></div></section></div>";
									config.innerHTML=html;
									var form = document.getElementById('quizconfiglist'); //2、找到父级元素
									form.appendChild(config);//插入到最左边
									$('.delete').click(function(){
										$(this).parent().parent().parent().parent().remove();
										recalculate();
									})
								}
							recalculate();
						}
					);
			}
			//删除试卷
			function deletequiz(){
				$.get("DeleteQuiz",
						{id: $('#quizid').val()},
						function(data){
								if(data=="success")
									{
										alert("删除成功，如有需要可在回收站恢复");
										window.location.reload();
									}
								else
									{
										alert("删除失败，可能是还未选择试卷");
										window.location.reload();
									}
						}
					);
			}
			//添加试卷
			function addquiz(){
				var name=$('#quizname').val();
				var starttime=$("#starttime")[0].innerHTML; 
				var endtime=$("#endtime")[0].innerHTML; 
				var timelast=$("#timelast").val();
				var totalscore=$("#totalscore").val();
				var passscore=$("#passscore").val();
				var timelimits=$("#timelimits").val();
				console.log(starttime);
				console.log(endtime);
				console.log(timelast);
				if(totalscore=='')
				{
					alert("总分不能为空，请检查配置项是否添加");
					return;
				}
				if(name=='')
				{
					alert("试卷名称不能为空");
					return;
				}
				if(starttime=='请选择时间')
				{
					alert("试卷开始时间不能为空");
					return;
				}
				if(endtime=='请选择时间')
				{
					alert("试卷结束不能为空");
					return;
				}
				if(timelast=='')
				{
					alert("考试时长不能为空");
					return;
				}
				if(passscore=='')
				{
					alert("及格分数不能为空");
					return;
				}
				if(timelimits=='')
				{
					alert("考试次数限制不能为空");
					return;
				}
				var type=$("#quiztype").val();
				if(type=="formal")
					{
						type=0;
					}
				else
					type=1;
				if(parseInt(passscore)>parseInt(totalscore))
					{
						alert("及格分不能超过总分！");
						return;
					}
				var fieldlist=$("#quizconfiglist").find("fieldset");
				var configs=[];
				for(i=0;i<fieldlist.length;i++)
				{
					var configobj={};
					field=fieldlist[i];
					configobj.knowledgeid=field.childNodes[0].childNodes[0].childNodes[1].childNodes[0].value;
					configobj.type=field.childNodes[0].childNodes[1].childNodes[1].childNodes[0].value;
					configobj.count=field.childNodes[0].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
					configobj.score=field.childNodes[0].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
					configs.push(configobj);
				}
				var st = JSON.stringify(configs);
				$.post("AddQuiz",
						{
							name:name,
							starttime:starttime,
							endtime:endtime,
							timelast:timelast,
							totalscore:totalscore,
							passscore:passscore,
							config:st,
							type:type,
							timelimit:timelimits
						},
						function(data){
							console.log(data);
							if(data=="success")
								{
									alert("添加成功");
									window.location.reload();
								}
							else
							{
								alert("添加失败");
								window.location.reload();
							}
							
						}
					);
			}
			//保存试卷
			function savequiz(){
				var name=$('#quizname').val();
				var starttime=$("#starttime")[0].innerHTML; 
				var endtime=$("#endtime")[0].innerHTML; 
				var timelast=$("#timelast").val();
				var totalscore=$("#totalscore").val();
				var passscore=$("#passscore").val();
				var timelimits=$("#timelimits").val();
				if(totalscore=='')
				{
					alert("总分不能为空，请检查配置项是否添加");
					return;
				}
				if(name=='')
				{
					alert("试卷名称不能为空");
					return;
				}
				if(starttime=='')
				{
					alert("试卷开始时间不能为空");
					return;
				}
				if(endtime=='')
				{
					alert("试卷结束不能为空");
					return;
				}
				if(timelast=='')
				{
					alert("考试时长不能为空");
					return;
				}
				if(passscore=='')
				{
					alert("及格分数不能为空");
					return;
				}
				if(timelimits=='')
				{
					alert("考试次数限制不能为空");
					return;
				}
				var type=$("#quiztype").val();
				if(type=="formal")
					{
						type=0;
					}
				else
					type=1;
				if(parseInt(passscore)>parseInt(totalscore))
					{
						alert("及格分不能超过总分！");
						return;
					}
				var fieldlist=$("#quizconfiglist").find("fieldset");
				var configs=[];
				for(i=0;i<fieldlist.length;i++)
				{
					var configobj={};
					field=fieldlist[i];
					configobj.knowledgeid=field.childNodes[0].childNodes[0].childNodes[1].childNodes[0].value;
					configobj.type=field.childNodes[0].childNodes[1].childNodes[1].childNodes[0].value;
					configobj.count=field.childNodes[0].childNodes[2].childNodes[1].childNodes[0].childNodes[0].value;
					configobj.score=field.childNodes[0].childNodes[3].childNodes[1].childNodes[0].childNodes[0].value;
					configs.push(configobj);
				}
				var st = JSON.stringify(configs);
				$.post("SaveQuiz",
						{
							id:$('#quizid').val(),
							name:name,
							starttime:starttime,
							endtime:endtime,
							timelast:timelast,
							totalscore:totalscore,
							passscore:passscore,
							config:st,
							type:type,
							timelimit:timelimits
						},
						function(data){
							console.log(data);
							if(data=="success")
								{
									alert("修改成功");
									window.location.reload();
								}
							else
							{
								alert("修改失败");
								window.location.reload();
							}
							
						}
					);
			}
			//下载成绩
			function DownloadGrades(){
				var id=$('#quizid').val();
				window.location.href="DownloadGrades?id="+id;
			}
			//下载答题记录
			function DownloadRecords(){
				var id=$('#quizid').val();
				window.location.href="DownloadRecords?id="+id;
			}		
			//自顶一下再
			$('#custom_download').click(function() {
				$('#dialog_simple').dialog('open');
				return false;
			});
			//设置自定义下载控件参数
			$('#dialog_simple').dialog({
				autoOpen : false,
				width : 600,
				resizable : false,
				modal : false,
				title : "选择筛选条件",
				buttons : [{
					html : "&nbsp; 下载成绩表",
					"class" : "btn btn-default",
					click : function() {
						var id=$('#quizid').val();
						if(id==-1)
							return;
						var department=$("#collegelist option:selected").val(); 
						var role=$("#rolelist option:selected").val();
						var year=$("#yearlist option:selected").val(); 
						var gradestype=$("#gradestype option:selected").val(); 
						window.location.href="CustomDownload?id="+id+"&department="+department+"&role="+role+"&year="+year+"&gradestype="+gradestype;
					}
				}, {
					html : "&nbsp; 下载分组统计表",
					"class" : "btn btn-default",
					click : function() {
						var id=$('#quizid').val();
						if(id==-1)
							return;
						var groupcondition=$("#groupcondition option:selected").val(); 
						window.location.href="GroupDownload?id="+id+"&condition="+groupcondition;
					}
				}]
			});