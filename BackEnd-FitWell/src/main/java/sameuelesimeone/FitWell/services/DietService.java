package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.config.MailgunSender;
import sameuelesimeone.FitWell.dao.DietDAO;
import sameuelesimeone.FitWell.dao.UserDAO;
import sameuelesimeone.FitWell.dto.AutoDietDTO;
import sameuelesimeone.FitWell.dto.MailRequestCoachDTO;
import sameuelesimeone.FitWell.dto.MailRequestNutritionistDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.Diet.Diet;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.models.Diet.Recipe;
import sameuelesimeone.FitWell.models.Role;
import sameuelesimeone.FitWell.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DietService {

    @Autowired
    DietDAO dietDAO;

    @Autowired
    AutoDietService autoDietService;

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    NutrientsService nutrientsService;

    @Autowired
    MailgunSender mailgunSender;


    public List<Diet> getAllDiet(User current){
        if (current.getRole().size() == 1 || current.getRole().get(1).equals(Role.COACH)){
            return dietDAO.findByUser(current);
        }else if (current.getRole().get(1).equals(Role.NUTRITIONIST)){
            return  dietDAO.findByNutritionist(current);
        }
        throw new UnauthorizedExeption("Invalid Role");
    }

    public List<Diet> getAllNutritionistDiet(User current){
        return dietDAO.findByUser(current);
    }

    public Diet generateNewDiet(AutoDietDTO DietDTO, User nutritionist){
        User user = userService.findById(UUID.fromString(DietDTO.user_id()));
        Diet newDiet = new Diet();
        Diet created = create(newDiet, DietDTO);
        created.setUser(user);
        created.setNutritionist(nutritionist);
        Diet saved = dietDAO.save(created);
        List<Diet> dietList = new ArrayList<>();
        dietList.add(saved);

        user.setDiets(dietList);
        userDAO.save(user);
        return saved;
    }

    public Diet generateDietNutritionist(AutoDietDTO dietDTO, User current){
        Diet newDiet = new Diet();
        Diet created = create(newDiet, dietDTO);
        created.setUser(current);
        Diet saved = dietDAO.save(created);
        List<Diet> dietList = new ArrayList<>();
        dietList.add(saved);

        current.setDiets(dietList);
        userDAO.save(current);
        return saved;
    }


    public Diet create(Diet diet, AutoDietDTO dietDTO){
        List<Integer> macro = new ArrayList<>();
        int KcalD = autoDietService.generatekCalDaily(dietDTO);
        int RMR = autoDietService.RMR(dietDTO);
        if (dietDTO.target().equals("bulk")){
            KcalD += 300;
            macro.addAll(Macro(dietDTO.weight(), "bulk", KcalD));
        }else if (dietDTO.target().equals("cut")){
            KcalD -= 300;
            macro.addAll(Macro(dietDTO.weight(), "cut", KcalD));
        }else {
            macro.addAll(Macro(dietDTO.weight(), "normo", KcalD));
        }
        int protein = macro.get(0);
        int carbo = macro.get(2);
        int fat = macro.get(1);
        List<Recipe> recipeList = dietDTO.recipe_id().stream()
                .map(el -> recipeService.findById(UUID.fromString(el))).toList();
        List<Nutrients> nutrients = nutrientsService.nutrientsByDiet(recipeList, carbo, fat, protein);
        diet.setRecipes(recipeList);
        diet.setNumberMeals(dietDTO.numberMeals());
        diet.setTotalCalories(KcalD);
        diet.setRMR(RMR);
        diet.setNutrients(nutrients);
        return diet;
    }


    public List<Integer> Macro(double weight, String target, int kCal){
        List<Integer> macro = new ArrayList<>();
        int protein = 0;
        int carbo = 0;
        int fat = 0;
        switch (target.toLowerCase()){
            case "bulk":
                protein = (int) (2.2 * weight);
                fat = ((kCal * 30)/100)/9;
                carbo = (kCal - (protein * 4) - (fat * 9))/4;
                break;
            case "cut":
                protein = (int) (2 * weight);
                fat = ((kCal * 20)/100)/9;
                carbo = (kCal - (protein * 4) - (fat * 9))/4;
                break;
            default:
                protein = (int) (1.8 * weight);
                fat = ((kCal * 25)/100)/9;
                carbo = (kCal - (protein * 4) - (fat * 9))/4;
                break;
        }
        macro.add(protein);
        macro.add(fat);
        macro.add(carbo);
        return macro;
    }

    public Diet findById(UUID id){
        return dietDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void deleteDiet (UUID id, User user){
        Diet found = this.findById(id);
        if (found.getNutritionist().getId().equals(user.getId()) || user.getRole().get(1).equals(Role.ADMIN)){
            dietDAO.delete(found);
        }else {
            throw new UnauthorizedExeption("you can't delete this diet");
        }
    }

    public Diet modDiet(UUID dietId, User user, AutoDietDTO dietDTO){
        Diet found = this.findById(dietId);
        if (found.getNutritionist().getId().equals(user.getId()) || user.getRole().get(1).equals(Role.ADMIN)){
           return  dietDAO.save(mod(dietDTO, found));
        }else {
            throw new UnauthorizedExeption("you can't modify this diet");
        }
    }


    public Diet mod(AutoDietDTO dietDTO, Diet diet){
        List<Integer> macro = new ArrayList<>();
        int KcalD = autoDietService.generatekCalDaily(dietDTO);
        int RMR = autoDietService.RMR(dietDTO);
        if (dietDTO.target().equals("bulk")){
            KcalD += 300;
            macro.addAll(Macro(dietDTO.weight(), "bulk", KcalD));
        }else if (dietDTO.target().equals("cut")){
            KcalD -= 300;
            macro.addAll(Macro(dietDTO.weight(), "cut", KcalD));
        }else {
            macro.addAll(Macro(dietDTO.weight(), "normo", KcalD));
        }
        int protein = macro.get(0);
        int carbo = macro.get(2);
        int fat = macro.get(1);
        List<Recipe> recipeList = dietDTO.recipe_id().stream()
                .map(el -> recipeService.findById(UUID.fromString(el))).toList();
        List<Nutrients> nutrients = nutrientsService.modNutrientsByDiet(recipeList, carbo, fat, protein, diet.getNutrients());
        diet.setRecipes(recipeList);
        diet.setNumberMeals(dietDTO.numberMeals());
        diet.setTotalCalories(KcalD);
        diet.setRMR(RMR);
        diet.setNutrients(nutrients);
        return diet;
    }


    public void requestOnDiet(User user, MailRequestNutritionistDTO mail){
        User nutritionist = userService.findById(UUID.fromString(mail.nutritionist_id()));
        Diet diet = null;
        if(mail.diet_id() != null){
            diet = this.findById(UUID.fromString(mail.diet_id()));
        }
        switch (mail.function().toLowerCase()){
            case "create":
                mailgunSender.sendRequestCreateDiet(user, nutritionist, mail);
                break;
            case "modify", "delete":
                mailgunSender.sendrequestOnDiet(user, nutritionist, diet, mail.function());
                break;
            default:
                throw new BadRequestException("Invalid request");
        }
    }
}
