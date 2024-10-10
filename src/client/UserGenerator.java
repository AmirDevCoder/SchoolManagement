package client;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class UserGenerator {
    private static final List<String> names = List.of("John", "Jane", "Alice", "Bob", "Emily", "Michael", "Sarah", "David", "Sophia", "James");
    private static final List<String> families = List.of("Smith", "Doe", "Brown", "Johnson", "Williams", "Jones", "Miller", "Davis", "Garcia", "Rodriguez");
    private static final List<Integer> ages = List.of(18, 23, 30, 35, 40, 45, 50, 55, 60, 65);
    private static final Set<String> generatedIds = new HashSet<>();

    private static final Random random = new Random();

    public static String getName() {
        return names.get(random.nextInt(names.size()));
    }

    public static String getFamily() {
        return families.get(random.nextInt(families.size()));
    }

    public static int getAge() {
        return ages.get(random.nextInt(ages.size()));
    }

    public static String getEmails() {
        String prefix = UUID.randomUUID().toString();
        return prefix + "." + getName().toLowerCase() + "@example.com";
    }

    public static String getNationalId() {
        String nationalId;
        do {
            nationalId = String.valueOf(1_000_000_000L + random.nextInt(900_000_000));
        } while (!isUnique(nationalId));

        generatedIds.add(nationalId);
        return nationalId;
    }

    private static boolean isUnique(String nationalId) {
        return !generatedIds.contains(nationalId);
    }

    public static Date getBirthday() {
        // 2000-01-01
        long minDay = 946684800000L;
        long maxDay = System.currentTimeMillis();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return new Date(randomDay);
    }

}
