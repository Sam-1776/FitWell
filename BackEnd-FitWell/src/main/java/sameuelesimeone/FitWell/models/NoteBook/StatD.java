package sameuelesimeone.FitWell.models.NoteBook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sameuelesimeone.FitWell.models.Diet.Diet;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "statsD")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class StatD {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "diet_id")
    @JsonIgnore
    private Diet diet;
    private LocalDate date;
    private double weight;

    public StatD(Diet diet, LocalDate date, double weight) {
        this.diet = diet;
        this.date = date;
        this.weight = weight;
    }
}
