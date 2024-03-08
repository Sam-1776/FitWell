package sameuelesimeone.FitWell.config;


import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dto.MailRequestCoachDTO;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.User;

@Service
public class MailgunSender {

    private String domainName;
    private String mailGunAPIKey;


    public MailgunSender(@Value("${mailgun.apikey}") String mailGunAPIKey, @Value("${mailgun.domain}") String domainName) {
        this.mailGunAPIKey = mailGunAPIKey;
        this.domainName = domainName;
    }

    public void sendRegistrationEmail(User userRegister){
            Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                    .basicAuth("api", mailGunAPIKey)
                    .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                    .queryString("to", userRegister.getEmail())
                    .queryString("subject", "Registration completed")
                    .queryString("text", "Thank you for choosing our services. \n" + "enjoy your life")
                    .asJson();
    }

    public void sendDeleteAccount(User userDelete){
        Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", mailGunAPIKey)
                .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                .queryString("to", userDelete.getEmail())
                .queryString("subject", "Cancellation completed")
                .queryString("text", "We're sorry we couldn't be of assistance to you. \n" + "enjoy your life")
                .asJson();
    }

    public void sendRequestCreateCard(User user, User coach){
        Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", mailGunAPIKey)
                .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                .queryString("to", coach.getEmail())
                .queryString("subject", "Create cardWorkout")
                .queryString("text", "request to create a cardWorkout for " + user.getName() + user.getSurname())
                .asJson();
    }

    public void sendrequestOnCard(User user, User coach, CardWorkout card, String function){
        Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", mailGunAPIKey)
                .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                .queryString("to", coach.getEmail())
                .queryString("subject", function +"cardWorkout")
                .queryString("text", "request to " + function + " a cardWorkout ID: " + card.getId() + " for " + user.getName() + user.getSurname())
                .asJson();
    }



}
