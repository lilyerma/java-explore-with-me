package explorewithme.ewm.events.controllers;

import explorewithme.ewm.events.admin.UpdateEventRequest;
import explorewithme.ewm.events.dto.EventFullDto;
import explorewithme.ewm.events.dto.EventShortDto;
import explorewithme.ewm.events.dto.NewEventDto;
import explorewithme.ewm.comments.service.CommentService;
import explorewithme.ewm.events.service.EventService;
import explorewithme.ewm.requests.dto.ParticipationRequestDto;
import explorewithme.ewm.requests.services.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Controler for authenticated users", description = "Includes management for events, categories, requests and comments")
public class EventsControllerPrivate {

    private final EventService eventService;
    private final RequestService requestService;

    private final CommentService commentService;

    @Operation(summary = "Get page of the events by user (as initiator)")
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@Positive @PathVariable long userId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                @Parameter(description = "Start element of the page")int from,
                                               @PositiveOrZero @RequestParam(name = "size", defaultValue = "10")
                                                   @Parameter(description = "Size of the page")int size)
            throws RuntimeException {

        log.debug("Get request to private controller get events by user "+ userId);
        return eventService.getEventsByUser(userId, from, size);
    }

    @Operation(summary = "Post update of the event it is in the PENDING")
    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(@Positive @PathVariable long userId,
                                    @Valid @RequestBody UpdateEventRequest updateEventRequest) throws RuntimeException {
        log.debug("Patch request to private controller to update event by user "+ userId);
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @Operation(summary = "User posts new event")
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@Positive @PathVariable long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) throws RuntimeException {
        log.debug("Post request to private controller to create event by user "+ userId);
        return eventService.createEvent(userId, newEventDto);
    }

    @Operation(summary = "Gets full info on event for the initiator")
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventsByUser(@Positive @PathVariable long userId,
                                        @Positive @PathVariable long eventId)
            throws RuntimeException {
        log.debug("Get request to private controller to get event " + eventId + " by user "+ userId);
        return eventService.getEventsById(userId, eventId);
    }

    @Operation(summary = "Initiator cancels event")
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@Positive @PathVariable long userId,
                                        @Positive @PathVariable long eventId)
            throws RuntimeException {
        log.debug("Patch request to private controller to cancel event " + eventId + " by user" + userId);
        return eventService.cancelEvent(userId, eventId);
    }

    @Operation(summary = "User gets requests for participation for self-initiated events")
    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@Positive @PathVariable long userId,
                                                     @Positive @PathVariable long eventId)
        throws RuntimeException {
        log.debug("Get request to private controller to get event " + eventId + " by user" + userId);
        return requestService.getRequestsByEvent(userId, eventId);
    }

    @Operation(summary = "User confirms request for participation for self-initiated event")
    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@Positive @PathVariable long userId,
                                                  @Positive @PathVariable long eventId,
                                                  @Positive @PathVariable long reqId)
        throws RuntimeException {
        log.debug("Patch request to private controller to confirm request "  + reqId + " event "
                + eventId + " by user" + userId);
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @Operation(summary = "User rejects request for participation for self-initiated events")
    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@Positive @PathVariable long userId,
                                                 @Positive @PathVariable long eventId,
                                                 @Positive @PathVariable long reqId)
            throws RuntimeException {
        log.debug("Patch request to private controller to request request " + reqId + " event "
                + eventId + " by user" + userId);
        return requestService.rejectRequest(userId, eventId, reqId);
    }

}
