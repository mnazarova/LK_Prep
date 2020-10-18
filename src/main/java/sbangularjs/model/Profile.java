package sbangularjs.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Profile { // Профиль
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "profile_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Syllabus> syllabuses;

    public Profile() {}
}
