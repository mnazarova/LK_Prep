package sbangularjs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class DeaneryGroupOrSubgroup {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "deanery_group_or_subgroup_id")
    private Long id;

    private Boolean isSubgroup;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="deanery_id")
    private Deanery deanery;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id")
    private Group group;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subgroup_id")
    private Subgroup subgroup;


    public DeaneryGroupOrSubgroup() {}
}
