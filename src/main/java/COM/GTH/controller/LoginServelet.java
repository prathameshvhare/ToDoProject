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
		
		UserModel model=new UserModel();
		UserService userservice=new UserServiceImpl();
		model.setEmail(email);
		model.setPassword(password);
		
		boolean result=userservice.isValidUser(model);
		if(result) {
			
			HttpSession session=request.getSession();
			
			session.setAttribute("email",model.getEmail());
			response.sendRedirect("Dashboard.html");
			
		}
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
