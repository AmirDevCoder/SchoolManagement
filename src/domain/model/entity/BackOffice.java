package domain.model.entity;

import java.util.List;

import lombok.*;
import org.bson.types.ObjectId;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BackOffice {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private int age;
    private String nationalId;
    private String role;
    private List<String> permissions;
    private Contact contact;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Contact {
        private String email;
        private String phone;
    }
}