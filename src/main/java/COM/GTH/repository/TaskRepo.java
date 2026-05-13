package COM.GTH.repository;

import java.util.List;

import COM.GTH.model.TaskModel;

public interface TaskRepo {

	boolean addTask(TaskModel model);

	List<TaskModel> findTasksByUserId(int userId);

	boolean updateTask(TaskModel model);

	boolean deleteTask(int taskId, int userId);

	TaskModel findTaskByIdAndUserId(int taskId, int userId);

	List<TaskModel> findPendingTasksByUserId(int userId);

	List<TaskModel> findCompletedTasksByUserId(int userId);
	
	public List<TaskModel> searchTasks(int userId, String keyword);

	List<TaskModel> findTasksWithFilters(int userId, String keyword, String priority, String status, String deadline);

}
