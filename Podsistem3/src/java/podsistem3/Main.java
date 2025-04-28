/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.AudioSnimak;
import entiteti.Korisnik;
import entiteti.Ocena;
import entiteti.Paket;
import entiteti.Pretplata;
import entiteti.Slusanje;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Tasa
 */
public class Main {

    @Resource(lookup = "myConnFactory")
    private static ConnectionFactory myConnFactory;
    
    @Resource(lookup = "p3Server")
    private static Queue podsistem3Server;
    
    @Resource(lookup = "serverP3")
    private static Queue serverPodsistem3;
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static JMSContext context;
    private static Connection connection;
    
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("Podsistem3PU");
        em = emf.createEntityManager();
        context = myConnFactory.createContext();
        
        
        JMSConsumer consumer  = context.createConsumer(serverPodsistem3);
        JMSProducer producer = context.createProducer();
        
        try {
            connection = myConnFactory.createConnection();
            connection.start();
            System.out.println("Pocelo!\n");
            while(true){
                Message msg = consumer.receive();
                if(!(msg instanceof ObjectMessage)) continue;
                ObjectMessage objMsg = (ObjectMessage) msg;
                ObjectMessage obj = null;
                switch(objMsg.getIntProperty("RedniBroj")){
                    case 9: obj = createPackage(objMsg); break;
                    case 10: obj = changePrice(objMsg); break;
                    case 11: obj = createSubscription(objMsg); break;
                    case 12: obj = createListening(objMsg); break;
                    case 13: obj = addToFavorites(objMsg); break;
                    case 14: obj = createGrade(objMsg); break;
                    case 15: obj = changeGrade(objMsg); break;
                    case 16: obj = deleteGrade(objMsg); break;
                    case 23: obj = showAllPackage(objMsg); break;
                    case 24: obj = showAllSubscriptionForUser(objMsg); break;
                    case 25: obj = showAllListeningForUser(objMsg); break;
                    case 26: obj = showAllGradesForAudio(objMsg); break;
                    case 27: obj = showAllFavoritesForUser(objMsg); break;
                }
                
                producer.send(podsistem3Server, obj);
                System.out.println("Poslata poruka ka serveru!\n");
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            consumer.close();
        }
        
    }

