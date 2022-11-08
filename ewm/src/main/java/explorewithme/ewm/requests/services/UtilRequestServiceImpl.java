package explorewithme.ewm.requests.services;

import explorewithme.ewm.requests.RequestRepository;
import explorewithme.ewm.requests.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service // Service to avoid cyclic dependecy with event service
@RequiredArgsConstructor
@Slf4j
public class UtilRequestServiceImpl implements UtilRequestService {


    private final RequestRepository requestRepository;


    @Override
    public int getCountOfApproveRequest(long eventId) {
        log.debug("requesting count of confirmed requests from request repo");
        return requestRepository.countRequestByEventAndStatus(eventId);
    }

    @Override
    public Map<Long, Long> getConfirmedRequestsByEvents(List<Long> eventIds) {
        return requestRepository.findRequestsByEventInAndAndStatusEquals(eventIds,Status.CONFIRMED).stream()
                .map(request -> request.getEvent())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


}


