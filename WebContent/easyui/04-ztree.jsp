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
<link rel="stylesheet" href="${pageContext.request.contextPath }/js/ztree/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.js"></script>
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
				<ul id="ztree1" class="ztree"></ul>
				<script type="text/javascript">
					$(function(){
						//使用ztree插件动态创建一个ztree树---使用标准json数据构造
						var setting = {};//指定ztree的相关属性设置
						//构造ztree树上的节点数据
						var znodes = [
						              {name:'节点一',children:[
						                                    	{name:'111',children:[
						                                    	                      {name:'111-1'}
						                                    	                      ]},
						                                    	{name:'222'}
						                                    ]},//每个json对象对应一个节点数据
						              {name:'节点二'},
						              {name:'节点三'}
						              ];
						//调用ztree的API动态创建ztree
						$.fn.zTree.init($("#ztree1"), setting, znodes);
					});
				</script>
			</div>
			<div title="面板三">
				<ul id="ztree2" class="ztree"></ul>
				<script type="text/javascript">
					$(function(){
						//使用ztree插件动态创建一个ztree树---使用简单json数据构造
						var setting2 = {
								data: {
									simpleData: {
										enable: true//使用简单格式的json数据
									}
								}
						};//指定ztree的相关属性设置
						//构造ztree树上的节点数据
						var znodes2 = [
							              {id:'1',pId:'0',name:'节点一'},//每个json对象对应一个节点数据
							              {id:'11',pId:'1',name:'111'},
							              {id:'111',pId:'11',name:'111-1'},
							              {id:'2',pId:'0',name:'节点二'},
							              {id:'3',pId:'0',name:'节点三'}
						              ];
						//调用ztree的API动态创建ztree
						$.fn.zTree.init($("#ztree2"), setting2, znodes2);
					});
				</script>
			</div>
			<div title="面板四">
				<ul id="ztree3" class="ztree"></ul>
				<script type="text/javascript">
					$(function(){
						//页面加载完成后，发送ajax请求，获取json数据---ajax post get load getScript getJSON
						var url = "${pageContext.request.contextPath }/json/menu.json";
						$.post(url,{},function(data){
							//使用返回的json数据构造ztree
							var setting3 = {
								data: {
									simpleData: {
										enable: true//使用简单格式的json数据
									}
								},
								callback: {
									onClick: function(event, treeId, treeNode){
										//判断当前是否需要添加选项卡
										if(treeNode.page != undefined){
											//判断选项卡是否已经存在
											var e = $("#mytabs").tabs("exists",treeNode.name);
											if(e){
												//已经存在，选中
												$("#mytabs").tabs("select",treeNode.name);
											}else{
												//动态添加选项卡
												$("#mytabs").tabs("add",{
													iconCls:'icon-help',
													title: treeNode.name,
													closable:true,
													content:'<iframe src="'+treeNode.page+'" frameborder="0" width="100%" height="100%"></iframe>'
												});//调用tabs对象的add方法
											}
										}
									}//为ztree上的节点绑定单击事件
								}
						};//指定ztree的相关属性设置
							//调用ztree的API动态创建ztree
						$.fn.zTree.init($("#ztree3"), setting3, data);
						},'json');
					});
				</script>
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