package co.ac.uk.doctor.repositories.mongo;

import co.ac.uk.doctor.entities.mongo.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends MongoRepository<Chat,String> {

}
