package authentications.authentications.controller;

import authentications.authentications.config.JwtUtil;
import authentications.authentications.presentations.UUserUpdateRequest;
import authentications.authentications.presentations.UserSign;
import authentications.authentications.repo.userloginRepository;
import authentications.authentications.service.userloginService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin to match your frontend URL
public class controller {

    @Autowired
    private userloginRepository userloginRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();




    @Autowired
    private userloginService userloginService;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/auth")
    public String helloworld(){
        return "hellow world from authentications";
    }


    
    @GetMapping("/auth/hello")
    public String hello(){
        return "hello world from authentications chandan sharma for testing purpose";
    }

    @PostMapping("/auth/savinguser")
    public ResponseEntity<String> savingUser(
            @RequestParam("name") String name,
            @RequestParam("contact") String contact,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        // Create a UserSign instance
        UserSign userSign = new UserSign();
        userSign.setName(name);
        userSign.setContact(contact);
        userSign.setEmail(email);
        userSign.setPassword(password);

        // Call your service to register the user
        String message = userloginService.regiser(userSign);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/auth/signing")
    public ResponseEntity<Map<String, String>> loginUser(@RequestParam String email, @RequestParam String password) {
        UserSign sign = userloginService.findUserByEmail(email);

        if (sign != null && passwordEncoder.matches(password, sign.getPassword())) {
            // Generating token
            String token = jwtUtil.generateToken(email);

            // Updating user record with new token
            sign.setToken(token);
            userloginRepository.save(sign);

            

           


            // Preparing the response body for token
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("email", email);
            responseBody.put("contact",sign.getContact());
            responseBody.put("sender_name",sign.getName());

            return ResponseEntity.ok(responseBody); // Return token and email in the response
        } else {
            // Return an error response if login failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login failed"));
        }
    }


    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            // Find the user by email
            UserSign user = userloginService.findUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
            }

            // Generate OTP, save it, and send the email
            int otp = generateOtp();
            user.setOtp(otp);
            userloginService.saveUser(user);
            sendOtpEmail(user.getEmail(), otp);

            return ResponseEntity.ok("Password reset OTP has been sent to your email.");

        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }

    // Method to generate a random 6-digit OTP
    private int generateOtp() {
        return new Random().nextInt(900000) + 100000;
    }

    // Method to send OTP email
    private void sendOtpEmail(String recipientEmail, int otp) throws MessagingException {
        String subject = "Your Password Reset OTP";
        String message = "Hello,\n\nYour OTP for password reset is: " + otp + "\n\nPlease use this OTP to reset your password.\n\nThank you.";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(message, false);

        mailSender.send(mimeMessage);
    }
    //verifying the otp

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        try {
            String message = userloginService.verifyOtp(email, otp);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            if (e.getMessage().contains("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else if (e.getMessage().contains("Invalid OTP")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
            }
        }
    }


    //changing the password

    @PostMapping("/auth/updating-password")
    public ResponseEntity<String> updatingpassword(@RequestParam String email, @RequestParam String newpassword) {
        UserSign user = userloginService.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found through this email");
        }

        // Check if the OTP is expired
        if (user.getOtp() != null ) {
            // If the OTP is expired, clear it and return a message
            String hashedPassword = passwordEncoder.encode(newpassword);
            user.setPassword(hashedPassword);
            user.setOtp(null); // Clear OTP after successful password change
            userloginService.saveUser(user);

            return ResponseEntity.ok("Password updated successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("otp has expired");
        }

        // Proceed to change the password

    }

    // Helper method to check if OTP is expired


    @GetMapping("/auth/profile/details")
    public ResponseEntity<?> getUserDetails(@RequestParam("token") String token) {
        try {
            UserSign user = userloginService.userdetails(token); // Service layer method
            if (user != null) {
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("name",user.getName());
                userDetails.put("email", user.getEmail());
                userDetails.put("contact", user.getContact());
                userDetails.put("image", user.getImage1());

                // Add any other fields you want to include in the response

                return ResponseEntity.ok(userDetails);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/auth/addpeoples")
    public ResponseEntity<?> addpeoples(@RequestParam("contact") String contact)throws Exception {
        UserSign user = userloginService.usercontact(contact);
        if( user != null){
            Map<String, Object> list = new HashMap<>();
            list.put("name", user.getName());
            list.put("contact",user.getContact());
            list.put("image",user.getImage1());

            return ResponseEntity.ok(list);
        }else{
            return  new ResponseEntity<>("user not found ",HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update/data")
    public ResponseEntity<?> updateUserData(@RequestParam("token") String token,
                                            @RequestParam("contact") String contact,
                                            @RequestParam("name") String name,
                                            @RequestParam("image") MultipartFile image) {
        try {
            // Create DTO with the provided request parameters
            UUserUpdateRequest updateRequest = new UUserUpdateRequest();
            updateRequest.setToken(token);
            updateRequest.setContact(contact);
            updateRequest.setName(name);
            updateRequest.setImage1(image);

            // Call service method to update user details
            UUserUpdateRequest updatedUser = userloginService.updateUser(updateRequest);

            // Return the updated user data as response
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            // Handle the case where token is invalid or user not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token or user not found");
        }
    }










}
