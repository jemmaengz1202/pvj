package com.jemmaengz.pvj.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Proveedores")
public class Proveedor {
	private LongProperty id = new SimpleLongProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private StringProperty telefono = new SimpleStringProperty();

    public Proveedor() {
    }

    public Proveedor(String nombre, String telefono) {
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id.get();
    }

    public  void setId(long value) {
        id.set(value);
    }

    public LongProperty idProperty() {
        return id;
    }

    @Column(name = "nombre")
    public  String getNombre() {
        return nombre.get();
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    @Column(name = "telefono")
    public String getTelefono() {
        return telefono.get();
    }

    public void setTelefono(String value) {
        telefono.set(value);
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }
    
}
