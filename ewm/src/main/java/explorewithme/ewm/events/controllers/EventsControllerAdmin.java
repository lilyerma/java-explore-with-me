package explorewithme.ewm.events.controllers;

import explorewithme.ewm.compilation.service.CompilationService;
import explorewithme.ewm.compilation.dto.CompilationDto;
import explorewithme.ewm.compilation.dto.NewCompilationDto;
import explorewithme.ewm.events.admin.AdminUpdateEventRequest;
import explorewithme.ewm.events.dto.CategoryDto;
import explorewithme.ewm.events.dto.EventFullDto;
import explorewithme.ewm.events.dto.NewCategoryDto;
import explorewithme.ewm.events.service.CategoryService;
import explorewithme.ewm.events.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "admin")
@RequiredArgsConstructor
@Tag(name = "Controller for admins", description = "Includes management for events, categories, compiltations and comments")
public class EventsControllerAdmin {

    private final EventService eventService;
    private final CompilationService compilationService;
    private final CategoryService categoryService;



    // Admin methods to work with events

    @Operation(
            summary = "Get events according to filters",
            description = "Pages of the events with full information"
    )
    @GetMapping("/events")
    public List<EventFullDto> getEventsForAdmin(@RequestParam(name = "users", required = false)
                                                    @Parameter(description = "List of user ids, not mandatory") long[] users,
                                                @RequestParam(name = "states", required = false)
                                                    @Parameter(description = "List of event states, not mandatory") String[] states,
                                                @RequestParam(name = "categories", required = false)
                                                    @Parameter(description = "List of categories") long[] categories,
                                                @RequestParam(name = "rangeStart", required = false)
                                                    @Parameter(description = "Start of the search period for the event") String start,
                                                @RequestParam(name = "rangeEnd", required = false)
                                                    @Parameter(description = "End of the search period for the event")String end,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                    @Parameter(description = "Start element for the page")int from,
                                                @PositiveOrZero @RequestParam(name = "size", defaultValue = "10")
                                                    @Parameter(description = "Size of the page")int size)
            throws RuntimeException {

        log.debug("Get request from admin, filters: users " + users + " states" + "categories " + categories + "start "
                + start + "end " + end + "from " + from +  "size " + size);
        return eventService.getEventsForAdmin(users, states, categories, start, end, from, size);
    }

    @Operation(
            summary = "Update event by admin",
            description = "Admin can update event information"
    )
    @PutMapping("/events/{eventId}")
    public EventFullDto updateEventAdmin(@RequestBody AdminUpdateEventRequest adminRequest,
                                         @Positive @PathVariable long eventId) throws RuntimeException {
        log.debug("Put request by admin update event with id "+ eventId);
        return eventService.updateEventAdmin(adminRequest, eventId);
    }

    @Operation(
            summary = "Publish event",
            description = "Admin sets status of the event to PUBLISHED"
    )
    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEventAdmin(@Positive @PathVariable long eventId) throws RuntimeException {
        log.debug("Patch request by admin publish event with id "+ eventId);
        return eventService.publishEvent(eventId);
    }

    @Operation(
            summary = "Reject event",
            description = "Admin sets status of the event to REJECTED"
    )
    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEventAdmin(@Positive @PathVariable long eventId) throws RuntimeException {
        log.debug("Patch request by admin to reject event with id "+ eventId);
        return eventService.rejectEvent(eventId);
    }

    //Admin methods to work with Categories
    @Operation(
            summary = "Admin changes name of the category"
    )
    @PatchMapping("/categories")
    public CategoryDto editCategoryAdmin(@RequestBody CategoryDto categoryDto) throws RuntimeException {
        log.debug("Patch request by admin to edit category");
        return categoryService.updateCategory(categoryDto);
    }

    @Operation(
            summary = "Admin adds new category"
    )
    @PostMapping("/categories")
    public CategoryDto addCategoryAdmin(@RequestBody NewCategoryDto categoryDto) throws RuntimeException {
        log.debug("Post request by admin to create category");
        return categoryService.createCategory(categoryDto);
    }

    @Operation(
            summary = "Admin deletes category"
    )
    @DeleteMapping("/categories/{catId}")
    public void deleteCategoryAdmin(@Positive @PathVariable long catId) throws RuntimeException {
        log.debug("Delete request by admin category " + catId);
         categoryService.deleteCategory(catId);
    }

    //Admin work with events compilation
    @Operation(
            summary = "Admin creates new compilation of the events"
    )
    @PostMapping("/compilations")
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto)
            throws RuntimeException {
        log.debug("Post request by admin to create compilation");
        return compilationService.create(newCompilationDto);
    }

    @Operation(
            summary = "Admin deletes empty compilation of the events"
    )
    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilationById(@Positive @PathVariable long compId) throws RuntimeException {
        log.debug("Delete request by admin to delete compilation " + compId);
        compilationService.delete(compId);
    }

    @Operation(
            summary = "Admin removes event from compilation of the events"
    )
    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@Positive @PathVariable long compId,
                                           @Positive @PathVariable long eventId)
            throws RuntimeException {
        log.debug("Delete event from compilation by admin request");
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @Operation(
            summary = "Admin adds event to compilation of the events"
    )
    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@Positive @PathVariable long compId,
                                                @Positive @PathVariable long eventId)
            throws RuntimeException {
        log.debug("Patch add event from compilation by admin request");
        compilationService.addEventToCompilation(compId, eventId);
    }

    @Operation(
            summary = "Admin unpins event on the front page"
    )
    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@Positive @PathVariable long compId)
            throws RuntimeException {
        log.debug("Patch pin compilation by admin request");
        compilationService.unpinCompilation(compId);
    }

    @Operation(
            summary = "Admin pins event on the front page"
    )
    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@Positive @PathVariable long compId)
            throws RuntimeException {
        log.debug("Patch unpin compilation by admin request");
        compilationService.pinCompilation(compId);
    }

}
