package sameuelesimeone.FitWell.dto;

public record MailRequestNutritionistDTO(
        String nutritionist_id,
        String diet_id,
        String function,
        double weight,
        int age,
        int numberMeals,
        String work,
        String workout,
        String target
) {
}
