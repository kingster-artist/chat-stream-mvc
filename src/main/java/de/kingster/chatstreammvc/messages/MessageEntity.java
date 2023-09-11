package de.kingster.chatstreammvc.messages;

import lombok.Data;

/**
 * the entity to be stored
 */
@Data
public class MessageEntity {

    public static MessageEntity createQuick(String id, String text) {
        MessageEntity e = new MessageEntity();
        e.setId(id);
        e.setText(text);
        return e;
    }

    private String id;

    private String text;

    private boolean seen;

}
