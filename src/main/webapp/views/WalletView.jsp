<%@page import="java.io.Console"%>
<%@page import="javax.sql.rowset.CachedRowSet"%>
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
TransactionFilter filter = (TransactionFilter) session.getAttribute("filter");
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
<link href="${pageContext.request.contextPath}/css/walletview.css"
	rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/js/WalletView.js">
	
</script>
</head>
<body>
	<div class="wrapper">
		<div class="container">
			<form id="deleteFilter" method="post"
				action="/wallet/filter?action=delete"></form>
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
							<button type="button" class="down" id="collapsible">
								<svg fill="#FFF" width="30px" height="30px" viewBox="0 0 56 56" class="icon"
									<%if (filter != null) {
    									out.println("style='fill: #118DA8;'");
									}%>
									xmlns="http://www.w3.org/2000/svg">
								<path
										d="M 9.9647 50.2070 L 46.0351 50.2070 C 50.0195 50.2070 52.5040 47.3476 52.5040 43.7383 C 52.5040 42.6601 52.2227 41.5586 51.6367 40.5508 L 33.5663 9.0742 C 32.3476 6.9414 30.2147 5.7930 28.0116 5.7930 C 25.8319 5.7930 23.6757 6.9414 22.4335 9.0742 L 4.3632 40.5742 C 3.7772 41.5820 3.4960 42.6601 3.4960 43.7383 C 3.4960 47.3476 6.0038 50.2070 9.9647 50.2070 Z M 10.0116 46.5273 C 8.3710 46.5273 7.2929 45.1914 7.2929 43.7383 C 7.2929 43.3164 7.3632 42.8242 7.5976 42.3320 L 25.6444 10.8554 C 26.1600 9.9648 27.0976 9.5430 28.0116 9.5430 C 28.9257 9.5430 29.8163 9.9414 30.3319 10.8554 L 48.3789 42.3555 C 48.6131 42.8242 48.7303 43.3164 48.7303 43.7383 C 48.7303 45.1914 47.6051 46.5273 45.9882 46.5273 Z" /></svg>
							</button>
							<a href="wallet/transaction?action=create"
								class="transaction-create"> <img
								src="${pageContext.request.contextPath}/icons/create.svg"
								alt="create" class="icon">
							</a>
						</div>
						<form method="post" action="/wallet/filter?action=create"
							class="filters" id="filters">
							<div class="filters-row">
								<div class="filters-col">
									<div class="selectTransactionType">
										<select name="type" id="selectTransactionType"
											onchange="changeTransactionType(this)">
											<option disabled selected hidden id="placeholderType">Тип</option>
											<option id="nullType"></option>
											<option value="INCOME" class="green">Доход</option>
											<option value="EXPENSES" class="red">Расход</option>
										</select>
									</div>
								</div>
								<div class="filters-col">
									<div class="selectTransactionCategory">

										<select name="category" id="selectTransactionCategory" onchange="changeTransactionCat(this)">
											<option disabled selected hidden id="placeholderCat">Категория</option>
											<option id="nullCat"></option>
											<%
											CachedRowSet transactionCategoryInfo = db.getTransactionCategoriesInfo();
											while (transactionCategoryInfo.next()) {
											    String name = transactionCategoryInfo.getString("TransactionCategoryName");
											    String transactionTypeName = transactionCategoryInfo.getString("TransactionTypeName");
											    int transactionCategoryId = transactionCategoryInfo.getInt("Id");
											    out.println(String.format("<option value=\"%d\" id=\"%sType\">%s</option>", transactionCategoryId, transactionTypeName, name));
											}
											%>
										</select>
									</div>
								</div>
							</div>
							<div class="filters-row">
								<div class="filters-col">
									<input type="number" step="0.01" name="minSum"
										placeholder="Мин" class="input">
								</div>
								<div class="filters-col">
									<input type="number" step="0.01" name="maxSum"
										placeholder="Макс" class="input">
								</div>
							</div>
							<div class="filters-row">
								<div class="filters-col">
									<input type="date" name="minDate" placeholder="От"
										class="input">
								</div>
								<div class="filters-col">
									<input type="date" name="maxDate" placeholder="До"
										class="input">
								</div>
							</div>
							<div class="filters-row">
								<div class="filters-col">
									<button type="submit" class="button">Применить</button>
								</div>
								<div class="filters-col">
									<button form="deleteFilter" type="submit" class="button">Сбросить</button>
								</div>
							</div>



						</form>
						<div id="transactions"></div>
						<button class="show" id="show" style="display: inline-block"
							onclick="loadTransactions(0)">Еще</button>
						<button class="show" id="hide" style="display: none"
							onclick="loadTransactions(5)">Скрыть</button>
					</div>
				</div>
		</div>
	</div>
</body>
</html>