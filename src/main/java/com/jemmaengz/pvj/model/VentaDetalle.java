package com.jemmaengz.pvj.model;

import java.math.BigDecimal;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VentasDetalle")
public class VentaDetalle {
	private LongProperty id = new SimpleLongProperty();
    private DoubleProperty cantidad = new SimpleDoubleProperty();
    private SimpleObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Producto> producto = new SimpleObjectProperty<>();
    private Venta venta;

    public VentaDetalle() {
    }
    
    public VentaDetalle(Double cantidad, BigDecimal total, Producto producto) {
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.total = new SimpleObjectProperty<>(total);
        this.producto = new SimpleObjectProperty<>(producto);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id.get();
    }

    public void setId(long value) {
        id.set(value);
    }

    public LongProperty idProperty() {
        return id;
    }

    @Column(name = "cantidad")
    public double getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(double value) {
        cantidad.set(value);
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }

    @Column(name = "total", columnDefinition="Decimal(10,2) default '0.00'")
    public BigDecimal getTotal() {
        return this.total.get();
    }
    
    public SimpleObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
    }

    @ManyToOne
    @JoinColumn(name = "idProducto")
    public Producto getProducto() {
        return this.producto.get();
    }
    
    public SimpleObjectProperty<Producto> productoProperty() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto.set(producto);
    }
    
    @ManyToOne
    @JoinColumn(name = "idVenta")
    public Venta getVenta() {
        return this.venta;
    }
    
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
}
