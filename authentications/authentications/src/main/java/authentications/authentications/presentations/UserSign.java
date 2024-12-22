package authentications.authentications.presentations;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userslogin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true) // Make name nullable
    private String name;

    @Column(nullable = true) // Make contact nullable
    private String contact;

    @Column(nullable = true) // Make email nullable
    private String email;

    @Column(nullable = true) // Make password nullable
    private String password;

    @Column(nullable = true) // Make OTP nullable
    private Integer otp;

    @Column(nullable = true) // Make token nullable
    private String token;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB", nullable = true) // Make image nullable
    private byte[] image1;
}
