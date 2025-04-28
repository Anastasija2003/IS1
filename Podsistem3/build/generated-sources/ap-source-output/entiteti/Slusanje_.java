package entiteti;

import entiteti.AudioSnimak;
import entiteti.Korisnik;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-29T15:50:24")
@StaticMetamodel(Slusanje.class)
public class Slusanje_ { 

    public static volatile SingularAttribute<Slusanje, Date> datumVreme;
    public static volatile SingularAttribute<Slusanje, Integer> pocetakSekunde;
    public static volatile SingularAttribute<Slusanje, Integer> trajanjeSekundi;
    public static volatile SingularAttribute<Slusanje, AudioSnimak> idSnimak;
    public static volatile SingularAttribute<Slusanje, Integer> id;
    public static volatile SingularAttribute<Slusanje, Korisnik> idKorisnik;

}