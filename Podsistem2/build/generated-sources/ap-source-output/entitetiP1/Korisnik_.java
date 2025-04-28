package entitetiP1;

import entiteti.AudioSnimak;
import entitetiP1.Mesto;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-29T15:30:20")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile ListAttribute<Korisnik, AudioSnimak> audioSnimakList;
    public static volatile SingularAttribute<Korisnik, Mesto> idMesto;
    public static volatile ListAttribute<Korisnik, AudioSnimak> audioSnimakList1;
    public static volatile SingularAttribute<Korisnik, Integer> godiste;
    public static volatile SingularAttribute<Korisnik, Integer> id;
    public static volatile SingularAttribute<Korisnik, Character> pol;
    public static volatile SingularAttribute<Korisnik, String> email;

}