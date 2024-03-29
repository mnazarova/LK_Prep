package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Deanery { // Деканат
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "deanery_id")
    private Long id;

    private boolean active;

    @JsonIgnore // сокрытие данных
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "deanery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="faculty_id")
    private Faculty faculty;

    public Deanery() {}
}
