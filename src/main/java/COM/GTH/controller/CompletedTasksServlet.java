package COM.GTH.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import COM.GTH.model.TaskModel;
import COM.GTH.service.TaskService;
import COM.GTH.service.TaskServiceImpl;

@WebServlet("/completedTasks")
public class CompletedTasksServlet extends HttpServlet {

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

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Completed Tasks</title>");
		out.println("<link rel=\"stylesheet\" href=\"CSS/styleDas.css\"></head><body>");
		request.getRequestDispatcher("/Dashboard.html").include(request, response);
		out.println("<div class=\"main-content\">");
		out.println("<div class=\"top-navbar\"><div class=\"welcome-text\" id=\"welcome-user\">Welcome</div>");
		out.println("<div class=\"date\">Daily Productivity Tracker</div></div>");
		out.println("<div class=\"task-panel\">");

		out.println("<h2>Completed tasks</h2>");
		out.println("<p><a href='viewTasks'>All tasks</a> · <a href='pendingTasks'>Pending</a></p>");

		List<TaskModel> list = taskService.listCompletedTasksForUser(userId);

		if (list.isEmpty()) {
			out.println("<p>No completed tasks yet.</p>");
		} else {
			out.println("<div class='task-table-wrap'>");
			out.println("<table>");
			out.println("<tr><th>Title</th><th>Description</th><th>Priority</th><th>Status</th><th>Deadline</th><th>Action</th></tr>");
			for (TaskModel t : list) {
				out.println("<tr>");
				out.println("<td>" + cell(t.getTaskTitle()) + "</td>");
				out.println("<td>" + cell(t.getDescription()) + "</td>");
				out.println("<td>" + cell(t.getPriority()) + "</td>");
				out.println("<td>" + cell(t.getStatus()) + "</td>");
				out.println("<td>" + cell(t.getDeadline()) + "</td>");
				out.println("<td class='task-actions'><a href='updateTask?tid=" + t.getTaskId() + "'>Edit</a>");
				out.println("<a href='deleteTask?tid=" + t.getTaskId() + "' onclick=\"return confirm('Delete this task?');\">Delete</a></td>");
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("</div>");
		}

		out.println("</div></div>");
		out.println(WELCOME_JS);
		out.println("</body></html>");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private static String cell(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
