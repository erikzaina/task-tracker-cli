package erikzaina.CLI;

import erikzaina.DAO.TarefasDAO;
import erikzaina.entity.Status;
import erikzaina.entity.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class TaskCLI {
    private static final Logger LOGGER = Logger.getLogger(TaskCLI.class.getName());
    private final Scanner scanner;
    private final TarefasDAO tarefasDAO;

    public TaskCLI(Scanner scanner, TarefasDAO tarefasDAO) {
        this.scanner = scanner;
        this.tarefasDAO = tarefasDAO;
    }

    public void startListening() {
        LOGGER.info("Task Tracker CLI - Digite 'help' para ver os comandos disponíveis ou 'exit' para sair.");
        try (scanner) {
            while (true) {
                LOGGER.info("> ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                makeDecisions(input);
            }
        }
        LOGGER.info("Aplicação encerrada.");
    }

    private void makeDecisions(String input) {
        List<String> tokens = Arrays.asList(input.split("\\s+", 2));
        String command = tokens.get(0).toLowerCase();

        if (command.equals("add") || command.equals("update")) {
            handleAddOrUpdate(input, command);
            return;
        }
        switch (tokens.size()) {
            case 1 -> handleSingleToken(command);
            case 2 -> handleTwoTokens(command, tokens.get(1));
            default -> LOGGER.warning("Comando inválido: " + input);
        }
    }

    private void handleSingleToken(String command) {
        switch (command.toLowerCase()) {
            case "list" -> listTasks(null);
            case "help" -> printHelp();
            case "exit" -> {} // Handled in startListening
            default -> LOGGER.warning("Comando inválido: " + command);
        }
    }

    private void handleTwoTokens(String command, String arg) {
        switch (command.toLowerCase()) {
            case "list" -> {
                switch (arg.toLowerCase()) {
                    case "todo" -> listTasks(Status.TODO);
                    case "done" -> listTasks(Status.DONE);
                    case "in-progress" -> listTasks(Status.IN_PROGRESS);
                    default -> LOGGER.warning("Status inválido: " + arg);
                }
            }
            case "delete" -> deleteTask(arg);
            case "mark-done" -> updateTaskStatus(arg, Status.DONE);
            case "mark-in-progress" -> updateTaskStatus(arg, Status.IN_PROGRESS);
            case "mark-todo" -> updateTaskStatus(arg, Status.TODO);
            default -> LOGGER.warning("Comando inválido: " + command);
        }
    }

    private void handleAddOrUpdate(String input, String command) {
        try {
            String[] parts = input.split("\"", 3);
            if (parts.length < 2) {
                LOGGER.warning("Descrição da tarefa ausente ou malformada");
                return;
            }
            String description = parts[1].trim();
            if (description.isEmpty()) {
                LOGGER.warning("A descrição da tarefa não pode ser vazia");
                return;
            }

            if (command.equals("add")) {
                Task task = new Task(description);
                tarefasDAO.createTask(task);
                LOGGER.info("Tarefa criada com sucesso (ID: " + task.getId() + ")");
            } else { // update
                String idStr = parts[0].split("\\s+")[1].trim();
                Integer id = parseId(idStr);
                if (id == null) return;
                Task task = tarefasDAO.listTaskById(id).orElse(null);
                if (task != null) {
                    task.setDescription(description);
                    tarefasDAO.updateTask(id, task);
                    LOGGER.info("Tarefa atualizada com sucesso (ID: " + id + ")");
                } else {
                    LOGGER.warning("Tarefa com ID " + id + " não encontrada");
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Erro ao processar comando " + command + ": " + e.getMessage());

        }
    }

    private void listTasks(Status status) {
        List<Task> tasks = status == null ? tarefasDAO.listAllTasks() : tarefasDAO.listTaskStatus(status);
        if (tasks.isEmpty()) {
            LOGGER.info("Nenhuma tarefa encontrada.");
            return;
        }
        LOGGER.info("ID | Descrição | Status | Criado em | Atualizado em");
        LOGGER.info("---|-----------|--------|-----------|--------------");
        for (Task task : tasks) {
            System.out.printf("%d | %s | %s | %s | %s%n",
                    task.getId(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getCreatedAt().toString().substring(0, 10),
                    task.getUpdatedAt() != null ? task.getUpdatedAt().toString().substring(0, 10) : "N/A");
        }
    }

    private void deleteTask(String idStr) {
        Integer id = parseId(idStr);
        if (id != null) {
            try {
                tarefasDAO.deleteTask(id);
                LOGGER.info("Tarefa excluida como sucesso" +"(ID: " + id + ")");
            } catch (IllegalArgumentException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    private void updateTaskStatus(String idStr, Status status) {
        Integer id = parseId(idStr);
        if (id != null) {
            try {
                Task task = tarefasDAO.listTaskById(id).orElse(null);
                if (task != null) {
                    task.setStatus(status);
                    tarefasDAO.updateTask(id, task);
                    LOGGER.info("Tarefa com ID " + id + " marcada como " + status);
                } else {
                    LOGGER.warning("Tarefa com ID " + id + " não encontrada");
                }
            } catch (IllegalArgumentException e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    private Integer parseId(String idStr) {
        try {
            return Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            LOGGER.warning("ID inválido: " + idStr);
            return null;
        }
    }

    private void printHelp() {
        LOGGER.info("""
                Comandos disponíveis:
                add "descrição"                     - Adiciona uma nova tarefa
                update ID "descrição"              - Atualiza a descrição de uma tarefa
                delete ID                          - Deleta uma tarefa
                mark-done ID                       - Marca uma tarefa como concluída
                mark-in-progress ID                - Marca uma tarefa como em progresso
                mark-todo ID                       - Marca uma tarefa como a fazer
                list                               - Lista todas as tarefas
                list [todo|done|in-progress]       - Lista tarefas por status
                help                               - Mostra lista de comandos
                exit                               - Sai da aplicação
                """);
    }
}