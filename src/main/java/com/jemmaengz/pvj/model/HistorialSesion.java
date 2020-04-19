package com.jemmaengz.pvj.model;

import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
@Table(name = "HistorialSesiones")
public class HistorialSesion {

	public static class Estado {
        public final static Integer LOGGED_IN = 0;
        public final static Integer LOGGED_OUT = 1;
    }
    
    private LongProperty id = new SimpleLongProperty();
    private IntegerProperty tipo = new SimpleIntegerProperty();
    private SimpleObjectProperty<Date> fecha = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Usuario> usuario = new SimpleObjectProperty<>();

    public HistorialSesion() {
    }

    @Override
    public String toString() {
        return "HistorialSesion{" + "id=" + id + ", tipo=" + tipo + ", fecha=" + fecha.get().toString() + ", usuario=" + usuario + '}';
    }
    
    public HistorialSesion(Integer tipo, Date fecha, Usuario usuario) {
        this.tipo = new SimpleIntegerProperty(tipo);
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.usuario = new SimpleObjectProperty<>(usuario);
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

    @Column(name = "tipo")
    public int getTipo() {
        return tipo.get();
    }

    public void setTipo(int value) {
        tipo.set(value);
    }

    public IntegerProperty tipoProperty() {
        return tipo;
    }
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha")
    public Date getFecha() {
        return this.fecha.get();
    }

    public SimpleObjectProperty<Date> fechaProperty() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha.set(fecha);
    }
    
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    public Usuario getUsuario() {
        return this.usuario.get();
    }

    public SimpleObjectProperty<Usuario> usuarioProperty() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario.set(usuario);
    }
    
}
