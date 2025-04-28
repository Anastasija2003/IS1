package entiteti;

import entiteti.AudioSnimci;
import entiteti.Mesta;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-17T14:49:37")
@StaticMetamodel(Korisnici.class)
public class Korisnici_ { 

    public static volatile SingularAttribute<Korisnici, String> ime;
    public static volatile ListAttribute<Korisnici, AudioSnimci> audioSnimciList;
    public static volatile SingularAttribute<Korisnici, Mesta> idMesto;
    public static volatile ListAttribute<Korisnici, AudioSnimci> audioSnimciList1;
    public static volatile SingularAttribute<Korisnici, Integer> godiste;
    public static volatile SingularAttribute<Korisnici, Integer> id;
    public static volatile SingularAttribute<Korisnici, Character> pol;
    public static volatile SingularAttribute<Korisnici, String> email;

}