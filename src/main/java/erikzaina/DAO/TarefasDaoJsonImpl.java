package erikzaina.DAO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import erikzaina.entity.Status;
import erikzaina.entity.Task;
import erikzaina.exception.readFileException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TarefasDaoJsonImpl implements TarefasDAO {
    private static final Logger LOGGER = Logger.getLogger(TarefasDaoJsonImpl.class.getName());
    private final File arquivo;
    private final ObjectMapper mapper;

    public TarefasDaoJsonImpl(ObjectMapper mapper, File arquivo) {
        this.arquivo = arquivo;
        this.mapper = mapper;
    }

    @Override
    public synchronized List<Task> listAllTasks() {
        try {
            return getListaTasks();
        } catch (IOException e) {
            throw new readFileException("Erro ao ler o arquivo de tarefas", e);
        }
    }

    @Override
    public synchronized Optional<Task> listTaskById(Integer id) {
        try {
            return getListaTasks().stream()
                    .filter(task -> task.getId().equals(id))
                    .findFirst();
        } catch (IOException e) {
            throw new readFileException("Erro ao ler o arquivo de tarefas", e);
        }
    }

    @Override
    public synchronized List<Task> listTaskStatus(Status status) {
        try {
            return getListaTasks().stream()
                    .filter(task -> task.getStatus().equals(status))
                    .toList();
        } catch (IOException e) {
            throw new readFileException("Erro ao ler o arquivo de tarefas", e);
        }
    }

    @Override
    public synchronized void createTask(Task task) {
        List<Task> taskList = listAllTasks();
        int nextId = taskList.isEmpty() ? 1 : taskList.stream()
                .mapToInt(Task::getId)
                .max()
                .getAsInt() + 1;
        task.setId(nextId);
        task.setStatus(Status.TODO);
        task.setCreatedAt(LocalDateTime.now());
        taskList.add(task);
        alteraArquivoTasks(taskList);
    }

    @Override
    public synchronized void updateTask(Integer id, Task task) {
        List<Task> taskList = listAllTasks();
        boolean found = taskList.stream()
                .filter(t -> t.getId().equals(id))
                .peek(t -> {
                    t.setDescription(task.getDescription());
                    t.setStatus(task.getStatus());
                    t.setUpdatedAt(LocalDateTime.now());
                })
                .findFirst()
                .isPresent();
        if (found) {
            alteraArquivoTasks(taskList);
        } else {
            throw new IllegalArgumentException("Tarefa com ID " + id + " não encontrada");
        }
    }

    @Override
    public synchronized void deleteTask(Integer id) {
        List<Task> taskList = listAllTasks();
        List<Task> updated = taskList.stream()
                .filter(t -> !t.getId().equals(id))
                .toList();
        if (taskList.size() != updated.size()) {
            alteraArquivoTasks(updated);
        } else {
            throw new IllegalArgumentException("Tarefa com ID " + id + " não encontrada");
        }
    }

    private List<Task> getListaTasks() throws IOException {
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(arquivo, new TypeReference<List<Task>>(){});
    }

    private void alteraArquivoTasks(List<Task> taskList) {
        try {
            mapper.writeValue(arquivo, taskList);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar tarefas", e);
        }
    }
}