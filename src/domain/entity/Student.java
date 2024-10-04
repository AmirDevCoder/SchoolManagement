package domain.entity;

public class Student extends User implements Comparable<Student> {
    private float gpu;

    public Student setGpu(float gpu) {
        this.gpu = gpu;
        return this;
    }

    public float getGpu() {
        return gpu;
    }

    // TODO: Implement builder design patter in correct way
    public static Student Builder() {
        return new Student();
    }

    @Override
    public int compareTo(Student student) {
        return 0;
    }
}
