package sameuelesimeone.FitWell.models.Diet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Nutrients {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private double amount;
    private String unit;

    public Nutrients(String name, double amount) {
        this.name = name;
        this.amount = amount;
        this.unit = "g";
    }
}
