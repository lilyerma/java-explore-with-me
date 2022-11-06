package explorewithme.ewm.events.repository;

public enum SearchOperation {
    GREATER_THAN, //works with LocalDateTime
    LESS_THAN, //works with LocalDateTime
    GREATER_THAN_EQUAL,
    LESS_THAN_EQUAL,
    NOT_EQUAL,
    EQUAL,
    LIKE, //works with text search
    LIKE_START,
    LIKE_END,
    IN, //works with List<String> and List<Long>
    NOT_IN
}
