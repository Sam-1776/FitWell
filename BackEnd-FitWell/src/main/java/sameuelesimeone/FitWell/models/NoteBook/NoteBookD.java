package sameuelesimeone.FitWell.models.NoteBook;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "noteBooksD")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoteBookD {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "stat_id")
    private List<StatD> stats;
    private int RMR;
    private double weight;
}
