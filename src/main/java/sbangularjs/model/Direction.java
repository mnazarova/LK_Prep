package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Direction { // Направление
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "direction_id")
    private Long id;

    private String code;

    private String name;

//    @JsonIgnore
    @OneToMany(mappedBy = "direction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Syllabus> syllabuses;

    public Direction() {}
}
