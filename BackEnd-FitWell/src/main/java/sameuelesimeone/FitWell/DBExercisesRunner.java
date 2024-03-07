package sameuelesimeone.FitWell;

import com.google.gson.Gson;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sameuelesimeone.FitWell.models.*;
import sameuelesimeone.FitWell.services.ExerciseService;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;



@Component
public class DBExercisesRunner implements CommandLineRunner {

    @Autowired
    ExerciseService exerciseService;
    @Override
    public void run(String... args) throws Exception {

        String[] muscleTypes = {
                "ABDOMINALS",
                "ABDUCTORS",
                "ADDUCTORS",
                "BICEPS",
                "CALVES",
                "CHEST",
                "FOREARMS",
                "GLUTES",
                "HAMSTRINGS",
                "LATS",
                "LOWER_BACK",
                "MIDDLE_BACK",
                "NECK",
                "QUADRICEPS",
                "SHOULDERS",
                "TRAPS",
                "TRICEPS"
        };

        String[] typeList ={
                "CARDIO",
                "WEIGHTLIFTING",
                "PLYOMETRICS",
                "POWERLIFTING",
                "STRENGTH",
                "STRETCHING",
                "STRONGMAN"
        };

        String[] expList={
                "BEGINNER",
                "INTERMEDIATE",
                "EXPERT"
        };

        if (!exerciseService.presenceOfRecords()){
            for (String muscle : muscleTypes) {
                takeAll(muscle);
            }
            for (String type : typeList) {
                takeAllByType(type);
            }
            for (String exp : expList) {
                takeAllByExp(exp);
            }
        }

    }

    public void takeAll(String muscle) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .version(Version.HTTP_2)
                .build();

        Builder builder = HttpRequest.newBuilder(new URI("https://api.api-ninjas.com/v1/exercises?muscle=" + muscle));
        HttpRequest httpRequest = builder.GET().header("X-Api-Key", "5KLG+fGD3EMDyNgTaOM2VQ==S8OaC358eBuDcifj").build();

        BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> httpResponse = client.send(httpRequest, bodyHandler);

        // Deserializzazione del JSON in un array di oggetti Esercizio
        Gson gson = new Gson();
        Esercizio[] esercizi = gson.fromJson(httpResponse.body(), Esercizio[].class);


