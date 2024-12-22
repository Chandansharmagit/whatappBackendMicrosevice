package authentications.authentications.presentations;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UUserUpdateRequest {
    private int id;
    private String token;
    private String name;
    private String contact;


    private MultipartFile image1;

    private byte[] returnimage1;


}
