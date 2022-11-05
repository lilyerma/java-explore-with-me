package explorewithme.statistics.controller;

import explorewithme.statistics.dto.ApiCall;
import explorewithme.statistics.dto.StatsDto;
import explorewithme.statistics.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Controller for the statistics service")
public class StatsController {

    private final StatsService statsService;


    @Operation(description = "Gets request dto when app's endpoint is called")
    @PostMapping("/hit")
    public void create(@RequestBody ApiCall apiCall) throws RuntimeException {
        log.debug("POST request to statistics");
        statsService.create(apiCall);
    }

    @Operation(description = "Provides stats by filters")
    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam(name = "start")
                                       @Parameter(description = "Start of the period for the statistics") String start,
                                   @RequestParam(name = "end")
                                       @Parameter(description = "End of the period for the statistics")String end,
                                   @RequestParam (name = "uris",required = false)
                                       @Parameter(description = "List of URIs to provide stats")String[] uris,
                                   @RequestParam (name = "unique", defaultValue = "false")
                                       @Parameter(description = "Show only unique requestors' IPs ot all") boolean unique)
            throws RuntimeException {
        log.debug("GET request to controller: start " + start + ", end " + end +  ", unique " + unique);
        return statsService.getStats(start, end, uris, unique);
    }

}
