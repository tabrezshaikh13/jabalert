package dev.tab.covalert.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SlotCheckerService {

    private final String PINCODE_API_URL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin";
    private EmailService mailService;

    public SlotCheckerService(EmailService mailService) {
        this.mailService = mailService;
    }

    @PostConstruct
    @Scheduled(cron = "0 0/30 * 1/1 * ?")
    public void check() {
        String jsonResponse = "";
        int pinCode = 411037;
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
                    if(vaccineAvailability > 0) {
                        this.generateNotificationEmail(vaccinationCenter, vaccineAvailability, ageGroup, vaccineBrand);
                        Thread.sleep(10000);
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

    public void generateNotificationEmail(String vaccinationCenter, int vaccineAvailability, int ageGroup, String vaccineBrand) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("cowinslotify@gmail.com");
        mailMessage.setSubject("Cowin slot alert");
        if(ageGroup == 18) {
            mailMessage.setText(vaccineAvailability + " doses available of " + vaccineBrand + " at " + vaccinationCenter + " for age group 18-44");
        } else {
            mailMessage.setText(vaccineAvailability + " doses available of " + vaccineBrand + " at " + vaccinationCenter + " for age group 45+");
        }
        mailMessage.setTo("tabrezshaikh17971@gmail.com");
        mailService.sendNotification(mailMessage);
    }
    
}