<%@ page import="com.myCRM.Settings.domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
String basePath = request.getScheme() + "://" +request.getServerName() + ":"+
request.getServerPort()+request.getContextPath()+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){


		//展开模态框之前，需要访问后端的数据，所以需要发送ajax

		$.ajax({
			url:"workbench/activity/getUserList.do",
			date:{

			},
			type:"get",
			dataType:"json",
            /*后端传过来的应该是json数组，里面都是user对象*/
			success:function (date) {
				//处理传来的json数据
				var html = "<option></option>";		//这里默认的选项应该是用户本身的名字
				$.each(date,function (i,n) {
					html += "<option value='"+n.id+"'>"+n.name+"</option>"
				})

				//将数据拼接
				$("#create-owner").html(html);

			}

		})

		$("#addBtn").click(function () {
			//开启模态框方式：模态框窗口对象.modol方法（show/hide）	展开或者隐藏
			$("#createActivityModal").modal("show");

			//创建时间样式（复制）
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			//将登录者设成默认选项
			<%
					User user = (User) session.getAttribute("user");
			%>
			$("#create-owner").val("<%=user.getId()%>");

		})


		$("#insertBtn").click(function () {
			$.ajax({
				url:"workbench/activity/insert.do",
				data: {
					"owner":$("#create-owner").val(),
					"name":$("#create-name").val(),	//事件名称
					"startDate":$("#create-startTime").val(),
					"endDate":$("#create-endTime").val(),
					"cost":$("#create-cost").val(),
					"description":$("#create-describe").val(),
					"createBy":$("#create-owner").find("option:selected").text()
				},
				type: "post",
				dataType: "json",
				success:function (date) {

					/*
						注意：如果是清空表单的数据，不能直接调用jquery对象的reset方法，这个方法没用，
							 但是idea会提醒，正确做法是转换成dom对象，然后调用js的reset方法。
					* */
					//获取表单对象并转化成dom对象
					$("#insertForm")[0].reset();


					//请求成功以后关闭模态框（在这之前需要将表单的数据清空）
					//不然下次打开还是之前保存的数据
					$("#createActivityModal").modal("hide");

					//添加操作之后需要保留展示的记录条数，并保持第一页的显示
					getList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

				}
			})
		})

		//页面刷新的时候需要访问数据库实时更新
		getList(1,2);


		/*搜索功能*/
		$("#searchBtn").click(function () {
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			//将数据存入隐藏域
			$("#hidden-name").val($.trim($("#searchName").val()));
			$("#hidden-owner").val($.trim($("#searchOwner").val()));
			$("#hidden-startDate").val($.trim($("#searchStartTime").val()));
			$("#hidden-endDate").val($.trim($("#searchEndTime").val()));


			//getList(1,2);
			//保留展示的页数
			getList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
		})

		//复选框
		$("#checkBox").click(function () {
			//将所有的复选框都选上，给其他复选框都附上name属性，这样就可以一下子拿一组
			$("input[name= 'ch']").prop("checked",this.checked);
		})

		//全选时自动打勾
		$("#getList").on("click",$("input[name= ch]"),function () {
			$("#checkBox").prop("checked",$("input[name = ch]").length == $("input[name = ch]:checked").length)
		})


		//修改
		$("#editBtn").click(function () {
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			var parameter = "";
			//获取复选框的value值
			var $checkbox  = $("input[name = 'ch']:checked");
			if ($checkbox.length == 0){
				alert("请选择需要修改的事件")
			}else if ($checkbox.length > 1){
				alert($checkbox.val())
				alert("一次只能修改一个哦！")
			}else {
				parameter = $checkbox.val();
			}

			//展开修改的模态框之前需要走后台将数据取出来
			$.ajax({
				url:"workbench/activity/edit.do",
				data:{
					"id":parameter
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					var html = "<option >"+data.activity.createBy+"</option>";
					$.each(data.list,function (i,n) {
						html += "<option value='"+n.id+"'>"+n.name+"</option>"
					})
					//拼接数据
					$("#edit-marketActivityOwner").html(html);
					//设计默认用户
					$("#edit-marketActivityOwner").val(""+data.activity.id+"");

					//将后端提取的数据展现出来
					$("#edit-id").val(data.activity.id);
					$("#edit-marketActivityName").val(data.activity.name);
					$("#edit-startTime").val(data.activity.startDate);
					$("#edit-endTime").val(data.activity.endDate);
					$("#edit-cost").val(data.activity.cost);
					$("#edit-describe").val(data.activity.description);



					//成功之后开启模态框
					$("#editActivityModal").modal("show");
				}
			})

		})

		//更新数据
		$("#updateBtn").click(function () {
			//获取页面数据，将数据发送给后端

			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var createBy = $("#edit-marketActivityOwner").find("option:selected").text();
			var name = $("#edit-marketActivityName").val();
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var cost = $("#edit-cost").val();
			var describe = $("#edit-describe").val();


			//传到后台
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":id,
					"owner":owner,
					"createBy":createBy,
					"name":name,
					"startDate":startDate,
					"endDate":endDate,
					"cost":cost,
					"describe":describe
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.success){
						alert("修改成功")
					}else {
						alert("修改失败")
					}

					//关闭模态框
					$("#editActivityModal").modal("hide");
					//刷新页面
					//getList(1,2);
					//更新之后需要保留当前页面和展示的条数
					getList($("#activityPage").bs_pagination('getOption', 'currentPage')
							,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
				}

			})
		})

		//删除
		$("#deleteBtn").click(function () {
			var parameter = "";
			//获取复选框元素
			var checkbox = $("input[name='ch']:checked");
			if (checkbox.length == 0){
				alert("请选择要删除的序号")
				//到这里说明用户肯定选了
			}else {
				for (var i = 0;i < checkbox.length;i++){
					//将dom对象重新转成jquery对象
					parameter += "id="+ $(checkbox[i]).val();//注意加id=
					if(i < checkbox.length -1){
						parameter += "&";
					}
				}

				//删除的时候给用户提示
				if (confirm("确定删除吗？")){
					/*发送ajax请求*/
					$.ajax({
						url:"workbench/activity/delete.do",
						data: parameter,
						type:"post",
						dataType:"json",
						success:function (data) {
							if (data.success){
								alert("删除成功");
								//删除成功后自动刷新页面
								getList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							}else {
								alert("删除失败")
							}
						}
					})
				}

			}


		})


	});


	//市场页面查询函数
	function getList(pageNo,pageSize){
		//创建时间样式（复制）
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//查询之前将数据从隐藏域取出来
		$("#searchName").val($.trim($("#hidden-name").val()));
		$("#searchOwner").val($.trim($("#hidden-owner").val()));
		$("#searchStartTime").val($.trim($("#hidden-startDate").val()));
		$("#searchEndTime").val($.trim($("#hidden-endDate").val()));

		//每次刷新页面的时候将全选的那个勾取消掉
		$("#checkBox").prop("checked",false)

		/*
		* pageNo:页码
		* pageSize:每页记录条数*/
		$.ajax({
			url:"workbench/activity/getList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$("#searchName").val(),	//事件名称
				"owner":$("#searchOwner").val(),	//创建人
				"startDate"	:$("#searchStartTime").val(),
				"endDate" : $("#searchEndTime").val()
			},
			type:"get",
			dataType:"json",
			success:function (date) {
				var html = "";

				$.each(date.list,function (i,n) {
					html += "<tr>"
					html += "<td><input name='ch' type=\"checkbox\" value='"+n.id+"'/></td>";
					html += '<td><a style="text-decoration: none; cursor: pointer;\" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\'">'+n.name+'</a></td>"';
					html += "<td>"+n.owner+"</td>";
					html += "<td>"+n.startDate+"</td>";
					html += "<td>"+n.endDate+"</td>";
					html += "</tr>"
				} )

				$("#getList").html(html);

				//分页组件
				var totalPages = date.total % pageSize == 0 ? date.total/pageSize : parseInt(date.total/pageSize)+1;
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: date.total, // 总记录条数

					visiblePageLinks: 5, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						getList(data.currentPage , data.rowsPerPage);
					}

					//这里有一个问题，当用户没点查询的时候，但你往搜索框里填了数据，这时候
					//点下一页的话就会搜索当前搜索框的内容。

					//解决方法:将数据存入隐藏域当中
				});

			}
		})

	}

	
</script>
</head>
<body>

	<%--隐藏域--%>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="insertForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">


								</select>
							</div>
                            <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" readonly class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label ">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" readonly class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="insertBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<%--隐藏域存放后端传来的id--%>
	<input type="hidden" id="edit-id">

	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input id="searchName" class="form-control" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input id="searchOwner" class="form-control" type="text">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="searchStartTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="searchEndTime">
				    </div>
				  </div>
				  
				  <button id="searchBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">

					<%--
						这里前端写好的模态框不能进行功能的拓展，所以要用jquery重写

					--%>

				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"  id="deleteBtn" ><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="checkBox" type="checkbox" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="getList">
						<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<%--分页控件--%>
				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>