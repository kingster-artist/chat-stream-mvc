package de.kingster.chatstreammvc.messages;

import lombok.Data;

/**
 * transfer object for POST
 * (create message)
 */
@Data
public class CreateMessageTO {

    private String text;

}