    private static ObjectMessage createPackage(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            BigDecimal cena = BigDecimal.valueOf(objMsg.getDoubleProperty("Cena"));
            
            Paket paket = new Paket();
            paket.setCena(cena);
            paket.setNaziv(naziv);
            em.getTransaction().begin();
            em.persist(paket);
            em.getTransaction().commit();
            message.setIntProperty("status", 1);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage changePrice(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            BigDecimal cena = BigDecimal.valueOf(objMsg.getDoubleProperty("Cena"));
            
            em.getTransaction().begin();
            Paket paket = em.createNamedQuery("Paket.findByNaziv", Paket.class).setParameter("naziv", naziv).getSingleResult();
            if(paket == null){
                message.setIntProperty("status", 0);
                em.getTransaction().rollback();
                return message;
            }
            paket.setCena(cena);
            em.getTransaction().commit();
            message.setIntProperty("status", 1);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage createSubscription(ObjectMessage objMsg) {ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Korisnik");
            String p = objMsg.getStringProperty("Paket");
            BigDecimal cena = BigDecimal.valueOf(objMsg.getDoubleProperty("Cena"));
            String d = objMsg.getStringProperty("Datum");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date datum = null;
            try {
                datum = format.parse(d);
            } catch (ParseException ex) {
                message.setIntProperty("status", 0);
                return message;
            }
            
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", naziv).getSingleResult();
            Paket paket = em.createNamedQuery("Paket.findByNaziv", Paket.class).setParameter("naziv", p).getSingleResult();
            if(korisnik != null && paket != null){
                List<Pretplata> pretplate = em.createNamedQuery("Pretplata.findAll", Pretplata.class).getResultList();
                for(Pretplata pretplata : pretplate){
                    if(pretplata.getIdKorisnik().getId() == korisnik.getId()){
                        Date datum1 = pretplata.getDatumPocetka();
                        if(datum1.after(datum)){
                            Date temp = datum1;
                            datum1 = datum;
                            datum = temp;
                        }
                        
                        Calendar kalendar = Calendar.getInstance();
                        kalendar.setTime(datum1);
                        kalendar.add(Calendar.MONTH,1);
                        
                        if(datum.before(kalendar.getTime())){
                            em.getTransaction().rollback();
                            message.setIntProperty("status", 0);
                            return message;
                        }
                    }
                }
                
                Pretplata pretplata = new Pretplata();
                pretplata.setCena(cena);
                pretplata.setDatumPocetka(datum);
                
                Korisnik k = new Korisnik();
                k.setId(korisnik.getId());
                k.setEmail(korisnik.getEmail());
                k.setGodiste(korisnik.getGodiste());
                k.setIme(korisnik.getIme());
                k.setPol(korisnik.getPol());
                
                pretplata.setIdKorisnik(k);
                
                Paket paket1 = new Paket();
                paket1.setId(paket.getId());
                paket1.setCena(paket.getCena());
                paket1.setNaziv(paket.getNaziv());
                
                pretplata.setIdPaket(paket1);
                
                em.persist(pretplata);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                message.setIntProperty("status", 0);
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage createListening(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Korisnik");
            String a = objMsg.getStringProperty("Audio");
            int pocetak = objMsg.getIntProperty("Pocetak");
            int odslusano = objMsg.getIntProperty("Odslusano");
            String d = objMsg.getStringProperty("Datum");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date datum = null;
            try {
                datum = format.parse(d);
            } catch (ParseException ex) {
                message.setIntProperty("status", 0);
                return message;
            }
            
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", naziv).getSingleResult();
            AudioSnimak audio = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", a).getSingleResult();
            if(korisnik != null && audio != null){
                Slusanje slusanje = new Slusanje();
                slusanje.setDatumVreme(datum);
                slusanje.setPocetakSekunde(pocetak);
                slusanje.setTrajanjeSekundi(odslusano);
                slusanje.setIdKorisnik(korisnik);
                slusanje.setIdSnimak(audio);
                em.persist(slusanje);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                message.setIntProperty("status", 0);
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage addToFavorites(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String k = objMsg.getStringProperty("Korisnik");
            String a = objMsg.getStringProperty("Audio");
            
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            AudioSnimak audio = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", a).getSingleResult();
            
            if(korisnik != null && audio != null){
                List<AudioSnimak> omiljeni = korisnik.getAudioSnimakList1();
                
                AudioSnimak audio1 = new AudioSnimak();
                audio1.setDatumPostavljanja(audio.getDatumPostavljanja());
                audio1.setId(audio.getId());
                audio1.setNaziv(audio.getNaziv());
                audio1.setTrajanje(audio.getTrajanje());
                
                omiljeni.add(audio1);
                korisnik.setAudioSnimakList1(omiljeni);
                message.setIntProperty("status", 1);
                em.getTransaction().commit();
            }else{
                message.setIntProperty("status", 0);
                em.getTransaction().rollback();
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage createGrade(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String k = objMsg.getStringProperty("Korisnik");
            String a = objMsg.getStringProperty("Audio");
            String d = objMsg.getStringProperty("Datum");
            int o = objMsg.getIntProperty("Ocena");
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date datum = null;
            try {
                datum = format.parse(d);
            } catch (ParseException ex) {
                message.setIntProperty("status", 0);
                return message;
            }
            
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            AudioSnimak audio = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", a).getSingleResult();
            
            if(korisnik != null && audio != null){
                Ocena ocena = new Ocena();
                ocena.setOcena(o);
                ocena.setIdKorisnik(korisnik);
                ocena.setIdSnimak(audio);
                ocena.setDatumVreme(datum);
                em.persist(ocena);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                message.setIntProperty("status", 0);
                em.getTransaction().rollback();
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage changeGrade(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String k = objMsg.getStringProperty("Korisnik");
            String a = objMsg.getStringProperty("Audio");
            int o = objMsg.getIntProperty("Ocena");
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            AudioSnimak audio = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", a).getSingleResult();
            if(korisnik != null && audio != null){
                List<Ocena> ocene = em.createNamedQuery("Ocena.findAll", Ocena.class).getResultList();
                for(Ocena ocena: ocene){
                    if(ocena.getIdKorisnik().getId() == korisnik.getId() && ocena.getIdSnimak().getId() == audio.getId()){
                        ocena.setOcena(o);
                        message.setIntProperty("status", 1);
                        em.getTransaction().commit();
                        return message;
                    }
                }
            }
            message.setIntProperty("status", 0);
            em.getTransaction().rollback();
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;
    }

    private static ObjectMessage deleteGrade(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String k = objMsg.getStringProperty("Korisnik");
            String a = objMsg.getStringProperty("Audio");
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            AudioSnimak audio = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", a).getSingleResult();
            if(korisnik != null && audio != null){
                List<Ocena> ocene = em.createNamedQuery("Ocena.findAll", Ocena.class).getResultList();
                for(Ocena ocena: ocene){
                    if(ocena.getIdKorisnik().getId() == korisnik.getId() && ocena.getIdSnimak().getId() == audio.getId()){
                        em.remove(ocena);
                        message.setIntProperty("status", 1);
                        em.getTransaction().commit();
                        return message;
                    }
                }
            }
            message.setIntProperty("status", 0);
            em.getTransaction().rollback();
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            em.getTransaction().rollback();
        }
        return message;    
    }

    private static ObjectMessage showAllPackage(ObjectMessage objMsg) {
       ObjectMessage message = context.createObjectMessage();
        try
        {
            List<Paket> paketList = em.createNamedQuery("Paket.findAll", Paket.class).getResultList();
            List<Paket> paketListReturn = new ArrayList<>();
            
            paketList.forEach(paket -> 
            {
                Paket p = new Paket();
                p.setId(paket.getId());
                p.setNaziv(paket.getNaziv());
                p.setCena(paket.getCena());
                
                paketListReturn.add(p);
            });
            
            message.setObject((Serializable) paketListReturn);
            message.setIntProperty("status", 1);

        } catch (JMSException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status", 0);
            } catch (JMSException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return message;   
    }

    private static ObjectMessage showAllSubscriptionForUser(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            List<Pretplata> pretplate = em.createNamedQuery("Pretplata.findAll", Pretplata.class).getResultList();
            List<Pretplata> retPretplata = new ArrayList<>();
            String k = objMsg.getStringProperty("Korisnik");
            
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            if(korisnik == null){
                message.setIntProperty("status",0);
                return message;
            }
            for(Pretplata p : pretplate){
                if(korisnik.getId() == p.getIdKorisnik().getId()){
                    Pretplata p1 = new Pretplata();
                    p1.setCena(p.getCena());
                    p1.setDatumPocetka(p.getDatumPocetka());
                    p1.setId(p.getId());
                    
                    retPretplata.add(p1);
                }
                
            }
            
            message.setObject((Serializable)retPretplata);
            message.setIntProperty("status", 1);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;    
    }

    private static ObjectMessage showAllListeningForUser(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            List<Slusanje> slusanja = em.createNamedQuery("Slusanje.findAll", Slusanje.class).getResultList();
            List<Slusanje> retSlusanja = new ArrayList<>();
            String k = objMsg.getStringProperty("Korisnik");
            
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            if(korisnik == null){
                message.setIntProperty("status",0);
                return message;
            }
            for(Slusanje s : slusanja){
                if(korisnik.getId() == s.getIdKorisnik().getId()){
                    Slusanje s1 = new Slusanje();
                    s1.setDatumVreme(s.getDatumVreme());
                    s1.setId(s.getId());
                    s1.setPocetakSekunde(s.getPocetakSekunde());
                    s1.setTrajanjeSekundi(s.getTrajanjeSekundi());
                    
                    AudioSnimak a = new AudioSnimak();
                    a.setId(s.getIdSnimak().getId());
                    a.setNaziv(s.getIdSnimak().getNaziv());
                    s1.setIdSnimak(a);
                    
                    retSlusanja.add(s1);
                }
                
            }
            
            message.setObject((Serializable)retSlusanja);
            message.setIntProperty("status", 1);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;  
    }

    private static ObjectMessage showAllGradesForAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
           String a = objMsg.getStringProperty("Audio");
           AudioSnimak audio = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", a).getSingleResult();
           if(audio != null){
               List<Ocena> ocene = em.createNamedQuery("Ocena.findAll", Ocena.class).getResultList();
               List<Ocena> retOcene = new ArrayList<>();
               
               for(Ocena o : ocene){
                   if(o.getIdSnimak().getId() == audio.getId()){
                       Ocena o1 = new Ocena();
                       o1.setId(o.getId());
                       
                       retOcene.add(o1);
                   }
               }
               
               message.setObject((Serializable)retOcene);
               message.setIntProperty("status", 1);
               return message;
           }
           message.setIntProperty("status", 0);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;     
    }

    private static ObjectMessage showAllFavoritesForUser(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String k = objMsg.getStringProperty("Korisnik");
            
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", k).getSingleResult();
            if(korisnik == null){
                message.setIntProperty("status",0);
                return message;
            }
            List<AudioSnimak> snimci = korisnik.getAudioSnimakList1();
            List<AudioSnimak> retSnimci = new ArrayList<>();
            for(AudioSnimak s: snimci){
                AudioSnimak s1 = new AudioSnimak();
                
                s1.setId(s.getId());
                s1.setNaziv(s.getNaziv());
                s1.setTrajanje(s.getTrajanje());
                s1.setDatumPostavljanja(s.getDatumPostavljanja());
                
                retSnimci.add(s1);
            }
            message.setObject((Serializable)retSnimci);
            message.setIntProperty("status", 1);
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                message.setIntProperty("status",0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;  
    }
    
}
