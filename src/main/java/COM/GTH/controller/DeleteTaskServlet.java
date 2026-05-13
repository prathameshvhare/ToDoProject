package COM.GTH.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import COM.GTH.service.TaskService;
import COM.GTH.service.TaskServiceImpl;

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {

	private final TaskService taskService = new TaskServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Integer userId = null;
		if (session != null) {
			Object o = session.getAttribute("userId");
			if (o instanceof Integer) {
				userId = (Integer) o;
			} else if (o instanceof Number) {
				userId = ((Number) o).intValue();
			}
		}
		if (userId == null) {
			response.sendRedirect("login.html");
			return;
		}
		try {
			int tid = Integer.parseInt(request.getParameter("tid"));
			taskService.deleteTask(tid, userId);
		} catch (Exception e) {
			// ignore bad tid
		}
		response.sendRedirect("viewTasks");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
