package sameuelesimeone.FitWell.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workouts")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Workout {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    private Exercise exercise;
    @OneToMany
    @JoinColumn(name = "sets")
    private List<Set> sets;

    public Workout(Exercise exercise, List<Set> sets) {
        this.exercise = exercise;
        this.sets = sets;
    }
}
