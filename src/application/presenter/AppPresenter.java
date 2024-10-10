package application.presenter;

import client.ClientManager;
import domain.model.dto.StudentDto;
import lombok.experimental.UtilityClass;

import java.sql.Date;
import java.util.List;

@UtilityClass
public class AppPresenter {

    public static void run() {

        var res1 = ClientManager.builder()
                .upsertTeacher()
                .upsertTeacher()
                .upsertCourse(1)
                .upsertCourse(2)
                .upsertStudent(List.of(1))
                .upsertStudent(List.of(1, 2))
                .build();

        System.out.println(res1);


    }


}
