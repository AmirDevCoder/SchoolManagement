package domain.entity;

import java.sql.Date;
import java.util.Objects;

public abstract class User {
    private long id;
    private String firstName;
    private String lastName;
    private Date dob;
    private String nationalId;

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Date getDob() {
        return dob;
    }

    public User setDob(Date dob) {
        this.dob = dob;
        return this;
    }

    public String getNationalId() {
        return nationalId;
    }

    public User setNationalId(String nationalId) {
        this.nationalId = nationalId;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", nationalId='" + nationalId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(dob, user.dob) && Objects.equals(nationalId, user.nationalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dob, nationalId);
    }
}
