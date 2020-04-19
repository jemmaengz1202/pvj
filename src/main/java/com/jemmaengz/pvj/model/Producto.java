package com.jemmaengz.pvj.model;

import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Productos")
public class Producto {
	private LongProperty id = new SimpleLongProperty();
    private StringProperty codigo = new SimpleStringProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private SimpleObjectProperty<BigDecimal> precio_venta = new SimpleObjectProperty<>();
    private SimpleObjectProperty<BigDecimal> precio_compra = new SimpleObjectProperty<>();
    private IntegerProperty stock = new SimpleIntegerProperty();
    private SimpleObjectProperty<Proveedor> proveedor = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Departamento> departamento = new SimpleObjectProperty<>();
    private Usuario usuario_que_registro;

    public Producto() {
    }
    
    public Producto(String codigo, String nombre, BigDecimal precio_venta, BigDecimal precio_compra, 
                            Integer stock, Proveedor proveedor, Departamento departamento) {
        this.codigo = new SimpleStringProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio_venta = new SimpleObjectProperty<>(precio_venta);
        this.precio_compra = new SimpleObjectProperty<>(precio_compra);
        this.stock = new SimpleIntegerProperty(stock);
        this.proveedor = new SimpleObjectProperty<>(proveedor);
        this.departamento = new SimpleObjectProperty<>(departamento);
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

    @Column(name = "codigo")
    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String value) {
        codigo.set(value);
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    @Column(name = "nombre")
    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    @Column(name = "stock")
    public Integer getStock() {
        return stock.get();
    }

    public void setStock(Integer value) {
        stock.set(value);
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    @Column(name="precio_venta", columnDefinition="Decimal(10,2) default '0.00'")
    public BigDecimal getPrecio_venta() {
        return precio_venta.get();
    }

    public void setPrecio_venta(SimpleObjectProperty<BigDecimal> precio_venta) {
        this.precio_venta = precio_venta;
    }
    
    public void setPrecio_venta(BigDecimal precio_venta) {
        this.precio_venta.set(precio_venta);
    }
    
    public SimpleObjectProperty<BigDecimal> precio_ventaProperty() {
        return precio_venta;
    }

    @Column(name="precio_compra", columnDefinition="Decimal(10,2) default '0.00'")
    public BigDecimal getPrecio_compra() {
        return precio_compra.get();
    }

    public void setPrecio_compra(SimpleObjectProperty<BigDecimal> precio_compra) {
        this.precio_compra = precio_compra;
    }
    
    public void setPrecio_compra(BigDecimal precio_compra) {
        this.precio_compra.set(precio_compra);
    }
    
    public SimpleObjectProperty<BigDecimal> precio_compraProperty() {
        return this.precio_compra;
    }

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    public Proveedor getProveedor() {
        return proveedor.get();
    }

    public void setProveedor(SimpleObjectProperty<Proveedor> proveedor) {
        this.proveedor = proveedor;
    }
    
    public void setProveedor(Proveedor proveedor) {
        this.proveedor.set(proveedor);
    }
    
    public SimpleObjectProperty<Proveedor> proveedorProperty() {
        return this.proveedor;
    }

    @ManyToOne
    @JoinColumn(name = "idDepartamento")
    public Departamento getDepartamento() {
        return departamento.get();
    }

    public void setDepartamento(SimpleObjectProperty<Departamento> departamento) {
        this.departamento = departamento;
    }
    
    public void setDepartamento(Departamento departamento) {
        this.departamento.set(departamento);
    }
    
    public SimpleObjectProperty<Departamento> departamentoProperty() {
        return this.departamento;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_que_registro")
    public Usuario getUsuario_que_registro() {
        return usuario_que_registro;
    }

    public void setUsuario_que_registro(Usuario usuario_que_registro) {
        this.usuario_que_registro = usuario_que_registro;
    }
    
}
