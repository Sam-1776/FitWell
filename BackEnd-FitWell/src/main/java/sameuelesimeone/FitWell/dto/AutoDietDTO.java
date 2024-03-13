package sameuelesimeone.FitWell.dto;

import java.util.List;

public record AutoDietDTO(
        int numberMeals,
        double weight,
        String gender,
        int age,
        String work,
        String workout,
        String target,
        String user_id,
        List<String> recipe_id
) {
}
