package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Faculty { // Факультет
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "faculty_id")
    private Long id;

    private String shortName;

    private String fullName;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Department> departments;

    public Faculty() {}
}
