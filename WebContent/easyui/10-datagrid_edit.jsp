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
<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.ocupload-1.1.2.js"></script>
</head>
<body>
	<table id="myGrid">
	</table>
	<script type="text/javascript">
		var index = -1;//全局变量，用于存储当前正在编辑的行的索引
		$(function(){
			$("#myGrid").datagrid({
				columns:[[
				          {id:'id',field:'id',title:'编号',checkbox:true},//每个json代表一列
				          {id:'name',field:'name',title:'姓名',
				        	  editor:{
				        		  type:'validatebox',
				        		  options:{required: true}
				        	  }
				          },
				          {id:'age',field:'age',title:'年龄',
				        	  editor:{
				        		  type:'validatebox',
				        		  options:{required: true}
				        	  }  
				          }
				          ]],//定义标题行所有的列
				url:'../json/data.json',//由datagrid对象自动向URL地址发送ajax请求
				rownumbers:true,//显示行号
				singleSelect:true,//是否可以单选
				//监听结束编辑状态的事件
				onAfterEdit:function(rowIndex,data,changes){
					//发送ajax请求，将数据提交到Action，操作数据库
					$.post("xxx.action",data,function(data){
						
					});
				},
				toolbar:[
				         	{text:'添加',iconCls:'icon-add',handler:function(){
				         		$("#myGrid").datagrid("insertRow",{
				         			index:0,//在第一行插入数据
				         			row:{}//空行
				         		});
				         		index = 0;
				         		$("#myGrid").datagrid("beginEdit",index);
				         	}},
				         	{text:'删除',iconCls:'icon-remove',handler:function(){
				         		
				         		//获取当前选中的行
				         		var rows = $("#myGrid").datagrid("getSelections");
				         		if(rows.length > 0){
				         			var row = rows[0];
					         		index = $("#myGrid").datagrid("getRowIndex",row);
					         		$("#myGrid").datagrid("deleteRow",index);
				         		}
				         		//$.post();
				         		//获取当前选中行的索引
				         	}},
				         	{text:'编辑',iconCls:'icon-edit',handler:function(){
				         		//获取当前选中的行
				         		var rows = $("#myGrid").datagrid("getSelections");
				         		if(rows.length > 0){
				         			var row = rows[0];
					         		index = $("#myGrid").datagrid("getRowIndex",row);
					         		$("#myGrid").datagrid("beginEdit",index);
				         		}
				         	}},
				         	{text:'保存',iconCls:'icon-save',handler:function(){
				         		$("#myGrid").datagrid("endEdit",index);
				         	}}
				         ]//工具栏
				
			});
		});
	</script>
</body>
</html>