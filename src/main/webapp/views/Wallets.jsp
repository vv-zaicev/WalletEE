<%@page import="database.DatabaseController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Доступные кошельки</title>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div class="wrapper">
		<div class="container jcc">

			<div class="wallets">
				<p class=>Доступные кошельки</p>
				<%
				DatabaseController db = (DatabaseController) session.getAttribute("db");
				String path = request.getContextPath();
				for (String walletName : db.getWalletNames()) {
				    out.println(String.format("<a href=\"%s/view?wallet=%s\" class=\"button\">%s</a>", path, walletName, walletName));
				}
				%>
			</div>
		</div>
	</div>
</body>
</html>