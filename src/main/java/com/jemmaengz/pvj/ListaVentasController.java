package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Usuario;
import com.jemmaengz.pvj.model.Venta;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
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

public class ListaVentasController implements Initializable {
    
    @FXML
    private TableView<Venta> tabla;

    @FXML
    private TableColumn<Venta, Long> idColumn;

    @FXML
    private TableColumn<Venta, Date> fechaColumn;

    @FXML
    private TableColumn<Venta, BigDecimal> totalColumn;

    @FXML
    private TableColumn<Venta, Usuario> cajeroColumn;
    
    @FXML
    private JFXTextField txfBusqueda;

    @FXML
    private JFXDatePicker datePicker;
    
    private ContextMenu cm;
    
    private Venta ventaClickeada;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        fechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        cajeroColumn.setCellValueFactory(cellData -> cellData.getValue().cajeroProperty());
        fechaColumn.setCellFactory(tc -> new TableCell<Venta, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : Utilities.getDateSpanishFormatted(item));
            }
        });
        totalColumn.setCellFactory(tc -> new TableCell<Venta, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "$" + item.toString());
            }
        });
        cajeroColumn.setCellFactory(tc -> new TableCell<Venta, Usuario>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });
        
        List<Venta> lista = Main.session.createQuery("from Venta h", Venta.class).list();
        // La tabla requiere una lista de Observables
        ObservableList<Venta> listaTabla = FXCollections.observableList(lista);
        
        System.out.println(lista);
        tabla.setItems(listaTabla);
        
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
       
       tabla.setRowFactory(tv -> {
            TableRow<Venta> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                && event.getClickCount() == 1) {
                    ventaClickeada = row.getItem();
                    
                        setContextMenu();
                        cm.show(row, event.getScreenX(), event.getScreenY());
                  
                }
            });
            return row ;
         });
    }

    public void buscar(String str) {
        String queryS;
        BigDecimal total;
        queryS = "SELECT v FROM Venta v "
                   + "INNER JOIN v.cajero c "
                   + "WHERE c.nombre LIKE :b "
                   + "OR v.total = :total";
        
        try {
            total = new BigDecimal(str);
        } catch (NumberFormatException ex) {
            total = new BigDecimal(0);
        }
        
        Query<Venta> query = Main.session.createQuery(queryS, Venta.class);
        query.setParameter("b", "%"+str+"%");
        query.setParameter("total", total);
        List<Venta> lista = query.list();
        // La tabla requiere una lista de Observables
        ObservableList<Venta> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
    public void buscar(Date fecha) {
        Query<Venta> query = Main.session.createQuery("SELECT v FROM Venta v WHERE DATE(v.fecha) = :d", Venta.class);
        query.setParameter("d", fecha);
        List<Venta> lista = query.list();
        // La tabla requiere una lista de Observables
        ObservableList<Venta> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
    public void setContextMenu() {
        cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Ver detalles de la venta");
        mi1.setOnAction(e -> {
            VentaDetalleController controller = new VentaDetalleController(ventaClickeada);
            PrincipalController.instance.setMainPaneContent(controller, "VentaDetalleView");
            PrincipalController.instance.txtCabecera.setText("Detalle de venta");
        });
        MenuItem mi2 = new MenuItem("Eliminar la venta");
        mi2.setOnAction(e -> {
            if(Utilities.showConfirmationDialog("¿Estás seguro de eliminar la venta?")) {
                Transaction transaction = Main.session.beginTransaction();
                Main.session.delete(ventaClickeada);
                transaction.commit();
                PrincipalController.instance.setMainPaneContent("ListaVentasView");
                Utilities.showToast("Elemento eliminado");
            }
        });
        if(ventaClickeada.getCajero().equals(LoginController.usuario) || !(LoginController.usuario.getRol() == Usuario.Rol.empleado)) {
            cm.getItems().addAll(mi1, mi2);
        } else {
            cm.getItems().add(mi1);
        }
        
    }
    
}
