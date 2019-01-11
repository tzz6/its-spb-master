<!doctype html>
<!--[if lt IE 9 ]><html dir="ltr" lang="zh-cmn-Hans" class="lt9"><![endif]-->
<!--[if IE 9 ]><html dir="ltr" lang="zh-cmn-Hans" class="ie9"><![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html dir="ltr" lang="zh-cmn-Hans"> <!--<![endif]-->
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="keywords" content="your keywords">
    <meta name="description" content="your description">
<title>SpringMVC整合Freemarker</title>
</head>
<body>
	<table>
		<tr>
			<td>
				<font size="6">
				<center>SpringMVC整合Freemarker</center>
				</font>
			</td>
		<tr>
		<tr>
			<td>用户id</td>
			<td>用户名</td>
			<td>性别</td>
			<td>注册日期</td>
			<td>部门</td>
			<td>岗位</td>
		<tr>
		[#list users as user]
			<tr>
				<td>${user.id }</td>
				<td>${user.name }</td>
				<td>${user.sex }</td>
				<td>${user.regDate }</td>
				<td>${user.department.name }</td>
				<td>
					[#list user.roles  as role]
						${role.name }
			        [/#list]
				</td>
			<tr>
        [/#list]
	</table>
</body>
</html>