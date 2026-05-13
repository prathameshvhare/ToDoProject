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

@WebServlet("/updateTask")
public class UpdateTaskServlet extends HttpServlet {

	private static final String WELCOME_JS = "<script>fetch('sessionDisplayName',{credentials:'same-origin'}).then(function(r){return r.ok?r.text():''}).then(function(n){var e=document.getElementById('welcome-user');if(e&&n&&n.trim())e.textContent='Welcome, '+n.trim();});</script>";

	private final TaskService taskService = new TaskServiceImpl();

	@Override
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

		int taskId;
		try {
			taskId = Integer.parseInt(request.getParameter("tid"));
		} catch (Exception e) {
			response.sendRedirect("viewTasks");
			return;
		}

		TaskModel task = taskService.getTaskForUser(taskId, userId);
		if (task == null) {
			response.sendRedirect("viewTasks");
			return;
		}

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Edit Task</title>");
		out.println("<link rel=\"stylesheet\" href=\"CSS/styleDas.css\"></head><body>");
		request.getRequestDispatcher("/Dashboard.html").include(request, response);
		out.println("<div class=\"main-content\">");
		out.println("<div class=\"top-navbar\"><div class=\"welcome-text\" id=\"welcome-user\">Welcome</div>");
		out.println("<div class=\"date\">Daily Productivity Tracker</div></div>");
		out.println("<div class=\"task-panel\">");

		String dl = task.getDeadline() == null ? "" : esc(task.getDeadline());
		out.println("<form class='task-form' action='updateTask' method='POST'>");
		out.println("<h2>Edit task</h2>");
		out.println("<input type='hidden' name='taskId' value='" + task.getTaskId() + "' />");
		out.println("<label>Title</label><input type='text' name='taskTitle' value='" + esc(task.getTaskTitle()) + "' />");
		out.println("<label>Description</label><textarea name='description'>" + esc(task.getDescription()) + "</textarea>");
		out.println("<label>Deadline</label><input type='date' name='deadline' value='" + dl + "' />");
		out.println("<label>Priority</label><select name='priority'>");
		out.println("<option value='High'" + sel(task.getPriority(), "High") + ">High</option>");
		out.println("<option value='Medium'" + sel(task.getPriority(), "Medium") + ">Medium</option>");
		out.println("<option value='Low'" + sel(task.getPriority(), "Low") + ">Low</option>");
		out.println("</select>");
		out.println("<label>Status</label><select name='status'>");
		out.println("<option value='Pending'" + sel(task.getStatus(), "Pending") + ">Pending</option>");
		out.println("<option value='inProgress'" + sel(task.getStatus(), "inProgress") + ">In progress</option>");
		out.println("<option value='Completed'" + sel(task.getStatus(), "Completed") + ">Completed</option>");
		out.println("</select>");
		out.println("<input type='submit' value='Save' />");
		out.println("<p><a href='viewTasks'>Back</a></p></form>");

		out.println("</div></div>");
		out.println(WELCOME_JS);
		out.println("</body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

		int taskId;
		try {
			taskId = Integer.parseInt(request.getParameter("taskId"));
		} catch (Exception e) {
			response.sendRedirect("viewTasks");
			return;
		}

		TaskModel model = new TaskModel();
		model.setTaskId(taskId);
		model.setUserId(userId);
		model.setTaskTitle(request.getParameter("taskTitle"));
		model.setDescription(request.getParameter("description"));
		model.setDeadline(request.getParameter("deadline"));
		model.setPriority(request.getParameter("priority"));
		model.setStatus(request.getParameter("status"));

		taskService.updateTask(model);
		response.sendRedirect("viewTasks");
	}

	private static String sel(String current, String value) {
		if (value.equals(current == null ? "" : current)) {
			return " selected";
		}
		return "";
	}

	private static String esc(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
