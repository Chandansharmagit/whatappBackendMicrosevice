package authentications.authentications.repo;

import authentications.authentications.presentations.UserSign;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userloginRepository extends JpaRepository<UserSign,Integer> {
    UserSign findByEmail(String email);
    UserSign findBytoken(String token);


    UserSign findByContact(String contact);
}
