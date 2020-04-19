package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Departamento;
import com.jemmaengz.pvj.model.Producto;
import com.jemmaengz.pvj.model.Usuario;
import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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

public class ListaProductosController implements Initializable {
    
    @FXML
    private TableView<Producto> tabla;

    @FXML
    private TableColumn<Producto, String> codigoColumn;

    @FXML
    private TableColumn<Producto, String> nombreColumn;

    @FXML
    private TableColumn<Producto, BigDecimal> precioColumn;

    @FXML
    private TableColumn<Producto, Integer> stockColumn;

    @FXML
    private TableColumn<Producto, Departamento> departamentoColumn;

    @FXML
    private JFXTextField txfBusqueda;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblDepartamento;
    
    private Producto productoClickeado;
    
    private ContextMenu cm;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColumn.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        departamentoColumn.setCellValueFactory(cellData -> cellData.getValue().departamentoProperty());
        precioColumn.setCellValueFactory(cellData -> cellData.getValue().precio_ventaProperty());
        stockColumn.setCellFactory(tc -> new TableCell<Producto, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item == -1 ? "No aplica" : item == 0 ? "Agotado" : item.toString());
            }
        });
        precioColumn.setCellFactory(tc -> new TableCell<Producto, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "$" + item.toString());
            }
        });
        
        fillTable();
        
        Platform.runLater(() -> {
            txfBusqueda.requestFocus();
        });
        
        txfBusqueda.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER)  {
                buscar(txfBusqueda.getText());
            }
        });
        
        setContextMenu();
        
        tabla.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY 
                && event.getClickCount() == 1) {
                    productoClickeado = row.getItem();
                    setContextMenu();
                    if(productoClickeado.getUsuario_que_registro().equals(LoginController.usuario) &&
                            !(LoginController.usuario.getRol() == Usuario.Rol.empleado)) {                        
                        cm.show(row, event.getScreenX(), event.getScreenY());
                    }
                    this.lblNombre.setText(productoClickeado.getNombre());
                    this.lblPrecio.setText("$" + productoClickeado.getPrecio_venta().toString());
                    if(productoClickeado.getDepartamento() != null) {
                        this.lblDepartamento.setText(productoClickeado.getDepartamento().getNombre());
                    } else {
                        this.lblDepartamento.setText("Departamento no registrado");
                    }
                    
                }
            });
            return row ;
         });
    }
    
    public void setContextMenu() {
        cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Ver detalles del producto");
        mi1.setOnAction(e -> {
            ProductoController controller = new ProductoController(this.productoClickeado);
            PrincipalController.instance.setMainPaneContent(controller, "ProductoView");
            PrincipalController.instance.txtCabecera.setText("Producto");
        });
        MenuItem mi2 = new MenuItem("Aumentar stock");
        mi2.setOnAction(e -> {
            aumentarStock();
        });
        MenuItem mi3 = new MenuItem("Eliminar el producto");
        mi3.setOnAction(e -> {
            if(Utilities.showConfirmationDialog("¿Estás seguro de eliminar el producto?")) {
                this.productoClickeado.setStock(-2);
                Transaction transaction = Main.session.beginTransaction();
                Main.session.saveOrUpdate(productoClickeado);
                transaction.commit();
                System.out.println(this.productoClickeado);
                PrincipalController.instance.setMainPaneContent("ListaProductosView");
                Utilities.showToast("Elemento eliminado");
            }
        });
        if(productoClickeado != null && productoClickeado.getStock() >= 0) {
            cm.getItems().addAll(mi1, mi2, mi3);
        } else {
            cm.getItems().addAll(mi1, mi3);
        }
        
    }
    
    public void buscar(String b) {
        b = b.toLowerCase();
        String queryS = "SELECT p from Producto p "
                             + "INNER JOIN p.departamento d "
                             + "INNER JOIN p.proveedor  o "
                             + "WHERE (p.codigo = :bexacta OR (lower(p.nombre) LIKE :b) OR (lower(d.nombre) LIKE :b)"
                             + "OR (lower(o.nombre) LIKE :b)) AND p.stock <> -1";
        
        Query<Producto> query = Main.session.createQuery(queryS, Producto.class);
        query.setParameter("bexacta", b);
        query.setParameter("b", "%" + b + "%");
        
        List<Producto> lista = query.list();
        
        ObservableList<Producto> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
        
        if(lista.size() == 1) {
            Producto productoVisible = lista.get(0);
            this.lblNombre.setText(productoVisible.getNombre());
            this.lblPrecio.setText("$" + productoVisible.getPrecio_venta().toString());
            this.lblDepartamento.setText(productoVisible.getDepartamento().getNombre());
            this.txfBusqueda.selectRange(0, 50);
        }
    } 
    
    public void aumentarStock() {
        Utilities.showTextInputDialog("Introduce la cantidad", "Cantidad: ").ifPresent(string -> {
                if(Utilities.isInteger(string)) {
                    Integer stock = Integer.parseInt(string);
                    this.productoClickeado.setStock(this.productoClickeado.getStock() + stock);
                    Transaction transaction = Main.session.beginTransaction();
                    Main.session.saveOrUpdate(productoClickeado);
                    transaction.commit();
                    PrincipalController.instance.setMainPaneContent("ListaProductosView");
                    Utilities.showToast("Stock del producto aumentado");
                } else {
                    Utilities.showAlert("Ingresa un valor numérico");
                    aumentarStock();
                }
            });
    }

    public void fillTable() {
        
        // Lista del tipo determinado
        List<Producto> lista = Main.session.createQuery("SELECT p FROM Producto p WHERE p.stock <> -2", Producto.class)
                                            .list();
        // La tabla requiere una lista de Observables
        ObservableList<Producto> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
        
        Producto productoVisible = lista.get(0);
        this.lblNombre.setText(productoVisible.getNombre());
        this.lblPrecio.setText("$" + productoVisible.getPrecio_venta().toString());
        this.lblDepartamento.setText(productoVisible.getDepartamento().getNombre());
        this.txfBusqueda.selectRange(0, 50);
    }
    
}
