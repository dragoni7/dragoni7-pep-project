package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUser(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(), Account.class);

        if (account.getUsername() != "" && account.getPassword().length() > 3) {
            Account addedAccount = accountService.AddAccount(account);

            if (addedAccount == null) {
                ctx.status(400);
            } else {
                ctx.json(mapper.writeValueAsString(addedAccount)).status(200);
            }
        }
        else {
            ctx.status(400);
        }
    }

    private void loginUser(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(ctx.body(), Account.class);

        Account foundAccount = accountService.GetAccount(account.getUsername(), account.getPassword());

        if (foundAccount == null){
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(foundAccount)).status(200);
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(ctx.body(), Message.class);
        Account foundAccount = accountService.getAccount(message.getPosted_by());

        int messageLength = message.getMessage_text().toCharArray().length;
        
        boolean invalidRequest = foundAccount == null || messageLength > 255 || message.getMessage_text() == "";

        if (invalidRequest) {
            ctx.status(400);
        }
        else {
            Message addedMessage = messageService.addMessage(message);

            if (addedMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(mapper.writeValueAsString(addedMessage)).status(200);
            }
        }
    }
    
    private void getAllMessages(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        List<Message> messages = messageService.getAllMessages();
        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }

    private void getMessage(Context ctx) throws JsonProcessingException {
        int messageId = Integer.valueOf(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message message = messageService.getMessage(messageId);

        if (message == null) {
            ctx.status(200);
        }
        else {
            ctx.json(mapper.writeValueAsString(message)).status(200);
        }
    }

    private void deleteMessage(Context ctx) throws JsonProcessingException {
        int messageId = Integer.valueOf(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message foundMessage = messageService.getMessage(messageId);

        if (foundMessage == null) {
            ctx.status(200);
        }
        else {
            messageService.deleteMessage(messageId);
            ctx.json(mapper.writeValueAsString(foundMessage)).status(200);
        }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        int messageId = Integer.valueOf(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message m = mapper.readValue(ctx.body(), Message.class);
        int messageLength = m.getMessage_text().toCharArray().length;

        if (messageLength > 255 || m.getMessage_text() == "") {
            ctx.status(400);
        }
        else {
            Message message = messageService.updateMessage(messageId, m.getMessage_text());

            if (message == null) {
                ctx.status(400);
            }
            else {
                ctx.json(mapper.writeValueAsString(message)).status(200);
            }
        }
    }

    private void getUserMessages(Context ctx) throws JsonProcessingException {
        int accountId = Integer.valueOf(ctx.pathParam("account_id"));
        ObjectMapper mapper = new ObjectMapper();

        List<Message> messages = messageService.getUserMessages(accountId);
        ctx.json(mapper.writeValueAsString(messages)).status(200);
    }
}