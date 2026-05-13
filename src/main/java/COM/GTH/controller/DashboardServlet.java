package COM.GTH.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

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

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Dashboard</title>");
		out.println("<link rel=\"stylesheet\" href=\"CSS/styleDas.css\"></head><body>");
		request.getRequestDispatcher("/Dashboard.html").include(request, response);
		out.println("<div class=\"main-content\">");
		out.println("<div class=\"top-navbar\"><div class=\"welcome-text\" id=\"welcome-user\">Welcome</div>");
		out.println("<div class=\"date\">Daily Productivity Tracker</div></div>");
		out.println("<div class=\"page-intro\"><p>Your task overview.</p></div>");
		out.println("<div class=\"task-panel\"><div class=\"dashboard-cards\">");
		out.println("<div class=\"card total-task\"><h2>Total Tasks</h2><p id=\"dash-stat-total\">0</p></div>");
		out.println("<div class=\"card completed-task\"><h2>Completed</h2><p id=\"dash-stat-completed\">0</p></div>");
		out.println("<div class=\"card pending-task\"><h2>Pending</h2><p id=\"dash-stat-pending\">0</p></div>");
		out.println("<div class=\"card high-priority\"><h2>High Priority</h2><p id=\"dash-stat-high\">0</p></div>");
		out.println("</div></div></div>");
		out.println("<script>");
		out.println("fetch('sessionDisplayName',{credentials:'same-origin'}).then(function(r){return r.ok?r.text():''}).then(function(n){var e=document.getElementById('welcome-user');if(e&&n&&n.trim())e.textContent='Welcome, '+n.trim();});");
		out.println("fetch('dashboardStats',{credentials:'same-origin'}).then(function(r){return r.ok?r.json():null}).then(function(d){if(!d||d.error)return;var ids=['dash-stat-total','dash-stat-completed','dash-stat-pending','dash-stat-high'];var vals=[d.total,d.completed,d.pending,d.highPriority];for(var i=0;i<ids.length;i++){var el=document.getElementById(ids[i]);if(el)el.textContent=vals[i];}});");
		out.println("</script></body></html>");
	}
}
