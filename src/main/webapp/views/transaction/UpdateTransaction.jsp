<%@page import="javax.sql.rowset.CachedRowSet"%>
<%@page import="transactions.TransactionType"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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
DatabaseController db = (DatabaseController) session.getAttribute("db");
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
Wallet wallet = (Wallet) session.getAttribute("wallet");
int id = Integer.valueOf(request.getParameter("id"));
Transaction transaction = wallet.getTransaction(id);
%>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">
<title>Запись</title>
<script src="${pageContext.request.contextPath}/js/Transaction.js">
	
</script>
</head>
<body>
	<div class="wrapper">
		<div class="container jcc">
			<form method="post"
				action="transaction?action=submitUpdate&id=<%=id%>" class="update">
				<div class="selectTransactionType">
					<select name="type" id="selectTransactionType"
						onchange="changeTransactionType(this)">
						<option value="INCOME" class="green"
							<%if (transaction.type() == TransactionType.INCOME)
    							out.print("selected");%>>Доход</option>
						<option value="EXPENSES" class="red"
							<%if (transaction.type() == TransactionType.EXPENSES)
    							out.print("selected");%>>Расход</option>
					</select>
				</div>
				<div class="selectTransactionCategory">
					<select name="category" id="selectTransactionCategory">
						<%
						CachedRowSet transactionCategoryInfo = db.getTransactionCategoriesInfo();
						while (transactionCategoryInfo.next()) {
						    String name = transactionCategoryInfo.getString("TransactionCategoryName");
						    String transactionTypeName = transactionCategoryInfo.getString("TransactionTypeName");
						    int transactionCategoryId = transactionCategoryInfo.getInt("Id");
						    if (transaction.category().id() == transactionCategoryId) {
							out.println(String.format("<option value=\"%d\" id=\"%sType\" selected>%s</option>", transactionCategoryId, transactionTypeName, name));
						    } else {
							out.println(String.format("<option value=\"%d\" id=\"%sType\">%s</option>", transactionCategoryId, transactionTypeName, name));
						    }

						}
						%>
					</select>
				</div>
				<input type="number" step="0.01" name="sum"
					value="<%=transaction.sum()%>" placeholder="<%=transaction.sum()%>"
					class="input" required> <input type="text" name="description"
					<% if(transaction.description() != null && transaction.description().length() > 0) {
					    out.print(String.format("value='%s' ", transaction.description()));
					    out.print(String.format("placeholder='%s' ", transaction.description()));
					} else {
					    out.print("placeholder='Описание' ");
					}
					%> class="input">
				<input type="date" name="calendar"
					value="<%=dateFormat.format(transaction.calendar().getTime())%>"
					class="input" required>
				<button type="submit" class="button">Сохранить</button>
			</form>
		</div>

	</div>
</body>
</html>