package client;

import domain.model.dto.CourseDto;
import domain.model.dto.StudentDto;
import domain.model.dto.TeacherDto;
import domain.service.*;
import locator.ServiceLocator;
import util.ResultWrapper;

import java.util.ArrayList;
import java.util.List;

public final class ClientManager {
    private final static StudentService studentSvc = ServiceLocator.getService(StudentService.class);
    private final static TeacherService teacherSvc = ServiceLocator.getService(TeacherService.class);
    private final static CourseService courseSvc = ServiceLocator.getService(CourseService.class);
    private final static ExamService examSvc = ServiceLocator.getService(ExamService.class);
    private final static GradeService gradeSvc = ServiceLocator.getService(GradeService.class);
    private final static BackOfficeService backOfficeSvc = ServiceLocator.getService(BackOfficeService.class);
    private final static List<ResultWrapper<?>> results = new ArrayList<>();

    private ClientManager() {
    }

    // semi builder-design pattern
    public static ClientManager builder() {
        return new ClientManager();
    }

    public ClientManager upsertTeacher() {
        results.add(teacherSvc.save(new TeacherDto.Request(
                UserGenerator.getName(),
                UserGenerator.getFamily(),
                UserGenerator.getEmails(),
                UserGenerator.getBirthday(),
                UserGenerator.getNationalId()
        )));

        return this;
    }

    public ClientManager upsertCourse(int teacherId) {
        results.add(courseSvc.save(new CourseDto.Request(
                CourseGenerator.getName(),
                teacherId,
                CourseGenerator.getDescription()
        )));

        return this;
    }

    public ClientManager upsertStudent(List<Integer> courseIds) {
        results.add(studentSvc.save(new StudentDto.Request(
                UserGenerator.getName(),
                UserGenerator.getFamily(),
                UserGenerator.getEmails(),
                UserGenerator.getBirthday(),
                UserGenerator.getNationalId(),
                courseIds
        )));

        return this;
    }

    public ClientManager upsertStudent(StudentDto.Request req) {
        results.add(studentSvc.save(req));
        return this;
    }

    public List<ResultWrapper<?>> build() {
        return results;
    }

}
