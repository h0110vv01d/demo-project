package org.h0110w.som.core.model.event;

/**
 * enum class used to distinguish one event from another
 */
public enum EventType {
    USER_AUTH,
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    POLL_CREATED,
    POLL_UPDATED,
    POLL_DELETED,
    VOTE_OPTION_CREATED,
    VOTE_OPTION_UPDATED,
    VOTE_OPTION_DELETED,
    VOTE_CREATED,
    VOTE_UPDATED,
    VOTE_DELETED
}
