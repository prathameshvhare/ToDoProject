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

@WebServlet("/pendingTasks")
public class PendingTasksServlet extends HttpServlet {

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
		out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Pending Tasks</title>");
		out.println("<link rel=\"stylesheet\" href=\"CSS/styleDas.css\"></head><body>");
		request.getRequestDispatcher("/Dashboard.html").include(request, response);
		out.println("<div class=\"main-content\">");
		out.println("<div class=\"top-navbar\"><div class=\"welcome-text\" id=\"welcome-user\">Welcome</div>");
		out.println("<div class=\"date\">Daily Productivity Tracker</div></div>");
		out.println("<div class=\"task-panel\">");

		out.println("<h2>Pending &amp; in progress</h2>");
		out.println("<p><a href='viewTasks'>All tasks</a></p>");
		out.println("<div style='display:flex; align-items:center; gap:10px; margin-top:25px; flex-wrap:nowrap;'>");

		// Search Box
		out.println("<input type='text' id=\"searchBox\" name='keyword' placeholder='Search task by name' "
		        + "style='width:350px; padding:10px; border:2px solid black; border-radius:5px; font-size:15px;'>");

		// Priority
		out.println("<select id='filterPriority' name='priority' "
		        + "style='padding:8px; width:140px; border:1px solid black; border-radius:4px;'>"
		        + "<option value=''>Select Priority</option>"
		        + "<option value='High'>High</option>"
		        + "<option value='Medium'>Medium</option>"
		        + "<option value='Low'>Low</option>"
		        + "</select>");

		// Date
		out.println("<input type='date' id='filterDeadline' name='deadline' "
		        + "style='padding:8px; border:1px solid black; border-radius:4px;'>");

		// Button
		out.println("<button type='button' id='filterBtn' "
		        + "style='padding:8px 15px; border:none; background:#2d89ef; color:white; border-radius:4px; cursor:pointer;'>Filter</button>");

		out.println("</div>");

		out.println("<script>");
		out.println("function loadTaskTable(){");
		out.println(" var keyword=document.getElementById('searchBox').value;");
		out.println(" var priority=document.getElementById('filterPriority').value;");
		out.println(" var deadline=document.getElementById('filterDeadline').value;");

		out.println(" fetch('search?taskList=pending&keyword='+keyword+'&priority='+priority+'&deadline='+deadline)");
		out.println(" .then(response => response.text())");
		out.println(" .then(data => {");
		out.println("   var el=document.getElementById('taskContainer'); if(el) el.innerHTML=data;");
		out.println(" });");
		out.println("}");

		out.println("document.getElementById('searchBox').onkeyup=loadTaskTable;");
		out.println("document.getElementById('filterBtn').onclick=loadTaskTable;");
		out.println("</script>");

		List<TaskModel> list = taskService.listPendingTasksForUser(userId);

		out.println("<div id='taskContainer'>");
		if (list.isEmpty()) {
			out.println("<p>No pending or in-progress tasks.</p>");
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
		out.println("</div>");

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
