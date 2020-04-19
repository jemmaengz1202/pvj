package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Compra;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.hibernate.Transaction;

public class CompraController implements Initializable {
    
     @FXML
    private JFXButton btnEditarGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private VBox vboxForm;

    @FXML
    private JFXTextField txtPrecio;

    @FXML
    private JFXComboBox<String> cmbxProveedor;
    
    @FXML
    private JFXButton btnAgregarProveedor;
    
    private List<Proveedor> proveedores;
    
    private Compra compra;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilities.setRequiredValidator(txtPrecio);
        txtPrecio.setTextFormatter(Utilities.getDecimalFormatter());
        
        this.proveedores = Main.session.createQuery("from Proveedor p", Proveedor.class).list();
        ArrayList<String> proveedoresNombres = (ArrayList<String>) proveedores.stream()
                .map(Proveedor::getNombre)
                .collect(Collectors.toList());
        ObservableList<String> cmbxList1 = FXCollections.observableList(proveedoresNombres);
        cmbxProveedor.setItems(cmbxList1);
        cmbxProveedor.getSelectionModel().select(0);
        
        this.btnAgregarProveedor.setOnAction(e -> {
            agregarProveedor();
        });
        
        if(this.compra != null) {
            this.txtPrecio.setText(this.compra.getTotal().toString());
            this.cmbxProveedor.setValue(this.compra.getProveedor().getNombre());
            
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
                    
                    txtPrecio.requestFocus();
                    
                    btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                        save();
                    });
                }}
            );
            btnEliminar.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                delete();
            });
        } else {
            this.compra = new Compra();
            btnEditarGuardar.addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> {
                  save();
            });
            btnEditarGuardar.setText("Guardar");
            btnEliminar.setVisible(false);
        }
    }

    public void save() {
        BigDecimal total = new BigDecimal(this.txtPrecio.getText());
        Proveedor proveedor = proveedores.stream()
                .filter(pro -> pro.getNombre().equals(cmbxProveedor.getValue()))
                .collect(Collectors.toList())
                .get(0);
        
        compra.setCajero(LoginController.usuario);
        compra.setTotal(total);
        compra.setProveedor(proveedor);
        
        if(!txtPrecio.validate()) {
            Utilities.showAlert("Rellena todos los campos que son requeridos");
            return;
        }
        Transaction transaction = Main.session.beginTransaction();
        Main.session.saveOrUpdate(this.compra);
        transaction.commit();
        PrincipalController.instance.setMainPaneContent("ListaComprasView");
        Utilities.showToast("Guardado");
    }
    
    public void delete() {
        if (Utilities.showConfirmationDialog("¿Estás seguro de eliminar el elemento?")) {
            Transaction transaction = Main.session.beginTransaction();
            Main.session.delete(this.compra);
            transaction.commit();
            PrincipalController.instance.setMainPaneContent("ListaComprasView");
            Utilities.showToast("Elemento eliminado");
        }
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

    public CompraController() {
        
    }
    
    public CompraController(Compra compra) {
        this.compra = compra;
    }
}
