package erikzaina.DAO;

import erikzaina.entity.Status;
import erikzaina.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TarefasDAO {
    List<Task> listAllTasks();
    Optional<Task> listTaskById(Integer id);
    List<Task> listTaskStatus(Status status);
    void createTask(Task task);
    void updateTask(Integer id, Task task);
    void deleteTask(Integer id);
}
