package sameuelesimeone.FitWell;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sameuelesimeone.FitWell.dao.AutomaticRecipeDAO;
import sameuelesimeone.FitWell.dao.NutrientsDAO;
import sameuelesimeone.FitWell.models.Diet.AutomaticRecipe;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.models.Diet.RecipeType;
import sameuelesimeone.FitWell.services.AutomaticRecipeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class DBDietRunner implements CommandLineRunner {

    @Autowired
    AutomaticRecipeDAO automaticRecipeDAO;

    @Autowired
    AutomaticRecipeService automaticRecipeService;

    @Autowired
    NutrientsDAO nutrientsDAO;

    @Value("${access.token.fatsecret}")
    private String myAccessToken;

    @Override
    public void run(String... args) throws Exception {
        String[] recipeType = {
                "SNACK",
                "BREAKFAST",
                "LUNCH"
        };
        if (!automaticRecipeService.presenceOfRecords()){
            for (String recipe : recipeType) {
                takeAllRecipe(recipe);
            }
        }
    }

    public void takeAllRecipe(String type) throws IOException {
        for (int i = 0; i < 3; i++) {
            // Sostituisci "TUO_TOKEN_DI_ACCESSO" con il tuo effettivo token di accesso
            String accessToken = myAccessToken;

            // Sostituisci "URL_DEL_API" con l'URL effettivo dell'API che desideri chiamare
            String apiUrl = "https://platform.fatsecret.com/rest/server.api?method=recipes.search.v3&format=json&page_number=" + i + "&max_results=10&recipe_types=" + type + "&recipe_types_matchall=true";

            // Crea un client HttpClient
            HttpClient httpClient = HttpClients.createDefault();

            // Crea una richiesta HTTP GET
            HttpGet httpGet = new HttpGet(apiUrl);

            // Aggiungi l'intestazione Authorization con il token di accesso
            httpGet.setHeader("Authorization", "Bearer " + accessToken);

            // Esegui la richiesta
            HttpResponse response = httpClient.execute(httpGet);

            // Estrae il corpo della risposta JSON
            String jsonResponseString = org.apache.http.util.EntityUtils.toString(response.getEntity());

            // Crea un oggetto ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Analizza la stringa JSON nella classe JsonNode
            JsonNode jsonNode = objectMapper.readTree(jsonResponseString);

            JsonNode recipes = jsonNode.path("recipes").path("recipe");

            generateRecipe(recipes);
        }
    }


    public void generateRecipe(JsonNode recipes) {
        for (JsonNode recipeNode : recipes) {
            AutomaticRecipe check = automaticRecipeService.findByName(recipeNode.get("recipe_name").asText());
            if (check == null){
                List<String> ingredientsList = new ArrayList<>();
                List<RecipeType> recipeTypesList = new ArrayList<>();
                String recipeName = recipeNode.get("recipe_name").asText();
//                String recipeDescription = recipeNode.get("recipe_description").asText();
//                String recipeImage = recipeNode.get("recipe_image").asText();

                // Estrai gli ingredienti
                JsonNode ingredientsNode = recipeNode.path("recipe_ingredients").path("ingredient");
                Iterator<JsonNode> ingredientIterator = ingredientsNode.elements();
                while (ingredientIterator.hasNext()) {
                    String ingredient = ingredientIterator.next().asText();
                    ingredientsList.add(ingredient);
                }

                // Estrai i tipi di ricetta
                JsonNode recipeTypesNode = recipeNode.path("recipe_types").path("recipe_type");
                Iterator<JsonNode> recipeTypeIterator = recipeTypesNode.elements();
                assignType(recipeTypeIterator, recipeTypesList);

                // Estrai le informazioni nutrizionali
                JsonNode nutritionNode = recipeNode.path("recipe_nutrition");
                String calories = nutritionNode.get("calories").asText();
                String carbohydrate = nutritionNode.get("carbohydrate").asText();
                String fat = nutritionNode.get("fat").asText();
                String protein = nutritionNode.get("protein").asText();
                Nutrients carbo = nutrientsDAO.save(new Nutrients("carbohydrate", Double.valueOf(carbohydrate)));
                Nutrients fatty = nutrientsDAO.save(new Nutrients("fat", Double.valueOf(fat)));
                Nutrients prot = nutrientsDAO.save(new Nutrients("protein", Double.valueOf(protein)));

                List<Nutrients> nutrientsList = new ArrayList<>();
                nutrientsList.add(carbo);
                nutrientsList.add(fatty);
                nutrientsList.add(prot);

                // Puoi quindi utilizzare queste informazioni come desideri
                AutomaticRecipe recipe = new AutomaticRecipe(recipeName, ingredientsList, Integer.valueOf(calories), nutrientsList, recipeTypesList);
                automaticRecipeDAO.save(recipe);
            }else {
                System.out.println(check);
            }
        }
    }

    public List<RecipeType> assignType(Iterator<JsonNode> recipeTypeIterator, List<RecipeType> recipeTypesList) {
        while (recipeTypeIterator.hasNext()) {
            String recipeType = recipeTypeIterator.next().asText();
            RecipeType recipe;
            switch (recipeType.toLowerCase()) {
                case "appetizer":
                    recipe = RecipeType.APPETIZER;
                    break;
                case "soup":
                    recipe = RecipeType.SOUP;
                    break;
                case "main dish":
                    recipe = RecipeType.MAIN_DISH;
                    break;
                case "side dish":
                    recipe = RecipeType.SIDE_DISH;
                    break;
                case "baked":
                    recipe = RecipeType.BAKED;
                    break;
                case "salad and salad dressing":
                    recipe = RecipeType.SALAD_AND_SALAD_DRESSING;
                    break;
                case "sauce and condiment":
                    recipe = RecipeType.SAUCE_AND_CONDIMENT;
                    break;
                case "dessert":
                    recipe = RecipeType.DESSERT;
                    break;
                case "snack":
                    recipe = RecipeType.SNACK;
                    break;
                case "beverage":
                    recipe = RecipeType.BEVERAGE;
                    break;
                case "other":
                    recipe = RecipeType.OTHER;
                    break;
                case "breakfast":
                    recipe = RecipeType.BREAKFAST;
                    break;
                case "lunch":
                    recipe = RecipeType.LUNCH;
                    break;
                default:
                    throw new IllegalArgumentException("Tipo non riconosciuto");
            }

            recipeTypesList.add(recipe);

        }

        return recipeTypesList;
    }
}
