package faculty.project.domain;


public abstract class User {

    private int dni;

    public String getPassword() {
        return password;
    }

    public String getCompleteName() {
        return completeName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String userName;
    private String password;
    private String completeName;
    private String email;
    private String address;
    private String phoneNumber;

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public User(int dni, String userName, String password) {
        this.dni = dni;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "dni=" + dni +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", completeName='" + completeName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public User(int dni, String userName, String password, String completeName, String email, String address, String phoneNumber) {
        this.dni = dni;
        this.userName = userName;
        this.password = password;
        this.completeName = completeName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public User() {

    }



}
