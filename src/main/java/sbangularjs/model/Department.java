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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceWork> placesWork;

    @JsonIgnore
    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL)
    private Secretary secretary;

//    @JsonIgnore
    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL)
    private Teacher headDepartment; // need

    @Transient
    private List<Group> groupsAssignmentDeputyDean;

    public Department() {}
}
