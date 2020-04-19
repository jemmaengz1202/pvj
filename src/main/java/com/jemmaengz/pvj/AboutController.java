package com.jemmaengz.pvj;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AboutController implements Initializable {

    @FXML
    private JFXButton btnAboutView;

    @FXML
    void btnAboutViewClicked(ActionEvent event) {
        Utilities.showAlert("Programa creado por Juan Emmanuel Enríquez González");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
