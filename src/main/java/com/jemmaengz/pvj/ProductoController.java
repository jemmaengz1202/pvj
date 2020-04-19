package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Departamento;
import com.jemmaengz.pvj.model.Producto;
import com.jemmaengz.pvj.model.Proveedor;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.hibernate.Transaction;

public class ProductoController implements Initializable {
    @FXML
    private VBox vboxForm;
    
    @FXML
    private JFXButton btnEditarGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXTextField txtCodigo;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtPrecio_venta;

    @FXML
    private JFXTextField txtPrecio_compra;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXComboBox<String> cmbxDepartamento;

    @FXML
    private JFXComboBox<String> cmbxProveedor;
    
    private Producto producto;
    
    private List<Departamento> departamentos;
    
    private List<Proveedor> proveedores;
    
    @FXML
    private JFXButton btnAgregarDepartamento;

    @FXML
    private JFXButton btnAgregarProveedor;

    
    void save() {
        if(! (txtCodigo.validate() && txtNombre.validate() && txtPrecio_venta.validate() && txtPrecio_compra.validate()) ) {
            Utilities.showAlert("Rellena todos los campos que son requeridos");
            return;
        }
        bindFields();
        
//        Chequear si no hay códigos duplicados
        List<Producto> productoDuplicado = Main.session.createQuery("from Producto p WHERE p.codigo = " + txtCodigo.getText(), Producto.class).list();
        if(productoDuplicado.size() > 0) {
            Utilities.showAlert("Ya existe un producto con ese código");
            this.txtCodigo.requestFocus();
            return;
        }
        
        if(this.producto.getPrecio_compra().compareTo(this.producto.getPrecio_venta()) != -1) {
            Utilities.showAlert("El precio de venta debe ser mayor al precio de compra");
            this.txtPrecio_venta.requestFocus();
            return;
        }
        
        Transaction transaction = Main.session.beginTransaction();
        Main.session.saveOrUpdate(producto);
        transaction.commit();
        
        PrincipalController.instance.setMainPaneContent("ListaProductosView");
        Utilities.showToast("Guardado");
    }
    
    void delete() {
        this.producto.setStock(-2);
        if (Utilities.showConfirmationDialog("¿Estás seguro de eliminar el elemento?")) {
            Transaction transaction = Main.session.beginTransaction();
            Main.session.saveOrUpdate(producto);
            transaction.commit();
        }
    }
    
    public void init() {
        Utilities.setMaxCharacters(txtCodigo, 15);
        
        Utilities.setRequiredValidator(txtCodigo);
        Utilities.setRequiredValidator(txtNombre);
        Utilities.setRequiredValidator(txtPrecio_venta);
        Utilities.setRequiredValidator(txtPrecio_compra);
        
        txtPrecio_compra.setTextFormatter(Utilities.getDecimalFormatter());
        txtPrecio_venta.setTextFormatter(Utilities.getDecimalFormatter());
        txtCodigo.setTextFormatter(Utilities.getNumberCodeFormatter());
        txtStock.setTextFormatter(Utilities.getNumberFormatter());

        departamentos = Main.session.createQuery("from Departamento d order by d.nombre", Departamento.class).list();
        ArrayList<String> departamentosNombres = (ArrayList<String>) departamentos.stream()
                .map(Departamento::getNombre)
                .collect(Collectors.toList());
        ObservableList<String> cmbxList1 = FXCollections.observableList(departamentosNombres);
        cmbxDepartamento.setItems(cmbxList1);
        cmbxDepartamento.getSelectionModel().select(0);
        
        proveedores = Main.session.createQuery("from Proveedor p order by p.nombre", Proveedor.class).list();
        ArrayList<String> proveedoresNombres = (ArrayList<String>) proveedores.stream()
                .map(Proveedor::getNombre)
                .collect(Collectors.toList());
        ObservableList<String> cmbxList2 = FXCollections.observableList(proveedoresNombres);
        cmbxProveedor.setItems(cmbxList2);
        cmbxProveedor.getSelectionModel().select(0);
        
        btnAgregarDepartamento.setOnAction(e -> {
            agregarDepartamento();
        });
        
        btnAgregarProveedor.setOnAction(e -> {
            agregarProveedor();
        });

//        Entrará al if si es edición
        if(this.producto != null) {
            cmbxDepartamento.setValue(producto.getDepartamento().getNombre());
            cmbxProveedor.setValue(producto.getProveedor().getNombre());
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            txtPrecio_compra.setText(producto.getPrecio_compra().toString());
            txtPrecio_venta.setText(producto.getPrecio_venta().toString());
            txtStock.setText(producto.getStock().toString());
            
            vboxForm.getChildren().forEach(e -> {
                e.setStyle("-fx-opacity: .75;");
                e.setDisable(true);
            });
            btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    btnEditarGuardar.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                
                    btnEditarGuardar.setText("Guardar");
                    
                    vboxForm.getChildren().forEach(el -> {
                        el.setStyle("-fx-opacity: 1.0;");
                        el.setDisable(false);
                    });
                    
                    txtCodigo.requestFocus();
                    
                    btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                        save();
                    });
                }}
            );
            btnEliminar.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                delete();
                PrincipalController.instance.setMainPaneContent("ListaProductosView");
                Utilities.showToast("Elemento eliminado");
            });
        
