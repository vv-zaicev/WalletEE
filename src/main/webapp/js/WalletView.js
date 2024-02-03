function loadTransactions(count) {
	fetch('/wallet/transaction?action=getTransactions&count=' + count)
		.then(function(response) {
			return response.json();
		}).then(function(apiJsonData) {
			fillTransactions(apiJsonData);
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

window.onload = loadTransactions(5);
