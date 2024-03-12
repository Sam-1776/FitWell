package sameuelesimeone.FitWell.models.Diet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "foods")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private double amount;
    private String unit;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id")
    private List<Nutrients> nutrition;
    private List<Category> categories;
    private int calories;

    public Food(String name, List<Nutrients> nutrition, List<Category> categories, int calories) {
        this.name = name;
        this.amount = 100.0;
        this.unit = "g";
        this.nutrition = nutrition;
        this.categories = categories;
        this.calories = calories;
    }
}
