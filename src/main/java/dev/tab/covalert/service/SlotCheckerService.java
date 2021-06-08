package dev.tab.covalert.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import dev.tab.covalert.model.Client;
import dev.tab.covalert.repository.ClientRepository;

@Service
public class SlotCheckerService {

    private final String PINCODE_API_URL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin";
    @Autowired
    private EmailService mailService;
    @Autowired
    private ClientRepository clientRepo;

    public SlotCheckerService() {

    }

    @Scheduled(cron = "0 0 0/1 ? * *")
    public void run() {
        List<Client> clients = clientRepo.findAll();
        Thread[] threads = new Thread[clients.size()];
        for (int i = 0; i < threads.length; i++) {
            Client client = clients.get(i);
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    checkSlotsForToday(client);
                    checkSlotsForTommorow(client);
                }
            });
            threads[i].start();
        }
    }

    @Async
    public void checkSlotsForToday(Client client) {
        String jsonResponse = "";
        int pinCode = client.getPincode();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String API_URL = PINCODE_API_URL + "?pincode=" + pinCode + "&date=" + date;
        
        try {
            URL url = new URL(API_URL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if(responseCode != 200) {
                throw new RuntimeException("responseCode: " + responseCode);
            } else {
                Scanner apiResponseScanner = new Scanner(url.openStream());
                while(apiResponseScanner.hasNextLine()) {
                    jsonResponse += apiResponseScanner.nextLine();
                } apiResponseScanner.close();

                JSONObject jsonResponseObject = new JSONObject(jsonResponse);
                JSONArray sessions = jsonResponseObject.getJSONArray("sessions");
                for(int i=0; i<sessions.length(); i++) {
                    JSONObject session = sessions.getJSONObject(i);
                    String vaccinationCenter = session.getString("name");
                    int vaccineAvailability = session.getInt("available_capacity");
                    int ageGroup = session.getInt("min_age_limit");
                    String vaccineBrand = session.getString("vaccine");
                    if(ageGroup == 18 && vaccineAvailability > 0) {
                        this.generateNotificationEmail(vaccinationCenter, vaccineAvailability, vaccineBrand, client);
                        Thread.sleep(30000);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Async
    public void checkSlotsForTommorow(Client client) {
        String jsonResponse = "";
        int pinCode = client.getPincode();
        String tommorowDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String API_URL = PINCODE_API_URL + "?pincode=" + pinCode + "&date=" + tommorowDate;
        
        try {
            URL url = new URL(API_URL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if(responseCode != 200) {
                throw new RuntimeException("responseCode: " + responseCode);
            } else {
                Scanner apiResponseScanner = new Scanner(url.openStream());
                while(apiResponseScanner.hasNextLine()) {
                    jsonResponse += apiResponseScanner.nextLine();
                } apiResponseScanner.close();

                JSONObject jsonResponseObject = new JSONObject(jsonResponse);
                JSONArray sessions = jsonResponseObject.getJSONArray("sessions");
                for(int i=0; i<sessions.length(); i++) {
                    JSONObject session = sessions.getJSONObject(i);
                    String vaccinationCenter = session.getString("name");
                    int vaccineAvailability = session.getInt("available_capacity");
                    int ageGroup = session.getInt("min_age_limit");
                    String vaccineBrand = session.getString("vaccine");
                    if(ageGroup == 18 && vaccineAvailability > 0) {
                        this.generateNotificationEmail(vaccinationCenter, vaccineAvailability, vaccineBrand, client);
                        Thread.sleep(30000);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void generateNotificationEmail(String vaccinationCenter, int vaccineAvailability, String vaccineBrand, Client client) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jabalert.com@gmail.com");
        mailMessage.setSubject("Cowin Slot Alert");
        mailMessage.setText(vaccineAvailability + " doses of " + vaccineBrand + " available at " + vaccinationCenter + " for age group 18+ \n\n" 
            + "Hurry up book your slots now! Click on the link before the slots run out https://selfregistration.cowin.gov.in/ \n\n"
            + "To unsubscribe from our notifications click here http://jabalert-env-1.eba-yv73pwka.ap-south-1.elasticbeanstalk.com/unsubscribe");
        mailMessage.setTo(client.getEmail());
        mailService.sendNotification(mailMessage);
    }
    
}