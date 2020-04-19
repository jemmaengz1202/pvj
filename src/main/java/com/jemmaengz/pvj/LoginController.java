package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.HistorialSesion;
import com.jemmaengz.pvj.model.Usuario;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class LoginController implements Initializable {
    
    public static Usuario usuario;
    
    public static boolean esEmpleado = false;
    
    @FXML
    private JFXTextField txtfUsuario;

    @FXML
    private JFXPasswordField txtfContrasena;

    @FXML
    private JFXButton btnAcceder;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            txtfUsuario.requestFocus();
        });
        
        if(Main.logueado) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/PrincipalView.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            Stage stage = new Stage();
            JFXDecorator decoratedRoot = new JFXDecorator(stage, root, false, false, true);

            Scene scene = new Scene(decoratedRoot);
            stage.setScene(scene);
            stage.setTitle("PVJ");
            stage.show();
            
            stage.setOnCloseRequest(e -> {
                Platform.exit();
            });
            
            txtfUsuario.getScene().getWindow().hide();
        }
        
        txtfContrasena.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER)  {
                try {
                    acceder(null);
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    } 
    
    @FXML
    void acceder(ActionEvent event) throws IOException {
        String username = txtfUsuario.getText();
        String pass = txtfContrasena.getText();
        boolean logueado = false;
        
        Session session = Main.session;
        
        Query<Usuario> query = session.createQuery("SELECT u FROM Usuario u WHERE u.usuario = :username AND u.contrasena = :pass", Usuario.class);
        query.setParameter("username", username);
        query.setParameter("pass", pass);
        
        usuario = query.uniqueResult();
        
        if(usuario != null && !usuario.getUsuario().equals("")) {
            logueado = true;
        }
        
        if(!logueado) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No se pudo acceder");
            alert.initStyle(StageStyle.UTILITY);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        } else {
            System.out.println("Logueado correctamente");
            if (LoginController.usuario.getRol() == Usuario.Rol.empleado) {
                LoginController.esEmpleado = true;
            } else {
                LoginController.esEmpleado = false;
            }
            
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/PrincipalView.fxml"));
        
            Stage stage = new Stage();
            JFXDecorator decoratedRoot = new JFXDecorator(stage, root, false, false, true);

            Scene scene = new Scene(decoratedRoot);
            stage.setScene(scene);
            stage.setTitle("PVJ");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon-16x16.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon-32x32.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon-96x96.png")));
            stage.show();
            
            stage.setOnCloseRequest(e -> {
//                Guardar logout
                HistorialSesion hs = new HistorialSesion(HistorialSesion.Estado.LOGGED_OUT, null, LoginController.usuario);
                Transaction transaction = Main.session.beginTransaction();
                Main.session.saveOrUpdate(hs);
                transaction.commit();
                
//                Cerrar absoultamente todo
                Platform.exit();
            });
            
            txtfUsuario.getScene().getWindow().hide();
            
            HistorialSesion hs = new HistorialSesion(HistorialSesion.Estado.LOGGED_IN, null, LoginController.usuario);
            Transaction transaction = Main.session.beginTransaction();
            Main.session.saveOrUpdate(hs);
            transaction.commit();
            Utilities.showToast("Autenticación correcta");
        }
    }
    
}
