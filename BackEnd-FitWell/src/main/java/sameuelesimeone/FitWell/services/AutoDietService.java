package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.AutoDietDAO;
import sameuelesimeone.FitWell.dao.NutrientsDAO;
import sameuelesimeone.FitWell.dao.UserDAO;
import sameuelesimeone.FitWell.dto.AutoDietDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.models.Diet.AutomaticRecipe;
import sameuelesimeone.FitWell.models.Diet.DietAuto;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.models.Diet.RecipeType;
import sameuelesimeone.FitWell.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AutoDietService {

    @Autowired
    AutoDietDAO autoDietDAO;

    @Autowired
    AutomaticRecipeService automaticRecipeService;

    @Autowired
    NutrientsDAO nutrientsDAO;

    @Autowired
    UserDAO userDAO;

    private Random rdm = new Random();

    public DietAuto generateDiet(AutoDietDTO autoDiet, User user){
        List<DietAuto> diets = new ArrayList<>();
        int KcalD = generatekCalDaily(autoDiet);
        int RMR = RMR(autoDiet);
        List<AutomaticRecipe> recipes = takeRecipeByNumberMeal(autoDiet.numberMeals(), KcalD);
        int KcalDiet = recipes.stream().mapToInt(el -> el.getCalories()).sum();
        List<Nutrients> nutrientsList = nutrients(recipes);
        DietAuto diet = autoDietDAO.save(new DietAuto(recipes,autoDiet.numberMeals(),KcalDiet, RMR, user, nutrientsList));
        diets.add(diet);
        user.setDietAutos(diets);
        userDAO.save(user);
        return diet;
    }


    public List<AutomaticRecipe> takeRecipeByNumberMeal(int numberMeal, int Kcal){
        List<AutomaticRecipe> automaticRecipeList = new ArrayList<>();
        switch (numberMeal){
            case 3:
                return generateDietByKcal3(Kcal,automaticRecipeList );
            case 5:
                return generateDietByKcal5(Kcal, automaticRecipeList);
            default:
                throw new BadRequestException("Recommended number of meals between 3 and 5");
        }
    }

    public List<AutomaticRecipe> generateDietByKcal3(int Kcal, List<AutomaticRecipe> recipeList){
        int cal = 0;
        do {
            List<AutomaticRecipe> breakfastList = automaticRecipeService.findByType(RecipeType.BREAKFAST);
            AutomaticRecipe breakFast = breakfastList.get(rdm.nextInt(1, breakfastList.size()));
            recipeList.add(breakFast);
            List<AutomaticRecipe> lunchList = automaticRecipeService.findByType(RecipeType.LUNCH);
            AutomaticRecipe lunch = lunchList.get(rdm.nextInt(1, lunchList.size()));
            recipeList.add(lunch);
            AutomaticRecipe dinner = lunchList.get(rdm.nextInt(1, lunchList.size()));
            recipeList.add(dinner);
            cal = breakFast.getCalories() + lunch.getCalories() + dinner.getCalories();
        }while (cal == Kcal);
        return recipeList;
    }

    public List<AutomaticRecipe> generateDietByKcal5(int Kcal, List<AutomaticRecipe> recipeList){
        int cal = 0;
        do {
            List<AutomaticRecipe> breakfastList = automaticRecipeService.findByType(RecipeType.BREAKFAST);
            AutomaticRecipe breakFast = breakfastList.get(rdm.nextInt(1, breakfastList.size()));
            recipeList.add(breakFast);
            List<AutomaticRecipe> lunchList = automaticRecipeService.findByType(RecipeType.LUNCH);
            AutomaticRecipe lunch = lunchList.get(rdm.nextInt(1, lunchList.size()));
            recipeList.add(lunch);
            AutomaticRecipe dinner = lunchList.get(rdm.nextInt(1, lunchList.size()));
            recipeList.add(dinner);
            List<AutomaticRecipe> snackList = automaticRecipeService.findByType(RecipeType.SNACK);
            AutomaticRecipe snack1 = snackList.get(rdm.nextInt(1, snackList.size()));
            AutomaticRecipe snack2 = snackList.get(rdm.nextInt(1, snackList.size()));
            recipeList.add(snack1);
            recipeList.add(snack2);
            cal = breakFast.getCalories() + lunch.getCalories() + dinner.getCalories() + snack1.getCalories() + snack2.getCalories();
        }while (cal == Kcal);
        return recipeList;
    }


    public List<Nutrients> nutrients(List<AutomaticRecipe> recipes){
        Nutrients carbo = new Nutrients();
        Nutrients protein = new Nutrients();
        Nutrients fat = new Nutrients();
        double amountC = 0.0;
        double amountP = 0.0;
        double amountF = 0.0;
        for (AutomaticRecipe recipe : recipes) {
            for (Nutrients nutrients1 : recipe.getNutrition()) {
                switch (nutrients1.getName()){
                    case "carbohydrate":
                        carbo.setName(nutrients1.getName());
                        amountC += nutrients1.getAmount();
                        carbo.setAmount(amountC);
                        carbo.setUnit(nutrients1.getUnit());
                        break;
                    case"protein":
                        protein.setName(nutrients1.getName());
                        amountP += nutrients1.getAmount();
                        protein.setAmount(amountP);
                        protein.setUnit(nutrients1.getUnit());
                        break;
                    default:
                        fat.setName(nutrients1.getName());
                        amountF += nutrients1.getAmount();
                        fat.setAmount(amountF);
                        fat.setUnit(nutrients1.getUnit());
                        break;
                }
            }
        }
        Nutrients carboSave = nutrientsDAO.save(carbo);
        Nutrients proteinSave = nutrientsDAO.save(protein);
        Nutrients fatSave = nutrientsDAO.save(fat);
        List<Nutrients> nutrientsList = new ArrayList<>();
        nutrientsList.add(carboSave);
        nutrientsList.add(proteinSave);
        nutrientsList.add(fatSave);
        return nutrientsList;
    }




    public int RMR(AutoDietDTO dietDTO){
        return RMRByAgeMan(dietDTO.age(), dietDTO.weight());
    }

    public int generatekCalDaily(AutoDietDTO diet){
        int Kcal = DCR(diet);
        return Kcal;
    }

    public int DCR(AutoDietDTO gender){
        int rmr = 0;
        switch (gender.gender().toLowerCase()){
            case "man":
                rmr = RMR(gender);
                if (gender.workout() == null){
                    rmr = (int) (rmr * LAFNWorkoutMan(gender.work(), gender.age()));
                }else {
                    rmr = (int) (rmr * LAFWorkoutMan(gender.age(), gender.workout()));
                }
                break;
            case "woman":
                rmr = RMR(gender);
                if (gender.workout() == null){
                    rmr = (int) (rmr * LAFNWorkoutWoman(gender.work(), gender.age()));
                }else {
                    rmr = (int) (rmr * LAFWorkoutWoman(gender.age(), gender.workout()));
                }
                break;
            default:
                throw new BadRequestException("Biological gender request");
        }
        return rmr;
    }

    public int RMRByAgeMan(int age, double weight){
        int rmr = 0;
        if (age > 18 && age < 29) {
             rmr += (15.3 * weight) + 679;
        } else if (age >= 30 && age < 59) {
             rmr += (11.6 * weight) + 879;
        } else if (age >= 60 && age < 74) {
             rmr += (11.9 * weight) + 700;
        } else if (age >= 74) {
             rmr += (8.4 * weight) + 819;
        }
        return rmr;
    }

    public int RMRByAgeWoman(int age, double weight){
        int rmr = 0;
        if (age > 18 && age < 29) {
            rmr += (14.7 * weight) + 496;
        } else if (age >= 30 && age < 59) {
            rmr += (8.7 * weight) + 829;
        } else if (age >= 60 && age < 74) {
            rmr += (9.2 * weight) + 688;
        } else if (age >= 74) {
            rmr += (9.8 * weight) + 624;
        }
        return rmr;
    }

    public double LAFNWorkoutWoman (String work, int age){
        double naa = 0.0;
        String law = activityLevel(work);
        if (age > 18 && age < 59) {
            if (law.equals("Light")){
                naa += 1.42;
            } else if (law.equals("Moderate")) {
                naa += 1.56;
            }else if(law.equals("Intense")){
                naa += 1.73;
            }
        } else if (age >= 60 && age < 74) {
            naa += 1.44;
        } else if (age >= 74) {
            naa += 1.37;
        }
        return naa;
    }

    public double LAFWorkoutWoman (int age, String workout){
        double naa = 0.0;
        String LAW = activityWorkoutLevel(workout);
        if (age > 18 && age < 59) {
            if (LAW.equals("Light")){
                naa += 1.56;
            } else if (LAW.equals("Moderate")) {
                naa += 1.64;
            }else if(LAW.equals("Intense")){
                naa += 1.82;
            }
        } else if (age >= 60 && age < 74) {
            naa += 1.56;
        } else if (age >= 74) {
            naa += 1.56;
        }
        return naa;
    }

    public double LAFNWorkoutMan (String work, int age){
        double naa = 0.0;
        String law = activityLevel(work);
        if (age > 18 && age < 59) {
            if (law.equals("Light")){
                naa += 1.41;
            } else if (law.equals("Moderate")) {
                naa += 1.70;
            }else if(law.equals("Intense")){
                naa += 2.01;
            }
        } else if (age >= 60 && age < 74) {
            naa += 1.40;
        } else if (age >= 74) {
            naa += 1.33;
        }
        return naa;
    }

    public double LAFWorkoutMan (int age, String workout){
        double naa = 0.0;
        String LAW = activityWorkoutLevel(workout);
        if (age > 18 && age < 59) {
            if (LAW.equals("Light")){
                naa += 1.55;
            } else if (LAW.equals("Moderate")) {
                naa += 1.78;
            }else if(LAW.equals("Intense")){
                naa += 2.10;
            }
        } else if (age >= 60 && age < 74) {
            naa += 1.51;
        } else if (age >= 74) {
            naa += 1.51;
        }
        return naa;
    }


    public String activityLevel(String category) {
        switch (category.toLowerCase()) {
            case "employees":
            case "administrative":
            case "manager":
            case "freelancers":
            case "technicians":
            case "student":
                return "Light";

            case "housewives":
            case "domestic collaborators":
            case "sales personnel":
            case "tertiary workers":
                return "Moderate";

            case "agriculture":
            case "livestock":
            case "forestry":
            case "fishing":
            case "manual workers":
            case "production operators":
            case "transport operators":
                return "Intense";

            default:
                return "Unspecified level";
        }
    }


    public String activityWorkoutLevel(String exerciseHours) {
        switch (exerciseHours.toLowerCase()) {
            case "up to 2 hours per week":
                return "Light";

            case "3 to 5 hours per week":
                return "Moderate";

            case "more than 5 hours per week":
                return "Intense";

            default:
                return null;
        }
    }
}
