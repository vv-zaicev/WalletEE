<%@page import="java.util.List"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.stream.Collector"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="transactions.TransactionType"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="transactions.filter.TransactionFilter"%>
<%@page import="transactions.Transaction"%>
<%@page import="transactions.Wallet"%>
<%@page import="database.DatabaseController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
String path = request.getContextPath();
String updateImg = String.format("<img src=\"%s/icons/update.svg\" alt=\"update\" class=\"icon\">", path);
String deleteImg = String.format("<img src=\"%s/icons/delete.svg\" alt=\"delete\" class=\"icon\">", path);

DatabaseController db = (DatabaseController) session.getAttribute("db");
String walletName = request.getParameter("name");
db.setCurrentWallet(walletName);
Wallet wallet = db.getWallet(1);
session.setAttribute("wallet", wallet);

BigDecimal divider = wallet.getIncome().max(wallet.getExpenses());
BigDecimal incomePercent = null;
BigDecimal expensesPercent = null;
if (!divider.equals(BigDecimal.ZERO)) {
	incomePercent = wallet.getIncome().divide(divider, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	expensesPercent = wallet.getExpenses().divide(divider, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
} else {
	incomePercent = new BigDecimal(100);
	expensesPercent = new BigDecimal(100);
}
%>
<title><%=walletName%></title>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/WalletView.js">
	
</script>
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<div class="row">
				<div class="general-statistic">
					<div class="col">
						<div class="wallet">
							<div class="wallet-info">
								<div class="wallet-name"><%=wallet.getWalletName()%></div>
								<div class="balance">
									Баланс:<%=String.format("%.2f", wallet.getBalance().doubleValue())%></div>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="cash-info">
							<div class="cash-parms">
								<div class="parm">Денежный поток</div>
								<div class="value"><%=String.format("%.2f", wallet.getIncome().subtract(wallet.getExpenses()).doubleValue())%></div>
							</div>
							<div class="line"></div>
							<div class="cash-parms green">
								<div class="parm">Доход</div>
								<div class="value"><%=String.format("%.2f", wallet.getIncome().doubleValue())%></div>
							</div>
							<div class="income-bar"
								style="width: <%=incomePercent.doubleValue()%>%"></div>
							<div class="cash-parms red">
								<div class="parm">Расход</div>
								<div class="value"><%=String.format("%.2f", wallet.getExpenses().doubleValue())%></div>
							</div>
							<div class="expenses-bar"
								style="width: <%=expensesPercent.doubleValue()%>%"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">

				<div class="transactions">
					<div class="tool-bar">
						<a href="wallet/transaction?action=create"
							class="transaction-create"> <img
							src="${pageContext.request.contextPath}/icons/create.svg"
							alt="create" class="icon">
						</a>
					</div>
					<div id="transactions"></div>
					<button class="button" onclick="loadTransactions(0)">LOAD</button>

				</div>
			</div>
		</div>
	</div>
</body>
</html>