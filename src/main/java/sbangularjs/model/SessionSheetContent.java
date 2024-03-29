package sbangularjs.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class SessionSheetContent { // Содержание сессионной ведомости
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "session_sheet_content_id")
    private Long id;

    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Transient
    private Evaluation admittance; // Допуск

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="evaluation_id")
    private Evaluation evaluation; // Оценивание

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="session_sheet_id")
    private SessionSheet sessionSheet; // Сессионная ведомость

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="set_evaluation_by_teacher")
    private Teacher setEvaluationByTeacher; // какой преподаватель установил поле "Оценивание"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="student_id")
    private Student student;

    public SessionSheetContent() {}
}
