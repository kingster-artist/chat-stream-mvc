package de.kingster.chatstreammvc.messages;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * database access to the set of messages
 */
@Repository
public class MessageRepository {

    private List<MessageEntity> messages = Collections.synchronizedList(new ArrayList<>(32));

    /**
     * creates new message
     * @param messageEntity
     */
    public void saveNewMessage(MessageEntity messageEntity) {
        messages.add(messageEntity);
    }

    public List<MessageEntity> findNewMessages() {
        return messages.stream().filter(m -> !m.isSeen()).map(m -> {
            m.setSeen(true);
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * returns one unseen message or NULL if none is found
     * @return
     */
    public MessageEntity oneUnseenMessage() {
        for (MessageEntity m : messages) {
            if (!m.isSeen()) {
                m.setSeen(true);
                return m;
            }
        }
        return null;
    }

}
