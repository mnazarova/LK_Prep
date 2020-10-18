package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Syllabus { // Учебный план
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "syllabus_id")
    private Long id;

    private Integer year;

    @JsonIgnore // когда 500
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id")
    private Department department;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="direction_id")
    private Direction direction;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id")
    private Profile profile;

    @JsonIgnore // когда 500
    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups;

    @JsonIgnore
    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SyllabusContent> syllabusesContent;

    public Syllabus() {}
}
