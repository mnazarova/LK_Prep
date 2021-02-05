package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class SplitAttestationForm { // Разделённая форма аттестации
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "split_attestation_form_id")
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "splitAttestationForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSheet> sessionSheets;

    public SplitAttestationForm() {}
}
