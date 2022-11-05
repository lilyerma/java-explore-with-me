package explorewithme.ewm.compilation.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "compilation_events", schema = "public")
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CompilationEvent.class)
public class CompilationEvent implements Serializable {


    @Id
    @Column (name = "compilation_id")
    private long id;
    @Id
    @Column (name = "event_id")
    private long event;

}
