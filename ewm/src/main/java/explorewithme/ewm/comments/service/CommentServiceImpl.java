package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.CommentMapper;
import explorewithme.ewm.comments.dto.CommentDto;
import explorewithme.ewm.comments.dto.CommentDtoForLists;
import explorewithme.ewm.comments.dto.FullCommentDto;
import explorewithme.ewm.comments.model.Comment;
import explorewithme.ewm.comments.repository.CommentRepository;
import explorewithme.ewm.comments.repository.CommentSpecifications;
import explorewithme.ewm.events.dto.*;
import explorewithme.ewm.events.service.EventService;
import explorewithme.ewm.exception.ArgumentException;
import explorewithme.ewm.exception.ConflictException;
import explorewithme.ewm.exception.NotFoundException;
import explorewithme.ewm.requests.services.UtilRequestService;
import explorewithme.ewm.search.SearchCriteria;
import explorewithme.ewm.search.SearchOperation;
import explorewithme.ewm.users.UserService;
import explorewithme.ewm.users.dto.UserDto;
import explorewithme.ewm.users.dto.UserShortDto;
import explorewithme.ewm.util.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static explorewithme.ewm.events.State.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final EventService eventService;
    private final UtilRequestService utilRequestService;
    private final CommentRepository repository;


    @Override
    @Transactional
    public CommentDto postComment(long userId, long eventId, CommentDto commentDto) {
        checkUserCanPost(userId, eventId);
        Comment toSave = CommentMapper.fromDtoToComment(commentDto);
        toSave.setAuthor(userId);
        toSave.setEvent(eventId);
        return CommentMapper.shortFromComment(repository.save(toSave));
    }


    @Override
    public List<FullCommentDto> getCommentsByUser(long userId) {
        userService.checkId(userId);
        List<Comment> comments = repository.findCommentsByAuthor(userId);
        return addEventsAndUsers(comments);
    }

    // Good for single operation
    private List<FullCommentDto> addEventsAndUsers(List<Comment> comments){
        List<FullCommentDto> commentDtos = new ArrayList<>();
        Map<Long,Comment> authorsIds = comments.stream()
                .collect(Collectors.toMap(Comment::getAuthor, Function.identity()));
        Map<Long,Comment> eventsIds = comments.stream()
                .collect(Collectors.toMap(Comment::getEvent, Function.identity()));
        Map<Long,EventMiniDto> eventMinis = eventService.getEventsByIds(new ArrayList<>(eventsIds.keySet()));
        Map<Long,UserShortDto> shortDtos = userService.getUsersByIds(new ArrayList<>(authorsIds.keySet()));
        for (Comment comment : comments){
            FullCommentDto commentDto = CommentMapper.fromComment(comment);
            commentDto.setEvent(eventMinis.get(comment.getEvent()));
            commentDto.setAuthor(shortDtos.get(comment.getAuthor()));
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    @Override
    @Transactional
    public FullCommentDto publishComment(long commentId) {
        checkId(commentId);
        if(repository.getReferenceById(commentId).getState()==PENDING){
            log.debug("Sending request to repo to change status");
             repository.updateCommentStatus(PUBLISHED,false,  commentId, LocalDateTime.now());
        } else {
            log.debug("State is not pending, cannot publish from this state");
            throw new ArgumentException("State is not pending, cannot publish from this state");
        }

        return addEventsAndUsers(repository.getReferenceById(commentId));
    }

    @Override
    @Transactional
    public FullCommentDto rejectComment(long commentId) {
        checkId(commentId);
        log.debug("Sending request to repo to change status");
        repository.updateCommentStatus(REJECTED,false, commentId, LocalDateTime.now());
        return addEventsAndUsers(repository.getReferenceById(commentId));
    }

    @Override
    public List<FullCommentDto> getCommentsForAdmin(Long[] users, Long[] eventIds, String[] states, String text,
                                                    Boolean moderation, String startStr, String endStr, int from, int size) {
        LocalDateTime start = null;
        LocalDateTime end = LocalDateTime.now();
        if(endStr != null) {
            end = LocalDateTime.parse(startStr);
            if(end.isAfter(LocalDateTime.now())){
                log.debug("No comment for future events allowed");
                throw new ArgumentException("No comments for future events allowded");
            }
        }
        if(startStr != null){
            start = LocalDateTime.parse(endStr);
            if (end.isBefore(start)){
                log.debug("Time range end should be after start, specify earlier date of the start." +
                        " Default end is " + LocalDateTime.now());
                throw new ConflictException("Time range end should be after start, specify earlier date of the start." +
                        " Default end is " + LocalDateTime.now());
            }
        }

        String sortColumn = "created";

        log.debug("Parsed default filters: start" + start + ", end " + end + ", sort " + sortColumn);
        Pageable pageable = new OffsetBasedPageRequest(size, from, Sort.by(Sort.Direction.ASC, sortColumn));

        List<SearchCriteria> filters = new ArrayList<>();
        if (text != null){
            log.debug("Building search criteria for text");
            SearchCriteria filterByText = SearchCriteria.builder()
                    .key("") // keys are preset to search text in comments
                    .operation(SearchOperation.LIKE)
                    .value(text.toString().toLowerCase())
                    .build();
            filters.add(filterByText);
        }

        if (states != null) {
            log.debug("Building search criteria for event state");
            SearchCriteria filterByStates = SearchCriteria.builder()
                    .key("state")
                    .operation(SearchOperation.IN)
                    .value(states)
                    .type("List<String>")
                    .build();
            filters.add(filterByStates);
        }


        log.debug("Building search criteria for end");
        SearchCriteria filterByEnd = SearchCriteria.builder()
                .key("created")
                .operation(SearchOperation.LESS_THAN)
                .value(end.toString())
                .build();
        filters.add(filterByEnd);

        if (start!= null) {
            log.debug("Building search criteria for end");
            SearchCriteria filterByStart = SearchCriteria.builder()
                    .key("created")
                    .operation(SearchOperation.LESS_THAN)
                    .value(start.toString())
                    .build();
            filters.add(filterByEnd);
        }

        CommentSpecifications commentSpecification = new CommentSpecifications();
        filters.stream()
                .map(searchCriterion -> new SearchCriteria(searchCriterion.getKey(), searchCriterion.getOperation(),
                        searchCriterion.getValue(), searchCriterion.getType()))
                .forEach(commentSpecification::add);

        log.debug("Asking repo for Page of events according to search");

        Page<Comment> comments = repository.findAll(commentSpecification, pageable);

        return addEventsAndUsers(comments.toList());
    }


    @Override
    public List<CommentDtoForLists> getCommentsForPastEvent(long eventId) {
        if (eventService.getEventById(eventId).getEventDate().isAfter(LocalDateTime.now())){
            log.debug("Event is coming, cannot confirm comment for the future event");
            throw new ArgumentException("Event is in the future only past events can be commented");
        }

        List<Comment> comments = repository.getCommentsByEvent(eventId);
        return addUsersToLists(comments);

    }


    @Override
    @Transactional
    public void unpublishComment(long commentId) {
        checkId(commentId);
        log.debug("Sending request to repo to change status");
        if(repository.getReferenceById(commentId).getState()==PUBLISHED){
            repository.updateCommentStatus(REJECTED,false, commentId, LocalDateTime.now());
            log.debug("Switched published to rejected and turned off moderation");
        } else {
            log.debug("Comment was not published cannot unpublish");
            throw new ArgumentException("Comment was not published cannot unpublish");
        }
    }

    @Override
    @Transactional
    public void deleteComment(long userId, long commentId) {
        Comment comment =repository.getCommentsByAuthorAndId(userId,commentId);
        if(comment!=null){
            if(comment.getState()==PENDING){
                repository.delete(comment);
                log.debug("Comment deleted");
            } else {
                log.debug("Comment is not in PENDING");
                throw new ArgumentException("Comment is in state that cannot be deleted");
            }
        } else {
            log.debug("Comment and user with these ids were not found");
            throw new NotFoundException("Comment and user with these ids were not found");
        }
    }

    @Override
    public CommentDto updateComment(long userId, CommentDto commentDto) {
        if(commentDto.getId()==0){
            log.debug("No id for the comment specified");
            throw new ArgumentException("No id for the comment specified");
        }
        Comment comment =repository.getCommentsByAuthorAndId(userId,commentDto.getId());
        if(comment!=null){
            if(comment.getState()==PENDING){
                comment.setText(commentDto.getText());
                log.debug("Saving update");
                return CommentMapper.shortFromComment(repository.save(comment));
            } else {
                log.debug("Comment is not in PENDING");
                throw new ArgumentException("Comment is in state that cannot be deleted");
            }
        } else {
            log.debug("Comment and user with these ids were not found");
            throw new NotFoundException("Comment and user with these ids were not found");
        }
    }


    public void checkId(long id) {
        if (repository.findById(id).isEmpty()){
            log.debug("Comment with id "+ id + " not found");
            throw new NotFoundException("Comment with id "+ id + " not found");
        }
    }

    //Add user and event infor for single comment
    private FullCommentDto addEventsAndUsers(Comment comment){
        FullCommentDto commentDto = CommentMapper.fromComment(comment);
        commentDto.setAuthor(new UserShortDto(comment.getAuthor(), userService.getUserById(comment.getAuthor()).getName()));
        EventMiniDto eventMini = eventService.getEventMiniByIds(comment.getEvent());
        commentDto.setEvent(eventMini);
        return commentDto;
    }


    //Add user information to lists of comments
    private List<CommentDtoForLists> addUsersToLists(List<Comment> list){

        List<Long> userIds = new ArrayList<>();
        for (Comment comment: list){
            userIds.add(comment.getAuthor());
        }
        Map<Long,UserShortDto> userShorts = userService.getUsersByIds(userIds);

        List<CommentDtoForLists> toReturn = new ArrayList<>();
        for (Comment comment : list) {
            UserShortDto userShortDto = userShorts.get(comment.getAuthor());
            CommentDtoForLists commentDto = CommentMapper.fromCommentToListsDto(comment);
            commentDto.setUserDto(userShortDto);
            toReturn.add(commentDto);
        }

        return toReturn;
    }

    //Checking that users can post
    private void checkUserCanPost(long userId, long eventId) {
        eventService.checkEventId(eventId);
        userService.checkId(userId);
        if (!utilRequestService.hasApproveRequests(userId, eventId)){
            log.debug("No request with status confirmed found for the user for the event");
            throw new ArgumentException("No confirmed requests for the event for the user. Cannot confirm visit");
        }
        if (eventService.getEventById(eventId).getEventDate().isAfter(LocalDateTime.now())){
            log.debug("Event is comming, cannot confirm comment for the future event");
            throw new ArgumentException("Event is in the future only past events can be commented");
        }
    }


}
