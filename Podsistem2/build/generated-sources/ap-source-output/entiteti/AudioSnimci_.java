package entiteti;

import entiteti.Kategorije;
import entiteti.Korisnici;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-17T18:29:04")
@StaticMetamodel(AudioSnimci.class)
public class AudioSnimci_ { 

    public static volatile SingularAttribute<AudioSnimci, Date> datumPostavljanja;
    public static volatile SingularAttribute<AudioSnimci, Integer> trajanje;
    public static volatile SingularAttribute<AudioSnimci, String> naziv;
    public static volatile SingularAttribute<AudioSnimci, Korisnici> idVlasnik;
    public static volatile SingularAttribute<AudioSnimci, Integer> id;
    public static volatile ListAttribute<AudioSnimci, Kategorije> kategorijeList;
    public static volatile ListAttribute<AudioSnimci, Korisnici> korisniciList;

}