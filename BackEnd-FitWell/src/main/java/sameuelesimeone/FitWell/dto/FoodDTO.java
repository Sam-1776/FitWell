package sameuelesimeone.FitWell.dto;

import java.util.List;

public record FoodDTO(
        String name,
        List<String> nutrients_id,
        List<String> category,
        int calories
) {
}
