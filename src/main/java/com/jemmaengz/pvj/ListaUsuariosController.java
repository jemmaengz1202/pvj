package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Usuario;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ListaUsuariosController implements Initializable {
    
    @FXML
    private TableView<Usuario> tabla;

    @FXML
    private TableColumn<Usuario, String> nombreColumn;

    @FXML
    private TableColumn<Usuario, String> usuarioColumn;

    @FXML
    private TableColumn<Usuario, Integer> rolColumn;

    @FXML
    private JFXTextField txfBusqueda;
    
    private Usuario usuarioClickeado;
    
    private ContextMenu cm;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        usuarioColumn.setCellValueFactory(cellData -> cellData.getValue().usuarioProperty());
        rolColumn.setCellValueFactory(cellData -> cellData.getValue().rolProperty().asObject());
        rolColumn.setCellFactory(tc -> new TableCell<Usuario, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.equals(Usuario.Rol.admin) ? "Administrador" : 
                                                 item.equals(Usuario.Rol.empleado) ? "Empleado" : 
                                                 item.equals(Usuario.Rol.root) ? "Super usuario" : "Usuario");
            }
        });
        
        // Lista del tipo determinado
        List<Usuario> lista = Main.session.createQuery("SELECT u FROM Usuario u WHERE u.usuario <> ''", Usuario.class).list();
        // La tabla requiere una lista de Observables
        ObservableList<Usuario> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
        
         txfBusqueda.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER)  {
                buscar(txfBusqueda.getText());
            }
        });
        
        setContextMenu();
        
        tabla.setRowFactory(tv -> {
            TableRow<Usuario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                && event.getClickCount() == 1) {
                    usuarioClickeado = row.getItem();
                    setContextMenu();
                    cm.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row ;
         });
    }

    public void buscar(String b) {
        b = b.toLowerCase();
        String queryS = "SELECT u from Usuario u "
                             + "WHERE u.nombre LIKE :b OR "
                             + "u.usuario LIKE :b";
        
        Query<Usuario> query = Main.session.createQuery(queryS, Usuario.class);
        query.setParameter("b", "%" + b + "%");
        
        List<Usuario> lista = query.list();
        
        ObservableList<Usuario> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
    public void setContextMenu() {
        cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Ver detalles del usuario");
        mi1.setOnAction(e -> {
            UsuarioController controller = new UsuarioController(this.usuarioClickeado);
            PrincipalController.instance.setMainPaneContent(controller, "UsuarioView");
            PrincipalController.instance.txtCabecera.setText("Usuario");
        });
        MenuItem mi2 = new MenuItem("Reiniciar contraseña");
        mi2.setOnAction(e -> {
            reiniciarContrasena();
        });
        MenuItem mi3 = new MenuItem("Eliminar el usuario");
        mi3.setOnAction(e -> {
            if(Utilities.showConfirmationDialog("¿Estás seguro de eliminar el usuario?")) {
                this.usuarioClickeado.setUsuario("");
                Transaction transaction = Main.session.beginTransaction();
                Main.session.saveOrUpdate(usuarioClickeado);
                transaction.commit();
                PrincipalController.instance.setMainPaneContent("ListaUsuariosView");
                Utilities.showToast("Elemento eliminado");
            }
        });
        cm.getItems().addAll(mi1, mi2, mi3);
    }
    
    public void reiniciarContrasena() {
        if(Utilities.showConfirmationDialog("El siguiente proceso eliminará la contraseña del usuario y la sustituirá con una nueva\n"
                                                        + "¿Está seguro de continuar?")) {
            Utilities.showTextInputDialog("Reinicio de contraseña", "Nueva contraseña: ").ifPresent(pass1 -> {
                Utilities.showTextInputDialog("Confirmación de contraseña", "Introduce nuevamente la contraseña: ").ifPresent(pass2 -> {
                    if(pass1.equals(pass2)) {
                        this.usuarioClickeado.setContrasena(pass2);
                        Transaction transaction = Main.session.beginTransaction();
                        Main.session.saveOrUpdate(usuarioClickeado);
                        transaction.commit();
                        PrincipalController.instance.setMainPaneContent("ListaUsuariosView");
                        Utilities.showToast("Contraseña reiniciada satisfactoriamente");
                    } else {
                        Utilities.showAlert("El valor de las contraseñas no coincide");
                    }
                });
            });
        }
    }
    
}
