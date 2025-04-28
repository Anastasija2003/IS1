package entiteti;

import entiteti.Korisnici;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-17T14:49:37")
@StaticMetamodel(Mesta.class)
public class Mesta_ { 

    public static volatile SingularAttribute<Mesta, String> naziv;
    public static volatile SingularAttribute<Mesta, Integer> id;
    public static volatile ListAttribute<Mesta, Korisnici> korisniciList;

}