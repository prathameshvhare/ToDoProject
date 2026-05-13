package COM.GTH.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Returns the logged-in user's display name as plain text (for the welcome line).
 */
@WebServlet("/sessionDisplayName")
public class SessionDisplayNameServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		String userName = (String) session.getAttribute("userName");
		if (userName == null || userName.isBlank()) {
			String em = (String) session.getAttribute("email");
			userName = (em != null && em.contains("@")) ? em.substring(0, em.indexOf('@')) : "User";
		} else {
			userName = userName.trim();
		}
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(userName);
	}
}
