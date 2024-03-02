import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseController;
import transactions.Wallet;

public class WalletsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");

	String action = req.getParameter("action");
	switch (action == null ? "info" : action) {
	case "update":
	    req.getRequestDispatcher("/wallets/update").forward(req, resp);
	    break;
	case "create":
	    req.getRequestDispatcher("/wallets/create").forward(req, resp);
	    break;
	default:
	    break;
	}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");

	Wallet wallet;
	String name;
	BigDecimal balance;

	String action = req.getParameter("action");
	int id = Integer.valueOf(req.getParameter("id") == null ? "0" : req.getParameter("id"));

	HttpSession session = req.getSession();
	DatabaseController db = (DatabaseController) session.getAttribute("db");

	switch (action) {
	case "submitUpdate":
	    name = req.getParameter("name");
	    balance = new BigDecimal(req.getParameter("balance"));
	    wallet = db.getWallet(id);
	    db.updateWallet(wallet, name, balance);
	    break;
	case "submitCreate":
	    name = req.getParameter("name");
	    balance = new BigDecimal(req.getParameter("balance"));
	    db.createWallet(name, balance);
	    break;
	case "submitDelete":
	    wallet = db.getWallet(id);
	    db.removeWallet(wallet);
	    break;
	default:
	    break;
	}

	resp.sendRedirect("/choice");
    }

}
