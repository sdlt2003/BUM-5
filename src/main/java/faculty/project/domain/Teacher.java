package faculty.project.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Teacher extends User {
    private Integer officeNumber;
    private String corporatePhone;


    private List<Subject> teaches = new ArrayList<>();

    public Teacher(int officeNumber, String corporatePhone){
        super();
    }

    public Teacher(int id, int officeNumber, String corporatePhone, String username, String password){
        super(id, username, password);
        this.officeNumber = officeNumber;
        this.corporatePhone = corporatePhone;
    }

    public Teacher() {

    }

    public List<Subject> getSubjects() {
        return teaches;
    }

    public void add(Subject subject){
        teaches.add(subject);
        subject.setTeacher(this);
    }


    public List<Subject> clearSubjects(){
        List<Subject> subjects = new ArrayList<>();
        for (Subject subject : teaches) {
            subject.setTeacher(null);
            subjects.add(subject);
        }
        teaches.clear();
        return subjects;
    }


    public Integer getOfficeNumber() {
        return officeNumber;
    }

    public String getCorporatePhone() {
        return corporatePhone;
    }
}
