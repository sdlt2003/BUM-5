package faculty.project.dataAccess;

import faculty.project.configuration.Config;
import faculty.project.domain.*;
import faculty.project.exceptions.UnknownUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbAccessManager {

    private Connection conn = null;
    private String dbpath;

    public void open() {
        try {
            String url = "jdbc:sqlite:" + dbpath;
            conn = DriverManager.getConnection(url);

            System.out.println("Database connection established");
        } catch (Exception e) {
            System.err.println("Cannot connect to database server " + e);
        }
    }

    public void close() {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        System.out.println("Database connection closed");
    }


    // singleton pattern
    private static final DbAccessManager instance = new DbAccessManager();


    public static DbAccessManager getInstance() {
        return instance;
    }

    private DbAccessManager() {

        dbpath = Config.getInstance().getDatabaseName();
        if (Config.getInstance().getDataBaseOpenMode().equals("initialize")) {
            truncateDB();
            initializeDB();
        }

    }

    private void truncateDB() {
        this.open();

        String[] commands = new String[]{
                "DELETE FROM user",
                "DELETE FROM subject",
                "DELETE FROM prerequisites",
                "DELETE FROM academic_record"
        };

        for (String command : commands) {
            try (PreparedStatement pstmt = conn.prepareStatement(command)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        this.close();
    }

    private void initializeDB() {

        this.open();

        Student oihane = new Student(1000, "Oihane", "123456", "Oihane Soraluze",
                "oihane@email.com", "c/ Melancolía 13", "678012345");

        Student aitor = new Student(2000, "Aitor", "123456", "Aitor Sarriegi",
                "aitor@email.com", "c/ Esperanza 14", "678999999");

        Subject softwareEngineering = new Subject(1, "Software Engineering", 6, 50);

        oihane.enroll(softwareEngineering);
        aitor.enroll(softwareEngineering);


        Teacher juanan = new Teacher(1, 230, "+34-123456", "juanan", "pasahitza");
        juanan.add(softwareEngineering);

        storeSubject(softwareEngineering);
        storeStudent(oihane);
        storeStudent(aitor);
        storeTeacher(juanan);

        this.close();

        System.out.println("The database has been initialized");

    }

    public void storeSubject(Subject subject) {

        String sql = "INSERT INTO subject (id, name, numCredits, maxNumStudents, taughtBy) VALUES(?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subject.getId());
            pstmt.setString(2, subject.getName());
            pstmt.setInt(3, subject.getCreditNumber());
            pstmt.setInt(4, subject.getMaxStudents());
            pstmt.setInt(5, subject.getTeacher().getDni());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void storeStudent(Student student) {
        String sql = "INSERT INTO user (dni, username, password, completeName, email, address, phoneNumber, type) VALUES(?,?,?,?,?,?,?, 'student')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student.getDni());
            pstmt.setString(2, student.getUserName());
            pstmt.setString(3, student.getPassword());
            pstmt.setString(4, student.getCompleteName());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getAddress());
            pstmt.setString(7, student.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (AcademicRecord academicRecord : student.getAcademicRecords()) {
            Subject subject = academicRecord.getSubject();

            sql = "INSERT INTO academic_record (student_id, subject_id, grade, year, teacher_id) VALUES(?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, student.getDni());
                pstmt.setInt(2, subject.getId());
                if (academicRecord.getGrade() != null)
                    pstmt.setFloat(3, academicRecord.getGrade());
                else
                    pstmt.setNull(3, Types.FLOAT);
                pstmt.setInt(4, academicRecord.getYear());
                if (academicRecord.getSignedBy() != null)
                    pstmt.setInt(5, academicRecord.getSignedBy().getDni());
                else
                    pstmt.setNull(5, Types.INTEGER);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void storeTeacher(Teacher teacher) {
        String sql = "INSERT INTO user (dni, username, password, completeName, email, address, phoneNumber, officeNumber, corporatePhone, type) VALUES(?,?,?,?,?,?,?,?,?,'teacher')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teacher.getDni());
            pstmt.setString(2, teacher.getUserName());
            pstmt.setString(3, teacher.getPassword());
            pstmt.setString(4, teacher.getCompleteName());
            pstmt.setString(5, teacher.getEmail());
            pstmt.setString(6, teacher.getAddress());
            pstmt.setString(7, teacher.getPhoneNumber());
            pstmt.setInt(8, teacher.getOfficeNumber());
            pstmt.setString(9, teacher.getCorporatePhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Teacher> removeTeachingAssignments() {
        // TBD
        return null;
    }

    public List<Subject> getAllSubjects() {
        // TBD
        return null;
    }


    public User login(String username, String password) throws UnknownUser {

        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int dni = rs.getInt("dni");
                String completeName = rs.getString("completeName");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");

                if (rs.getString("type").equals("teacher")) {

                    Teacher teacher = new Teacher(dni, rs.getInt("officeNumber"), rs.getString("corporatePhone"), username, password);

                    String sqlSubjects = "SELECT * FROM subject WHERE taughtBy = ?";
                    try (PreparedStatement pstmtSub = conn.prepareStatement(sqlSubjects)) {
                        pstmtSub.setInt(1, dni);
                        ResultSet rsSub = pstmtSub.executeQuery();
                        while (rsSub.next()) {
                            teacher.add(new Subject(rsSub.getInt("id"), rsSub.getString("name"), rsSub.getInt("numCredits"), rsSub.getInt("maxNumStudents")));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    return teacher;
                } else if (rs.getString("type").equals("student"))
                    return new Student(rs.getInt("dni"), username, password, completeName, email, address, phoneNumber);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Student> getUngradedStudentsEnrolledIn(Subject subject) {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        List<Student> students = new ArrayList<>();
        String sql = "SELECT student_id FROM academic_record WHERE subject_id =?1 AND year =?2 AND teacher_id is null";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subject.getId());
            pstmt.setInt(2, currentYear);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String studentId = rs.getString("student_id");
                students.add(getStudent(studentId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return students;
    }

    private Student getStudent(String studentId) {
        String sql = "SELECT * FROM user WHERE dni = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int dni = rs.getInt("dni");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String completeName = rs.getString("completeName");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");

                return new Student(dni, username, password, completeName, email, address, phoneNumber);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Add the grade (if passed -> update the earnedCredits value of the student)
     * sign the record
     *
     * @param student
     * @param subject
     * @param grade
     * @param teacher
     * @return DB successfully updated
     */
    public boolean gradeStudent(Student student, Subject subject, float grade, Teacher teacher) {
        // TBD
        return false;
    }

    public void assign(Subject subject, Teacher teacher) {
        // TBD
    }

    public boolean isFull(Subject subject) {
        // TBD
        return false;

    }

    public boolean hasPassed(Subject subject, Student student) {
        // TBD
        return false;

    }


    public void enrol(Student currentStudent, Subject subject) {
        AcademicRecord ar = new AcademicRecord(subject, currentStudent);
        storeAcademicRecord(ar);
    }

    private void storeAcademicRecord(AcademicRecord ar) {

        String sql = "INSERT INTO academic_record (student_id, subject_id, year) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ar.getStudent().getDni());
            pstmt.setInt(2, ar.getSubject().getId());
            pstmt.setInt(3, ar.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*

    public List<Pilot> getAllPilots() {

        var pilots = new ArrayList<Pilot>();
        this.open();

        try {
            String query = "SELECT id, name, nationality, points FROM pilots";
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                pilots.add(new Pilot(rs.getInt("id"), rs.getString("name"), rs.getString("nationality"), rs.getInt("points")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.close();
        return pilots;
    }

    public List<Pilot> getPilotsByNationality(String nationality) {
        this.open();
        String sql = "SELECT id, name, nationality, points "
                + "FROM pilots WHERE nationality = ?";

        var result = new ArrayList<Pilot>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nationality);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new Pilot(rs.getInt("id"), rs.getString("name"), rs.getString("nationality"), rs.getInt("points")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        this.close();
        return result;
    }

    public Pilot getPilotByName(String name) {

        this.open();
        String sql = "SELECT id, name, nationality, points "
                + "FROM pilots WHERE name = ?";

        Pilot pilot = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            // will return the first pilot that matches the condition
            // usually, there will be only one pilot with a given name
            if (rs.next()) {
                pilot = new Pilot(rs.getInt("id"), rs.getString("name"), rs.getString("nationality"), rs.getInt("points"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        this.close();
        return pilot;
    }

    public int deletePilotByName(String pilotName) {
        this.open();
        int amount = 0;
        String sql = "DELETE FROM pilots WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pilotName);
            amount = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        this.close();

        return amount;
    }

    public void addPointsToPilot(int morePoints, int pilotId) {
        this.open();
        String sql = "UPDATE pilots SET points = points + ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, morePoints);
            pstmt.setInt(2, pilotId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        this.close();
    }

    public static DbAccessManager getInstance() {
        return instance;
    }


    public void deletePilotById(int id) {
        this.open();
        String sql = "DELETE FROM pilots WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        this.close();
    }


     */
}
