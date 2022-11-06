package explorewithme.ewm.events.service;

import explorewithme.ewm.events.admin.AdminUpdateEventRequest;
import explorewithme.ewm.events.admin.UpdateEventRequest;
import explorewithme.ewm.events.dto.*;
import explorewithme.ewm.events.model.Event;
import explorewithme.ewm.search.FilterSort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EventService {


    void checkEventId(long id);

    List<EventShortDto> getEvents(String text, long[] categories, Boolean paid, String startStr, String endStr, boolean onlyAvailable,
                                  FilterSort sort, int from, int size);

    EventFullDto getEventById(long id);

  //  EventShortDto getEventByIdShort(long id);


    List<EventShortDto> eventsShortDtoList(List<Event> events);

    List<EventShortDto> getEventsByUser(long userId, int from, int size);

    List<EventFullDto> getEventsForAdmin(long[] users, String[] states, long[] categories, String startStr,
                                         String endStr, int from, int size);

    EventFullDto updateEventAdmin(AdminUpdateEventRequest adminRequest, long eventId);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);

    EventFullDto updateEvent(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto getEventsById(long userId, long eventId);

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto cancelEvent(long userId, long eventId);

    boolean checkOwnership(long userId, long eventId);

    int checkRequestIsAllowed(long userId, long eventId);

    Map<Long, EventMiniDto> getEventsByIds(List<Long> eventIds);

    //Method for the Comments service, returns eventMiniDtos to add to comments
    EventMiniDto getEventMiniByIds(Long eventIds);

    //Method for the Comments service, returns eventMiniDtos to add to comments
    List<EventShortDto> getShortEventsByIds(List<Long> eventIds);
}
