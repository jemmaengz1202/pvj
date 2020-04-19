package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Compra;
import com.jemmaengz.pvj.model.Proveedor;
import com.jemmaengz.pvj.model.Usuario;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

public class ListaComprasController implements Initializable {
    
    @FXML
    private TableView<Compra> tabla;

    @FXML
    private TableColumn<Compra, Date> fechaColumn;

    @FXML
    private TableColumn<Compra, BigDecimal> precioColumn;

    @FXML
    private TableColumn<Compra, Proveedor> proveedorColumn;

    @FXML
    private TableColumn<Compra, Usuario> cajeroColumn;

    @FXML
    private JFXTextField txfBusqueda;

    @FXML
    private JFXDatePicker datePicker;
    
    private ContextMenu cm;
    
    private Compra compraClickeada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        precioColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        proveedorColumn.setCellValueFactory(cellData -> cellData.getValue().proveedorProperty());
        cajeroColumn.setCellValueFactory(cellData -> cellData.getValue().cajeroProperty());
        
        fechaColumn.setCellFactory(tc -> new TableCell<Compra, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : Utilities.getDateSpanishFormatted(item));
            }
        });
        
        precioColumn.setCellFactory(tc -> new TableCell<Compra, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "$" + item.toString());
            }
        });
        
        proveedorColumn.setCellFactory(tc -> new TableCell<Compra, Proveedor>() {
            @Override
            protected void updateItem(Proveedor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });
        
        cajeroColumn.setCellFactory(tc -> new TableCell<Compra, Usuario>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });
        
        fillTable();
        
        Platform.runLater(() -> {
            txfBusqueda.requestFocus();
        });
        
        datePicker.setOnKeyPressed(k -> {
           if (k.getCode() == KeyCode.ENTER)  {
                buscar(java.sql.Date.valueOf(datePicker.getValue()));
            }
       });
       
       txfBusqueda.setOnKeyPressed(k -> {
           if(k.getCode() == KeyCode.ENTER) {
               buscar(txfBusqueda.getText());
           }
       });
       
       setContextMenu();
       
       tabla.setRowFactory(tv -> {
            TableRow<Compra> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                && event.getClickCount() == 1) {
                    compraClickeada = row.getItem();
                    if(compraClickeada.getCajero().equals(LoginController.usuario) && 
                            !(LoginController.usuario.getRol() == Usuario.Rol.empleado)) {                        
                        cm.show(row, event.getScreenX(), event.getScreenY());
                    }
                }
            });
            return row ;
         });
    }
    
    public void setContextMenu() {
        cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Ver detalles de la venta");
        mi1.setOnAction(e -> {
            CompraController controller = new CompraController(this.compraClickeada);
            PrincipalController.instance.setMainPaneContent(controller, "CompraView");
            PrincipalController.instance.txtCabecera.setText("Compra");
        });
        MenuItem mi2 = new MenuItem("Eliminar la compra");
        mi2.setOnAction(e -> {
            if(Utilities.showConfirmationDialog("¿Estás seguro de eliminar la compra?")) {
                Transaction transaction = Main.session.beginTransaction();
                Main.session.delete(compraClickeada);
                transaction.commit();
                System.out.println(this.compraClickeada);
                PrincipalController.instance.setMainPaneContent("ListaComprasView");
                Utilities.showToast("Elemento eliminado");
            }
        });
        cm.getItems().addAll(mi1, mi2);
    }

    public void fillTable() {
        // Lista del tipo determinado
        List<Compra> lista = Main.session.createQuery("from Compra c", Compra.class).list();
        // La tabla requiere una lista de Observables
        ObservableList<Compra> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
    public void buscar(String b) {
        b = b.toLowerCase();
        String queryS = "SELECT p from Compra c "
                             + "INNER JOIN c.proveedor p "
                             + "INNER JOIN c.cajero  u "
                             + "WHERE (lower(p.nombre) LIKE :b) OR (lower(u.nombre) LIKE :b)";
        
        Query<Compra> query = Main.session.createQuery(queryS, Compra.class);
        query.setParameter("b", "%" + b + "%");
        
        List<Compra> lista = query.list();
        
        ObservableList<Compra> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
    public void buscar(Date fecha) {
        Query<Compra> query = Main.session.createQuery("SELECT c FROM Compra c WHERE DATE(c.fecha) = :d", Compra.class);
        query.setParameter("d", fecha);
        List<Compra> lista = query.list();
        // La tabla requiere una lista de Observables
        ObservableList<Compra> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
}