        // Ora puoi lavorare con l'array di oggetti Esercizio
        for (Esercizio esercizio : esercizi) {
            Exercise check = exerciseService.findByName(esercizio.getName());
            System.out.println(check);
            if (check == null){
                Exercise newExercise = new Exercise(esercizio.getName(), esercizio.getInstructions());
                Exercise exerciseSetMuscle = setMuscle(newExercise, muscle);
                Exercise exerciseSetType = setType(exerciseSetMuscle, esercizio.getType());
                Exercise exerciseSetExp = setExp(exerciseSetType, esercizio.getDifficulty());
                exerciseService.save(exerciseSetExp);
            }
        }
    }

    public void takeAllByType(String type) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient
                .newBuilder()
                .version(Version.HTTP_2)
                .build();

        for (int i = 0; i < 3; i++) {
            Builder builder = HttpRequest.newBuilder(new URI("https://api.api-ninjas.com/v1/exercises?type=" + type + "&offset=" + i));
            HttpRequest httpRequest = builder.GET().header("X-Api-Key", "5KLG+fGD3EMDyNgTaOM2VQ==S8OaC358eBuDcifj").build();

            BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = client.send(httpRequest, bodyHandler);

            // Deserializzazione del JSON in un array di oggetti Esercizio
            Gson gson = new Gson();
            Esercizio[] esercizi = gson.fromJson(httpResponse.body(), Esercizio[].class);


            // Ora puoi lavorare con l'array di oggetti Esercizio
            for (Esercizio esercizio : esercizi) {
                Exercise check = exerciseService.findByName(esercizio.getName());
                if (check == null){
                    Exercise newExercise = new Exercise(esercizio.getName(), esercizio.getInstructions());
                    Exercise exerciseSetMuscle = setMuscle(newExercise, esercizio.getMuscle());
                    Exercise exerciseSetType = setType(exerciseSetMuscle, type);
                    Exercise exerciseSetExp = setExp(exerciseSetType, esercizio.getDifficulty());
                    exerciseService.save(exerciseSetExp);
                }
            }
        }
    }

    public void takeAllByExp(String exp) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient
                .newBuilder()
                .version(Version.HTTP_2)
                .build();

        for (int i = 0; i < 3; i++) {
            Builder builder = HttpRequest.newBuilder(new URI("https://api.api-ninjas.com/v1/exercises?difficulty=" + exp + "&offset=" + i));
            HttpRequest httpRequest = builder.GET().header("X-Api-Key", "5KLG+fGD3EMDyNgTaOM2VQ==S8OaC358eBuDcifj").build();

            BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> httpResponse = client.send(httpRequest, bodyHandler);

            // Deserializzazione del JSON in un array di oggetti Esercizio
            Gson gson = new Gson();
            Esercizio[] esercizi = gson.fromJson(httpResponse.body(), Esercizio[].class);


            // Ora puoi lavorare con l'array di oggetti Esercizio
            for (Esercizio esercizio : esercizi) {
                Exercise check = exerciseService.findByName(esercizio.getName());
                if (check == null){
                    Exercise newExercise = new Exercise(esercizio.getName(), esercizio.getInstructions());
                    Exercise exerciseSetMuscle = setMuscle(newExercise, esercizio.getMuscle());
                    Exercise exerciseSetType = setType(exerciseSetMuscle, esercizio.getType());
                    Exercise exerciseSetExp = setExp(exerciseSetType, exp);
                    exerciseService.save(exerciseSetExp);
                }
            }
        }
    }

    public Exercise setExp(Exercise newExercise, String exp){
        switch (exp.toLowerCase()){
            case "beginner":
                newExercise.setExp(Exp.BEGINNER);
                break;
            case "intermediate":
                newExercise.setExp(Exp.INTERMEDIATE);
                break;
            case "expert":
                newExercise.setExp(Exp.EXPERT);
                break;
        }
        return newExercise;
    }

    public Exercise setType(Exercise newExercise, String type){
        switch (type.toLowerCase()) {
            case "cardio":
                newExercise.setType(Type.CARDIO);
                break;
            case "olympic_weightlifting":
                newExercise.setType(Type.WEIGHTLIFTING);
                break;
            case "plyometrics":
                newExercise.setType(Type.PLYOMETRICS);
                break;
            case "powerlifting":
                newExercise.setType(Type.POWERLIFTING);
                break;
            case "strength":
                newExercise.setType(Type.STRENGTH);
                break;
            case "stretching":
                newExercise.setType(Type.STRETCHING);
                break;
            case "strongman":
                newExercise.setType(Type.STRONGMAN);
                break;
            default:
                break;
        }
        return newExercise;
    }

    public Exercise setMuscle(Exercise newExercise, String muscle){

        switch (muscle.toLowerCase()) {
            case "abdominals":
                newExercise.setMuscle(Muscle.ABDOMINALS);
                break;
            case "abductors":
                newExercise.setMuscle(Muscle.ABDUCTORS);
                break;
            case "adductors":
                newExercise.setMuscle(Muscle.ADDUCTORS);
                break;
            case "biceps":
                newExercise.setMuscle(Muscle.BICEPS);
                break;
            case "calves":
                newExercise.setMuscle(Muscle.CALVES);
                break;
            case "chest":
                newExercise.setMuscle(Muscle.CHEST);
                break;
            case "forearms":
                newExercise.setMuscle(Muscle.FOREARMS);
                break;
            case "glutes":
                newExercise.setMuscle(Muscle.GLUTES);
                break;
            case "hamstrings":
                newExercise.setMuscle(Muscle.HAMSTRINGS);
                break;
            case "lats":
                newExercise.setMuscle(Muscle.LATS);
                break;
            case "lower_back":
                newExercise.setMuscle(Muscle.LOWER_BACK);
                break;
            case "middle_back":
                newExercise.setMuscle(Muscle.MIDDLE_BACK);
                break;
            case "neck":
                newExercise.setMuscle(Muscle.NECK);
                break;
            case "quadriceps":
                newExercise.setMuscle(Muscle.QUADRICEPS);
                break;
            case "shoulders":
                newExercise.setMuscle(Muscle.SHOULDERS);
                break;
            case "traps":
                newExercise.setMuscle(Muscle.TRAPS);
                break;
            case "triceps":
                newExercise.setMuscle(Muscle.TRICEPS);
                break;
            default:
                break;
        }
        return newExercise;
    }
}
