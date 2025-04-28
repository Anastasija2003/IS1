/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Tasa
 */
@Entity
@Table(name = "audio_snimak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AudioSnimak.findAll", query = "SELECT a FROM AudioSnimak a"),
    @NamedQuery(name = "AudioSnimak.findById", query = "SELECT a FROM AudioSnimak a WHERE a.id = :id"),
    @NamedQuery(name = "AudioSnimak.findByNaziv", query = "SELECT a FROM AudioSnimak a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "AudioSnimak.findByTrajanje", query = "SELECT a FROM AudioSnimak a WHERE a.trajanje = :trajanje"),
    @NamedQuery(name = "AudioSnimak.findByDatumPostavljanja", query = "SELECT a FROM AudioSnimak a WHERE a.datumPostavljanja = :datumPostavljanja")})
public class AudioSnimak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanje")
    private int trajanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumPostavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPostavljanja;
    @ManyToMany(mappedBy = "audioSnimakList")
    private List<Korisnik> korisnikList;
    @JoinTable(name = "audio_snimak_kategorija", joinColumns = {
        @JoinColumn(name = "idSnimak", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "idKategorija", referencedColumnName = "id")})
    @ManyToMany
    private List<Kategorija> kategorijaList;
    @JoinColumn(name = "idVlasnik", referencedColumnName = "id")
    @ManyToOne
    private Korisnik idVlasnik;

    public AudioSnimak() {
    }

    public AudioSnimak(Integer id) {
        this.id = id;
    }

    public AudioSnimak(Integer id, String naziv, int trajanje, Date datumPostavljanja) {
        this.id = id;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datumPostavljanja = datumPostavljanja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    public Date getDatumPostavljanja() {
        return datumPostavljanja;
    }

    public void setDatumPostavljanja(Date datumPostavljanja) {
        this.datumPostavljanja = datumPostavljanja;
    }

    @XmlTransient
    public List<Korisnik> getKorisnikList() {
        return korisnikList;
    }

    public void setKorisnikList(List<Korisnik> korisnikList) {
        this.korisnikList = korisnikList;
    }

    @XmlTransient
    public List<Kategorija> getKategorijaList() {
        return kategorijaList;
    }

    public void setKategorijaList(List<Kategorija> kategorijaList) {
        this.kategorijaList = kategorijaList;
    }

    public Korisnik getIdVlasnik() {
        return idVlasnik;
    }

    public void setIdVlasnik(Korisnik idVlasnik) {
        this.idVlasnik = idVlasnik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AudioSnimak)) {
            return false;
        }
        AudioSnimak other = (AudioSnimak) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.AudioSnimak[ id=" + id + " ]";
    }
    
}
