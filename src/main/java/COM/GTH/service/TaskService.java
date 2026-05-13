package COM.GTH.service;

import java.util.List;

import COM.GTH.model.TaskModel;

public interface TaskService {

	boolean addTask(TaskModel model);

	List<TaskModel> listTasksForUser(int userId);

	boolean updateTask(TaskModel model);

	boolean deleteTask(int taskId, int userId);

	TaskModel getTaskForUser(int taskId, int userId);

	List<TaskModel> listPendingTasksForUser(int userId);

	List<TaskModel> listCompletedTasksForUser(int userId);

	int[] getDashboardCounts(int userId);
	
	List<TaskModel> serarchTasks(int userId,String keyword);
	
}
