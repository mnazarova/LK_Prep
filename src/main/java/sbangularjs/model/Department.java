package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Department { // Кафедра
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "department_id")
    private Long id;

    private String shortName;

    private String fullName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="faculty_id")
    private Faculty faculty;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Syllabus> syllabuses;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discipline> disciplines;

    public Department() {}
}