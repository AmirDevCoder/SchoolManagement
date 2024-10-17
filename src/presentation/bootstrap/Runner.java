package presentation.bootstrap;

import client.MockClient;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Runner {

    public static void run() {

//        Logy.log(LogyLevel.SUCCESS, ClientManager.builder().upsertStudent(List.of(3, 4, 1)).build());


//        Logy.log(LogyLevel.SUCCESS,
//                ClientManager.builder()
//                        .fetchStudents()
//                        .build());

        System.out.println(MockClient.builder()
                .upsertTeacher()
                .upsertTeacher()
                .upsertTeacher()
                .upsertTeacher()
                .upsertCourse(1)
                .upsertCourse(2)
                .upsertCourse(3)
                .upsertCourse(4)
                .upsertStudent(List.of(1, 2, 3, 4))
                .upsertStudent(List.of(1, 2))
                .upsertStudent(List.of(3, 4))
                .upsertStudent(List.of(1))
                .upsertStudent(List.of(2, 4))
                .upsertStudent(List.of(1, 2, 3))
                .upsertStudent(List.of(4))
                .build());


    }

}

