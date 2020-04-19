package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Usuario;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsuarioController implements Initializable {
    
    @FXML
    private JFXButton btnEditarGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private VBox vboxForm;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtUsuario;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXComboBox<String> cmbxRol;
    
    private Usuario usuario;
    
    private boolean edicion = false;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilities.setRequiredValidator(txtNombre);
        Utilities.setRequiredValidator(txtUsuario);
        Utilities.setRequiredValidator(txtPassword);
        txtNombre.setTextFormatter(Utilities.getCharactersFormatter());
        
        ObservableList<String> oar = FXCollections.observableArrayList(
            "Administrador",
            "Empleado"
        );
        cmbxRol.setItems(oar);
        if(LoginController.usuario.getRol() == Usuario.Rol.root) {
            cmbxRol.getItems().add("Super usuario");
        }
        
        if(this.usuario != null) {
            this.txtNombre.setText(usuario.getNombre());
            this.txtUsuario.setText(usuario.getUsuario());
            this.txtPassword.setText(usuario.getContrasena());
            this.cmbxRol.getSelectionModel().select(usuario.getRol());
            
            vboxForm.getChildren().forEach(e -> {
                e.setStyle("-fx-opacity: .75;");
                e.setDisable(true);
            });
            
            txtPassword.setVisible(false);
            
            btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    btnEditarGuardar.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                
                    btnEditarGuardar.setText("Guardar");
                    
                    vboxForm.getChildren().forEach(el -> {
                        el.setStyle("-fx-opacity: 1.0;");
                        el.setDisable(false);
                    });
                    
                    txtNombre.requestFocus();
                    
                    btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                        save();
                    });
                }}
            );
            btnEliminar.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                delete();
            });
        } else {
            this.cmbxRol.getSelectionModel().select(1);
            this.usuario = new Usuario();
            btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                  save();
            });
            btnEditarGuardar.setText("Guardar");
            btnEliminar.setVisible(false);
        }
    }    
    
    public void save() {
        this.usuario.setNombre(txtNombre.getText());
        this.usuario.setUsuario(this.txtUsuario.getText());
        this.usuario.setRol(this.cmbxRol.getSelectionModel().getSelectedIndex());
        
        if(!this.edicion) {
            this.usuario.setContrasena(this.txtPassword.getText());
        } 
        
        if (this.edicion && !(txtNombre.validate() && txtUsuario.validate())) {
            Utilities.showAlert("Rellena todos los campos que son requeridos");
            return;
        }
        
        if(!this.edicion && !(txtNombre.validate() && txtUsuario.validate() && txtPassword.validate())) {
            Utilities.showAlert("Rellena todos los campos que son requeridos");
            return;
        }
        
//        Si el usuario no está guardado en la bd, checar si no hay otro username
        if(!(this.usuario.getId() > 0)) {
            Query<Usuario> query = Main.session.createQuery("SELECT u FROM Usuario u WHERE u.usuario = :us", Usuario.class);
            query.setParameter("us", this.usuario.getUsuario());
            List<Usuario> usuarioUsername = query.list();
            if(!usuarioUsername.isEmpty()) {
                Utilities.showAlert("Ya existe un usuario registrado con ese nombre de usuario.");
                txtUsuario.requestFocus();
                return;
            }
        }
        
        Transaction transaction = Main.session.beginTransaction();
        Main.session.saveOrUpdate(this.usuario);
        transaction.commit();
        PrincipalController.instance.setMainPaneContent("ListaUsuariosView");
        Utilities.showToast("Guardado");
    }
    
    public void delete() {
        this.usuario.setUsuario("");
        if (Utilities.showConfirmationDialog("¿Estás seguro de eliminar el elemento?")) {
            Transaction transaction = Main.session.beginTransaction();
            Main.session.saveOrUpdate(this.usuario);
            transaction.commit();
            PrincipalController.instance.setMainPaneContent("ListaUsuariosView");
            Utilities.showToast("Elemento eliminado");
        }
    }
    
    public UsuarioController() {}
    
    public UsuarioController(Usuario usuario) {
        this.usuario = usuario;
        this.edicion = true;
    }
}
