package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class StudentSubgroup { // Студент подгруппы
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "student_subgroup_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subgroup_id")
    private Subgroup subgroup;

    public StudentSubgroup() {}

    public StudentSubgroup(Student student, Subgroup subgroup) {
        this.student = student;
        this.subgroup = subgroup;
    }
}
