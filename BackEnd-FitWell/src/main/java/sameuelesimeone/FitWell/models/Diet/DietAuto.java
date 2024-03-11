package sameuelesimeone.FitWell.models.Diet;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sameuelesimeone.FitWell.models.User;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dietsAuto")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DietAuto {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "auto_diet_id")
    private List<AutomaticRecipe> recipes;
    private int numberMeals;
    private int totalCalories;
    private int RMR;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "auto_diet_id")
    private List<Nutrients> nutrients;

    public DietAuto(List<AutomaticRecipe> recipes, int numberMeals, int totalCalories, int RMR, User user, List<Nutrients> nutrients) {
        this.recipes = recipes;
        this.numberMeals = numberMeals;
        this.totalCalories = totalCalories;
        this.RMR = RMR;
        this.user = user;
        this.nutrients = nutrients;
    }
}
