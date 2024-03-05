package sameuelesimeone.FitWell.models;

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
public class Set {
    @Id
    @GeneratedValue
    private UUID id;
    private int rep;
    private double weight;

    public Set(int rep, double weight) {
        this.rep = rep;
        this.weight = weight;
    }
}
