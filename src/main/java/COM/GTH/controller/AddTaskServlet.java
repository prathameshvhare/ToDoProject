package COM.GTH.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import COM.GTH.model.TaskModel;
import COM.GTH.service.TaskService;
import COM.GTH.service.TaskServiceImpl;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {

	private static final String WELCOME_JS = "<script>fetch('sessionDisplayName',{credentials:'same-origin'}).then(function(r){return r.ok?r.text():''}).then(function(n){var e=document.getElementById('welcome-user');if(e&&n&&n.trim())e.textContent='Welcome, '+n.trim();});</script>";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId;
		if (session == null) {
			response.sendRedirect("login.html");
			return;
		}
		Object raw = session.getAttribute("userId");
		if (raw instanceof Integer) {
			userId = (Integer) raw;
		} else if (raw instanceof Number) {
			userId = ((Number) raw).intValue();
		} else {
			response.sendRedirect("login.html");
			return;
		}

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Add Task</title>");
		out.println("<link rel=\"stylesheet\" href=\"CSS/styleDas.css\"></head><body>");
		request.getRequestDispatcher("/Dashboard.html").include(request, response);
		out.println("<div class=\"main-content\">");
		out.println("<div class=\"top-navbar\"><div class=\"welcome-text\" id=\"welcome-user\">Welcome</div>");
		out.println("<div class=\"date\">Daily Productivity Tracker</div></div>");
		out.println("<div class=\"task-panel\">");

		out.println("<form class='task-form' name='frm' action='addTask' method='POST'>");
		out.println("<h2>Add task</h2>");
		out.println("<label for='taskTitle'>Title</label>");
		out.println("<input id='taskTitle' type='text' name='taskTitle' value='' placeholder='Task title' />");
		out.println("<label for='description'>Description</label>");
		out.println("<textarea id='description' name='description' placeholder='Description'></textarea>");
		out.println("<label for='deadline'>Deadline</label>");
		out.println("<input id='deadline' type='date' name='deadline' />");
		out.println("<label for='priority'>Priority</label>");
		out.println("<select id='priority' name='priority'>");
		out.println("<option value='High'>High</option>");
		out.println("<option value='Medium' selected>Medium</option>");
		out.println("<option value='Low'>Low</option>");
		out.println("</select>");
		out.println("<label for='status'>Status</label>");
		out.println("<select id='status' name='status'>");
		out.println("<option value='Pending' selected>Pending</option>");
		out.println("<option value='inProgress'>In progress</option>");
		out.println("<option value='Completed'>Completed</option>");
		out.println("</select>");
		out.println("<input type='submit' name='s' value='Add task' />");
		out.println("</form>");

		String btnValue = request.getParameter("s");
		if (btnValue != null && !btnValue.isEmpty()) {
			TaskModel model = new TaskModel();
			model.setTaskTitle(request.getParameter("taskTitle"));
			model.setDescription(request.getParameter("description"));
			model.setDeadline(request.getParameter("deadline"));
			model.setPriority(request.getParameter("priority"));
			model.setStatus(request.getParameter("status"));
			model.setUserId(userId);
			TaskService taskService = new TaskServiceImpl();
			boolean result = taskService.addTask(model);
			if (result) {
				out.println("<p class='task-msg'>Task saved. <a href='viewTasks'>View tasks</a>.</p>");
			}
		}

		out.println("</div></div>");
		out.println(WELCOME_JS);
		out.println("</body></html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
