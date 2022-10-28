package explorewithme.ewm.requests.services;

import explorewithme.ewm.requests.RequestRepository;
import explorewithme.ewm.requests.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public boolean hasApproveRequests(long userId, long eventId) {
        log.debug("requesting request repo for approveed requests for the user and for the event");
        if(requestRepository.getRequestsByEventAndAndRequesterAndStatus(eventId, userId, Status.CONFIRMED) ==0){
            return false;
        }
        return true;
    }


}
