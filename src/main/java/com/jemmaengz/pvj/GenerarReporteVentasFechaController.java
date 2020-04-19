package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.Compra;
import com.jemmaengz.pvj.model.Venta;
import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.hibernate.query.Query;

public class GenerarReporteVentasFechaController implements Initializable {
    
    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblCompras;

    @FXML
    private Label lblDiferencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblTotal.setText("$0.00");
        lblCompras.setText("$0.00");
        lblDiferencia.setText("$0.00");
        
        datePicker.setOnKeyPressed(k -> {
           if (k.getCode() == KeyCode.ENTER)  {
                buscar(java.sql.Date.valueOf(datePicker.getValue()));
            }
       });
    }    
    
    public void buscar(Date fecha) {
        Query<Venta> query = Main.session.createQuery("SELECT v FROM Venta v WHERE DATE(v.fecha) = :d", Venta.class);
        query.setParameter("d", fecha);
        BigDecimal totalVentas = new BigDecimal(0);
        List<Venta> lista = query.list();
        for (Venta venta : lista) {
            totalVentas = totalVentas.add(venta.getTotal());
        }
        
        Query<Compra> query2 = Main.session.createQuery("SELECT c FROM Compra c WHERE DATE(c.fecha) = :d", Compra.class);
        query2.setParameter("d", fecha);
        BigDecimal totalCompras = new BigDecimal(0);
        List<Compra> lista2 = query2.list();
        for (Compra venta : lista2) {
            totalCompras = totalCompras.add(venta.getTotal());
        }
        
        BigDecimal diferencia = totalVentas.subtract(totalCompras);
        
        this.lblTotal.setText("$" + totalVentas.toString());
        this.lblCompras.setText("$" + totalCompras.toString());
        this.lblDiferencia.setText("$" + diferencia.toString());
    }
    
}
