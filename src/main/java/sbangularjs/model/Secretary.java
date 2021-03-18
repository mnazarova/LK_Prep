package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Secretary {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "secretary_id")
    private Long id;

    private boolean active;

    @JsonIgnore // сокрытие данных
    private String username;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="department_id")
    private Department department;

    public Secretary() {}
}
