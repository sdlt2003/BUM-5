package faculty.project.domain;

import java.util.Calendar;


public class AcademicRecord {

    private Long id;

    private Float grade;
    private int year;

    private Subject subject;

    public Float getGrade() {
        return grade;
    }

    public int getYear() {
        return year;
    }

    public Subject getSubject() {
        return subject;
    }

    public Student getStudent() {
        return student;
    }

    public Teacher getSignedBy() {
        return signedBy;
    }

    private Student student;
    private Teacher signedBy;

    public AcademicRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AcademicRecord(Subject subject, Student student) {
        this.subject = subject;
        this.student = student;
        year = Calendar.getInstance().get(Calendar.YEAR);
    }
}
