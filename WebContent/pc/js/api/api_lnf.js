//审核通过
function pass(id)
		{
			$.post("PassLostnfound",
					 {
				 		id:id
					 },
					 function(data){
						if(data=="success")
							{
								alert("操作成功");
								load();
							}
						
					 });
		}
//审核不通过
		function unpass(id)
		{
			$.post("UnpassLostnfound",
					 {
				 		id:id
					 },
					 function(data){
						if(data=="success")
							{
								alert("操作成功");
								load();
							}
						
					 });
		}
		//进行删除
		function del(id)
		{
			$.post("DeleteLostnfound",
					 {
				 		id:id
					 },
					 function(data){
						if(data=="success")
							{
								alert("操作成功");
								load();
							}
						
					 });
		}
		//重新加载
		function load()
		{
			var type2=$("#type option:selected").val();
			var status=$("#status option:selected").val();
			$.post("GetLostnfoundlist",
					 {
				 		type:type2,
				 		status:status
					 },
					 function(data){
						$("#list").empty();
						var html="";
						for(i=0;i<data.length;i++)
						{
							var type="寻物";
							if(data[i].type==1)
								type="招领";
							var status="待审核"
							if(data[i].checked==1)
								status="已通过";
							else
								if(data[i].checked==2)
									status="已拒绝";
							var tr = document.createElement('acticle'); //创建元素
							tr.className = 'col-sm-4 col-md-4 col-lg-4';  
							var html="<div class='jarviswidget' id='wid-id-0' data-widget-colorbutton='false' data-widget-editbutton='false'><header><span class='widget-icon'> <i class='fa fa-eye'></i> </span>";
							html=html+"<h2>"+type+"</h2></header><div><div class='widget-body'><div class='row'><div class='col-md-6'>";
							html=html+"<img src='../wechat/Upload/"+data[i].path+"' class='img-responsive' alt='img'>";
							html=html+"</div><div class='col-md-6 padding-left-0'><h3 class='margin-top-0'><a href='javascript:void(0);'>"+data[i].title+"</a><br><small class='font-xs'><i>"+status+"</i></small></h3>";
							html=html+"<p>时间:"+data[i].date+"</p>";
							html=html+"<p>位置:"+data[i].position+"</p>";
							html=html+"<p>联系方式:"+data[i].contact+"</p>";
							html=html+"<p>详情:"+data[i].details+"</p>";
							if(data[i].checked==0)
							{
								html=html+"<a class='btn btn-success' href='javascript:pass("+data[i].id+")'>通过</a>";
								html=html+"<a class='btn btn-primary' href='javascript:unpass("+data[i].id+")'>不通过</a>";
							}
							html=html+"<a class='btn btn-warning' href='javascript:del("+data[i].id+")'>删除</a>";
							html=html+"</div></div></div></div></div>";
							tr.innerHTML=html;
							var field = document.getElementById('list'); //2、找到父级元素
							field.appendChild(tr);//插入到最左边
							}
					 });
		}
		load();