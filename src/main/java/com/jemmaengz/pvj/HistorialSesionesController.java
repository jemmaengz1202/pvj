package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.HistorialSesion;
import com.jemmaengz.pvj.model.Usuario;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.hibernate.query.Query;

public class HistorialSesionesController implements Initializable {
    @FXML
    private TableView<HistorialSesion> tabla;

    @FXML
    private TableColumn<HistorialSesion, Date> fechaColumn;

    @FXML
    private TableColumn<HistorialSesion, Usuario> usuarioColumn;

    @FXML
    private TableColumn<HistorialSesion, Integer> tipoColumn;

    @FXML
    private JFXTextField txfBusqueda;
    
    @FXML
    private JFXDatePicker datePicker;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       fechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
       usuarioColumn.setCellValueFactory(cellData -> cellData.getValue().usuarioProperty());
       tipoColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty().asObject());
       
       fechaColumn.setCellFactory(tc -> new TableCell<HistorialSesion, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : Utilities.getDateSpanishFormatted(item));
            }
        });
       
       usuarioColumn.setCellFactory(tc -> new TableCell<HistorialSesion, Usuario>() {
           @Override
           protected void updateItem(Usuario item, boolean empty) {
               super.updateItem(item, empty);
               setText(empty ? null : item.getNombre());
           }
       });
       
       tipoColumn.setCellFactory(tc -> new TableCell<HistorialSesion, Integer>() {
           @Override
           protected void updateItem(Integer tipo, boolean empty) {
               super.updateItem(tipo, empty);
               String texto;
               if(Objects.equals(tipo, HistorialSesion.Estado.LOGGED_IN)) {
                   texto = "Conexión";
               } else {
                   texto = "Desconexión";
               }
               setText(empty ? null : texto);
           }
       });
       
       fillTable();
       
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
    }
    
    public void buscar(String str) {
        String queryS;
        queryS = "SELECT h FROM HistorialSesion h "
                   + "INNER JOIN h.usuario u "
                   + "WHERE h.usuario.nombre LIKE :b";
        Query<HistorialSesion> query = Main.session.createQuery(queryS, HistorialSesion.class);
        query.setParameter("b", "%"+str+"%");
        List<HistorialSesion> lista = query.list();
        // La tabla requiere una lista de Observables
        ObservableList<HistorialSesion> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }

    public void buscar(Date fecha) {
        Query<HistorialSesion> query = Main.session.createQuery("SELECT h FROM HistorialSesion h WHERE DATE(h.fecha) = :d", HistorialSesion.class);
        query.setParameter("d", fecha);
        List<HistorialSesion> lista = query.list();
        // La tabla requiere una lista de Observables
        ObservableList<HistorialSesion> listaTabla = FXCollections.observableList(lista);
        
        tabla.setItems(listaTabla);
    }
    
    private void fillTable() {
        // Lista del tipo determinado
        List<HistorialSesion> lista = Main.session.createQuery("from HistorialSesion h", HistorialSesion.class).list();
        // La tabla requiere una lista de Observables
        ObservableList<HistorialSesion> listaTabla = FXCollections.observableList(lista);
        
        System.out.println(lista);
        tabla.setItems(listaTabla);
//        lista.forEach(el -> {
//            System.out.println(el.toString());
//        });
    }
}
