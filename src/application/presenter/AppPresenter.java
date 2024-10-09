package application.presenter;

import domain.model.dto.CourseDto;
import domain.model.dto.StudentDto;
import domain.model.dto.TeacherDto;
import domain.service.CourseService;
import domain.service.StudentService;
import domain.service.TeacherService;
import locator.ServiceLocator;
import lombok.experimental.UtilityClass;

import java.sql.Date;
import java.util.List;

@UtilityClass
public class AppPresenter {

    public static void run() {
//        TeacherService teacherSvc = ServiceLocator.getService(TeacherService.class);
//        teacherSvc.save(new TeacherDto.Request("t_name", "t_family", "t_email", new Date(System.currentTimeMillis()), "t_nationalId"));

//        CourseService courseSvc = ServiceLocator.getService(CourseService.class);
//        var courseRes = courseSvc.save(new CourseDto.Request("math", 1, "test description"));
//        if (!courseRes.isSuccess()) {
//            System.out.println(courseRes.getError());
//        }

        StudentService studentSvc = ServiceLocator.getService(StudentService.class);
        var StudentRes = studentSvc.save(new StudentDto.Request(
                "s_name", "s_family", "s_email", new Date(System.currentTimeMillis()), "s_nationalId", List.of(5)));
        if (!StudentRes.isSuccess()) {
            System.out.println(StudentRes.getError());
        }

    }


}
