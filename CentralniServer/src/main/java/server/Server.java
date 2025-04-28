/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Queue;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tasa
 */
@Path("server")
public class Server {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    
    @Resource(lookup = "myConnFactory")
    private ConnectionFactory myConnFactory;
    
    @Resource(lookup = "p1Server")
    private Queue podsistem1Server;
    
    @Resource(lookup = "serverP1")
    private Queue serverPodsistem1;
   
    @Resource(lookup = "p2Server")
    private Queue podsistem2Server;
    
    @Resource(lookup = "serverP2")
    private Queue serverPodsistem2;
    
    
    @Resource(lookup = "p3Server")
    private Queue podsistem3Server;
    
    @Resource(lookup = "serverP3")
    private Queue serverPodsistem3;
    
    @POST
    @Path("q1/{naziv}")
    public Response createPlace(@PathParam("naziv") String naziv){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem1Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 1);
            objMsg.setStringProperty("Naziv", naziv);
            producer.send(serverPodsistem1, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity("Uspesno kreirano mesto!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    
    @POST
    @Path("q2/{ime}/{email}/{godiste}/{pol}/{mesto}")
    public Response createUser(@PathParam("ime") String ime,@PathParam("email") String email,@PathParam("godiste") int godiste,@PathParam("pol") String pol,@PathParam("mesto") String mesto){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem1Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 2);
            objMsg.setStringProperty("Ime", ime);
            objMsg.setStringProperty("Email", email);
            objMsg.setIntProperty("Godiste", godiste);
            objMsg.setStringProperty("Pol", pol);
            objMsg.setStringProperty("Mesto", mesto);
            producer.send(serverPodsistem1, objMsg);
            Message message = consumer.receive(1000);
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity("Uspesno kreiran korisnik!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    
    @POST
    @Path("q3/{ime}/{email}")
    public Response changeEmailForUser(@PathParam("ime") String ime, @PathParam("email") String email){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem1Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 3);
            objMsg.setStringProperty("Korisnik", ime);
            objMsg.setStringProperty("Email", email);
            producer.send(serverPodsistem1, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)                
                return Response.status(Response.Status.OK).entity("Uspesno promenjena email adresa!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q4/{ime}/{mesto}")
    public Response changePlaceForUser(@PathParam("ime") String ime, @PathParam("mesto") String mesto){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem1Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 4);
            objMsg.setStringProperty("Korisnik", ime);
            objMsg.setStringProperty("Mesto", mesto);
            producer.send(serverPodsistem1, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity("Uspesno promenjena email adresa!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q5/{naziv}")
    public Response createCategory(@PathParam("naziv") String naziv){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 5);
            objMsg.setStringProperty("Naziv", naziv);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno kreirana kategorija!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q6/{naziv}/{trajanje}/{korisnik}/{datum}")
    public Response createAudio(@PathParam("naziv") String naziv,@PathParam("trajanje") int trajanje,@PathParam("korisnik") String korisnik,@PathParam("datum") String datum){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 6);
            objMsg.setStringProperty("Naziv", naziv);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setIntProperty("Trajanje", trajanje);
            objMsg.setStringProperty("Datum", datum);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno kreiran audio snimak!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q7/{naziv}/{novi}")
    public Response changeNameForAudio(@PathParam("naziv") String naziv,@PathParam("novi") String novi){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 7);
            objMsg.setStringProperty("Naziv", naziv);
            objMsg.setStringProperty("NoviNaziv", novi);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno promenjen naziv audio snimka!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q8/{audio}/{kategorija}")
    public Response addCategoryToAudio(@PathParam("audio") String audio, @PathParam("kategorija") String kategorija){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 8);
            objMsg.setStringProperty("Kategorija", kategorija);
            objMsg.setStringProperty("Audio", audio);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno dodata kategorija audio snimku!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q9/{naziv}/{cena}")
    public Response createPackage(@PathParam("naziv") String naziv,@PathParam("cena") Double cena){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 9);
            objMsg.setStringProperty("Naziv", naziv);
            objMsg.setDoubleProperty("Cena", cena);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno kreiran paket!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q10/{naziv}/{cena}")
    public Response changePrice(@PathParam("naziv") String naziv,@PathParam("cena") Double cena){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 10);
            objMsg.setStringProperty("Naziv", naziv);
            objMsg.setDoubleProperty("Cena", cena);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno promenjena cena!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q11/{korisnik}/{paket}/{cena}/{datum}")
    public Response createSubscription(@PathParam("korisnik") String korisnik,@PathParam("paket") String paket,@PathParam("cena") double cena,@PathParam("datum") String datum){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 11);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setStringProperty("Paket", paket);
            objMsg.setDoubleProperty("Cena", cena);
            objMsg.setStringProperty("Datum", datum);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno kreirana pretplata!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q12/{korisnik}/{audio}/{datum}/{pocetak}/{odslusano}")
    public Response createListening(@PathParam("korisnik") String korisnik,@PathParam("audio") String audio,@PathParam("datum") String datum,@PathParam("pocetak") int pocetak,@PathParam("odslusano") int odslusano){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 12);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setStringProperty("Audio", audio);
            objMsg.setIntProperty("Pocetak", pocetak);
            objMsg.setIntProperty("Odslusano", odslusano);
            objMsg.setStringProperty("Datum", datum);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno kreirano slusanje!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q13/{korisnik}/{audio}")
    public Response addToFavorites(@PathParam("korisnik") String korisnik,@PathParam("audio") String audio){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 13);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setStringProperty("Audio", audio);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno dodat audio snimak u omiljene snimke!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q14/{korisnik}/{audio}/{datum}/{ocena}")
    public Response createGrade(@PathParam("korisnik") String korisnik,@PathParam("audio") String audio,@PathParam("datum") String datum,@PathParam("ocena") int ocena){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 14);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setStringProperty("Audio", audio);
            objMsg.setStringProperty("Datum", datum);
            objMsg.setIntProperty("Ocena", ocena);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno kreirana ocena!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q15/{korisnik}/{audio}/{ocena}")
    public Response changeGrade(@PathParam("korisnik") String korisnik, @PathParam("audio") String audio, @PathParam("ocena") int ocena){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 15);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setStringProperty("Audio", audio);
            objMsg.setIntProperty("Ocena", ocena);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno promenjena ocena!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @POST
    @Path("q16/{korisnik}/{audio}")
    public Response deleteGrade(@PathParam("korisnik") String korisnik,@PathParam("audio") String audio){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 16);
            objMsg.setStringProperty("Korisnik", korisnik);
            objMsg.setStringProperty("Audio", audio);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno izbrisana ocena!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    @POST
    @Path("q17/{naziv}/{korisnik}")
    public Response deleteAudio(@PathParam("naziv") String naziv, @PathParam("korisnik") String korisnik){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try {
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 17);
            objMsg.setStringProperty("Naziv", naziv);
            objMsg.setStringProperty("Korisnik", korisnik);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity("Uspesno izbrisan audio snimak!").build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q18")
    public Response showAllPlaces(){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem1Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 18);
            producer.send(serverPodsistem1, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                    return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q19")
    public Response showAllUsers(){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem1Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 19);
            producer.send(serverPodsistem1, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q20")
    public Response showAllCategories(){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 20);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q21")
    public Response showAllAudio(){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 21);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q22/{naziv}")
    public Response showAllCategoryForAudio(@PathParam("naziv") String naziv){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem2Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 22);
            objMsg.setStringProperty("Naziv", naziv);
            producer.send(serverPodsistem2, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q23")
    public Response showAllPackage(){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 23);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q24/{korisnik}")
    public Response showAllSubscriptionForUser(@PathParam("korisnik") String korisnik){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 24);
            objMsg.setStringProperty("Korisnik", korisnik);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q25/{korisnik}")
    public Response showAllListeningForUser(@PathParam("korisnik") String korisnik){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 25);
            objMsg.setStringProperty("Korisnik", korisnik);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q26/{audio}")
    public Response showAllGradesForAudio(@PathParam("audio") String audio){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 26);
            objMsg.setStringProperty("Audio", audio);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
    
    @GET
    @Path("q27/{korisnik}")
    public Response showAllFavoritesForUser(@PathParam("korisnik") String korisnik){
        JMSContext context = myConnFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(podsistem3Server);
        try{
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("RedniBroj", 27);
            objMsg.setStringProperty("Korisnik", korisnik);
            producer.send(serverPodsistem3, objMsg);
            Message message = consumer.receive();
            System.out.println("Stigla poruka\n");
            if(message instanceof ObjectMessage && ((ObjectMessage)message).getIntProperty("status") == 1)
                return Response.status(Response.Status.OK).entity(((ObjectMessage)message).getObject()).build();
        } catch (JMSException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            consumer.close();
        }
        
        return Response.serverError().build();
    }
}
