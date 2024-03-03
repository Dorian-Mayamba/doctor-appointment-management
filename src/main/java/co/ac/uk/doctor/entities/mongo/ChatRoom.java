package co.ac.uk.doctor.entities.mongo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    private String chatId;

    private String senderId;

    private String recipientId;
}
