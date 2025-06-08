# Gerenciador de Tarefas (Task Tracker)

Este é um aplicativo de gerenciador de tarefas via linha de comando (CLI) desenvolvido em Java. O projeto segue o guia prático proposto pelo [roadmap.sh](https://roadmap.sh/projects/task-tracker) e implementa as funcionalidades essenciais para criar, gerenciar e persistir tarefas.

As tarefas são salvas localmente em um arquivo `tarefas.json`, que é criado e gerenciado pela própria aplicação.

## Funcionalidades

* **Adicionar Tarefas:** Crie novas tarefas com uma descrição.
* **Listar Tarefas:** Visualize todas as tarefas ou filtre-as por status.
* **Atualizar Tarefas:** Modifique a descrição ou o status de uma tarefa existente.
* **Deletar Tarefas:** Remova tarefas da lista.
* **Persistência de Dados:** As tarefas são salvas em um arquivo JSON (`tarefas.json`), garantindo que os dados não sejam perdidos ao fechar a aplicação.
* **Interface de Linha de Comando (CLI):** Interação simples e direta através do terminal.

## 🛠️ Tecnologias e Bibliotecas

* **Java 21+**
* **Jackson:** Biblioteca para serialização e desserialização de objetos Java para JSON.
    * `jackson-databind`
    * `jackson-datatype-jsr310` (para suporte a `java.time.LocalDateTime`)

## ⚙️ Pré-requisitos

* **Java Development Kit (JDK) 21** ou superior instalado e configurado no seu sistema.
* **Apache Maven** instalado e configurado no seu sistema ou executar diretamente na IDE **IntelliJ**.

## 🚀 Como Executar

O Maven gerencia as dependências do projeto. Garanta que o seu arquivo `pom.xml` contenha as seguintes dependências da biblioteca Jackson:

```xml
<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.17.1</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
        <version>2.17.1</version>
    </dependency>
</dependencies>
```

1. Clone o repositório:
   ```bash
   git clone <url-do-repositório>
   cd task-tracker-cli
   ```

2. Compile o projeto usando Maven:
   ```bash
   mvn clean install
   ```

3. **Execute a Aplicação**:

   Use o plugin `exec-maven-plugin` para compilar e rodar a aplicação com um único comando:

   ```bash
   mvn compile exec:java -Dexec.mainClass="erikzaina.Main"
   ```
   

## 📋 Comandos Disponíveis

A seguir, a lista de comandos que você pode usar na aplicação:

| Comando                      | Descrição                                                    |
| :--------------------------- | :----------------------------------------------------------- |
| `add "descrição"`            | Adiciona uma nova tarefa. A descrição deve estar entre aspas. |
| `update ID "nova descrição"` | Atualiza a descrição de uma tarefa existente.                |
| `delete ID`                  | Deleta uma tarefa pelo seu ID.                               |
| `mark-done ID`               | Marca uma tarefa como concluída (`DONE`).                    |
| `mark-in-progress ID`        | Marca uma tarefa como em progresso (`IN_PROGRESS`).          |
| `mark-todo ID`               | Marca uma tarefa como a fazer (`TODO`).                      |
| `list`                       | Lista todas as tarefas cadastradas.                          |
| `list todo`                  | Lista apenas as tarefas com status `TODO`.                   |
| `list in-progress`           | Lista apenas as tarefas com status `IN_PROGRESS`.            |
| `list done`                  | Lista apenas as tarefas com status `DONE`.                   |
| `help`                       | Mostra a lista de todos os comandos disponíveis.             |
| `exit`                       | Encerra a aplicação.                                         |

## 🏗️ Estrutura do Projeto

O código está organizado nos seguintes pacotes:

* `erikzaina`: Contém a classe `Main` que inicializa a aplicação.
* `erikzaina.entity`: Contém as classes de modelo (`Task`, `Status`).
* `erikzaina.DAO`: Contém a interface `TarefasDAO` e sua implementação `TarefasDaoJsonImpl`, responsáveis pela lógica de acesso aos dados.
* `erikzaina.CLI`: Contém a classe `TaskCLI`, que gerencia a interação com o usuário pela linha de comando.
* `erikzaina.exception`: Contém as exceções customizadas da aplicação.
