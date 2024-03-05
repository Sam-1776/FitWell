package sameuelesimeone.FitWell.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "workout_id")
    @JsonIgnore
    private Workout workout;

    public Set(int rep, double weight) {
        this.rep = rep;
        this.weight = weight;
    }
}
