import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseController;
import transactions.TransactionCategory;
import transactions.TransactionType;
import transactions.Wallet;
import transactions.filter.CompareType;
import transactions.filter.Operation;
import transactions.filter.TransactionFilter;

public class TransactionFilterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");

	HttpSession session = req.getSession();
	Wallet wallet = (Wallet) session.getAttribute("wallet");
	String action = req.getParameter("action");
	if (action.equals("create")) {
	    String typeStr = req.getParameter("type");
	    String categoryStr = req.getParameter("category");
	    String minSumStr = req.getParameter("minSum");
	    String maxSumStr = req.getParameter("maxSum");
	    String minDateStr = req.getParameter("minDate");
	    String maxDateStr = req.getParameter("maxDate");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    DatabaseController db = (DatabaseController) session.getAttribute("db");

	    TransactionFilter.Builder builder = new TransactionFilter.Builder();

	    if (typeStr != null && typeStr.length() > 0) {
		builder.transactionType(new Operation<TransactionType>(CompareType.EQUALS, TransactionType.valueOf(typeStr)));
	    }
	    if (categoryStr != null && categoryStr.length() > 0) {
		int categoryId = Integer.valueOf(categoryStr);
		builder.transactionCat(new Operation<TransactionCategory>(CompareType.EQUALS, db.getTransactionCategory(categoryId)));
	    }
	    if (minSumStr != null && minSumStr.length() > 0) {
		System.out.println(minSumStr);
		builder.minSum(new Operation<BigDecimal>(CompareType.MORE, new BigDecimal(minSumStr)));
	    }
	    if (maxSumStr != null && maxSumStr.length() > 0) {
		builder.maxSum(new Operation<BigDecimal>(CompareType.LESS, new BigDecimal(maxSumStr)));
	    }
	    if (minDateStr != null && minDateStr.length() > 0) {
		Calendar minDate = GregorianCalendar.getInstance();
		try {
		    minDate.setTime(dateFormat.parse(minDateStr));
		} catch (Exception e) {
		    System.out.println(e.getMessage());
		}
		builder.minDate(new Operation<Calendar>(CompareType.MORE, minDate));
	    }
	    if (maxDateStr != null && maxDateStr.length() > 0) {
		Calendar maxDate = GregorianCalendar.getInstance();
		try {
		    maxDate.setTime(dateFormat.parse(maxDateStr));
		} catch (Exception e) {
		    System.out.println(e.getMessage());
		}
		builder.maxDate(new Operation<Calendar>(CompareType.LESS, maxDate));
	    }

	    session.setAttribute("filter", builder.build());
	}

	if (action.equals("delete")) {
	    session.removeAttribute("filter");
	}

	String path = String.format("%s/wallet?name=%s", req.getContextPath(), URLEncoder.encode(wallet.getWalletName(), "UTF-8"));
	resp.sendRedirect(path);
    }

}
