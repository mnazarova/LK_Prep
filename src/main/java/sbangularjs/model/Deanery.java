package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Deanery {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "deanery_id")
    private Long id;

    private boolean active;
    private boolean isDeputyDean; // Заместитель декана

    @JsonIgnore // сокрытие данных
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "deanery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeaneryGroup> deaneryGroupList;


    public Deanery() {}
}
