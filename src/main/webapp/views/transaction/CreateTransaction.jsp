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
<title>Запись</title>
</head>
<body>
	<div class="wrapper">
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
					class="input"> <input type="text" name="description"
					placeholder="Описание" class="input"> <input type="date"
					name="calendar" class="input">
				<button type="submit" class="button">Сохранить</button>
			</form>
		</div>
	</div>
</body>
</html>