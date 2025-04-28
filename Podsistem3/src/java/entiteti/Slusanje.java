/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tasa
 */
@Entity
@Table(name = "slusanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slusanje.findAll", query = "SELECT s FROM Slusanje s"),
    @NamedQuery(name = "Slusanje.findById", query = "SELECT s FROM Slusanje s WHERE s.id = :id"),
    @NamedQuery(name = "Slusanje.findByDatumVreme", query = "SELECT s FROM Slusanje s WHERE s.datumVreme = :datumVreme"),
    @NamedQuery(name = "Slusanje.findByPocetakSekunde", query = "SELECT s FROM Slusanje s WHERE s.pocetakSekunde = :pocetakSekunde"),
    @NamedQuery(name = "Slusanje.findByTrajanjeSekundi", query = "SELECT s FROM Slusanje s WHERE s.trajanjeSekundi = :trajanjeSekundi")})
public class Slusanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pocetakSekunde")
    private int pocetakSekunde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanjeSekundi")
    private int trajanjeSekundi;
    @JoinColumn(name = "idSnimak", referencedColumnName = "id")
    @ManyToOne
    private AudioSnimak idSnimak;
    @JoinColumn(name = "idKorisnik", referencedColumnName = "id")
    @ManyToOne
    private Korisnik idKorisnik;

    public Slusanje() {
    }

    public Slusanje(Integer id) {
        this.id = id;
    }

    public Slusanje(Integer id, Date datumVreme, int pocetakSekunde, int trajanjeSekundi) {
        this.id = id;
        this.datumVreme = datumVreme;
        this.pocetakSekunde = pocetakSekunde;
        this.trajanjeSekundi = trajanjeSekundi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public int getPocetakSekunde() {
        return pocetakSekunde;
    }

    public void setPocetakSekunde(int pocetakSekunde) {
        this.pocetakSekunde = pocetakSekunde;
    }

    public int getTrajanjeSekundi() {
        return trajanjeSekundi;
    }

    public void setTrajanjeSekundi(int trajanjeSekundi) {
        this.trajanjeSekundi = trajanjeSekundi;
    }

    public AudioSnimak getIdSnimak() {
        return idSnimak;
    }

    public void setIdSnimak(AudioSnimak idSnimak) {
        this.idSnimak = idSnimak;
    }

    public Korisnik getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(Korisnik idKorisnik) {
        this.idKorisnik = idKorisnik;
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
        if (!(object instanceof Slusanje)) {
            return false;
        }
        Slusanje other = (Slusanje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Slusanje[ id=" + id + " ]";
    }
    
}
