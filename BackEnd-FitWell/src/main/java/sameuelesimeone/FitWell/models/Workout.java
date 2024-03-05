package sameuelesimeone.FitWell.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<Set> sets;

    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private CardWorkout cardWorkout;

    public Workout(Exercise exercise, List<Set> sets) {
        this.exercise = exercise;
        this.sets = sets;
    }
}
