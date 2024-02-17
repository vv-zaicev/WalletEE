function loadTransactions(count) {
	fetch('/wallet/transaction?action=getTransactions&count=' + count)
		.then(function(response) {
			return response.json();
		}).then(function(apiJsonData) {
			fillTransactions(apiJsonData['transactions']);
			var show = document.getElementById("show");
			var hide = document.getElementById("hide");

			if (apiJsonData['hasMoreTransactions'] == true) {
				show.style.display = "inline-block";
				hide.style.display = "none";
			} else if (apiJsonData['transactions'].length <= 5) {
				show.style.display = "none";
				hide.style.display = "none";
			} else {
				hide.style.display = "inline-block";
				show.style.display = "none";
			}
		});


}

function fillTransactions(apiJsonData) {
	const trasactions = document.getElementById("transactions");
	while (trasactions.firstChild) {
		trasactions.removeChild(trasactions.lastChild);
	}
	apiJsonData.forEach(data => {
		let newRow = createTransaction(data);
		trasactions.appendChild(newRow);
	});

}

function createTransaction(data) {
	let transaction = document.createElement('div');
	transaction.classList.add('transaction');

	transaction.appendChild(createLeftTransaction(data));
	transaction.appendChild(createRightTransaction(data));
	transaction.appendChild(createUpdate(data));
	transaction.appendChild(createRemove(data));

	return transaction;
}

function createLeftTransaction(data) {
	let left = document.createElement('div');
	left.classList.add('transaction-left');

	let cat = document.createElement('div');
	cat.classList.add('transaction-cat');
	cat.innerText = data['category']['name'];
	left.appendChild(cat);

	let des = document.createElement('div');
	des.classList.add('transaction-des');
	des.innerText = data['description'];
	left.appendChild(des);

	return left;
}

function createRightTransaction(data) {
	let right = document.createElement('div');
	right.classList.add('transaction-right');

	let sum = document.createElement('div');
	sum.classList.add('transaction-sum');
	let sumValue = data['type'] == 'INCOME' ? '+' : '-';
	sumValue += data['sum'];
	sum.style.color = data['type'] == 'INCOME' ? 'green' : 'red';
	sum.innerText = sumValue;
	right.appendChild(sum);

	let date = document.createElement('div');
	let calendar = data['calendar'];
	let dateValue = new Date(calendar['year'], calendar['month'], calendar['dayOfMonth']);
	date.innerText = dateValue.ddmmyyyy();
	date.classList.add('transaction-date');
	right.appendChild(date);

	return right;
}

function createUpdate(data) {
	let update = document.createElement('div');
	update.classList.add('transaction-update');

	let link = document.createElement('a');
	link.href = '/wallet/transaction?action=update&id=' + data['id'];
	update.appendChild(link);

	let img = document.createElement('img');
	img.src = '/icons/update.svg';
	img.alt = 'update';
	img.classList.add('icon');
	link.appendChild(img);

	return update;
}

function createRemove(data) {
	let remove = document.createElement('form');
	remove.method = 'post';
	remove.action = 'wallet/transaction?action=submitDelete&id=' + data['id'];
	remove.classList.add('transaction-remove');

	let button = document.createElement('button');
	button.type = 'submit';
	remove.appendChild(button);

	let img = document.createElement('img');
	img.src = '/icons/delete.svg';
	img.alt = 'delete';
	img.classList.add('icon');
	button.appendChild(img);

	return remove;
}

Date.prototype.ddmmyyyy = function() {
	var mm = this.getMonth() + 1;
	var dd = this.getDate();

	return [(dd > 9 ? '' : '0') + dd,
	(mm > 9 ? '' : '0') + mm,
	this.getFullYear()
	].join('.');
};

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
	loadTransactions(5);
	
	var s = document.getElementById("selectTransactionType");
	changeTransactionType(s);
	
	var coll = document.getElementById("collapsible");
	console.log(coll);
	coll.addEventListener("click", function() {
		this.classList.toggle("down");
		var content = document.getElementById("filters");
		if (content.style.maxHeight) {
			content.style.maxHeight = null;
		} else {
			content.style.maxHeight = content.scrollHeight + "px";
		}
	});


};

