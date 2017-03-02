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
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<h3>数据表格使用方式一：将页面中静态的HTML代码渲染为datagrid样式</h3>
	<table class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name'">姓名</th>
				<th data-options="field:'age'">年龄</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>001</td>
				<td>小明</td>
				<td>88</td>
			</tr>
			<tr>
				<td>002</td>
				<td>老李</td>
				<td>3</td>
			</tr>
		</tbody>
	</table>
	
	<h3>数据表格使用方式二：发送ajax请求获取动态json数据构造datagrid</h3>
	<table class="easyui-datagrid" data-options="url:'../json/data.json'">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name'">姓名</th>
				<th data-options="field:'age'">年龄</th>
			</tr>
		</thead>
	</table>
	
	<h3>数据表格使用方式三：使用插件提供的API动态构造datagrid</h3>
	<table id="myGrid">
	</table>
	<script type="text/javascript">
		$(function(){
			$("#myGrid").datagrid({
				columns:[[
				          {id:'id',field:'id',title:'编号',checkbox:true},//每个json代表一列
				          {id:'name',field:'name',title:'姓名'},
				          {id:'age',field:'age',title:'年龄'}
				          ]],//定义标题行所有的列
				url:'../json/data.json',//由datagrid对象自动向URL地址发送ajax请求
				rownumbers:true,//显示行号
				singleSelect:true,//是否可以单选
				toolbar:[
				         	{text:'添加',iconCls:'icon-add',handler:function(){
				         		alert("add...");
				         	}},
				         	{text:'删除',iconCls:'icon-remove'}
				         ],//工具栏
				pagination:true,//显示分页条
				pageList:[10,20,30]
			});
		});
	</script>
</body>
</html>