<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
</head>
<body class="easyui-layout">
	<!-- 每个div代表一个区域 -->
	<div title="XXXX管理系统" style="height: 100px" data-options="region:'north'">北部区域</div>
	<div title="系统菜单" style="width: 200px" data-options="region:'west'">
		<!-- 折叠面板效果 -->
		<!-- fit：true 自适应，适应父容器的大小 -->
		<div class="easyui-accordion" data-options="fit:true">
			<!-- 使用子div表示每个面板 -->
			<div data-options="iconCls:'icon-edit'" title="面板一">
				<a id="but1" class="easyui-linkbutton">按钮</a>
				<script type="text/javascript">
					$(function(){
						//页面加载完成后，为上面的按钮绑定事件
						$("#but1").click(function(){
							//判断当前传智播客选项卡是否已经存在
							var e = $("#mytabs").tabs("exists","传智播客");
							if(e){
								//已经存在，选中
								$("#mytabs").tabs("select","传智播客");
							}else{
								//不存在，添加
								//动态添加一个选项卡面板
								$("#mytabs").tabs("add",{
									iconCls:'icon-help',
									title: '传智播客',
									closable:true,
									content:'<iframe src="../abc.jsp" frameborder="0" width="100%" height="100%"></iframe>'
								});//调用tabs对象的add方法
							}
						});
					});
				</script>
			</div>
			
			<div title="面板二">
				内容二
			</div>
		</div>
	</div>
	<div data-options="region:'center'">
		<!-- 选项卡面板 -->
		<!-- fit：true 自适应，适应父容器的大小 -->
		<div id="mytabs" class="easyui-tabs" data-options="fit:true">
			<!-- 使用子div表示每个面板 -->
			<div data-options="iconCls:'icon-edit',closable:true" title="面板一">
				
			</div>
			<div title="面板二">
				内容二
			</div>
		</div>
	</div>
	<div style="height: 50px" data-options="region:'south'">南部区域</div>
</body>
</html>