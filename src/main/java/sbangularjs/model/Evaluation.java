package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Evaluation { // Оценивание
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "evaluation_id")
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheetContent> sessionSheetContents;

    public Evaluation() {}
}
