package client;

import java.util.List;
import java.util.Random;
import java.util.UUID;

class CourseGenerator {

    private static final Random random = new Random();

    private static final List<String> courseNames = List.of(
            "Introduction to Programming", "Data Structures", "Algorithms",
            "Database Systems", "Software Engineering", "Operating Systems",
            "Computer Networks", "Web Development", "Artificial Intelligence",
            "Machine Learning", "Mobile App Development", "Cybersecurity"
    );

    private static final List<String> courseDescriptions = List.of(
            "A comprehensive introduction to programming concepts.",
            "Study of data structures such as arrays, lists, stacks, and queues.",
            "An in-depth look at algorithms for problem-solving and optimization.",
            "Covers database design, SQL, and NoSQL database systems.",
            "Focuses on software development processes and methodologies.",
            "Introduction to operating system concepts, including processes, memory, and file systems.",
            "Covers networking concepts, protocols, and network architecture.",
            "Learn how to build dynamic and responsive websites.",
            "Explore the foundations of artificial intelligence and its applications.",
            "A course on machine learning algorithms and their applications.",
            "Develop mobile applications for Android and iOS platforms.",
            "Covers topics in cybersecurity including encryption and network security."
    );

    public static String getName() {
        String suffix = UUID.randomUUID().toString();
        return courseNames.get(random.nextInt(courseNames.size())) + " " + suffix;
    }

    public static String getDescription() {
        return courseDescriptions.get(random.nextInt(courseDescriptions.size()));
    }

}
