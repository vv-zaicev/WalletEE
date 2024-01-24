import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseController;
import transactions.Transaction;
import transactions.TransactionType;
import transactions.Wallet;

public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String action = req.getParameter("action");
	switch (action == null ? "info" : action) {
	case "update":
	    req.getRequestDispatcher("transaction/update").forward(req, resp);
	    break;
	case "create":
	    req.getRequestDispatcher("transaction/create").forward(req, resp);
	    break;
	case "remove":
	    req.getRequestDispatcher("transaction/remove").forward(req, resp);
	    break;
	case "info":
	default:
	    break;
	}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String action = req.getParameter("action");
	int id = Integer.valueOf(req.getParameter("id") == null ? "0" : req.getParameter("id"));

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
	    transaction.setDescriprion(req.getParameter("description"));

	    wallet.addTransaction(transaction);
	    db.updateTransaction(transaction);
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
	    Transaction newTransaction = new Transaction(desc, sum, type, calendar);
	    db.addTransaction(newTransaction);
	    wallet.addTransaction(newTransaction);
	}

	String path = String.format("%s/wallet?name=%s", req.getContextPath(), wallet.getWalletName());
	resp.sendRedirect(path);
    }

}
