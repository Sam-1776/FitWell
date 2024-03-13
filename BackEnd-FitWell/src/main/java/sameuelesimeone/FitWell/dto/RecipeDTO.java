package sameuelesimeone.FitWell.dto;

import java.util.List;

public record RecipeDTO(
        String name,
        List<String> food_id
) {
}
