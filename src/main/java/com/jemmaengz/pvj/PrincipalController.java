package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Conexion;
import com.jemmaengz.pvj.model.HistorialSesion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Transaction;

public class PrincipalController implements Initializable {

    @FXML
    private AnchorPane navigationMenuPane;

    @FXML
    private JFXButton btnInicio;

    @FXML
    private JFXButton btnProductos;

    @FXML
    private JFXButton btnCompras;

    @FXML
    private JFXButton btnVentas;

    @FXML
    private JFXButton btnReportes;

    @FXML
    private JFXButton btnUsuarios;

    @FXML
    public AnchorPane mainPane;

    @FXML
    private BorderPane statusBarPane;
    
    @FXML
    public Label txtCabecera;
    
    @FXML
    private Label txtBienvenida;
    
    @FXML
    private JFXButton btnLogOut;
    
    public static PrincipalController instance;

    @FXML
    void btnInicioClicked(ActionEvent event) {
        setMainPaneContent("AboutView");
        txtCabecera.setText("Inicio");
    }
    
    @FXML
    void btnLogOutClick(ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        logOut();
    }
    
    public void setMainPaneContent(String content) {
         mainPane.getChildren().clear();
         try {
            Node node = FXMLLoader.load(getClass().getResource("/fxml/" + content + ".fxml"));
            mainPane.getChildren().add(node);
                                AnchorPane.setBottomAnchor(node, 0.0);
                                AnchorPane.setTopAnchor(node, 0.0);
                                AnchorPane.setRightAnchor(node, 0.0);
                                AnchorPane.setLeftAnchor(node, 0.0);
         } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public <C> void setMainPaneContent (C controlador, String nombreView) {
        mainPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/fxml/" + nombreView + ".fxml"
            )
        );
        loader.setController(controlador);
        try {
            Node node = loader.load();
            mainPane.getChildren().add(node);
                                AnchorPane.setBottomAnchor(node, 0.0);
                                AnchorPane.setTopAnchor(node, 0.0);
                                AnchorPane.setRightAnchor(node, 0.0);
                                AnchorPane.setLeftAnchor(node, 0.0);
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    
    void setBtnProductosConfig() {
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Ver lista de productos");
        mi1.setOnAction(e -> {
            setMainPaneContent("ListaProductosView");
            txtCabecera.setText("Lista de productos");
        });
        MenuItem mi2 = new MenuItem("Agregar un producto");
        mi2.setOnAction(e -> {
            setMainPaneContent(new ProductoController(), "ProductoView");
            txtCabecera.setText("Nuevo producto");
        });
        MenuItem mi3 = new MenuItem("Ver producto más vendido");
        mi3.setOnAction(e -> {
            try (Connection con = Conexion.getConexion(); 
                    Statement stmt = con.createStatement(); 
                    ResultSet rs = stmt.executeQuery("SELECT * FROM productoMasVendidoView")) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    Utilities.showAlert("El producto más vendido es: " + nombre);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        cm.getItems().addAll(mi1, mi2, mi3);
        
        btnProductos.setOnMouseClicked(e -> {
            cm.show(btnProductos, e.getScreenX(), e.getScreenY());
        });
    }
    
    void setBtnComprasConfig() {
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Ver lista de compras");
        mi1.setOnAction(e -> {
            setMainPaneContent("ListaComprasView");
            txtCabecera.setText("Lista de compras");
        });
        MenuItem mi2 = new MenuItem("Agregar una compra");
        mi2.setOnAction(e -> {
            setMainPaneContent(new CompraController(), "CompraView");
            txtCabecera.setText("Nueva compra");
        });
        cm.getItems().addAll(mi1, mi2);
        
        btnCompras.setOnMouseClicked(e -> {
            cm.show(btnCompras, e.getScreenX(), e.getScreenY());
        });
    }
    
    void setBtnVentasConfig() {
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Generar ventas");
        mi1.setOnAction(e -> {
            setMainPaneContent("GenerarVentasView");
            txtCabecera.setText("Ventas");
        });
        MenuItem mi2 = new MenuItem("Lista de ventas");
        mi2.setOnAction(e -> {
            setMainPaneContent("ListaVentasView");
            txtCabecera.setText("Lista de ventas");
        });
        cm.getItems().addAll(mi1, mi2);
        
        btnVentas.setOnMouseClicked(e -> {
            cm.show(btnVentas, e.getScreenX(), e.getScreenY());
        });
    }
    
    void setBtnReportesConfig() {
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Generar reporte de ventas por fecha");
        mi1.setOnAction(e -> {
            setMainPaneContent("GenerarReporteVentasFechaView");
            txtCabecera.setText("Reporte por fecha");
        });
        cm.getItems().addAll(mi1);
        
        btnReportes.setOnMouseClicked(e -> {
            cm.show(btnReportes, e.getScreenX(), e.getScreenY());
        });
    }
    
    void setBtnUsuariosConfig() {
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Crear usuario");
        mi1.setOnAction(e -> {
            setMainPaneContent(new UsuarioController(), "UsuarioView");
            txtCabecera.setText("Crear usuario");
        });
        MenuItem mi2 = new MenuItem("Lista de usuarios");
        mi2.setOnAction(e -> {
            setMainPaneContent("ListaUsuariosView");
            txtCabecera.setText("Lista de usuarios");
        });
        MenuItem mi3 = new MenuItem("Historial de sesiones");
        mi3.setOnAction(e -> {
            setMainPaneContent("HistorialSesionesView");
            txtCabecera.setText("Historial de sesiones");
        });
        cm.getItems().addAll(mi1, mi2, mi3);
        
        btnUsuarios.setOnMouseClicked(e -> {
            cm.show(btnUsuarios, e.getScreenX(), e.getScreenY());
        });
    }
    
    private void logOut() throws IOException {
        Stage newStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));
        
        JFXDecorator decoratedRoot = new JFXDecorator(newStage, root, false, false, true);
        
        Scene scene = new Scene(decoratedRoot);
        
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.setTitle("PVJ - Login");
        
        newStage.show();
        
//        Guardar logout
        HistorialSesion hs = new HistorialSesion(HistorialSesion.Estado.LOGGED_OUT, null, LoginController.usuario);
        Transaction transaction = Main.session.beginTransaction();
        Main.session.saveOrUpdate(hs);
        transaction.commit();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtBienvenida.setText("Bienvenid@: " + LoginController.usuario.getNombre());
        setMainPaneContent("AboutView");
        
        if(LoginController.esEmpleado) {
            btnReportes.setVisible(false);
            btnUsuarios.setVisible(false);
        }
        
        setBtnProductosConfig();
        setBtnComprasConfig();
        setBtnVentasConfig();
        setBtnReportesConfig();
        setBtnUsuariosConfig();
        instance = this;
    }    
    
}
