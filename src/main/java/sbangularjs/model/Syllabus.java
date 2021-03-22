package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Syllabus { // Учебный план
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "syllabus_id")
    private Long id;

    private Integer year;

//    @JsonIgnore // когда 500
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="department_id")
    private Department department;

    //@JsonIgnore // need
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="direction_id")
    private Direction direction;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="profile_id")
    private Profile profile;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="qualification_id")
    private Qualification qualification;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="form_of_training_id")
    private FormOfTraining form_of_training;

//    @JsonIgnore // need
    @OrderBy("id")
    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups;

    @JsonIgnore
    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SyllabusContent> syllabusesContent;

    @Transient
    private Date deadline; // for deanery and deputyDean

    public Syllabus() {}
}