//            Y si no es edición (nuevo producto)
        } else {
            this.producto = new Producto();
            btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                  save();
            });
            btnEditarGuardar.setText("Guardar");
            btnEliminar.setVisible(false);
        }
    }
    
    private void agregarDepartamento() {
        if(!Utilities.showConfirmationDialog("Si el departamento no está en la lista, agregue uno nuevo. ¿Desea continuar?")) {
            return;
        }
        Utilities.showTextInputDialog("Agregar departamento a la lista", "Nomre: ").ifPresent(nombre -> {
            Departamento departamento = new Departamento(nombre);
            Transaction transaction = Main.session.beginTransaction();
            Main.session.saveOrUpdate(departamento);
            transaction.commit();
            departamentos = Main.session.createQuery("from Departamento d order by d.nombre", Departamento.class).list();
            ArrayList<String> departamentosNombres = (ArrayList<String>) departamentos.stream()
                    .map(Departamento::getNombre)
                    .collect(Collectors.toList());
            ObservableList<String> cmbxList1 = FXCollections.observableList(departamentosNombres);
            cmbxDepartamento.setItems(cmbxList1);
            cmbxDepartamento.getSelectionModel().select(departamento.getNombre());
        });
    }
    
    private void agregarProveedor() {
        if(!Utilities.showConfirmationDialog("Si el proveedor no está en la lista, agregue uno nuevo. ¿Desea continuar?")) {
            return;
        }
        Utilities.showTextInputDialog("Agregar proveedor a la lista", "Nombre: ").ifPresent(nombre -> {
            Utilities.showTextInputDialog("Inserte el teléfono. Ingrese 'no aplica' o 'no tiene' en caso necesario", "Teléfono: ").ifPresent(tel -> {
                Proveedor proveedor = new Proveedor(nombre, tel);
                Transaction transaction = Main.session.beginTransaction();
                Main.session.saveOrUpdate(proveedor);
                transaction.commit();
                proveedores = Main.session.createQuery("from Proveedor p order by p.nombre", Proveedor.class).list();
                ArrayList<String> proveedoresNombres = (ArrayList<String>) proveedores.stream()
                        .map(Proveedor::getNombre)
                        .collect(Collectors.toList());
                ObservableList<String> cmbxList2 = FXCollections.observableList(proveedoresNombres);
                cmbxProveedor.setItems(cmbxList2);
                cmbxProveedor.getSelectionModel().select(proveedor.getNombre());
            });
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    public ProductoController(Producto producto) {
        this.producto = producto;
    }

    public ProductoController() {
    }
    
    private void bindFields() {
        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();
        BigDecimal precioVenta = new BigDecimal(this.txtPrecio_venta.getText());
        BigDecimal precioCompra = new BigDecimal("0" + this.txtPrecio_compra.getText());
        Integer stock;
        
        if(this.txtStock.getText().equals("") || this.txtStock.getText() == null) {
            stock = -1;
        } else {
            stock = Integer.parseInt("0" + this.txtStock.getText());
        }
        
        Departamento departamento = departamentos.stream()
                                .filter(dep -> dep.getNombre().equals(cmbxDepartamento.getValue()))
                                .collect(Collectors.toList())
                                .get(0);
        Proveedor proveedor = proveedores.stream()
                                .filter(pro -> pro.getNombre().equals(cmbxProveedor.getValue()))
                                .collect(Collectors.toList())
                                .get(0);
        
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setPrecio_venta(new SimpleObjectProperty<>(precioVenta));
        producto.setPrecio_compra(new SimpleObjectProperty<>(precioCompra));
        producto.setStock(stock);
        producto.setDepartamento(new SimpleObjectProperty<>(departamento));
        producto.setProveedor(new SimpleObjectProperty<>(proveedor));
        producto.setUsuario_que_registro(LoginController.usuario);
    }
    
}
