package faculty.project.domain;




public class AdministrativeOfficer extends User{
    Integer officeNumber;
    String corporatePhone;

    public AdministrativeOfficer(int officeNumber, String corporatePhone) {
        super();
        this.officeNumber = officeNumber;
        this.corporatePhone = corporatePhone;
    }


    public AdministrativeOfficer() {

    }
}
