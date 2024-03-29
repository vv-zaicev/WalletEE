import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import database.DatabaseController;
import transactions.Transaction;
import transactions.TransactionCategory;
import transactions.TransactionType;
import transactions.Wallet;
import transactions.filter.TransactionFilter;

public class TransactionServlet extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");

	String action = req.getParameter("action");
	switch (action == null ? "info" : action) {
	case "update":
	    req.getRequestDispatcher("transaction/update").forward(req, resp);
	    break;
	case "create":
	    req.getRequestDispatcher("transaction/create").forward(req, resp);
	    break;
	case "getTransactions":
	    HttpSession session = req.getSession();
	    int count = Integer.parseInt(req.getParameter("count"));
	    Wallet wallet = (Wallet) session.getAttribute("wallet");
	    TransactionFilter filter = (TransactionFilter) session.getAttribute("filter");
	    if (filter == null) {
		filter = new TransactionFilter.Builder().build();
	    }
	    List<Transaction> transactions;

	    boolean hasMoreTransactions = false;
	    if (count == 0) {
		transactions = wallet.getTransactions(filter);
	    } else {
		transactions = wallet.getTransactions(filter, count);
		hasMoreTransactions = wallet.getTransactions(filter).size() > transactions.size();
	    }

	    JsonObject jsonObject = new JsonObject();

	    jsonObject.addProperty("hasMoreTransactions", hasMoreTransactions);
	    jsonObject.add("transactions", this.gson.toJsonTree(transactions));

	    resp.setContentType("application/json");
	    PrintWriter out = resp.getWriter();
	    out.print(jsonObject);
	    out.flush();
	default:
	    break;
	}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");

	String action = req.getParameter("action");
	int id = Integer.valueOf(req.getParameter("id") == null ? "0" : req.getParameter("id"));
	int categoryId = Integer.valueOf(req.getParameter("category") == null ? "0" : req.getParameter("category"));

	HttpSession session = req.getSession();
	DatabaseController db = (DatabaseController) session.getAttribute("db");
	Wallet wallet = (Wallet) session.getAttribute("wallet");

	Transaction transaction = wallet.getTransaction(id);

	if (action.equals("submitUpdate") && transaction != null) {
	    wallet.removeTransaction(transaction);

	    Calendar calendar = GregorianCalendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		calendar.setTime(dateFormat.parse(req.getParameter("calendar")));
	    } catch (Exception e) {
		System.out.println(e.getMessage());
	    }

	    transaction.setSum(new BigDecimal(req.getParameter("sum")));
	    transaction.setType(TransactionType.valueOf(req.getParameter("type")));
	    transaction.setCalendar(calendar);
	    transaction.setDescription(req.getParameter("description"));
	    transaction.setCategory(db.getTransactionCategory(categoryId));

	    wallet.addTransaction(transaction);
	    db.updateTransaction(wallet.getId(), transaction);
	}

	if (action.equals("submitCreate")) {
	    Calendar calendar = GregorianCalendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    try {
		calendar.setTime(dateFormat.parse(req.getParameter("calendar")));
	    } catch (Exception e) {
		System.out.println(e.getMessage());
	    }

	    BigDecimal sum = new BigDecimal(req.getParameter("sum"));
	    TransactionType type = TransactionType.valueOf(req.getParameter("type"));
	    String desc = req.getParameter("description");
	    TransactionCategory category = db.getTransactionCategory(categoryId);

	    Transaction newTransaction = new Transaction(desc, sum, type, calendar, category);
	    db.addTransaction(wallet.getId(), newTransaction);
	    wallet.addTransaction(newTransaction);
	}

	if (action.equals("submitDelete")) {
	    wallet.removeTransaction(transaction);
	    db.removeTransaction(wallet.getId(), transaction);
	}

	String path = String.format("%s/wallet?id=%d", req.getContextPath(), wallet.getId());
	resp.sendRedirect(path);
    }

}
