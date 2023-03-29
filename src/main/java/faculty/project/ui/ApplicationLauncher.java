package faculty.project.ui;

import faculty.project.businessLogic.BlFacade;
import faculty.project.businessLogic.BlFacadeImplementation;
import faculty.project.exceptions.UnknownUser;

import java.util.Locale;

public class ApplicationLauncher {

  public static void main(String[] args) {

    System.out.println("Locale: " + Locale.getDefault());

    BlFacade businessLogic = new BlFacadeImplementation();


    // hardcode current user for testing purposes
    try {
      businessLogic.login("juanan","pasahitza");
    } catch (UnknownUser e) {
      e.printStackTrace();
    }

    new MainGUI(businessLogic);

  }


}
