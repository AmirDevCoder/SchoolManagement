package domain.model.dto;

import domain.model.entity.BackOffice;

import java.util.ArrayList;
import java.util.List;

public interface BackOfficeDto {
    record Request(String firstName, String lastName, int age, String nationalId, String role, List<String> permissions,
                   BackOffice.Contact contact) {

        public Request {
            List<String> errors = new ArrayList<>();

            if (firstName.isEmpty() || firstName.isBlank()) {
                errors.add("firstName is blank");
            }
            if (lastName.isEmpty() || lastName.isBlank()) {
                errors.add("lastName is blank");
            }
            if (age < 18) {
                errors.add("age is less than 18");
            }
            if (nationalId.isEmpty() || nationalId.isBlank()) {
                errors.add("nationalId is blank");
            }
            if (role.isEmpty() || role.isBlank()) {
                errors.add("role is blank");
            }
            if (permissions.isEmpty()) {
                errors.add("permissions is blank");
            }
            if (contact == null) {
                errors.add("contact is blank");
            } else {
                if (contact.getEmail().isEmpty() || contact.getEmail().isBlank()) {
                    errors.add("contact email is blank");
                }
                if (contact.getPhone().isEmpty() || contact.getPhone().isBlank()) {
                    errors.add("contact phone is blank");
                }
            }

            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Validation errors: " + String.join(", ", errors));
            }
        }

        public BackOffice toBackOffice() {
            return BackOffice.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .age(age)
                    .nationalId(nationalId)
                    .role(role)
                    .permissions(permissions)
                    .contact(contact)
                    .build();
        }

    }

    record Response(String id, String fullName) {
    }
}
