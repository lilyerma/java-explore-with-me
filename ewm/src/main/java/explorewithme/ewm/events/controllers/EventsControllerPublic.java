package explorewithme.ewm.events.controllers;

import explorewithme.ewm.compilation.service.CompilationService;
import explorewithme.ewm.compilation.dto.CompilationDto;
import explorewithme.ewm.events.dto.CategoryDto;
import explorewithme.ewm.events.dto.EventFullDto;
import explorewithme.ewm.events.dto.EventShortDto;
import explorewithme.ewm.events.dto.StatsDto;
import explorewithme.ewm.events.repository.FilterSort;
import explorewithme.ewm.events.service.CategoryService;
import explorewithme.ewm.events.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Public controler for any user", description = "Includes get requests for events, categories, and compilations")
public class EventsControllerPublic {

    private final EventService eventService;
    private final CompilationService compilationService;

    private final CategoryService categoryService;

    @Operation(summary = "Public endpoint to get list of events by pages and filters")
    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(name = "text", required = false)
                                             @Parameter(description = "Text to search, optional")String text,
                                         @RequestParam(name = "categories", required = false)
                                            @Parameter(description = "Lists of the categories, optional")long[] categories,
                                         @RequestParam(name = "rangeStart", required = false)
                                             @Parameter(description = "Start of the search period for the event")String start,
                                         @RequestParam(name = "rangeEnd", required = false)
                                             @Parameter(description = "End of the search period for the event")String end,
                                         @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                             @Parameter(description = "Looking only for the events with available participation limit")boolean onlyAvailable,
                                         @RequestParam(name = "filterSort", defaultValue = "EVENT_DATE") FilterSort filterSort,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                             @Parameter(description = "Start element for the page")int from,
                                         @PositiveOrZero @RequestParam(name = "size", defaultValue = "10")
                                             @Parameter(description = "Size of the page")int size,
                                         HttpServletRequest request)
            throws RuntimeException {

        log.debug("Get request to public API for events with filters: text " + text + " categories " + categories + ", rangeStart " + start
                + ", rangeEnd " + end + ", onlyAvailable " + onlyAvailable + ", filterSort " + filterSort + ", from" + from + ", size " + size);


        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        sendStats(request);

        return eventService.getEvents(text,categories,start,end,onlyAvailable, filterSort,from,size);
    }

    @Operation(summary = "Public endpoint to get event by id")

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@Positive @PathVariable long id, HttpServletRequest request) throws RuntimeException {

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        sendStats(request);

        return eventService.getEventById(id);
    }

    @Operation(summary = "Public endpoint to get list of categories")
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                           @PositiveOrZero @RequestParam(name = "size", defaultValue = "10") int size)
            throws RuntimeException {

        log.debug("Get request to public Api for categories");
        return categoryService.getCategories(from, size);
    }

    @Operation(summary = "Public endpoint to get category by id")
    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@Positive @PathVariable int catId)
            throws RuntimeException {
        log.debug("Get request to public Api for category by id " + catId);
        return categoryService.getCategoryById(catId);
    }

    @Operation(summary = "Public endpoint to get compilations of the events by pages and pinned status")
    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                @PositiveOrZero @RequestParam(name = "size", defaultValue = "10") int size,
                                              @RequestParam(name = "pinned", required = false) boolean pinned)
            throws RuntimeException {
        log.debug("Get request to public Api for compilations with filter pinned:" + pinned);
        return compilationService.getComilations(from, size, pinned);
    }

    @Operation(summary = "Public endpoint to get compilation by id")
    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@Positive @PathVariable int compId)
            throws RuntimeException {
        log.debug("Get request to public Api for compilation with id: " + compId);
        return compilationService.getComilationById(compId);
    }

    // Method to send hits and uris

    private void sendStats(HttpServletRequest request) {
        Mono<ClientResponse> clientResponse = WebClient.builder().build()
                .post().uri("http://localhost:9090/hit")
                .body(Mono.just(new StatsDto("ewm", request.getRequestURI(),
                        request.getRemoteAddr(), LocalDateTime.now().toString())), StatsDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        clientResponse.subscribe((response) -> {

            // here you can access headers and status code
            ClientResponse.Headers headers = response.headers();
            HttpStatus stausCode = response.statusCode();

            Mono<String> bodyToMono = response.bodyToMono(String.class);
            // the second subscribe to access the body
            bodyToMono.subscribe((body) -> {

                log.info("stausCode:" + stausCode + " body: " + body + " headers: " + headers.asHttpHeaders());

            },(ex) -> {
                throw new RuntimeException("stats not working");
            });
        }, (ex) -> {
            throw new RuntimeException("network bad");
        });
    }

}
