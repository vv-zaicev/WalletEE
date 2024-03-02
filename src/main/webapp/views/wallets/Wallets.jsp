<%@page import="transactions.Wallet"%>
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
	<link href="${pageContext.request.contextPath}/css/wallets.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div class="wrapper jcc">
		<div class="container jcc">

			<div class="wallets">
				<div class="wallets-bar">
					<div class="titles">Доступные кошельки</div>
					<a href="/wallets?action=create" class="wallets-create">
						<img src="${pageContext.request.contextPath}/icons/create.svg"
						alt="create" class="icon">
					</a>
				</div>
				<%
				DatabaseController db = new DatabaseController();
				session.setAttribute("db", db);

				String path = request.getContextPath();
				for (Wallet wallet : db.getWallets()) {
				    out.println("<div class=\"wallet\">");
				    out.println(String.format("<a href=\"%s/wallet?id=%s\" class=\"button\">", path, wallet.getId(), wallet.getName()));
				    out.println(String.format("<div class=\"wallet-name\">%s</div>", wallet.getName()));
				    out.println(String.format("<div class=\"wallet-balance\">%.2f</div>", wallet.getBalance().doubleValue()));
				    out.println("</a>");
				    out.println(String.format("<div class=\"wallet-update\"><a href='/wallets?action=update&id=%d'><img src=\"/icons/update.svg\" alt='update' class='icon'></a></div>", wallet.getId()));
				    out.println(String.format("<form method='post' action='/wallets?action=submitDelete&id=%d' class='wallet-remove'><button type='submit'><img src=\"/icons/delete.svg\" alt='delete' class='icon'></button></form>", wallet.getId()));
				    out.println("</div>");
				}
				%>
			</div>
		</div>
	</div>
</body>
</html>