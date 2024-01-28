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
<script type="text/javascript">
	window.onload = function() {
		var s = document.getElementById("selectTransactionType");
		changeTransactionType(s);
	};

	function changeTransactionType (select) {
		var o = select.options[select.selectedIndex];
		var type = o.value.toLowerCase() + "type";
		if (o.value == "INCOME")
			select.style.color = "green";
		else
			select.style.color = "red";
		
		var categoryList = document.getElementById("selectTransactionCategory");
		var categories = categoryList.getElementsByTagName("option");
		
		var changedType = categoryList.options[categoryList.selectedIndex].id.toLowerCase() != type;
		var first = true;
		
		for (var category of categories){
			if (category.id.toLowerCase() == type){
				category.style.display = "block";
				if (changedType && first){
					category.selected = true;
					first = false;
				}
			} else {
				category.style.display = "none";
			}
		}
	}
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
							if (transaction.category().id() == transactionCategoryId){
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
					class="input"> <input type="text" name="description"
					value="<%=transaction.descriprion()%>"
					placeholder="<%=transaction.descriprion()%>" class="input">
				<input type="date" name="calendar"
					value="<%=dateFormat.format(transaction.calendar().getTime())%>"
					class="input">
				<button type="submit" class="button">Сохранить</button>
			</form>
		</div>

	</div>
</body>
</html>