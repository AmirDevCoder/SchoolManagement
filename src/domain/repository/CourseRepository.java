package domain.repository;

import domain.model.entity.Course;
import domain.util.BaseRepository;
import util.ResultWrapper;

import java.util.List;

public interface CourseRepository extends BaseRepository<Course> {
    ResultWrapper<List<Course>> findCoursesByIds(List<Integer> ids);
}
