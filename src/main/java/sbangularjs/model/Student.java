package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "student_id")
    private Long id;

    private String surname;

    private String name;

    private String patronymic;

    private String numberRecordBook;

    private Boolean expelled;

//    @JsonIgnore // когда 500
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_id")
    private Group group;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttestationContent> attestationContents;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheetContent> sessionSheetContents;

    public Student() {}

    public Student(String surname, String name, String numberRecordBook, Boolean expelled) {
        this.surname = surname;
        this.name = name;
        this.numberRecordBook = numberRecordBook;
        this.expelled = expelled;
    }
}
