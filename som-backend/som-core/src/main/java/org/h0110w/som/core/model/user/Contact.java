package org.h0110w.som.core.model.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Contact {
    private String type;
    private String info;
    @Column(columnDefinition="text")
    private String description;
}
