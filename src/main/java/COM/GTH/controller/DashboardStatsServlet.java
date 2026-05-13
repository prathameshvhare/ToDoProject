package COM.GTH.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import COM.GTH.service.TaskService;
import COM.GTH.service.TaskServiceImpl;

@WebServlet("/dashboardStats")
public class DashboardStatsServlet extends HttpServlet {

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

		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (userId == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			out.print("{\"error\":\"unauthorized\"}");
			return;
		}

		int[] c = taskService.getDashboardCounts(userId);
		out.print("{\"total\":");
		out.print(c[0]);
		out.print(",\"completed\":");
		out.print(c[1]);
		out.print(",\"pending\":");
		out.print(c[2]);
		out.print(",\"highPriority\":");
		out.print(c[3]);
		out.print("}");
	}
}
