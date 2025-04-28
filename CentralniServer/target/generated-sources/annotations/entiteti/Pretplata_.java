package entiteti;

import entiteti.Korisnik;
import entiteti.Paket;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-01-25T16:18:33")
@StaticMetamodel(Pretplata.class)
public class Pretplata_ { 

    public static volatile SingularAttribute<Pretplata, Date> datumPocetka;
    public static volatile SingularAttribute<Pretplata, Paket> idPaket;
    public static volatile SingularAttribute<Pretplata, Integer> id;
    public static volatile SingularAttribute<Pretplata, BigDecimal> cena;
    public static volatile SingularAttribute<Pretplata, Korisnik> idKorisnik;

}