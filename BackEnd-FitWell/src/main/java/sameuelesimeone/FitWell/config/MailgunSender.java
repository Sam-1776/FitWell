package sameuelesimeone.FitWell.config;


import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.models.User;

@Service
public class MailgunSender {

    private String domainName;
    private String mailGunAPIKey;

    private String mailFeed;

    public MailgunSender(@Value("${mailgun.apikey}") String mailGunAPIKey, @Value("${mailgun.domain}") String domainName, @Value("${mailgun.feed}") String mailFeed) {
        this.mailGunAPIKey = mailGunAPIKey;
        this.domainName = domainName;
        this.mailFeed = mailFeed;
    }

    public void sendRegistrationEmail(User userRegister){
            Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                    .basicAuth("api", mailGunAPIKey)
                    .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                    .queryString("to", userRegister.getEmail())
                    .queryString("subject", "Registrazione completata")
                    .queryString("text", "Complimenti per esserti registrato")
                    .asJson();
    }

}
