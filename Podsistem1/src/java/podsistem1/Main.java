/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.Korisnik;
import entiteti.Mesto;
import java.io.Serializable;
import java.util.ArrayList;
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
    
    @Resource(lookup = "p1Server")
    private static Queue podsistem1Server;
    
    @Resource(lookup = "serverP1")
    private static Queue serverPodsistem1;
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static JMSContext context;
    private static Connection connection;
    
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("Podsistem1PU");
        em = emf.createEntityManager();
        context = myConnFactory.createContext();
        
        JMSConsumer consumer  = context.createConsumer(serverPodsistem1);
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
                    case 1: obj = createPlace(objMsg); break;
                    case 2: obj = createUser(objMsg); break;
                    case 3: obj = changeEmailForUser(objMsg); break;
                    case 4: obj = changePlaceForUser(objMsg); break;
                    case 18: obj = showAllPlaces(objMsg); break;
                    case 19: obj = showAllUsers(objMsg); break;
                }
                
                producer.send(podsistem1Server, obj);
                System.out.println("Poslata poruka ka serveru!\n");
            }
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            consumer.close();
        }
        
    }

    private static ObjectMessage createPlace(ObjectMessage objMsg) {
        ObjectMessage msg = context.createObjectMessage();
        try {
            String naziv = objMsg.getStringProperty("Naziv");
            Mesto mesto = new Mesto();
            mesto.setNaziv(naziv);
            
            em.getTransaction().begin();
            em.persist(mesto);
            em.getTransaction().commit();
            
            msg.setIntProperty("status", 1);
            System.out.println("Kreirano mesto!\n");
            return msg;
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                msg.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        return null;
    }

    private static ObjectMessage createUser(ObjectMessage objMsg) {
        ObjectMessage msg = context.createObjectMessage();
        try {
            String ime = objMsg.getStringProperty("Ime");
            String email = objMsg.getStringProperty("Email");
            int godiste = objMsg.getIntProperty("Godiste");
            String pol = objMsg.getStringProperty("Pol");
            String m = objMsg.getStringProperty("Mesto");
            
            em.getTransaction().begin();
            
            Mesto mesto = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", m).getSingleResult();
            
            
            if(mesto!=null){
                Korisnik korisnik = new Korisnik();
                korisnik.setIme(ime);
                korisnik.setGodiste(godiste);
                korisnik.setEmail(email);
                korisnik.setPol(pol.charAt(0));
                korisnik.setIdMesto(mesto);
                
                em.persist(korisnik);
                em.getTransaction().commit();
                msg.setIntProperty("status", 1);
                System.out.println("Kreiran korisnik\n");
            }else{
                em.getTransaction().rollback();
                msg.setIntProperty("status", 0);
            }
            
            return msg;
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                msg.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        return null;
    }

    private static ObjectMessage changeEmailForUser(ObjectMessage objMsg) {            
        ObjectMessage msg = context.createObjectMessage();
        try {
            String ime = objMsg.getStringProperty("Korisnik");
            String email = objMsg.getStringProperty("Email");
            
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", ime).getSingleResult();
            if(korisnik != null){
                korisnik.setEmail(email);
                em.getTransaction().commit();
                msg.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                msg.setIntProperty("status", 0);
            }
            return msg;
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                msg.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        return null;
    }

    private static ObjectMessage changePlaceForUser(ObjectMessage objMsg) {
        ObjectMessage msg = context.createObjectMessage();
        try {
            String ime = objMsg.getStringProperty("Korisnik");
            String m = objMsg.getStringProperty("Mesto");
            
            em.getTransaction().begin();
            Korisnik korisnik = em.createNamedQuery("Korisnik.findByIme", Korisnik.class).setParameter("ime", ime).getSingleResult();
            Mesto mesto = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", m).getSingleResult();
            
            if(korisnik != null && mesto != null){
                korisnik.setIdMesto(mesto);
                em.getTransaction().commit();
                msg.setIntProperty("status", 1);
            }else{
                em.getTransaction().rollback();
                msg.setIntProperty("status", 0);
            }
            return msg;
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                msg.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        return null;
    }

    private static ObjectMessage showAllPlaces(ObjectMessage objMsg) {
        ObjectMessage msg = context.createObjectMessage();
        try {
            List<Mesto> mesta = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
            List<Mesto> retMesta = new ArrayList<>();
            
            for(Mesto m : mesta){
                Mesto m1 = new Mesto();
                m1.setNaziv(m.getNaziv());
                m1.setId(m.getId());
                
                retMesta.add(m1);
            }
            
            msg.setObject((Serializable)retMesta);
            msg.setIntProperty("status", 1);
            return msg;
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                msg.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        return null;
    }

    private static ObjectMessage showAllUsers(ObjectMessage objMsg) {
        ObjectMessage msg = context.createObjectMessage();
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
            List<Korisnik> retKorisnici = new ArrayList<>();
            
            for(Korisnik k : korisnici){
                Korisnik k1 = new Korisnik();
                Mesto m = new Mesto();
                
                m.setId(k.getIdMesto().getId());
                
                k1.setIme(k.getIme());
                k1.setId(k.getId());
                k1.setEmail(k.getEmail());
                k1.setPol(k.getPol());
                k1.setGodiste(k.getGodiste());
                k1.setIdMesto(m);
                retKorisnici.add(k1);
            }
            
            msg.setObject((Serializable)retKorisnici);
            msg.setIntProperty("status", 1);
            return msg;
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            try {
                msg.setIntProperty("status", 0);
            } catch (JMSException ex1) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
        }
        return null;    
    }
    
}
