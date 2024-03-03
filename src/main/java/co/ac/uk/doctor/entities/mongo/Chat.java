package co.ac.uk.doctor.entities.mongo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;


@Document(collection = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    private String chatId;

    private String senderName;

    private String recipientName;

    private String messageContent;

    Timestamp date;
}
