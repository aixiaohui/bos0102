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
<script type="text/javascript">
	$(function(){
		$.messager.alert("标题内容","具体信息！","question");
		window.setTimeout(function(){
			$.messager.show({
				  title:'My Title',
				  msg:'Message will be closed after 5 seconds.',  	
				  timeout:5000,
				  showType:'slide'
				});
		}, 3000);
		$.messager.confirm('提示信息',"你确定要删除当前记录吗？",function(r){
			if(r){
				//提交请求
				alert('提交请求');
			}
		});
		$.messager.prompt('Prompt', 'Please enter your name:', function(r){if (r){ alert('Your name is:' + r); }}); 
		$.messager.progress();
		window.setTimeout(function(){
			$.messager.progress('close');
		}, 3000);
	});
</script>
</head>
<body>
</body>
</html>