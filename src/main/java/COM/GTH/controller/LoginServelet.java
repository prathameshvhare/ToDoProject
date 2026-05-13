package COM.GTH.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import COM.GTH.model.UserModel;
import COM.GTH.service.UserService;
import COM.GTH.service.UserServiceImpl;

@WebServlet("/validateLogin")
public class LoginServelet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String email=request.getParameter("email");
		String password=request.getParameter("pword");
		
		UserModel model = new UserModel();
		UserService userservice = new UserServiceImpl();
		model.setEmail(email);
		model.setPassword(password);

		UserModel loggedIn = userservice.findUserByCredentials(model);
		if (loggedIn != null) {
			HttpSession session = request.getSession();
			session.setAttribute("email", loggedIn.getEmail());
			session.setAttribute("userId", loggedIn.getUser_id());
			String userName = loggedIn.getName();
			if (userName == null || userName.isBlank()) {
				String em = loggedIn.getEmail();
				userName = (em != null && em.contains("@")) ? em.substring(0, em.indexOf('@')) : "User";
			} else {
				userName = userName.trim();
			}
			session.setAttribute("userName", userName);
			response.sendRedirect("dashboard");
		} else {
			response.sendRedirect("login.html?error=1");
		}
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
