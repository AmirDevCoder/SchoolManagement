package data.util;


// TODO: create a custom lib to auto-generate query for insert,update,etc.
public final class QueryConst {

    private QueryConst() {}

    // language=SQL
    public static final String UPSERT_TEACHERS = """
                INSERT INTO teachers (first_name, last_name, email, dob, national_id)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (national_id) DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                email = EXCLUDED.email,
                dob = EXCLUDED.dob,
                national_id = EXCLUDED.national_id
                RETURNING *
                """;
    // language=SQL
    public static final String DELETE_TEACHER_BY_NATIONAL_ID = "DELETE FROM teachers WHERE national_id = ?";
    // language=SQL
    public static final String FETCH_ALL_TEACHERS = "SELECT * FROM teachers";
    // language=SQL
    public static final String DELETE_STUDENT_BY_NATIONAL_ID = "DELETE FROM students WHERE national_id = ?";
    // language=SQL
    public static final String FETCH_ALL_STUDENTS = "SELECT * FROM students";
    // language=SQL
    public static final String COUNT_OF_STUDENTS = "SELECT COUNT(*) FROM students";
    // language=SQL
    public static final String FETCH_BY_TEACHER_ID = """
            SELECT * FROM students s
            JOIN student_teacher st ON s.id = st.student_id
            JOIN teachers t ON t.id = st.teacher_id
            WHERE t.id = ?
            """;
    // language=SQL
    public static final String UPSERT_STUDENTS = """
                INSERT INTO students (first_name, last_name, email, dob, national_id)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (national_id) DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                email = EXCLUDED.email,
                dob = EXCLUDED.dob,
                national_id = EXCLUDED.national_id
                RETURNING *
                """;
    // language=SQL
    public static final String DELETE_COURSES_BY_NAME = "DELETE FROM courses WHERE name = ?";
    // language=SQL
    public static final String UPSERT_COURSES = """
                INSERT INTO courses (name, teacher_id, description) VALUES (?, ?, ?)
                ON CONFLICT (name) DO UPDATE SET
                name = EXCLUDED.name,
                teacher_id = EXCLUDED.teacher_id,
                description = EXCLUDED.description
                RETURNING *
                """;
    // language=SQL
    public static final String FETCH_ALL_COURSES = "SELECT * FROM courses";
    // language=SQL
    public static final String UPSERT_EXAMS = """
                INSERT INTO exams (name, course_id, teacher_id) VALUES (?, ?, ?)
                ON CONFLICT (name) DO UPDATE SET
                name = EXCLUDED.name,
                course_id = EXCLUDED.course_id,
                teacher_id = EXCLUDED.teacher_id
                RETURNING *
                """;
    // language=SQL
    public static final String DELETE_EXAMS_BY_NAME = "DELETE FROM exams WHERE name = ?";
    // language=SQL
    public static final String FETCH_ALL_EXAMS = "SELECT * FROM exams";

}
