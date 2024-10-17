package locator;

import presentation.aop.ProxyFactory;
import presentation.service.*;
import data.repository.*;
import domain.model.entity.BackOffice;
import domain.model.entity.Logs;
import domain.service.*;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ServiceFactory {
    @Getter
    private static final Map<Class<? extends LocatableService>, LocatableService> services = new HashMap<>();

    static {

        var loggerRepo = new LoggerRepositoryImpl(ConnectionManager.getMongoDatabase().getCollection(Logs.class.getSimpleName().toLowerCase(), Logs.class));
        var loggerSvc = new LoggerServiceImpl(loggerRepo);

        var teacherRepo = new TeacherRepositoryImpl(ConnectionManager.getPostgresConnection());
        var teacherSvc = new TeacherServiceImpl(teacherRepo, loggerSvc);

        var courseRepo = new CourseRepositoryImpl(ConnectionManager.getPostgresConnection());
        var courseSvc = new CourseServiceImpl(courseRepo, loggerSvc);

        var studentRepo = new StudentRepositoryImpl(ConnectionManager.getPostgresConnection(), courseRepo);
        var studentSvc = new StudentServiceImpl(studentRepo, loggerSvc);
        var proxyFactory = new ProxyFactory(loggerSvc);
        var studentSvcProxy = proxyFactory.createProxy(studentSvc, StudentService.class);

        var gradeRepo = new GradeRepositoryImpl(ConnectionManager.getPostgresConnection());
        var gradeSvc = new GradeServiceImpl(gradeRepo, loggerSvc);

        var examRepo = new ExamRepositoryImpl(ConnectionManager.getPostgresConnection());
        var examSvc = new ExamServiceImpl(examRepo, loggerSvc);

        var backOfficeRepo = new BackOfficeRepositoryImpl(ConnectionManager.getMongoDatabase().getCollection(BackOffice.class.getSimpleName().toLowerCase(), BackOffice.class));
        var backOfficeSvc = new BackOfficeServiceImpl(backOfficeRepo, loggerSvc);

        services.put(StudentService.class, studentSvcProxy);
        services.put(LoggerService.class, loggerSvc);
        services.put(GradeService.class, gradeSvc);
        services.put(ExamService.class, examSvc);
        services.put(CourseService.class, courseSvc);
        services.put(TeacherService.class, teacherSvc);
        services.put(BackOfficeService.class, backOfficeSvc);

    }

}
