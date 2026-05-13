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
import COM.GTH.repository.TaskRepo;
import COM.GTH.repository.TaskRepoImpl;

@WebServlet("/search")
public class SerarchServelet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final TaskRepo taskRepo = new TaskRepoImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			response.sendRedirect("login.html");
			return;
		}
		Object raw = session.getAttribute("userId");
		int userId;
		if (raw instanceof Integer) {
			userId = (Integer) raw;
		} else if (raw instanceof Number) {
			userId = ((Number) raw).intValue();
		} else {
			response.sendRedirect("login.html");
			return;
		}

		String keyword = request.getParameter("keyword");
		String priority = request.getParameter("priority");
		String status = request.getParameter("status");
		String deadline = request.getParameter("deadline");

		List<TaskModel> list = taskRepo.findTasksWithFilters(userId, keyword, priority, status, deadline);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		writeTaskListFragment(out, list);
	}

	private static void writeTaskListFragment(PrintWriter out, List<TaskModel> list) {
		if (list.isEmpty()) {
			out.println("<p>No tasks found.</p>");
			return;
		}
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

	private static String cell(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
