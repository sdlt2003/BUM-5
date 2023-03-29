package faculty.project.businessLogic;

import faculty.project.configuration.Config;
import faculty.project.dataAccess.DbAccessManager;
import faculty.project.domain.Student;
import faculty.project.domain.Subject;
import faculty.project.domain.Teacher;
import faculty.project.domain.User;
import faculty.project.exceptions.UnknownUser;

import java.util.Collection;
import java.util.List;

public class BlFacadeImplementation implements BlFacade {

  DbAccessManager dbManager = DbAccessManager.getInstance();
  Config config = Config.getInstance();

  private User currentUser;

  public BlFacadeImplementation() {

    System.out.println("Creating BlFacadeImplementation instance");


  }

  @Override
  public void consultStudentsRecords() {

  }

  @Override
  public boolean gradeStudent(Student student, Subject subject, float grade) {

    Teacher teacher = (Teacher) this.currentUser;
    dbManager.open();

    boolean ok = dbManager.gradeStudent(student, subject, grade, teacher);

    dbManager.close();

    return ok;
  }

  /***
   * for each subject, reset the 'enrolledIn' association for this year's students
   */
  @Override
  public void restartSystem() {
    // TBD
  }

  @Override
  public void authenticate(String login, String password) {

  }

  @Override
  public List<Subject> getSubjects() {
      Teacher teacher = (Teacher) this.currentUser;
      return teacher.getSubjects();
  }

  @Override
  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  public void login(String username, String password) throws UnknownUser {

    dbManager.open();
    this.currentUser = dbManager.login(username, password);
    dbManager.close();

  }

  @Override
  public List<Student> getUngradedStudentsEnrolledIn(Subject subject) {
    List<Student> students;
    dbManager.open();
    students = dbManager.getUngradedStudentsEnrolledIn(subject);
    dbManager.close();
    return students;
  }

  @Override
  public List<Subject> getAllSubjects() {
    List<Subject> subjects;
    dbManager.open();
    subjects = dbManager.getAllSubjects();
    dbManager.close();
    return subjects;
  }

  @Override
  public void assign(Subject subject, Teacher teacher) {
    dbManager.open();
    dbManager.assign(subject, teacher);
    dbManager.close();
  }

  /***
   * Checks if a student is eligible to enroll in a subject.
   * @param subject
   * @return boolean
   */
  @Override
  public boolean isEligible(Subject subject) {
    // TBD
    boolean eligible = false;
    return eligible;
  }

  @Override
  public void enrol(List<Subject> subjects) {
    Student currentStudent = (Student)currentUser;
    dbManager.open();
    for (Subject subject : subjects) {
      dbManager.enrol(currentStudent, subject);
    }
    dbManager.close();
  }

}
