package sameuelesimeone.FitWell.models.Diet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autoRecipes")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AutomaticRecipe {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private List<String> ingredients;
    private int calories;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "automatic_recipe_id")
    private List<Nutrients> nutrition;
    @Enumerated(EnumType.STRING)
    private List<RecipeType> recipeTypes;

    public AutomaticRecipe(String name, List<String> ingredients, int calories, List<Nutrients> nutrition, List<RecipeType> recipeTypes) {
        this.name = name;
        this.ingredients = ingredients;
        this.calories = calories;
        this.nutrition = nutrition;
        this.recipeTypes = recipeTypes;
    }
}
