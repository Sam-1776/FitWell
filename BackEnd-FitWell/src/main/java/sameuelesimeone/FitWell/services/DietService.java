package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.DietDAO;
import sameuelesimeone.FitWell.dao.UserDAO;
import sameuelesimeone.FitWell.dto.AutoDietDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
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

    public Diet generateNewDiet(AutoDietDTO DietDTO, User nutritionist){
        User user = userService.findById(UUID.fromString(DietDTO.user_id()));
        List<Integer> macro = new ArrayList<>();
        int KcalD = autoDietService.generatekCalDaily(DietDTO);
        int RMR = autoDietService.RMR(DietDTO);
        if (DietDTO.target().equals("bulk")){
            KcalD += 300;
            macro.addAll(Macro(DietDTO.weight(), "bulk", KcalD));
        }else if (DietDTO.target().equals("cut")){
            KcalD -= 300;
            macro.addAll(Macro(DietDTO.weight(), "cut", KcalD));
        }else {
            macro.addAll(Macro(DietDTO.weight(), "normo", KcalD));
        }
        int protein = macro.get(0);
        int carbo = macro.get(2);
        int fat = macro.get(1);
        List<Recipe> recipeList = DietDTO.recipe_id().stream()
                .map(el -> recipeService.findById(UUID.fromString(el))).toList();
        List<Nutrients> nutrients = nutrientsService.nutrientsByDiet(recipeList, carbo, fat, protein);
        Diet newDiet = dietDAO.save(new Diet(recipeList, DietDTO.numberMeals(), KcalD, RMR, user, nutrients));
        List<Diet> dietList = new ArrayList<>();
        dietList.add(newDiet);

        user.setDiets(dietList);
        userDAO.save(user);
        return newDiet;
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
}
