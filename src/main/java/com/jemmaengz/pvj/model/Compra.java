package com.jemmaengz.pvj.model;

import java.math.BigDecimal;
import java.util.Date;
import javafx.beans.property.LongProperty;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Compras")
public class Compra {
	private LongProperty id = new SimpleLongProperty();
    private SimpleObjectProperty<Date> fecha = new SimpleObjectProperty<>();
    private SimpleObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Proveedor> proveedor =  new SimpleObjectProperty<>();
    private SimpleObjectProperty<Usuario> cajero = new SimpleObjectProperty<>();

    public Compra () {
    }
    
    public Compra(Date fecha, BigDecimal total, Proveedor proveedor) {
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.total = new SimpleObjectProperty<>(total);
        this.proveedor = new SimpleObjectProperty<>(proveedor);
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

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha")
    public Date getFecha() {
        return fecha.get();
    }

    public void setFecha(Date fecha) {
        this.fecha.set(fecha);
    }
    
    public SimpleObjectProperty<Date> fechaProperty() {
        return this.fecha;
    }

    @Column(name="precio", columnDefinition="Decimal(10,2) default '0.00'")
    public BigDecimal getTotal() {
        return total.get();
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
    }
    
    public SimpleObjectProperty<BigDecimal> totalProperty() {
        return this.total;
    }

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    public Proveedor getProveedor() {
        return proveedor.get();
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor.set(proveedor);
    }
    
    public SimpleObjectProperty<Proveedor> proveedorProperty() {
        return this.proveedor;
    }

    @ManyToOne
    @JoinColumn(name = "idCajero")
    public Usuario getCajero() {
        return cajero.get();
    }

    public void setCajero(Usuario cajero) {
        this.cajero.set(cajero);
    }
    
    public SimpleObjectProperty<Usuario> cajeroProperty() {
        return this.cajero;
    }
    
}
