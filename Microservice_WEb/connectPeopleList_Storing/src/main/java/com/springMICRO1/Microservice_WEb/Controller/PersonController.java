package com.springMICRO1.Microservice_WEb.Controller;

import com.springMICRO1.Microservice_WEb.presentation.ChatMessage;
import com.springMICRO1.Microservice_WEb.presentation.ConnectedPeoples;
import com.springMICRO1.Microservice_WEb.presentation.Message;
import com.springMICRO1.Microservice_WEb.repositiory.MessageRepository;
import com.springMICRO1.Microservice_WEb.service.MessageService;
import com.springMICRO1.Microservice_WEb.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin to match your frontend URL
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private MessageService messageService; // Injecting MessageService to save the message in the database

    // Constructor injection for required services

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/naming/connected-people")
    public String first() {
        return "hell world from connected peoples";
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @MessageMapping("/chat")
   
    public void processMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Received message in /app/chat: " + chatMessage);

        try {
            List<ConnectedPeoples> senderList = personService.getConnectedPeopleByContact(chatMessage.getSender());
            List<ConnectedPeoples> receiverList = personService
                    .getConnectedPeopleByContact(chatMessage.getReceiverContact());

            if (senderList.isEmpty() || receiverList.isEmpty()) {
                System.err.println("Sender or Receiver not found in the connected people list.");
                return;
            }

            ConnectedPeoples sender = senderList.get(0); // Assumption based on your logic
            ConnectedPeoples receiver = receiverList.get(0); // Assumption based on your logic

            if (chatMessage.getImage() != null && !chatMessage.getImage().isEmpty()) {
                System.out.println("Message contains an image. Saving into database.");
                byte[] imageBytes = chatMessage.getImage().getBytes(); // Convert the image to byte array
                Message message = messageService.saveMessage(
                        sender.getContact(),
                        receiver.getContact(),
                        chatMessage.getMessage(),
                        imageBytes);
                System.out.println("Message saved successfully for receiver: " + receiver.getContact());
            } else {
                System.out.println("Message does not contain an image. Saving text-only message into database.");
                Message message = messageService.saveMessage(
                        sender.getContact(),
                        receiver.getContact(),
                        chatMessage.getMessage(),
                        null // No image, pass null for imageBytes
                );
                System.out.println("Text-only message saved successfully for receiver: " + receiver.getContact());
            }

            // Send the message to the receiver through WebSocket
            messagingTemplate.convertAndSend(
                    "/queue/messages/" + chatMessage.getReceiverContact(),
                    chatMessage);
            System.out.println("Message sent to receiver: " + chatMessage.getReceiverContact());
            System.out.println("massage reevied by user " + chatMessage.getSender() + " and the chat massage is "
                    + chatMessage.getMessage());

        } catch (Exception e) {
            System.err.println("Error processing the message: " + e.getMessage());
        }
    }

    // @PostMapping("/messages")
    // public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
    // try {
    // Message savedMessage = messageService.saveMessage(message);
    // return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
    // }

    @GetMapping("/naming/hello")
    public String hello() {
        return "hello world connected people list";
    }

    // POST endpoint to add a new person
    @PostMapping("/naming/savepeoples")
    public ResponseEntity<?> savePerson(@RequestBody ConnectedPeoples person) {
        try {
            ConnectedPeoples savedPerson = personService.addPerson(person);
            return ResponseEntity.ok("Connected! Now you can chat with each other." + savedPerson);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("There was an error connecting the person.");
        }
    }

    // GET endpoint to retrieve a list of connected people based on email
    @GetMapping("/naming/connectpersion")
    public ResponseEntity<List<ConnectedPeoples>> getConnectedPeoples(@RequestParam("email") String email) {
        try {
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(null); // Bad request if email is missing
            }

            List<ConnectedPeoples> connectedPeoples = personService.getConnectedPeoplesByEmail(email);

            if (connectedPeoples.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList()); // Return empty list if no connected people
            } else {
                return ResponseEntity.ok(connectedPeoples);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Internal Server Error
        }
    }

    // making for message saving

    @GetMapping("/list/{contact}")
    public ResponseEntity<List<ConnectedPeoples>> getConnectedPeopless(@PathVariable String contact) {
        List<ConnectedPeoples> connectedPeoples = messageService.getConnectedPeoplesList(contact);
        return ResponseEntity.ok(connectedPeoples);
    }

    // making the getting the massages of connected oerson by their contact number
    // using list data structure

    @GetMapping("/getting/messages/{senderContact}/{receiverContact}")
    public List<Message> getMessages(
            @PathVariable String senderContact,
            @PathVariable String receiverContact) {
        return messageService.getMessagesBySenderAndReceiver(senderContact, receiverContact);
    }

    // @PostMapping("/connected-list/add")
    // public ResponseEntity<?> addToConnectedList(@RequestBody Map<String, String>
    // details) {
    // String receiverContact = details.get("receiverContact");
    // String senderName = details.get("senderName");
    // String senderContact = details.get("senderContact");

    // if (receiverContact == null || senderName == null || senderContact == null) {
    // return ResponseEntity.badRequest().body("Missing required details.");
    // }

    // User receiver = userRepository.findByContact(receiverContact);
    // if (receiver != null) {
    // // Add the sender to the receiver's connected list
    // User sender = new User();
    // sender.setName(senderName);
    // sender.setContact(senderContact);
    // // Add sender if not already connected
    // if (!receiver.getConnectedPeople().contains(sender)) {
    // receiver.getConnectedPeople().add(sender);
    // userRepository.save(receiver);
    // }
    // return ResponseEntity.ok("Sender added to receiver's connected list.");
    // }
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not
    // found.");
    // }

}
