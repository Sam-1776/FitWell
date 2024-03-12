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
@Table(name = "diets")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Diet {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "diet_id")
    private List<Recipe> recipes;
    private int numberMeals;
    private int totalCalories;
    private int RMR;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name = "nutritionist_id")
    private User nutritionist;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "diet_id")
    private List<Nutrients> nutrients;


    public Diet(List<Recipe> recipes, int numberMeals, int totalCalories, int RMR, User user, List<Nutrients> nutrients) {
        this.recipes = recipes;
        this.numberMeals = numberMeals;
        this.totalCalories = totalCalories;
        this.RMR = RMR;
        this.user = user;
        this.nutrients = nutrients;
    }
}
