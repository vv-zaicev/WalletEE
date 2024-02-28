import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorizationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");
	HttpSession session = req.getSession();
	PrintWriter pw = resp.getWriter();
	Optional<String> pass = readCookie(req, "pass");
	Optional<String> login = readCookie(req, "login");
	if (pass.isEmpty() || login.isEmpty()) {
	    req.setAttribute("succes", true);
	    resp.sendRedirect(req.getContextPath() + "/choice");
	}

    }

    private Optional<String> readCookie(HttpServletRequest request, String key) {
	if (request.getCookies() == null || request.getCookies().length == 0) {
	    return Optional.empty();
	}
	return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(Cookie::getValue).findAny();
    }

}
