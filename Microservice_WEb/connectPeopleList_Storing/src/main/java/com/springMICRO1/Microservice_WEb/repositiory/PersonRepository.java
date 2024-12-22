package com.springMICRO1.Microservice_WEb.repositiory;

import com.springMICRO1.Microservice_WEb.presentation.ConnectedPeoples;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<ConnectedPeoples, Long> {
    // ConnectedPeoples findByContact(String contact);
    List<ConnectedPeoples> findByEmail(String email);
    boolean existsByContact(String contact);
    List<ConnectedPeoples> findByContact(String contact);
    List<ConnectedPeoples> findByContactIn(Set<String> contacts);

  
}
