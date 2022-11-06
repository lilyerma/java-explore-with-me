package explorewithme.statistics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Expected Dto to store stats for the endpoint call")
public class ApiCall {

    private long id;
    @Schema(description = "Name of the app for which we collect info")
    private String app;
    @Schema(description = "Uri of the call")
    private String uri;
    @Schema(description = "Ip of the user")
    private String ip;
    @Schema(description = "Timestamp of the call")
    @JsonDeserialize
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;


}
