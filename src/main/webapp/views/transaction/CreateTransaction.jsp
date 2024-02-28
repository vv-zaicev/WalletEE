<%@page import="javax.sql.rowset.CachedRowSet"%>
<%@page import="database.DatabaseController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
DatabaseController db = (DatabaseController) session.getAttribute("db");
%>
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">

<script src="${pageContext.request.contextPath}/js/Transaction.js">
	
</script>
<title>Запись</title>
</head>
<body>
	<div class="wrapper jcc">
		<div class="container jcc">
			<form method="post" action="transaction?action=submitCreate"
				class="update">
				<div class="selectTransactionType">
					<select name="type" id="selectTransactionType"
						onchange="changeTransactionType(this)">
						<option value="INCOME" class="green">Доход</option>
						<option value="EXPENSES" class="red" selected>Расход</option>
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

						    out.println(String.format("<option value=\"%d\" id=\"%sType\">%s</option>", transactionCategoryId, transactionTypeName, name));
						}
						%>
					</select>
				</div>
				<input type="number" step="0.01" name="sum" placeholder="Сумма"
					class="input" required> <input type="text" name="description"
					placeholder="Описание" class="input"> <input type="date"
					name="calendar" class="input" required>
				<button type="submit" class="button">Сохранить</button>
			</form>
		</div>
	</div>
</body>
</html>