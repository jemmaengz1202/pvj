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
@Table(name = "Departamentos")
public class Departamento {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty nombre = new SimpleStringProperty();

    public Departamento() {
    }

    @Override
    public String toString() {
        return nombre.get();
    }
    
    public Departamento(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
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
    
}
