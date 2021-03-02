package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class DeputyDean { // Зам. декана
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "deputy_dean_id")
    private Long id;

    private boolean active;

    @JsonIgnore // сокрытие данных
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "deputyDean", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups;

    public DeputyDean() {}
}
