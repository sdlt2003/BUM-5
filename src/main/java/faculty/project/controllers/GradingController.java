package faculty.project.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import faculty.project.businessLogic.BlFacade;
import faculty.project.domain.Student;
import faculty.project.domain.Subject;
import faculty.project.ui.MainGUI;
import faculty.project.uicontrollers.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class GradingController implements Controller {

  private BlFacade bl;

  public GradingController(BlFacade bl) {
    this.bl = bl;
  }

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private ComboBox<Subject> comboSubjects;

  @FXML
  private ComboBox<Student> comboStudents;

  @FXML
  private TextField grade;

  @FXML
  private Label lblMessage;

  private MainGUI mainGUI;

  ObservableList<Subject> subjects = FXCollections.observableArrayList();
  ObservableList<Student> students = FXCollections.observableArrayList();

  @FXML
  void initialize() {

    comboStudents.setConverter(new StringConverter<>() {
      @Override
      public String toString(Student object) {
        if (object==null){
          return null;
        }else {
          return object.getUserName();
        }
      }

      @Override
      public Student fromString(String string) {
        return null;
      }
    });

    comboSubjects.setConverter(new StringConverter<>() {
      @Override
      public String toString(Subject object) {
        if (object==null){
          return null;
        }else {
          return object.getName();
        }
      }

      @Override
      public Subject fromString(String string) {
        return null;
      }
    });

    comboStudents.valueProperty().addListener((obs, oldval, subject) -> {
      lblMessage.setText("");
      lblMessage.getStyleClass().clear();
    });

    comboSubjects.valueProperty().addListener((obs, oldval, subject) -> {
      if(subject != null) {
        System.out.println("Selected subject: " + subject.getName());
        // get all students enrolled in this subject
        List<Student> studentList = bl.getUngradedStudentsEnrolledIn(subject);
        students.setAll(studentList);
        comboStudents.setItems(students);
      }
    });

    subjects.setAll(bl.getSubjects());
    comboSubjects.setItems(subjects);

  }

  @FXML
  void setGrade(ActionEvent event) {
    Subject subject = comboSubjects.getSelectionModel().getSelectedItem();
    Student student = comboStudents.getSelectionModel().getSelectedItem();
    float gradeValue;
    try {
      gradeValue = Float.parseFloat(grade.getText());
    }catch (Exception e){
      gradeValue = -1;
    }

    if (subject == null || student == null || gradeValue < 0 || gradeValue > 10){
      lblMessage.setText("Some of the parameters are wrong");
      lblMessage.getStyleClass().setAll("lbl","lbl-danger");
    }else {
      if (bl.gradeStudent(student, subject, gradeValue)) {
        students.remove(student);
        comboStudents.getSelectionModel().clearSelection();
        grade.setText("");
        lblMessage.setText("Grade Updated: " + student.getUserName() + ":" + gradeValue);
        lblMessage.getStyleClass().setAll("lbl", "lbl-success");
      } else {
        lblMessage.setText("Something went wrong");
        lblMessage.getStyleClass().setAll("lbl", "lbl-danger");
      }
    }
  }

  @Override
  public void setMainApp(MainGUI mainGUI) {
    this.mainGUI = mainGUI;
  }
}
