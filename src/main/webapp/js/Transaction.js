function changeTransactionType(select) {
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

	for (var category of categories) {
		if (category.id.toLowerCase() == type) {
			category.style.display = "block";
			if (changedType && first) {
				category.selected = true;
				first = false;
			}
		} else {
			category.style.display = "none";
		}
	}
}

window.onload = function() {
	var s = document.getElementById("selectTransactionType");
	changeTransactionType(s);
};