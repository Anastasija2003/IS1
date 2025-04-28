/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.AudioSnimak;
import entiteti.Korisnik;
import entiteti.Kategorija;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    
    @Resource(lookup = "p2Server")
    private static Queue podsistem2Server;
    
    @Resource(lookup = "serverP2")
    private static Queue serverPodsistem2;
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static JMSContext context;
    private static Connection connection;
    
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("Podsistem2PU");
        em = emf.createEntityManager();
        context = myConnFactory.createContext();
        
        
        JMSConsumer consumer  = context.createConsumer(serverPodsistem2);
        JMSProducer producer = context.createProducer();
        
        try {
            connection = myConnFactory.createConnection();
            connection.start();
            
            while(true){
                Message msg = consumer.receive();
                if(!(msg instanceof ObjectMessage)) continue;
                ObjectMessage objMsg = (ObjectMessage) msg;
                ObjectMessage obj = null;
                switch(objMsg.getIntProperty("RedniBroj")){
                    case 5: obj = createCategory(objMsg); break;
                    case 6: obj = createAudio(objMsg); break;
                    case 7: obj = changeNameForAudio(objMsg); break;
                    case 8: obj = addCategoryToAudio(objMsg); break;
                    case 17: obj =  deleteAudio(objMsg); break;
                    case 20: obj = showAllCategories(objMsg); break;
                    case 21: obj = showAllAudio(objMsg); break;
                    case 22: obj = showAllCategoryForAudio(objMsg); break;
                }
                
                producer.send(podsistem2Server, obj);
                System.out.println("Poslata poruka ka serveru!\n");
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            consumer.close();
        }
        
    }

    private static ObjectMessage createCategory(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            Kategorija kategorija = new Kategorija();
            
            kategorija.setNaziv(naziv);
            em.getTransaction().begin();
            em.persist(kategorija);
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

    private static ObjectMessage createAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            int trajanje = objMsg.getIntProperty("Trajanje");
            String korisnik = objMsg.getStringProperty("Korisnik");
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
            Korisnik k = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", korisnik).getSingleResult();
            
            
            if(k != null){
                AudioSnimak audioSnimak = new AudioSnimak();
                audioSnimak.setDatumPostavljanja(datum);
                audioSnimak.setNaziv(naziv);
                audioSnimak.setTrajanje(trajanje);
                audioSnimak.setIdVlasnik(k);
                em.persist(audioSnimak);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                message.setIntProperty("status", 0);
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            try {
                message.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;
    }

    private static ObjectMessage changeNameForAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            String novi = objMsg.getStringProperty("NoviNaziv");
            
            em.getTransaction().begin();
            AudioSnimak snimak = em.createNamedQuery("AudioSnimak.findByNaziv",AudioSnimak.class).setParameter("naziv", naziv).getSingleResult();
            
            if(snimak != null){
                snimak.setNaziv(novi);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                message.setIntProperty("status", 0);
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            try {
                message.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;
    }

    private static ObjectMessage addCategoryToAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String kategorija = objMsg.getStringProperty("Kategorija");
            String audio = objMsg.getStringProperty("Audio");
            
            em.getTransaction().begin();
            AudioSnimak audioSnimak = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", audio).getSingleResult();
            Kategorija kat = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).setParameter("naziv", kategorija).getSingleResult();
            
            if(audioSnimak != null && kat != null){
                List<Kategorija> kategorijeList = audioSnimak.getKategorijaList();
                Kategorija kat1 = new Kategorija();
                kat1.setId(kat.getId());
                kat1.setNaziv(kat.getNaziv());
                kategorijeList.add(kat1);
                audioSnimak.setKategorijaList(kategorijeList);
                
                List<AudioSnimak> audioSnimciList = kat.getAudioSnimakList();
                AudioSnimak a1 = new AudioSnimak();
                a1.setId(audioSnimak.getId());
                a1.setNaziv(audioSnimak.getNaziv());
                Korisnik vlasnik = new Korisnik();
                vlasnik.setId(audioSnimak.getIdVlasnik().getId());
                vlasnik.setIme(audioSnimak.getIdVlasnik().getIme());
                vlasnik.setEmail(audioSnimak.getIdVlasnik().getEmail());
                vlasnik.setPol(audioSnimak.getIdVlasnik().getPol());
                a1.setIdVlasnik(vlasnik);
                a1.setDatumPostavljanja(audioSnimak.getDatumPostavljanja());
                a1.setTrajanje(audioSnimak.getTrajanje());
                audioSnimciList.add(a1);
                kat.setAudioSnimakList(audioSnimciList);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                message.setIntProperty("status", 0);
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            try {
                message.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return message;
    }

    private static ObjectMessage deleteAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            String ime = objMsg.getStringProperty("Korisnik");
            
            em.getTransaction().begin();
            AudioSnimak audioSnimak = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", naziv).getSingleResult();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme",Korisnik.class).setParameter("ime", ime).getSingleResult();
            
            if(korisnik != null && audioSnimak != null && audioSnimak.getIdVlasnik().getId().equals(korisnik.getId())){
                
                em.remove(audioSnimak);
                em.getTransaction().commit();
                message.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                message.setIntProperty("status", 0);
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            try {
                message.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }        
        }
        return message;
    }

    private static ObjectMessage showAllCategories(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            List<Kategorija> kategorije = em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
            List<Kategorija> retKategorije = new ArrayList<>();
            
            for(Kategorija k : kategorije){
                Kategorija k1 = new Kategorija();
                
                k1.setId(k.getId());
                k1.setNaziv(k.getNaziv());
                
                retKategorije.add(k1);
            }
            
            message.setObject((Serializable)retKategorije);
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

    private static ObjectMessage showAllAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            List<AudioSnimak> audioSnimci = em.createNamedQuery("AudioSnimak.findAll", AudioSnimak.class).getResultList();
            List<AudioSnimak> retAudioSnimci = new ArrayList<>();
            
            for(AudioSnimak a: audioSnimci){
                AudioSnimak a1 = new AudioSnimak();
                
                a1.setId(a.getId());
                a1.setNaziv(a.getNaziv());
                Korisnik vlasnik = new Korisnik();
                vlasnik.setId(a.getIdVlasnik().getId());
                vlasnik.setIme(a.getIdVlasnik().getIme());
                vlasnik.setEmail(a.getIdVlasnik().getEmail());
                vlasnik.setPol(a.getIdVlasnik().getPol());
                a1.setIdVlasnik(vlasnik);
                a1.setDatumPostavljanja(a.getDatumPostavljanja());
                a1.setTrajanje(a.getTrajanje());
                
                retAudioSnimci.add(a1);
            }
            
            message.setObject((Serializable)retAudioSnimci);
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

    private static ObjectMessage showAllCategoryForAudio(ObjectMessage objMsg) {
        ObjectMessage message = context.createObjectMessage();
        try {
            String audio = objMsg.getStringProperty("Naziv");
            AudioSnimak audioSnimak = em.createNamedQuery("AudioSnimak.findByNaziv", AudioSnimak.class).setParameter("naziv", audio).getSingleResult();
            if(audioSnimak == null){
                message.setIntProperty("status", 0);
                return message;
            }
            List<Kategorija> kategorije = audioSnimak.getKategorijaList();
            List<Kategorija> retKategorije = new ArrayList<>();
            
            for(Kategorija k : kategorije){
                Kategorija k1 = new Kategorija();
                
                k1.setId(k.getId());
                k1.setNaziv(k.getNaziv());
                
                retKategorije.add(k1);
            }
            
            message.setObject((Serializable)retKategorije);
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

