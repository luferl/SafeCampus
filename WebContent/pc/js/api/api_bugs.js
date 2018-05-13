function showmodal(id)
		{
			$('#id').val(id);
			$('#myModal').modal('show');
		}
		function reply()
		{
			var result=$('#result').val();
			var id=$('#id').val();
			$('#result').val("");
			$('#id').val("");
			console.log(id);
			$('#myModal').modal('hide');
			$.post("ReplyBugs",
					 {
				 		id:id,
				 		reply:result
					 },
					 function(data){
						if(data=="success")
							{
								alert("操作成功");
								load();
							}
						
					 });
		}
		function load()
		{
			var status=$("#status option:selected").val();
			$.post("GetBuglist",
					 {
				 		status:status
					 },
					 function(data){
						$("#list").empty();
						var html="";
						for(i=0;i<data.length;i++)
						{
							var status="待处理"
							if(data[i].checked==1)
								status="已处理";
							var tr = document.createElement('acticle'); //创建元素
							tr.className = 'col-sm-4 col-md-4 col-lg-4';  
							var html="<div class='jarviswidget' id='wid-id-0' data-widget-colorbutton='false' data-widget-editbutton='false'><header><span class='widget-icon'> <i class='fa fa-eye'></i> </span>";
							html=html+"<h2>隐患</h2></header><div><div class='widget-body'><div class='row'><div class='col-md-6'>";
							html=html+"<img src='../wechat/Upload/"+data[i].path+"' class='img-responsive' alt='img'>";
							html=html+"</div><div class='col-md-6 padding-left-0'><h3 class='margin-top-0'><a href='javascript:void(0);'>"+data[i].title+"</a><br><small class='font-xs'><i>"+status+"</i></small></h3>";
							html=html+"<p>时间:"+data[i].date+"</p>";
							html=html+"<p>位置:"+data[i].position+"</p>";
							html=html+"<p>联系方式:"+data[i].contact+"</p>";
							html=html+"<p>详情:"+data[i].details+"</p>";
							html=html+"<p>处理结果:"+data[i].reply+"</p>";
							if(data[i].checked==0)
							{
								html=html+"<a class='btn btn-success' href='javascript:showmodal("+data[i].id+")'>进行处理</a>";
							}
							html=html+"</div></div></div></div></div>";
							tr.innerHTML=html;
							var field = document.getElementById('list'); //2、找到父级元素
							field.appendChild(tr);//插入到最左边
							}
					 });
		}
		load();