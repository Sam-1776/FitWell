package sameuelesimeone.FitWell.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Muscle muscle;
    @Column(columnDefinition = "TEXT")
    private String instructions;
    @Enumerated(EnumType.STRING)
    private Exp exp;
    public Exercise(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }
}
