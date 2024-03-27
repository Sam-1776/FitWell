package sameuelesimeone.FitWell.config;


import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dto.MailRequestCoachDTO;
import sameuelesimeone.FitWell.dto.MailRequestNutritionistDTO;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.Diet.Diet;
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
                .queryString("text", "request to create a cardWorkout for " + user.getName() + user.getSurname() + " UserId:" + user.getId())
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

    public void sendRequestCreateDiet(User user, User nutritionist, MailRequestNutritionistDTO mail){
        Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", mailGunAPIKey)
                .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                .queryString("to", nutritionist.getEmail())
                .queryString("subject", "Create Diet")
                .queryString("text", "request to create a Diet for " + user.getName() + user.getSurname()
                        + "/n Info user:"
                        + "/n UserId: " + user.getId()
                        + "/n User Gender: " + user.getGender()
                        + "/n User age: " + mail.age()
                        + "/n User weight: " + mail.weight()
                        + "/n User N. meal: " + mail.numberMeals()
                        + "/n User work: " + mail.work()
                        + "/n User workout: " + mail.workout()
                        + "/n User target: " + mail.target())
                .asJson();
    }

    public void sendrequestOnDiet(User user, User nutritionist, Diet diet, String function){
        Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
                .basicAuth("api", mailGunAPIKey)
                .queryString("from", "FitWell <FitWellSRL@gmail.com>")
                .queryString("to", nutritionist.getEmail())
                .queryString("subject", function +"cardWorkout")
                .queryString("text", "request to " + function + " a Diet ID: " + diet.getId() + " for " + user.getName() + user.getSurname())
                .asJson();
    }



}
