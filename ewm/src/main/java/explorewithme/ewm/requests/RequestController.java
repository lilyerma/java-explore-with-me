package explorewithme.ewm.requests;

import explorewithme.ewm.requests.dto.ParticipationRequestDto;
import explorewithme.ewm.requests.services.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Requests controller for authenticated users", description = "Helps users manage self-created requests")
public class RequestController {

    //Users manage own requests to events
    private final RequestService requestService;

    @Operation(summary = "User gets self-created request")
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestByUser(@PathVariable long userId){
        log.debug("Get request to private user api call for requests");
        return requestService.getRequestsByUser(userId);
    }
    @Operation(summary = "User posts request to other user's event")
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequest(@PathVariable long userId,
                                                @RequestParam long eventId){
        log.debug("Post request to private user api to post request for event " + eventId);
        return requestService.createRequest(userId, eventId);
    }

    @Operation(summary = "User cancels self-created request")
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                @PathVariable long requestId){
        log.debug("Patch request to private user api to cancel own request for the event, req no." + requestId);
        return requestService.cancelRequest(userId, requestId);
    }

}
