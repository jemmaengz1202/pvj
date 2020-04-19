package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Usuario;
import com.jemmaengz.pvj.model.Venta;
import com.jemmaengz.pvj.model.VentaDetalle;
import com.jfoenix.controls.JFXButton;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.hibernate.Transaction;

public class VentaDetalleController implements Initializable {
    
    @FXML
    private TableView<VentaDetalle> tabla;

    @FXML
    private TableColumn<VentaDetalle, Double> cantidadColumn;

    @FXML
    private TableColumn<VentaDetalle, String> nombreColumn;

    @FXML
    private TableColumn<VentaDetalle, BigDecimal> precioColumn;

    @FXML
    private TableColumn<VentaDetalle, BigDecimal> subtotalColumn;

    @FXML
    private Label lblTotal;
    
    @FXML
    private Label lblCajero;

    @FXML
    private Label lblId;

    @FXML
    private Label lblFecha;

    @FXML
    private JFXButton btnCerrarVenta;
    
    private Venta venta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cantidadColumn.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().getProducto().nombreProperty());
        precioColumn.setCellValueFactory(cellData -> cellData.getValue().getProducto().precio_ventaProperty());
        subtotalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        precioColumn.setCellFactory(tc -> new TableCell<VentaDetalle, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "$" + item.toString());
            }
        });
        subtotalColumn.setCellFactory(tc -> new TableCell<VentaDetalle, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "$" + item.toString());
            }
        });
        
        this.lblTotal.setText("$" + this.venta.getTotal().toString());
        this.lblCajero.setText("Cajero: " + this.venta.getCajero().getNombre());
        this.lblFecha.setText("Fecha: " + Utilities.getDateSpanishFormatted(this.venta.getFecha()));
        this.lblId.setText("Identificador de venta: " + this.venta.getId());
        
        List<VentaDetalle> ventasDetalle = new ArrayList<>(venta.getVentas_detalle());
        ObservableList<VentaDetalle> listTabla = FXCollections.observableArrayList(ventasDetalle);
        this.tabla.setItems(listTabla);
        
        this.btnCerrarVenta.setOnAction(e -> {
            if(this.venta.getCajero().equals(LoginController.usuario) || !(LoginController.usuario.getRol() == Usuario.Rol.empleado)) {
                if (Utilities.showConfirmationDialog("¿Seguro que desea eliminar la venta? Este cambio es irreversible")) {
                    Transaction transaction = Main.session.beginTransaction();
                    Main.session.delete(venta);
                    transaction.commit();
                    PrincipalController.instance.txtCabecera.setText("Lista de ventas");
                    PrincipalController.instance.setMainPaneContent("ListaVentasView");
                }
            }
        });
        
        if(!(this.venta.getCajero().equals(LoginController.usuario) || !(LoginController.usuario.getRol() == Usuario.Rol.empleado))) {
            this.btnCerrarVenta.setVisible(false);
        }
    }    
    
    public VentaDetalleController() {}
    
    public VentaDetalleController(Venta venta) {
        this.venta = venta;
    }
    
}
