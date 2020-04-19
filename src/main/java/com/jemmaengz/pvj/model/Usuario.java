package com.jemmaengz.pvj.model;

import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
@Table(name = "Usuarios")
public class Usuario {

	public static final class Rol {
        public static final Integer admin = 0;
        public static final Integer empleado = 1;
        public static final Integer root = 2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.usuario);
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.rol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.rol, other.rol)) {
            return false;
        }
        return true;
    }
    
    private LongProperty id = new SimpleLongProperty();
    private StringProperty usuario = new SimpleStringProperty();
    private StringProperty contrasena = new SimpleStringProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private IntegerProperty rol = new SimpleIntegerProperty();

    public Usuario() {
    }
    
    public Usuario(String usuario, String contrasena, String nombre, Integer rol) {
        this.usuario = new SimpleStringProperty(usuario);
        this.contrasena = new SimpleStringProperty(contrasena);
        this.nombre = new SimpleStringProperty(nombre);
        this.rol = new SimpleIntegerProperty(rol);
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

    @Column(name = "contrasena")
    public  String getContrasena() {
        return contrasena.get();
    }

    public void setContrasena(String value) {
        contrasena.set(value);
    }

    @Column(name = "usuario")
    public String getUsuario() {
        return usuario.get();
    }

    public void setUsuario(String value) {
        usuario.set(value);
    }

    public StringProperty usuarioProperty() {
        return usuario;
    }

    public StringProperty contrasenaProperty() {
        return contrasena;
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

    @Column(name = "rol")
    public int getRol() {
        return rol.get();
    }

    public void setRol(int value) {
        rol.set(value);
    }

    public IntegerProperty rolProperty() {
        return rol;
    }

    @Override
    public String toString() {
        return nombre.get();
    }
    
}
