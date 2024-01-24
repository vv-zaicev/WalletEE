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
		changeColor(s);
	};

	function changeColor (select) {
		var o = select.options[select.selectedIndex];
		if (o.value == "INCOME")
			select.style.color = "green";
		else
			select.style.color = "red";
	}
</script>
</head>
<body>
	<div class="wrapper">
		<div class="container jcc">
			<form method="post"
				action="transaction?action=submitUpdate&id=<%=id%>"
				class="update">
				<div class="selectTransactionType">
					<select name="type" id="selectTransactionType" onchange="changeColor(this)">
						<option value="INCOME" class="green" <% if(transaction.type() == TransactionType.INCOME) out.print("selected"); %>>Доход</option>
						<option value="EXPENSES" class="red" <% if(transaction.type() == TransactionType.EXPENSES) out.print("selected"); %>>Расход</option>
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