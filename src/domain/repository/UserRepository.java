package domain.repository;

import domain.entity.Student;
import util.BaseRepository;
import util.ResultWrapper;

public interface UserRepository extends BaseRepository<Student> {

    ResultWrapper<Long> getCountOfStudents();

    ResultWrapper<Boolean> existsByNationalId(long nationalId);

}
