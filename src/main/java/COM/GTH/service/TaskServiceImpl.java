package COM.GTH.service;

import java.util.List;

import COM.GTH.model.TaskModel;
import COM.GTH.repository.TaskRepo;
import COM.GTH.repository.TaskRepoImpl;

public class TaskServiceImpl implements TaskService {

	private final TaskRepo taskRepo = new TaskRepoImpl();

	@Override
	public boolean addTask(TaskModel model) {
		return taskRepo.addTask(model);
	}

	@Override
	public List<TaskModel> listTasksForUser(int userId) {
		return taskRepo.findTasksByUserId(userId);
	}

	@Override
	public boolean updateTask(TaskModel model) {
		return taskRepo.updateTask(model);
	}

	@Override
	public boolean deleteTask(int taskId, int userId) {
		return taskRepo.deleteTask(taskId, userId);
	}

	@Override
	public TaskModel getTaskForUser(int taskId, int userId) {
		return taskRepo.findTaskByIdAndUserId(taskId, userId);
	}

	@Override
	public List<TaskModel> listPendingTasksForUser(int userId) {
		return taskRepo.findPendingTasksByUserId(userId);
	}

	@Override
	public List<TaskModel> listCompletedTasksForUser(int userId) {
		return taskRepo.findCompletedTasksByUserId(userId);
	}

	@Override
	public int[] getDashboardCounts(int userId) {
		List<TaskModel> list = taskRepo.findTasksByUserId(userId);
		int completed = 0;
		int pending = 0;
		int high = 0;
		for (TaskModel t : list) {
			String s = t.getStatus();
			if ("Completed".equals(s)) {
				completed++;
			} else if ("Pending".equals(s) || "inProgress".equals(s)) {
				pending++;
			}
			if ("High".equals(t.getPriority())) {
				high++;
			}
		}
		return new int[] { list.size(), completed, pending, high };
	}
}
