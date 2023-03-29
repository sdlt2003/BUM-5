package faculty.project.domain;

import java.util.ArrayList;
import java.util.Collection;

public class Student extends User {

  private static final int MAXCREDITSDEGREE = 255;
  private int earnedCredits = 0;

  private Collection<AcademicRecord> academicRecords;


  public Student(int dni, String userName, String password, String completeName, String email, String address, String phoneNumber) {
    super(dni, userName, password, completeName, email, address, phoneNumber);

    academicRecords = new ArrayList<>();
  }

  public Student() {

  }


  public void enroll(Subject subject) {
    academicRecords.add(new AcademicRecord(subject, this));
  }

  public int getEarnedCredits() {
    return earnedCredits;
  }

  public void setEarnedCredits(int earnedCredits) {
    this.earnedCredits = earnedCredits;
  }

  public Collection<AcademicRecord> getAcademicRecords() {
    return academicRecords;
  }

  public boolean isEligibleForCredits(int credits){
    // the credits a student has already passed in previous years, cannot exceed the maximum number of credits of the degree (255).
    return getEarnedCredits() + credits <= MAXCREDITSDEGREE;
  }

}
