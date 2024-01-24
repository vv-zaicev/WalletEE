<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/css/mainstyle.css"
	rel="stylesheet" type="text/css">

<script type="text/javascript">
	window.onload = function() {
		var s = document.getElementById("selectTransactionType");
		changeColor(s);
	};

	function changeColor(select) {
		var o = select.options[select.selectedIndex];
		if (o.value == "INCOME")
			select.style.color = "green";
		else
			select.style.color = "red";
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
						onchange="changeColor(this)">
						<option value="INCOME" class="green">Доход</option>
						<option value="EXPENSES" class="red" selected>Расход</option>
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