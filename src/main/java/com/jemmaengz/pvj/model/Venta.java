package com.jemmaengz.pvj.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Ventas")
public class Venta {
	private LongProperty id = new SimpleLongProperty();
    private SimpleObjectProperty<Date> fecha = new SimpleObjectProperty<>();
    private SimpleObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Usuario> cajero = new SimpleObjectProperty<>();
    private Set<VentaDetalle> ventas_detalle = new HashSet<>();

    public Venta() {
    }
    
    public Venta(Usuario usuario) {
        this.cajero = new SimpleObjectProperty<>(usuario);
    }
    
    public Venta(Date fecha, BigDecimal total, Usuario usuario) {
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.total = new SimpleObjectProperty<>(total);
        this.cajero = new SimpleObjectProperty<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
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
        return fecha;
    }
    
    @Column(name = "total", columnDefinition="Decimal(10,2) default '0.00'")
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

    @OneToMany(mappedBy = "venta", orphanRemoval = true, cascade = CascadeType.REMOVE)
    public Set<VentaDetalle> getVentas_detalle() {
        return ventas_detalle;
    }

    public void setVentas_detalle(Set<VentaDetalle> ventas_detalle) {
        this.ventas_detalle = ventas_detalle;
    }
    
    public void addVenta_detalle(VentaDetalle venta_detalle) {
        this.ventas_detalle.add(venta_detalle);
        venta_detalle.setVenta(this);
    }
    
    public void removeVenta_detalle(VentaDetalle venta_detalle) {
        this.ventas_detalle.remove(venta_detalle);
        venta_detalle.setVenta(null);
    }
}
