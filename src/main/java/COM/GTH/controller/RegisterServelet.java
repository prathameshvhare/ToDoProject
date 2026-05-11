package COM.GTH.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import COM.GTH.model.UserModel;
import COM.GTH.service.UserService;
import COM.GTH.service.UserServiceImpl;


@WebServlet("/reg")
public class RegisterServelet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("pword");
		
		
		UserModel model=new UserModel();
		model.setName(name);
		model.setEmail(email);
		model.setPassword(password);
		
		UserService userservice=new UserServiceImpl();
		boolean result=userservice.isAddUser(model);
		
		if(result) {
			response.sendRedirect("login.html");
			
		}
		
		
		
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
