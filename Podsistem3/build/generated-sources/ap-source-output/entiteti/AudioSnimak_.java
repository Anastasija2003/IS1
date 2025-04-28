package entiteti;

import entiteti.Korisnik;
import entiteti.Ocena;
import entiteti.Slusanje;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-29T15:50:24")
@StaticMetamodel(AudioSnimak.class)
public class AudioSnimak_ { 

    public static volatile SingularAttribute<AudioSnimak, Date> datumPostavljanja;
    public static volatile ListAttribute<AudioSnimak, Slusanje> slusanjeList;
    public static volatile SingularAttribute<AudioSnimak, Integer> trajanje;
    public static volatile ListAttribute<AudioSnimak, Ocena> ocenaList;
    public static volatile SingularAttribute<AudioSnimak, String> naziv;
    public static volatile SingularAttribute<AudioSnimak, Korisnik> idVlasnik;
    public static volatile SingularAttribute<AudioSnimak, Integer> id;
    public static volatile ListAttribute<AudioSnimak, Korisnik> korisnikList;

}