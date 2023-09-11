package de.kingster.chatstreammvc.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class MessageController {

    /**
     * access to database
     */
    @Autowired
    private MessageRepository messageRepository;

    @PostConstruct
    public void postConstruct() {
        for (int i = 0; i < 10; i++) {
            messageRepository.saveNewMessage(MessageEntity.createQuick(UUID.randomUUID().toString().toLowerCase(Locale.ROOT), "Nachricht " + i));
            debug("created dummy message");
        }
    }

    /**
     * just for testing if instance is there
     *
     * @return
     */
    @GetMapping("/api/v1/info")
    public String getInfo() {
        return "OK-mvc";
    }

    /**
     * get stream of message, see in browser
     *
     * @return
     */
    @GetMapping(path = "/api/v1/messages/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getMessagesStream() {

        debug("accessing stream");
        SseEmitter emitter = new SseEmitter(20000L);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {

                for (int i = 0; i < 100; i++) {
                    var message = this.messageRepository.oneUnseenMessage();

                    if (message != null) {

                        ObjectMapper objectMapper = new ObjectMapper();
                        String data = objectMapper.writeValueAsString(message);

                        var event = SseEmitter.event()
                                .id(message.getId())
                                .name("chat-messsage-stream")
                                .data(data)
                                .reconnectTime(5000);
                        emitter.send(event);
                    }
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally {
                emitter.complete();
            }
        });

        return emitter;
    }

    /**
     * create a new message
     *
     * @param to
     * @return
     */
    @PostMapping("/api/v1/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageEntity createMessage(@RequestBody CreateMessageTO to) {
        MessageEntity e = new MessageEntity();
        e.setText(to.getText());
        this.messageRepository.saveNewMessage(e);
        debug("POST - created and saved message");
        return e;
    }

    @GetMapping("/api/v1/create-message")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageEntity createMessageGET() {
        var e = MessageEntity.createQuick(UUID.randomUUID().toString().toLowerCase(Locale.ROOT), "Testnachricht durch GET am " + LocalDateTime.now().toString());
        this.messageRepository.saveNewMessage(e);
        debug("GET - created and saved message");
        return e;
    }

    private void debug(Object o) {
        System.out.println(o);
    }

}
