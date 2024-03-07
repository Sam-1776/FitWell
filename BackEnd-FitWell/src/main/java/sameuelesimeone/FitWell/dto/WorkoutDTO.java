package sameuelesimeone.FitWell.dto;

import java.util.List;

public record WorkoutDTO(
        String exerciseId,
        List<String> setId
) {
}
