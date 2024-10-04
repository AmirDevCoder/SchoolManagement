package locator;

import application.service.StudentServiceImpl;
import data.db.conf.Postgres;
import data.repository.UserRepositoryImpl;
import lib.db.DbConnection;
import domain.repository.UserRepository;
import domain.service.StudentService;

import java.sql.Connection;

public final class ServiceLocator {

    private static StudentService studentSvc;

    private ServiceLocator() {}

    static {
        Throwable firstException = null;
        Connection connection = null;
        try {
            DbConnection dbConnection = DbConnection.getInstance(Postgres.class);
            connection = dbConnection.getConnection();

            UserRepository studentRepo = new UserRepositoryImpl(connection);
            studentSvc = new StudentServiceImpl(studentRepo);
        } catch (Exception e) { // suppressed exception
            firstException = e;
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception closeException) {
                    firstException.addSuppressed(closeException);
                }
            }
        }

        if (firstException != null) {
            throw new RuntimeException(firstException);
        }

    }

    public static <T extends LocatableService> T getService(Class<T> clazz) {
        if (clazz == StudentService.class) {
            return clazz.cast(studentSvc);
        } else {
            throw new IllegalArgumentException("Unknown service: " + clazz.getName());
        }
    }

}
