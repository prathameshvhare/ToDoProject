package COM.GTH.repository;

import java.util.ArrayList;
import java.util.List;

import COM.GTH.model.TaskModel;

public class TaskRepoImpl extends DBConfig implements TaskRepo {

	@Override
	public boolean addTask(TaskModel model) {
		try {
			stmt = conn.prepareStatement(
					"INSERT INTO task (task_title, task_description, deadline, priority, status, user_id) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setString(1, model.getTaskTitle());
			stmt.setString(2, model.getDescription());
			stmt.setString(3, model.getDeadline());
			stmt.setString(4, model.getPriority());
			stmt.setString(5, model.getStatus());
			stmt.setInt(6, model.getUserId());
			return stmt.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return false;
	}

	@Override
	public List<TaskModel> findTasksByUserId(int userId) {
		List<TaskModel> list = new ArrayList<>();
		try {
			stmt = conn.prepareStatement(
					"SELECT task_id, task_title, task_description, deadline, priority, status, user_id FROM task WHERE user_id=? ORDER BY task_id DESC");
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TaskModel t = new TaskModel();
				t.setTaskId(rs.getInt("task_id"));
				t.setTaskTitle(rs.getString("task_title"));
				t.setDescription(rs.getString("task_description"));
				t.setDeadline(rs.getString("deadline"));
				t.setPriority(rs.getString("priority"));
				t.setStatus(rs.getString("status"));
				t.setUserId(rs.getInt("user_id"));
				list.add(t);
			}
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return list;
	}

	@Override
	public boolean updateTask(TaskModel model) {
		try {
			stmt = conn.prepareStatement(
					"UPDATE task SET task_title=?, task_description=?, deadline=?, priority=?, status=? WHERE task_id=? AND user_id=?");
			stmt.setString(1, model.getTaskTitle());
			stmt.setString(2, model.getDescription());
			stmt.setString(3, model.getDeadline());
			stmt.setString(4, model.getPriority());
			stmt.setString(5, model.getStatus());
			stmt.setInt(6, model.getTaskId());
			stmt.setInt(7, model.getUserId());
			return stmt.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return false;
	}

	@Override
	public boolean deleteTask(int taskId, int userId) {
		try {
			stmt = conn.prepareStatement("DELETE FROM task WHERE task_id=? AND user_id=?");
			stmt.setInt(1, taskId);
			stmt.setInt(2, userId);
			return stmt.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return false;
	}

	@Override
	public TaskModel findTaskByIdAndUserId(int taskId, int userId) {
		try {
			stmt = conn.prepareStatement(
					"SELECT task_id, task_title, task_description, deadline, priority, status, user_id FROM task WHERE task_id=? AND user_id=?");
			stmt.setInt(1, taskId);
			stmt.setInt(2, userId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				TaskModel t = new TaskModel();
				t.setTaskId(rs.getInt("task_id"));
				t.setTaskTitle(rs.getString("task_title"));
				t.setDescription(rs.getString("task_description"));
				t.setDeadline(rs.getString("deadline"));
				t.setPriority(rs.getString("priority"));
				t.setStatus(rs.getString("status"));
				t.setUserId(rs.getInt("user_id"));
				return t;
			}
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return null;
	}

	@Override
	public List<TaskModel> findPendingTasksByUserId(int userId) {
		List<TaskModel> list = new ArrayList<>();
		try {
			stmt = conn.prepareStatement(
					"SELECT task_id, task_title, task_description, deadline, priority, status, user_id FROM task WHERE user_id=? AND status IN ('Pending','inProgress') ORDER BY task_id DESC");
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TaskModel t = new TaskModel();
				t.setTaskId(rs.getInt("task_id"));
				t.setTaskTitle(rs.getString("task_title"));
				t.setDescription(rs.getString("task_description"));
				t.setDeadline(rs.getString("deadline"));
				t.setPriority(rs.getString("priority"));
				t.setStatus(rs.getString("status"));
				t.setUserId(rs.getInt("user_id"));
				list.add(t);
			}
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return list;
	}

	@Override
	public List<TaskModel> findCompletedTasksByUserId(int userId) {
		List<TaskModel> list = new ArrayList<>();
		try {
			stmt = conn.prepareStatement(
					"SELECT task_id, task_title, task_description, deadline, priority, status, user_id FROM task WHERE user_id=? AND status='Completed' ORDER BY task_id DESC");
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				TaskModel t = new TaskModel();
				t.setTaskId(rs.getInt("task_id"));
				t.setTaskTitle(rs.getString("task_title"));
				t.setDescription(rs.getString("task_description"));
				t.setDeadline(rs.getString("deadline"));
				t.setPriority(rs.getString("priority"));
				t.setStatus(rs.getString("status"));
				t.setUserId(rs.getInt("user_id"));
				list.add(t);
			}
		} catch (Exception ex) {
			System.out.println("Error is:" + ex);
		}
		return list;
	}

	public List<TaskModel> searchTasks(int userId, String keyword) {
		String kw = keyword == null ? "" : keyword;
		return findTasksWithFilters(userId, kw, null, null, null);
	}

	@Override
	public List<TaskModel> findTasksWithFilters(int userId, String keyword, String priority, String status,
			String deadline) {
		List<TaskModel> list = new ArrayList<>();
		try {
			StringBuilder sql = new StringBuilder(
					"SELECT task_id, task_title, task_description, deadline, priority, status, user_id FROM task WHERE user_id=?");
			if (keyword != null && !keyword.isBlank()) {
				sql.append(" AND task_title LIKE ?");
			}
			if (priority != null && !priority.isBlank()) {
				sql.append(" AND priority=?");
			}
			if (status != null && !status.isBlank()) {
				sql.append(" AND status=?");
			}
			if (deadline != null && !deadline.isBlank()) {
				sql.append(" AND deadline=?");
			}
			sql.append(" ORDER BY task_id DESC");
			stmt = conn.prepareStatement(sql.toString());
			int i = 1;
			stmt.setInt(i++, userId);
			if (keyword != null && !keyword.isBlank()) {
				stmt.setString(i++, "%" + keyword + "%");
			}
			if (priority != null && !priority.isBlank()) {
				stmt.setString(i++, priority);
			}
			if (status != null && !status.isBlank()) {
				stmt.setString(i++, status);
			}
			if (deadline != null && !deadline.isBlank()) {
				stmt.setString(i++, deadline);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				TaskModel t = new TaskModel();
				t.setTaskId(rs.getInt("task_id"));
				t.setTaskTitle(rs.getString("task_title"));
				t.setDescription(rs.getString("task_description"));
				t.setDeadline(rs.getString("deadline"));
				t.setPriority(rs.getString("priority"));
				t.setStatus(rs.getString("status"));
				t.setUserId(rs.getInt("user_id"));
				list.add(t);
			}
		} catch (Exception ex) {
			System.out.println("Exception :" + ex);
		}
		return list;
	}

	@Override
	public List<TaskModel> findPendingTasksWithFilters(int userId, String keyword, String priority, String deadline) {
		return findTasksWithFiltersAndStatusScope(userId, keyword, priority, deadline,
				" AND status IN ('Pending','inProgress')");
	}

	@Override
	public List<TaskModel> findCompletedTasksWithFilters(int userId, String keyword, String priority, String deadline) {
		return findTasksWithFiltersAndStatusScope(userId, keyword, priority, deadline, " AND status='Completed'");
	}

	private List<TaskModel> findTasksWithFiltersAndStatusScope(int userId, String keyword, String priority,
			String deadline, String statusScopeSql) {
		List<TaskModel> list = new ArrayList<>();
		try {
			StringBuilder sql = new StringBuilder(
					"SELECT task_id, task_title, task_description, deadline, priority, status, user_id FROM task WHERE user_id=?");
			sql.append(statusScopeSql);
			if (keyword != null && !keyword.isBlank()) {
				sql.append(" AND task_title LIKE ?");
			}
			if (priority != null && !priority.isBlank()) {
				sql.append(" AND priority=?");
			}
			if (deadline != null && !deadline.isBlank()) {
				sql.append(" AND deadline=?");
			}
			sql.append(" ORDER BY task_id DESC");
			stmt = conn.prepareStatement(sql.toString());
			int i = 1;
			stmt.setInt(i++, userId);
			if (keyword != null && !keyword.isBlank()) {
				stmt.setString(i++, "%" + keyword + "%");
			}
			if (priority != null && !priority.isBlank()) {
				stmt.setString(i++, priority);
			}
			if (deadline != null && !deadline.isBlank()) {
				stmt.setString(i++, deadline);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				TaskModel t = new TaskModel();
				t.setTaskId(rs.getInt("task_id"));
				t.setTaskTitle(rs.getString("task_title"));
				t.setDescription(rs.getString("task_description"));
				t.setDeadline(rs.getString("deadline"));
				t.setPriority(rs.getString("priority"));
				t.setStatus(rs.getString("status"));
				t.setUserId(rs.getInt("user_id"));
				list.add(t);
			}
		} catch (Exception ex) {
			System.out.println("Exception :" + ex);
		}
		return list;
	}
}
