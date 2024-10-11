package client;

import domain.model.dto.BackOfficeDto;
import domain.model.dto.CourseDto;
import domain.model.dto.StudentDto;
import domain.model.dto.TeacherDto;
import domain.service.*;
import locator.ServiceLocator;
import util.ResultWrapper;

import java.util.ArrayList;
import java.util.List;

/// There may be an issue when performing an upsert operation on entities (e.g.student, course) at different times
public final class ClientManager {
    private final static List<ResultWrapper<?>> results = new ArrayList<>();
    private final static StudentService studentSvc = ServiceLocator.getService(StudentService.class);
    private final static TeacherService teacherSvc = ServiceLocator.getService(TeacherService.class);
    private final static CourseService courseSvc = ServiceLocator.getService(CourseService.class);
    private final static ExamService examSvc = ServiceLocator.getService(ExamService.class);
    private final static GradeService gradeSvc = ServiceLocator.getService(GradeService.class);
    private final static BackOfficeService backOfficeSvc = ServiceLocator.getService(BackOfficeService.class);

    private ClientManager() {
    }

    // semi builder design-pattern
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

    public ClientManager deleteStudent(String nationalId) {
        results.add(studentSvc.delete(nationalId));
        return this;
    }

    public ClientManager fetchStudents() {
        results.add(studentSvc.getAll());
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

    public ClientManager findStudentByTeacherId(int teacherId) {
        results.add(studentSvc.findByTeacherId(teacherId));
        return this;
    }

    public ClientManager findStudentCount() {
        results.add(studentSvc.getCount());
        return this;
    }

    public ClientManager upsertStudent(StudentDto.Request req) {
        results.add(studentSvc.save(req));
        return this;
    }

    public ClientManager insertBackOffice() {
        results.add(backOfficeSvc.save(new BackOfficeDto.Request(
                UserGenerator.getName(),
                UserGenerator.getFamily(),
                UserGenerator.getAge(),
                UserGenerator.getNationalId(),
                BackOfficeGenerator.getRole(),
                BackOfficeGenerator.getPermissions(),
                BackOfficeGenerator.getContact()
        )));

        return this;
    }

    public ClientManager fetchBackOffices() {
        results.add(backOfficeSvc.getAll());
        return this;
    }

    public ClientManager deleteBackOffice(String nationalId) {
        results.add(backOfficeSvc.delete(nationalId));
        return this;
    }

    public List<ResultWrapper<?>> build() {
        return results;
    }

}
