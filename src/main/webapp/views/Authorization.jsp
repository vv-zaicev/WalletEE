<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">
<title>Авторизация</title>
</head>
<body>
	<div class="wrapper">
		<div class="container jcc">
			<form method="post" action="/wallet/start" class="authorization">
				<%
				boolean succes = (boolean) request.getAttribute("succes");
				if (!succes) {
				    out.println("<p>Не удалось авторизоваться</p>");
				}
				%>

				<input name="login" type="text" placeholder="Логин" class="input">

				<input name="pass" type="text" placeholder="Пароль" class="input">

				<button type="submit" class="button">Авторизация</button>

			</form>
		</div>
	</div>
</body>
</html>