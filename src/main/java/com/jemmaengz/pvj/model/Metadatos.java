package com.jemmaengz.pvj.model;

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
@Table(name = "metadatos")
public class Metadatos {
	private LongProperty id = new SimpleLongProperty();
    private StringProperty nombreNegocio = new SimpleStringProperty();
    private StringProperty slogan = new SimpleStringProperty();
    private StringProperty textoTicket = new SimpleStringProperty();
    private IntegerProperty tamanoFuente = new SimpleIntegerProperty();

    public Metadatos() {
    }
    
    public Metadatos(String nombreNegocio, String slogan, String textoTicket, Integer tamanoFuente) {
        this.nombreNegocio = new SimpleStringProperty(nombreNegocio);
        this.slogan = new SimpleStringProperty(slogan);
        this.textoTicket = new SimpleStringProperty(textoTicket);
        this.tamanoFuente = new SimpleIntegerProperty(tamanoFuente);
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
    public String getNombreNegocio() {
        return nombreNegocio.get();
    }

    public void setNombreNegocio(String value) {
        nombreNegocio.set(value);
    }

    public StringProperty nombreNegocioProperty() {
        return nombreNegocio;
    }

    @Column(name = "slogan")
    public String getSlogan() {
        return slogan.get();
    }

    public void setSlogan(String value) {
        slogan.set(value);
    }

    public StringProperty sloganProperty() {
        return slogan;
    }

    @Column(name = "texto_ticket")
    public String getTextoTicket() {
        return textoTicket.get();
    }

    public void setTextoTicket(String value) {
        textoTicket.set(value);
    }

    public StringProperty textoTicketProperty() {
        return textoTicket;
    }

    @Column(name = "tamano_fuente")
    public int getTamanoFuente() {
        return tamanoFuente.get();
    }

    public void setTamanoFuente(int value) {
        tamanoFuente.set(value);
    }

    public IntegerProperty tamanoFuenteProperty() {
        return tamanoFuente;
    }
    
}
