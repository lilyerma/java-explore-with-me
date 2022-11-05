package explorewithme.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dto returned for statistics element")
public class StatsDto {

    @Schema(description = "App name or id")
    String app;
    @Schema(description = "Uri - what was requested")
    String uri;
    @Schema(description = "Count of the hits")
    Long hits;
}
