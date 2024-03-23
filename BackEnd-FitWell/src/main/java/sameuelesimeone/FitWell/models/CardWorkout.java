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
@Table(name = "cardsWorkout")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CardWorkout {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "cardWorkout", cascade = CascadeType.ALL)
    private List<Workout> workouts;
    private int restTimer;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    public CardWorkout(String name, List<Workout> workouts, int restTimer, User user, User coach) {
        this.name = name;
        this.workouts = workouts;
        this.restTimer = restTimer;
        this.user = user;
        this.coach = coach;
    }
}
