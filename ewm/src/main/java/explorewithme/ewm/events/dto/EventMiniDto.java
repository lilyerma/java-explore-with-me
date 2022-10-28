package explorewithme.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMiniDto {

    long eventId;
    String eventTitle;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

}
