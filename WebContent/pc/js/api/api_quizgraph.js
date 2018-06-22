//设置饼图菜单项
Highcharts.setOptions({
		    lang:{
		       contextButtonTitle:"图表导出菜单",
		       decimalPoint:".",
		       downloadJPEG:"下载JPEG图片",
		       downloadPDF:"下载PDF文件",
		       downloadPNG:"下载PNG文件",
		       downloadSVG:"下载SVG文件",
		       printChart:"打印图表"
		    }
		}); 
//获取饼图
		function ShowGraph()
		{
			if($('#quizid').val()==-1)
				{
					alert("请先选择试卷！");
					
				}
			else
				{
				$.get("ShowGraph",{
					id:$('#quizid').val(),
					college:$("#college option:selected").val()
				},function(data){
					var attendrate=data.attend/data.total;
					var passrate=data.pass/data.total;
					$(function () {
					    $('#involvedrate').highcharts({
					        chart: {
					            plotBackgroundColor: null,
					            plotBorderWidth: null,
					            plotShadow: false
					        },
					        title: {
					            text: $("#college option:selected").val()+'学院参加考试人数比例'
					        },
					        tooltip: {
					            headerFormat: '{series.name}<br>',
					            pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
					        },
					        plotOptions: {
					            pie: {
					                allowPointSelect: true,
					                cursor: 'pointer',
					                dataLabels: {
					                    enabled: true,
					                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					                    style: {
					                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					                    }
					                }
					            }
					        },
					        series: [{
					            type: 'pie',
					            name: '人数比例',
					            data: [
					                ['未参加考试',1-attendrate],
					                ['参加考试',attendrate]
					            ]
					        }]
					    });
					    $('#passrate').highcharts({
					        chart: {
					            plotBackgroundColor: null,
					            plotBorderWidth: null,
					            plotShadow: false
					        },
					        title: {
					            text: $("#college option:selected").val()+'学院及格率'
					        },
					        tooltip: {
					           pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
					        },
					        plotOptions: {
					            pie: {
					                allowPointSelect: true,
					                cursor: 'pointer',
					                dataLabels: {
					                    enabled: true,
					                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					                    style: {
					                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					                    }
					                }
					            }
					        },
					        series: [{
					            type: 'pie',
					            name: '人数比例',
					            data: [
					                ['及格',passrate],
					                ['不及格',1-passrate]
					            ]
					        }]
					    });
					});
				}
				);
				}
		}
		$(function () {
			//考试参与人数图初始化
			$('#container').highcharts({
				chart: {
					type: 'spline',
					animation: Highcharts.svg, // don't animate in old IE
					marginRight: 10,
					events: {
						load: function() {
							// set up the updating of the chart each second
							var series = this.series[0];
							 chart = this;
							setInterval(function() {
										//建立一个ajax请求，每一秒向loadlog.php请求一次数据，返回到jsonobj中
										var jsonobj=$.ajax({
											url:"GetCurCount",
											data:{id:$('#quizid').val()},
											//async设定为false，表明这是同步请求，确保数据获取之后在执行后续步骤
											async:false,
										});
										var x,y;
										//获取的数据序列化
										var jjj=jsonobj.responseJSON;
										//赋值给x和y
										var time = (new Date()).getTime(),
										x=parseInt(time);
										y=parseInt(jjj.count);
										series.addPoint([x, y], true, true);
										activeLastPointToolip(chart)
										//1000表明1s执行一次
									}, 1000);
								}
							}
					}
				,
				title: {
					//表名
					text: '当前参加考试总人数'
				},
				xAxis: {
					type: 'datetime',
					tickPixelInterval: 100
				},
				yAxis: {
					title: {
						//纵坐标相关信息
						text: '人数'
					},
					plotLines: [{
						value: 0,
						width: 20,
						color: '#808080'
					}]
				},
				tooltip: {
					formatter: function() {
							return '<b>'+ this.series.name +'</b><br/>'+
							Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+this.y;
					}
				},
				legend: {
					enabled: false
				},
				exporting: {
					enabled: false
				},
				series: [{
					//这里是表格初始化时所用参数，我们把data设置为tempdata，让程序用tempdata数组对表格进行初始化
					name: '当前人数',
					data:
						(function () {
			            // generate an array of random data
			            var data = [],
			                time = (new Date()).getTime(),
			                i;
			            for (i = -9; i <= 0; i += 1) {
			                data.push({
			                    x: time + i * 1000,
			                    y: 0
			                });
			            }
			            return data;			        
				}())
				}]
			}, function(c) {
			    activeLastPointToolip(c)
			});
		});
		function activeLastPointToolip(chart) {
		    var points = chart.series[0].points;
		    chart.tooltip.refresh(points[points.length -1]);
		}
			var id=0;
			var knowledgelist;
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
							{
								$("#collegelist").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
								$("#college").append("<option value='"+data[i]+"'>"+data[i]+"</option>"); 
							}
							
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
			//更改试卷时触发
			function changechoices(node)
			{
				console.log(node.id);
				issimulate=node.issimulate;
				$('#quizid').val(node.id);
				document.getElementById('quizname').innerText=node.text; 
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
			//自定义下载
			$('#custom_download').click(function() {
				$('#dialog_simple').dialog('open');
				return false;
			});
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
			
