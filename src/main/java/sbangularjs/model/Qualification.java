package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Qualification { // Квалификация
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "qualification_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "qualification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Syllabus> syllabuses;

    public Qualification() {}
}
