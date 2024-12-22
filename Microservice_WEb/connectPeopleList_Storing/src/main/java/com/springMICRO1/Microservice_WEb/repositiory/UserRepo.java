package com.springMICRO1.Microservice_WEb.repositiory;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springMICRO1.Microservice_WEb.presentation.User;
@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByContactNumber(String contactNumber);
}
