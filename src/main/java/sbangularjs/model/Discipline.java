package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

// если сериализация происходит до того, как загрузится сущность, то либо
// (@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})), либо закомментировать (fetch = FetchType.EAGER)
//   @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne//(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "discipline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SyllabusContent> syllabusesContent;

    public Discipline() {}
}
