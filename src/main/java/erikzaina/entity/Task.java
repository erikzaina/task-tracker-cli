package erikzaina.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

     private Integer id;
     private String description;
     private Status status;
     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;

     public Task(String description) {
          this.description = description;
     }

     private Task() {
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public Status getStatus() {
          return status;
     }

     public void setStatus(Status status) {
          this.status = status;
     }

     public LocalDateTime getCreatedAt() {
          return createdAt;
     }

     public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
     }

     public LocalDateTime getUpdatedAt() {
          return updatedAt;
     }

     public void setUpdatedAt(LocalDateTime updatedAt) {
          this.updatedAt = updatedAt;
     }

     @Override
     public String toString() {
          return "Task{" +
                  "id=" + id +
                  ", description='" + description + '\'' +
                  ", status=" + status +
                  ", createdAt=" + createdAt +
                  ", updatedAt=" + updatedAt +
                  '}';
     }
}
