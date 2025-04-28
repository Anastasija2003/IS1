/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klijent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Tasa
 */
public class Klijent {

    public static Scanner scanner;
            
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        while(true){
            printOptions();
            int option = getOption();
            performOption(option);
        }
    }

    private static void printOptions() {
        System.out.println("Izaberite jednu opciju:");
        System.out.println(""
                + "1.Kreiranje grada\n"
                + "2.Kreiranje korisnika\n"
                + "3.Promena email adrese za korisnika\n"
                + "4.Promena mesta za korisnika\n"
                + "5.Kreiranje kategorije\n"
                + "6.Kreiranje audio snimka\n"
                + "7.Promena naziva audio snimka\n"
                + "8.Dodavanje kategorije audio snimku\n"
                + "9.Kreiranje paketa\n"
                + "10.Promena mesecne cene paketa\n"
                + "11.Kreiranje pretplate korisnika na paket\n"
                + "12.Kreiranje slusanja audio snimka od strane korisnika\n"
                + "13.Dodavanje audio snimka u omiljenje od strane korisnika\n"
                + "14.Kreiranje ocene korisnika za audio snimak\n"
                + "15.Menjanje ocene korisnika za audio snimak\n"
                + "16.Brisanje ocene za audio snimak\n"
                + "17.Brisanje audio snimka od strane korisnika koji ga je kreirao\n"
                + "18.Dohvatanje svih mesta\n"
                + "19.Dohvatanje svih korisnika\n"
                + "20.Dohvatanje svih kategorija\n"
                + "21.Dohvatanje svih audio snimaka\n"
                + "22.Dohvatanje kategorija za odredjeni audio snimak\n"
                + "23.Dohvatanje svih paketa\n"
                + "24.Dohvatanje svih pretplata za korisnika\n"
                + "25.Dohvatanje svih slusanja za audio snimak\n"
                + "26.Dohvatanje svih ocena za audio snimak\n"
                + "27.Dohvatanje liste omiljenih audio snimaka za korisnika\n");
    }

    private static int getOption() {
        int option = scanner.nextInt();
        scanner.nextLine();
        
        if(option == -1) return -1;
        
        if(option<1 || option>27){
            System.err.println("Morate izabrati broj koji oznacava funkcionalnost!");
        }
        
        return option;
    }

    private static void performOption(int option) {
        switch (option) {
            case 1:
                createPlace();
                break;
            case 2:
                createUser();
                break;
            case 3:
                changeEmailForUser();
                break;
            case 4:
                changePlaceForUser();
                break;
            case 5:
                createCategory();
                break;
            case 6:
                createAudio();
                break;
            case 7:
                changeNameForAudio();
                break;
            case 8:
                addCategoryToAudio();
                break;
            case 9:
                createPackage();
                break;
            case 10:
                changePrice();
                break;
            case 11:
                createSubscription();
                break;
            case 12:
                createListening();
                break;
            case 13:
                addToFavorites();
                break;
            case 14:
                createGrade();
                break;
            case 15:
                changeGrade();
                break;
            case 16:
                deleteGrade();
                break;
            case 17:
                deleteAudio();
                break;
            case 18:
                showAllPlaces();
                break;
            case 19:
                showAllUsers();
                break;
            case 20:
                showAllCategories();
                break;
            case 21:
                showAllAudio();
                break;
            case 22:
                showAllCategoryForAudio();
                break;
            case 23:
                showAllPackage();
                break;
            case 24:
                showAllSubscriptionForUser();
                break;
            case 25:
                showAllListeningForUser();
                break;
            case 26:
                showAllGradesForAudio();
                break;
            case 27:
                showAllFavoritesForUser();
                break;
        }
    }

    private static void createPlace() {
        System.out.println("Zapoceto kreiranje mesta!\n");
        System.out.println("Unesite naziv mesta: ");
        String naziv = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q1/" + naziv;
        sendHttpRequest(url,"POST");
    }

    private static void createUser() {
        System.out.println("Pocelo kreiranje korisnika.\n");
        System.out.println("Unesite ime: ");
        String ime = scanner.nextLine();
        
        System.out.println("Unesite email adresu: ");
        String email = scanner.nextLine();
        
        System.out.println("Unesite godiste: ");
        int godiste = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Unesite pol: ");
        String pol = scanner.nextLine();
        
        System.out.println("Unesite mesto iz kojeg dolazi: ");
        String mesto = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q2/" + ime + "/" + email + "/" + godiste + "/" + pol + "/" + mesto;
        sendHttpRequest(url,"POST");
    }

    private static void changeEmailForUser() {
        System.out.println("Zapoceta promena emaila korisnika.\n");
        System.out.println("Unesite korisnicko ime: ");
        String ime = scanner.nextLine();
        
        System.out.println("Unesite novu email adresu: ");
        String email = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q3/" + ime + "/" + email;
        sendHttpRequest(url,"POST");
        
    }

    private static void changePlaceForUser() {
        System.out.println("Zapoceta promena mesta korisnika.\n");
        System.out.println("Unesite korisnicko ime: ");
        String ime = scanner.nextLine();
        
        System.out.println("Unesite novo mesto: ");
        String mesto = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q4/" + ime + "/" + mesto;
        sendHttpRequest(url,"POST");
        
    }

    private static void showAllPlaces() {
        String url = "http://localhost:8080/CentralniServer/api/server/q18";
        sendHttpRequest(url,"GET");
    }

    private static void showAllUsers() {
        String url = "http://localhost:8080/CentralniServer/api/server/q19";
        sendHttpRequest(url,"GET");
    }

    private static void sendHttpRequest(String urlString, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);

            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                String[] lista = response.toString().split("},\\{");
                
                for(String s : lista){
                    s = s.replaceAll("^\\{","").replaceAll("\\}$", "");
                    System.out.println(s + "\n");
                }
            }
        } catch (IOException ex) {
                Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void createCategory() {
        System.out.println("Pocelo kreiranje kategorije!\n");
        System.out.println("Unesite naziv: \n");
        String naziv = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q5/" + naziv;
        sendHttpRequest(url,"POST");
    }

    private static void createAudio() {
        System.out.println("Pocelo kreiranje audio snimka!\n");
        System.out.println("Unesite naziv: \n");
        String naziv = scanner.nextLine();
        
        System.out.println("Unesite trajanje u sekundama: \n");
        int trajanje = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        
        System.out.println("Unesite datum u formatu yyyy-MM-dd: \n");
        String datum = scanner.nextLine();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = format.parse(datum);
        } catch (ParseException ex) {
            System.out.println("Datum nije u dobrom formatu!\n");
            return;
        }
       
        String url = "http://localhost:8080/CentralniServer/api/server/q6/" + naziv + "/" + trajanje + "/" + korisnik + "/" + datum;
        sendHttpRequest(url,"POST");
    }

    private static void changeNameForAudio() {
        System.out.println("Zapoceto azuriranje naslova audio snimka!\n");
        System.out.println("Unesite naziv audio snimka:\n");
        String naziv = scanner.nextLine();
        
        
        System.out.println("Unesite novi naziv audio snimka:\n");
        String novi = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q7/" + naziv + "/" + novi;
        sendHttpRequest(url, "POST");
    }

    private static void addCategoryToAudio() {
        System.out.println("Zapoceto dodavanje kategorije audio snimku!\n");
        System.out.println("Unesite naziv audio snimka:\n");
        String audio = scanner.nextLine();
        
        System.out.println("Unesite naziv kategorije: \n");
        String kategorija = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q8/" + audio + "/" + kategorija;
        sendHttpRequest(url, "POST");
    }

    private static void deleteAudio() {
        System.out.println("Zapoceto brisanje audio snimka!\n");
        System.out.println("Unesite naziv audio snimka: \n");
        String naziv = scanner.nextLine();
        
        System.out.println("Unesite naziv korisnika: \n");
        String korisnik = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q17/" + naziv + "/" + korisnik;
        sendHttpRequest(url,"POST");
    }

    private static void showAllCategories() {
        String url = "http://localhost:8080/CentralniServer/api/server/q20";
        sendHttpRequest(url,"GET");    
    }

    private static void showAllAudio() {
        String url = "http://localhost:8080/CentralniServer/api/server/q21";
        sendHttpRequest(url,"GET");
    }

    private static void showAllCategoryForAudio() {
        System.out.println("Unesite naziv audio snimka: \n");
        String naziv = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q22/" + naziv;
        sendHttpRequest(url,"GET");
    }

    private static void createPackage() {
        System.out.println("Zapoceto kreiranje paketa!\n");
        System.out.println("Unesite naziv paketa: \n");
        String naziv = scanner.nextLine();
        System.out.println("Unesite cenu paketa: \n");
        Double cena = scanner.nextDouble();
        scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q9/" + naziv + "/" +  cena;
        sendHttpRequest(url,"POST");
    }

    private static void changePrice() {
        System.out.println("Zapoceta promena cena paketa!\n");
        System.out.println("Unesite naziv paketa: \n");
        String naziv = scanner.nextLine();
        System.out.println("Unesite novu cenu: \n");
        Double cena = scanner.nextDouble();
        scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q10/" + naziv + "/" + cena;
        sendHttpRequest(url,"POST");
    }

    private static void createSubscription() { 
        System.out.println("Zapoceto kreiranje pretplate!\n");
        System.out.println("Unesite naziv korisnika: \n");
        String korisnik = scanner.nextLine();
        System.out.println("Unesite naziv paketa: \n");
        String paket = scanner.nextLine();
        System.out.println("Unesite cenu pretplate: \n");
        Double cena = scanner.nextDouble();
        scanner.nextLine();
        
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Unesite datum pocetka pretplate(yyyy-MM-dd):\n");
        String datum = scanner.nextLine();
        Date d = null;
        try {
            d = format.parse(datum);
        } catch (ParseException ex) {
            System.out.println("Datum nije u dobrom formatu!");
            return;
        }
        
        String url = "http://localhost:8080/CentralniServer/api/server/q11/" + korisnik + "/" + paket + "/" + cena + "/" + datum;
        sendHttpRequest(url,"POST");
    }

    private static void createListening() {
        System.out.println("Kreiranje slusanja audio snimka zapoceto!\n");
        System.out.println("Unesite naziv korisnika:\n");
        String korisnik = scanner.nextLine();
        System.out.println("Unesite naziv audio snimka:\n");
        String audio = scanner.nextLine();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Unesite datum pocetka slusanja(yyyy-MM-dd):\n");
        String datum = scanner.nextLine();
        Date d = null;
        try {
            d = format.parse(datum);
        } catch (ParseException ex) {
            System.out.println("Datum nije u dobrom formatu!");
            return;
        }
        
        System.out.println("Unesite sekund pocetka: \n");
        int pocetak = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Unesite koliko je sekundii odgledano: \n");
        int odgledano = scanner.nextInt();
        scanner.nextLine();
        
        
        String url = "http://localhost:8080/CentralniServer/api/server/q12/" + korisnik + "/" + audio + "/" + datum + "/" + pocetak + "/" + odgledano;
        sendHttpRequest(url,"POST");
    }

    private static void addToFavorites() {
        System.out.println("Zapoceto dodavanje audio u omiljenje!\n");
        System.out.println("Unesite naziv korisnika: \n");
        String korisnik = scanner.nextLine();
        System.out.println("Unesite naziv audio snimka: \n");
        String audio = scanner.nextLine();
        
        
        String url = "http://localhost:8080/CentralniServer/api/server/q13/" + korisnik + "/" + audio;
        sendHttpRequest(url,"POST");
    }

    private static void createGrade() {
        System.out.println("Zapoceto kreiranje ocene!\n");
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        System.out.println("Unesite audio snimak: \n");
        String audio = scanner.nextLine();
        System.out.println("Unesite ocenu:\n");
        int ocena = scanner.nextInt();
        scanner.nextLine();
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Unesite datum kreiranja ocene(yyyy-MM-dd):\n");
        String datum = scanner.nextLine();
        Date d = null;
        try {
            d = format.parse(datum);
        } catch (ParseException ex) {
            System.out.println("Datum nije u dobrom formatu!");
            return;
        }
        
         String url = "http://localhost:8080/CentralniServer/api/server/q14/" + korisnik + "/" + audio + "/" + datum + "/" + ocena;
        sendHttpRequest(url,"POST");
    }

    private static void changeGrade() {
        System.out.println("Zapoceto menjanje ocene!\n");
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        System.out.println("Unesite audio snimak: \n");
        String audio = scanner.nextLine();
        System.out.println("Unesite ocenu:\n");
        int ocena = scanner.nextInt();
        scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q15/" + korisnik + "/" + audio + "/" + ocena;
        sendHttpRequest(url,"POST");
    }

    private static void deleteGrade() {
        System.out.println("Zapoceto brisanje ocene!\n");
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        System.out.println("Unesite audio snimak: \n");
        String audio = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q16/" + korisnik + "/" + audio;
        sendHttpRequest(url,"POST");
    }
   
    private static void showAllPackage() {
        System.out.println("Zapoceto prikazivanje svih paketa!\n");
        String url = "http://localhost:8080/CentralniServer/api/server/q23";
        sendHttpRequest(url,"GET");
    }
    
    private static void showAllSubscriptionForUser() {
        System.out.println("Zapoceto prikazivanje pretplata za korisnika!\n");
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q24/" + korisnik;
        sendHttpRequest(url,"GET");
    }

    private static void showAllListeningForUser() {
        System.out.println("Zapoceto prikazivanje slusanja za korisnika!\n");
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q25/" + korisnik;
        sendHttpRequest(url,"GET");
    }

    private static void showAllGradesForAudio() {
        System.out.println("Zapoceto prikazivanje ocena za audio!\n");
        System.out.println("Unesite naziv audio snimka: \n");
        String audio = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q26/" + audio;
        sendHttpRequest(url,"GET");
    }

    private static void showAllFavoritesForUser() {
        System.out.println("Zapoceto prikazivanje omiljenih audio snimaka za korisnika!\n");
        System.out.println("Unesite korisnika: \n");
        String korisnik = scanner.nextLine();
        
        String url = "http://localhost:8080/CentralniServer/api/server/q27/" + korisnik;
        sendHttpRequest(url,"GET");
    }
    
}
