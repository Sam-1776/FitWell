package sameuelesimeone.FitWell.models.Diet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class FoodsIntermediate {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private double amount;
    private String unit;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "food_inter_id")
    private List<Nutrients> nutrition;
    private List<Category> categories;
    private int calories;

    public FoodsIntermediate(String name, double amount, String unit, List<Nutrients> nutrition, List<Category> categories, int calories) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.nutrition = nutrition;
        this.categories = categories;
        this.calories = calories;
    }
}
