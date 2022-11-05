package explorewithme.ewm.requests.services;

public interface UtilRequestService {
    int getCountOfApproveRequest(long eventId);

    boolean hasApproveRequests(long userId, long eventId);
}
