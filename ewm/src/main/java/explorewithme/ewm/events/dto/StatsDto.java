package explorewithme.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto to send to Statistics module")
public class StatsDto {

    @Schema(description = "name of the service, here is EWM")
    String app;
    @Schema(description = "URI of the request")
    String uri;
    @Schema(description = "IP address form where the call came")
    String ip;
    @Schema(description = "Timestamp for the call")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonSerialize
    String timestamp;

}
