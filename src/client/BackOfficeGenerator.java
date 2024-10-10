package client;

import domain.model.entity.BackOffice;

import java.util.List;
import java.util.Random;

class BackOfficeGenerator {
    private static final List<String> roles = List.of("Admin", "Manager", "Clerk", "Supervisor");
    private static final List<String> permissions = List.of("READ", "WRITE", "DELETE", "UPDATE");

    private static final Random random = new Random();


    public static String getRole() {
        return roles.get(random.nextInt(roles.size()));
    }

    public static List<String> getPermissions() {
        int numberOfPermissions = random.nextInt(permissions.size()) + 1;
        return permissions.subList(0, numberOfPermissions);
    }

    public static String getPhoneNumber() {
        return String.valueOf(1_000_000_000L + random.nextInt(900_000_000));
    }

    public static BackOffice.Contact getContact() {
        return BackOffice.Contact.builder()
                .email(UserGenerator.getEmails())
                .phone(getPhoneNumber())
                .build();
    }
}
