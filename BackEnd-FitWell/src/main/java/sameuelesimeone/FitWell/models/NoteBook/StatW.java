package sameuelesimeone.FitWell.models.NoteBook;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sameuelesimeone.FitWell.models.CardWorkout;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "statsW")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class StatW {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "cardWorkout_id")
    private CardWorkout cardWorkout;
    private LocalDate date;

    public StatW(CardWorkout cardWorkout, LocalDate date) {
        this.cardWorkout = cardWorkout;
        this.date = date;
    }
}
