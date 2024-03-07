package sameuelesimeone.FitWell.dto;

import jakarta.validation.constraints.NotEmpty;
import sameuelesimeone.FitWell.models.Workout;

import java.util.List;

public record CardWorkoutDTO(
        @NotEmpty(message = "name required")
        String name,

        @NotEmpty(message = "workouts required")
        List<String> workouts_id,
        int restTimer,
        String user_id,
        String coach_id
) {
}
