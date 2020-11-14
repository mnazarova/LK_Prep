package sbangularjs.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class FormOfTraining { // Форма обучения
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "form_of_training_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "form_of_training", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Syllabus> syllabuses;

    public FormOfTraining() {}
}
