<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">
<title>Авторизация</title>
</head>
<body>
	<div class="wrapper jcc">
		<div class="container jcc">
			<form method="post" action="j_security_check" class="authorization">
				<%
				Object succes = request.getAttribute("succes");
				if (succes != null && !(boolean) succes) {
				    out.println("<p>Не удалось авторизоваться</p>");
				}
				%>

				<input name="j_username" type="text" placeholder="Логин" class="input">

				<input name="j_password" type="password" placeholder="Пароль" class="input">

				<button type="submit" class="button" value="login">Авторизация</button>

			</form>
		</div>
	</div>
</body>
</html>