package authentications.authentications.service;

import authentications.authentications.config.JwtUtil;
import authentications.authentications.presentations.UUserUpdateRequest;
import authentications.authentications.presentations.UserSign;
import authentications.authentications.repo.userloginRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class userloginService {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private userloginRepository repository;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private authentications.authentications.repo.userloginRepository userloginRepository;


    public String regiser(UserSign login){
        if(repository.findByEmail(login.getEmail()) != null){
            return "user already exits";
        }else{
            //hasing the password
            login.setPassword(passwordEncoder.encode(login.getPassword()));

            repository.save(login);
        }

        return "user register sucessful";
    }


    public ResponseEntity<Map<String, String>> loginuser(String email, String password) {
        UserSign sign = repository.findByEmail(email);

        if (sign != null && passwordEncoder.matches(password, sign.getPassword())) {
            // Generating token
            String token = jwtUtil.generateToken(email);

            // Updating user record with new token
            sign.setToken(token);
            repository.save(sign);

            // Preparing the response body for token
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("email", email);

            return ResponseEntity.ok(responseBody); // Return token and email in the response
        } else {
            // Return an error response if login failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login failed"));
        }
    }


    public UserSign findUserByEmail(String email) {
        // Use the repository to find the user by email
        return repository.findByEmail(email);
    }

    public void saveUser(UserSign user) {
        // Method to save user details (including OTP)
        repository.save(user);
    }


    //verifying the opt

    public String verifyOtp(String email, int otp) throws Exception {
        // Logic to retrieve the user based on email
        UserSign user = repository.findByEmail(email); // Assuming this method exists

        if (user == null) {
            throw new Exception("User not found for the provided email.");
        }

        // Logic to validate the OTP (this might depend on how you're storing OTPs)
        if (user.getOtp() == otp) {
            // OTP is valid; proceed with password reset or whatever logic you need

            repository.save(user);
            return "OTP verified successfully.";
        } else {
            throw new Exception("Invalid OTP.");
        }
    }



    //changing the password as new replacing the old password

    public boolean changePassword(String email, String newPassword) {
        UserSign user = repository.findByEmail(email);

        if (user == null) {
            return false; // User not found
        }

        // Hash the new password before saving (if you are using password hashing)
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        repository.save(user); // Save the updated user
        return true;
    }


    //getting the user details

    public UserSign userdetails(String token){
        return repository.findBytoken(token);

    }

    //fidning the user by contact

    public UserSign usercontact(String contact){
        return repository.findByContact(contact);
    }


    //updating the user detials

    public UUserUpdateRequest updateUser(UUserUpdateRequest request) throws Exception {

        // Fetch user by token from the database (you can implement this method in your repository)
        UserSign userSign = repository.findBytoken(request.getToken());

        // If the user with the given token doesn't exist, throw an exception
        if (userSign == null) {
            throw new Exception("Token is invalid or user not found");
        }

        // Update user details
        userSign.setContact(request.getContact());
        userSign.setName(request.getName());
        userSign.setImage1(request.getImage1().getBytes());  // Convert MultipartFile to byte array

        // Save updated user details
        UserSign savedUser = repository.save(userSign);

        // Create and return updated user DTO
        UUserUpdateRequest updatedUserRequest = new UUserUpdateRequest();
        updatedUserRequest.setId(savedUser.getId());

        return updatedUserRequest;
    }


 

    


  











}
