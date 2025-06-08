package erikzaina;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import erikzaina.CLI.TaskCLI;
import erikzaina.DAO.TarefasDAO;
import erikzaina.DAO.TarefasDaoJsonImpl;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            File arquivo = criaArquivoInicial();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            TarefasDAO tarefasDAO = new TarefasDaoJsonImpl(mapper, arquivo);
            TaskCLI taskCLI = new TaskCLI(new Scanner(System.in), tarefasDAO);
            taskCLI.startListening();
        } catch (Exception ex) {
            LOGGER.severe("Erro ao inicializar a aplicação: " + ex.getMessage());
            System.exit(1);
        }
    }

    public static File criaArquivoInicial() {
        try {
            File file = new File("tarefas.json"); // Diretório atual
            if (file.createNewFile()) {
                LOGGER.info("Arquivo criado: " + file.getName());
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            } else {
                LOGGER.info("Arquivo já existe: " + file.getName());
            }
            return file;
        }
        catch (Exception ex) {
            throw new RuntimeException("Erro ao criar o arquivo de tarefas", ex);
        }
    }
}