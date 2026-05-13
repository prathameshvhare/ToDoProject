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
}
