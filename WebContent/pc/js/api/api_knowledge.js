		$.get("GetKnowledge",
				function(data){
					for(i=0;i<data.length;i++)
					{
						//count++;
						var know = document.createElement('div'); //1、创建元素
						know.setAttribute("class", "form-group"); 
						know.innerHTML="<label class='col-md-2 control-label'>知识点</label><div class='col-md-8'><input class='form-control' placeholder='' type='text' id='"+data[i].id+"' value='"+data[i].text+"'></div></div><a href='#' class='delete btn btn-labeled btn-danger'> <span class='btn-label'><i class='glyphicon glyphicon-trash'></i></span>删除 </a>"
						var field = document.getElementById('knowledge'); //2、找到父级元素
						field.appendChild(know);//插入到最左边
						$('.delete').click(function(){
							$(this).parent().remove();
							//count--;
						});
					}
				//$("#schoolname").val(data.schoolname);
				//$("#schoolcode").val(data.schoolcode);
				}
			);
			function addOneRow()
			{
				//count++;
				var know = document.createElement('div'); //1、创建元素
				know.setAttribute("class", "form-group"); 
				know.innerHTML="<label class='col-md-2 control-label'>知识点</label><div class='col-md-8'><input class='form-control' placeholder='' type='text'></div></div><a href='#' class='delete btn btn-labeled btn-danger'> <span class='btn-label'><i class='glyphicon glyphicon-trash'></i></span>删除 </a>"
				var field = document.getElementById('knowledge'); //2、找到父级元素
				field.appendChild(know);//插入到最左边
				$('.delete').click(function(){
					$(this).parent().remove();
					//var str=$("label.control-label:last").text();
					//count=str.substr(str.length-1,1);
				})
			};
			function delOne(x)
			{
				row.parentNode.removeChild(row);
			}
			function saveknowledge()
			{
				var knowledge=[];
				var obj=$(".form-control");
				for(i=0;i<obj.length;i++)
				{
					var know=new Object();
					if(obj[i].id=="")
						{
							know.id=-1;
						}
					else
						know.id=obj[i].id;
					know.value=obj[i].value;
					knowledge.push(know);
				}
				var str=JSON.stringify(knowledge);
				console.log(str);
				$.post("SaveKnowledge",{knowledge:str},
						function(data){
								alert("保存成功!");
								window.location.reload();
							}
						//$("#schoolname").val(data.schoolname);
						//$("#schoolcode").val(data.schoolcode);
					);
			};