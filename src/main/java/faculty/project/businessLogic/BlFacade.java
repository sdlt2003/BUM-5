package faculty.project.businessLogic;


import faculty.project.domain.Student;
import faculty.project.domain.Subject;
import faculty.project.domain.Teacher;
import faculty.project.domain.User;
import faculty.project.exceptions.UnknownUser;

import java.util.List;

/**
 * Interface that specifies the business logic.
 */
public interface BlFacade {

  /**
   * ************** Teacher *******************/
  void consultStudentsRecords();
  boolean gradeStudent(Student student, Subject subject, float gradeValue);
  /**
   * Academic Officer ***/
  void restartSystem();

  /**
   *  User ***/
  void authenticate(String login, String password);

  /**
   * Get subjects for current teacher
   */
  List<Subject> getSubjects();
  void setCurrentUser(User user);
  void login(String username, String password) throws UnknownUser;

  List<Student> getUngradedStudentsEnrolledIn(Subject subject);

  List<Subject> getAllSubjects();

  void assign(Subject subject, Teacher teacher);

  boolean isEligible(Subject subject);

  void enrol(List<Subject> subjects);

  }
