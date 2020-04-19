package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Producto;
import com.jemmaengz.pvj.model.Venta;
import com.jemmaengz.pvj.model.VentaDetalle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class GenerarVentasController implements Initializable {

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
    private JFXTextField txfBusqueda;

    @FXML
    private Label lblTotal;
    
    @FXML
    private JFXButton btnCerrarVenta;
    
    private ContextMenu cm;
    
    private Venta venta;
    
    private VentaDetalle ventaDetalleClickeada;
    
    private ObservableList<VentaDetalle> listaTabla;
    
    private BigDecimal total;
    
    private boolean ventaRealizada = false;
    
    @FXML
    private Label lblTexto1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PrincipalController.instance.txtCabecera.setText("Punto de venta");
        
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
        
        Platform.runLater(() -> {
            txfBusqueda.requestFocus();
        });
        
        txfBusqueda.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER)  {
                insertar(txfBusqueda.getText());
                this.txfBusqueda.setText("");
                this.txfBusqueda.requestFocus();
            }
        });
        
        setContextMenu();
        
        tabla.setRowFactory(tv -> {
            TableRow<VentaDetalle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                && event.getClickCount() == 1) {
                    ventaDetalleClickeada = row.getItem();
                    cm.show(row, event.getScreenX(), event.getSceneY());
                }
            });
            return row ;
         });
        
        listaTabla = FXCollections.observableList(new ArrayList<>());
        tabla.setItems(listaTabla);
        this.venta = new Venta(LoginController.usuario);
        this.total = new BigDecimal(0);
        this.ventaRealizada = false;
        this.btnCerrarVenta.setOnAction(e -> {
            if(this.ventaRealizada) {
                PrincipalController.instance.setMainPaneContent("GenerarVentasView");
            } else {
                if(this.listaTabla.isEmpty()) {
                    return;
                }
                cerrarVenta();
            }
        });
    }
    
    public void cerrarVenta() {
        Utilities.showTextInputDialog("Cantidad con la que se paga", "Cantidad: ").ifPresent(string -> {
            BigDecimal cantidad;
                try {
                    cantidad = new BigDecimal(string).setScale(2, RoundingMode.DOWN);
                    
                    if(cantidad.compareTo(total) == -1) {
                        Utilities.showAlert("Ingresa un valor mayor al total");
                        cerrarVenta();
                    }
                    
                    Transaction transaction = Main.session.beginTransaction();
                    Main.session.saveOrUpdate(venta);
                    listaTabla.forEach(el -> {
                        el.setVenta(venta);
                        Main.session.save(el);
                    });
                    venta.setTotal(total);
                    this.lblTotal.setText("$" + total.subtract(cantidad).abs().toString());
                    this.btnCerrarVenta.setText("Realizar otra venta");
                    this.ventaRealizada = true;
                    this.lblTexto1.setText("Cambio: ");
                    this.txfBusqueda.setDisable(true);
                    this.tabla.setDisable(true);
                    Main.session.flush();
                    Main.session.clear();
                    transaction.commit();
                    imprimirTicket(cantidad.toString(), total.subtract(cantidad).abs().toString());
                } catch(NumberFormatException ex) {
                    Utilities.showAlert("Ingresa un valor numérico");
                    cerrarVenta();
                }
         });
    }

    public void insertar(String codigo) {
        codigo = codigo.toUpperCase();
        Query<Producto> query = Main.session.createQuery("SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class);
        query.setParameter("codigo", codigo);
        List<Producto> producto = query.list();
        if(producto.isEmpty()) {
            Utilities.showToast("Producto no registrado. Inserte una X y presione ENTER para productos genéricos");
        } else {
            Producto p = producto.get(0);
            
            List<VentaDetalle> vdRegistradas = this.listaTabla.stream()
                    .filter(el -> el.getProducto().getCodigo().equals(p.getCodigo()))
                    .collect(Collectors.toList());
            
            if(!vdRegistradas.isEmpty()) {
                vdRegistradas.get(0).setCantidad(vdRegistradas.get(0).getCantidad() + 1);
                this.ventaDetalleClickeada = vdRegistradas.get(0);
                calcularSubtotal();
            } else {
                VentaDetalle vd = new VentaDetalle(1.0, p.getPrecio_venta(), p);
                listaTabla.add(vd);
                calcularTotal();
            }
        }
        
        tabla.refresh();
        this.txfBusqueda.setText("");
        this.txfBusqueda.requestFocus();
    }
    
    public void setContextMenu() {
        cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Cambiar cantidad");
        mi1.setOnAction(e -> {
            cambiarCantidad();
            this.txfBusqueda.requestFocus();
        });
        MenuItem mi2 = new MenuItem("Remover el producto");
        mi2.setOnAction(e -> {
            if(Utilities.showConfirmationDialog("¿Estás seguro de remover el detalle de venta?")) {
                Utilities.showToast("Elemento removido");
            }
            this.txfBusqueda.requestFocus();
        });
        cm.getItems().addAll(mi1, mi2);
    }
    
    public void cambiarCantidad() {
        Utilities.showTextInputDialog("Introduce la cantidad", "Cantidad $: ").ifPresent(string -> {
            BigDecimal cantidad;
            try {
                cantidad = new BigDecimal(string);
                cantidad = cantidad.setScale(2, RoundingMode.DOWN);
                Double cantidadD = cantidad.doubleValue();
                this.ventaDetalleClickeada.setCantidad(cantidadD);
                calcularSubtotal();
            } catch(NumberFormatException ex) {
                Utilities.showAlert("Ingresa un valor numérico");
                cambiarCantidad();
            }
        });
    }
    
    public void calcularSubtotal() {
        BigDecimal cantidad = new BigDecimal(this.ventaDetalleClickeada.getCantidad());
        cantidad = cantidad.setScale(2, RoundingMode.DOWN);
        BigDecimal precioVenta = this.ventaDetalleClickeada.getProducto().getPrecio_venta();
        precioVenta = precioVenta.setScale(2, RoundingMode.DOWN);
        BigDecimal subtotal = precioVenta.multiply(cantidad);
        subtotal = subtotal.setScale(2, RoundingMode.DOWN);
        this.ventaDetalleClickeada.setTotal(subtotal);
        
//        Recalcular total
        calcularTotal();
    }
    
    public void calcularTotal() {
        System.out.println("Calculando total");
        total = new BigDecimal(0);
        listaTabla.forEach((ventaDetalle) -> {
            total = total.add(ventaDetalle.getTotal());
            System.out.println("Subtotal: " + ventaDetalle.getTotal());
        });
//        lblTotal.setStyle("-fx-text-fill: red");
        lblTotal.setText("$" + total.toString());
    }
    
    public void imprimirTicket(String recibo, String change) {
        String nameLocal = "ABARROTES ENRIQUEZ";
        String expedition = "Arandas, Jalisco";
        String ticket = "" + venta.getId();
        String dateTime = Utilities.getDateSpanishFormatted(venta.getFecha());
        String items = "";
        for (VentaDetalle ventaDetalle : listaTabla) {
            items = items.concat("" + ventaDetalle.getCantidad() + " " + ventaDetalle.getProducto().getNombre() + "$ c/u: " + 
                    ventaDetalle.getProducto().getPrecio_venta() + " $subtotal: " + ventaDetalle.getTotal().toString()) + "\n";
        }
        String totalVenta = "$" + venta.getTotal().toString();
        
        Ticket ticketNuevo = 
                new Ticket(nameLocal, expedition, ticket, dateTime, items, totalVenta, recibo, change);
        
        ticketNuevo.print();
    }
    
}
