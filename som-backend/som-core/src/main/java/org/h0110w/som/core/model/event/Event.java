package org.h0110w.som.core.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * entity for registering events that might happen during working of an app
 * contains EventInfo
 * @see EventInfo
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "log_events")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LogEventIdSeq")
    @SequenceGenerator(name = "LogEventIdSeq", sequenceName = "log_events_id_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "eventType")
    private EventType eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @NotNull
    private ZonedDateTime stamp;

    @Type(type = "jsonb")
    @Column(name = "info", nullable = false, columnDefinition = "json default '{}' ")
    protected EventInfo info;

    public Event(EventType eventType) {
        this.eventType = eventType;
        this.stamp = ZonedDateTime.now();;
    }
}
