package sameuelesimeone.FitWell.models.Diet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Column(columnDefinition = "TEXT")
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "automatic_recipe_id")
    private List<FoodsIntermediate> ingredients;
    private int calories;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    private List<Nutrients> nutrition;

    public Recipe(String name, List<FoodsIntermediate> ingredients, int calories, List<Nutrients> nutrition) {
        this.name = name;
        this.ingredients = ingredients;
        this.calories = calories;
        this.nutrition = nutrition;
    }
}
