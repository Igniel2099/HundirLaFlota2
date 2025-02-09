package org.example.hundirlaflota2.Windows;


import org.example.hundirlaflota2.Controllers.LoginController;

public class LoginWindow extends FatherWindow {

    public LoginWindow (){
        controller = new LoginController();
        pathView = "/org/example/hundirlaflota2/Views/loginView.fxml";
    }


}
