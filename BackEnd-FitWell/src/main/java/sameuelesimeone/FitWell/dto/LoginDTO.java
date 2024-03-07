package sameuelesimeone.FitWell.dto;

import java.util.List;

public record LoginDTO(
        String accessToken,
        List<String> role
) {
}
