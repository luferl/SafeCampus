var id=0;
			var knowledgelist;
			$(document).ready(function() {
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
			$.get("GetQuizlist_D",
				function(data){
					$('#tree').treeview({
						data: data,
						onNodeSelected: function(event, node) {
							changechoices(node);
						}
					});
				}
			);
			function changechoices(node)
			{
				console.log(node.id);
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
			function revertquiz(){
				$.get("RevertQuiz",
						{id: $('#quizid').val()},
						function(data){
								if(data=="success")
									{
										alert("还原成功");
										window.location.reload();
									}
								else
									{
										alert("还原失败，可能是还未选择试卷");
										window.location.reload();
									}
						}
					);
			}
			function deletequiz(){
				$.get("DeleteQuiz_D",
						{id: $('#quizid').val()},
						function(data){
								if(data=="success")
									{
										alert("彻底删除试卷成功");
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