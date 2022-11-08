package explorewithme.ewm.requests.services;

import java.util.List;
import java.util.Map;

public interface UtilRequestService {
    int getCountOfApproveRequest(long eventId);

    Map<Long, Long> getConfirmedRequestsByEvents(List<Long> eventIds);
}
