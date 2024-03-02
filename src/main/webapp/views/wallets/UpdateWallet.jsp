<%@page import="transactions.Wallet"%>
<%@page import="database.DatabaseController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
DatabaseController db = (DatabaseController) session.getAttribute("db");
int id = Integer.valueOf(request.getParameter("id"));
Wallet wallet = db.getWallet(id);
%>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">
<title>Кошелек</title>
</head>
<body>
	<div class="wrapper jcc">
		<div class="container jcc">
			<form method='post'
				action='/wallets?action=submitUpdate&id=<%=wallet.getId()%>'
				class="create">
				<input type="text" name="name" placeholder="Название" class="input"
					required value="<%=wallet.getName()%>"> <input
					type="number" step="0.01" name="balance" placeholder="Баланс"
					class="input" required value="<%=wallet.getBalance()%>">
				<button type="submit" class="button">Сохранить</button>
			</form>
		</div>
	</div>
</body>
</html>