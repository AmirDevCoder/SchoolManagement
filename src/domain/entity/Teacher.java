package domain.entity;

public class Teacher extends User {
    private long courseId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }


    // TODO: Implement builder design patter in correct way
    public static Teacher Builder(long courseId) {
        Teacher teacher = new Teacher();
        teacher.setCourseId(courseId);
        return teacher;
    }
}
