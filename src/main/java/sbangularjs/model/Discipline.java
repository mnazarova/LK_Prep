package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Discipline { // Дисциплина
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "discipline_id")
    private Long id;

    private String name;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SyllabusContent> syllabusesContent;

    public Discipline() {}
}
