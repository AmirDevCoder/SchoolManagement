package application.presenter;

import domain.entity.Student;
import domain.service.StudentService;
import locator.ServiceLocator;

import java.sql.Date;
import java.util.Collections;

public final class App {

    private App() {
    }

    public static void run() {
        StudentService studentSvc = ServiceLocator.getService(StudentService.class);

//        System.out.println(studentSvc.getAllStudents());
        System.out.println(studentSvc.getCountOfStudents());
        System.out.println(studentSvc.save((Student) Student.Builder()
                .setGpu(5)
                .setFirstName("John")
                .setLastName("Doe")
                .setDob(new Date(3))
                .setNationalId("8888888888")
        ));
    }

}
