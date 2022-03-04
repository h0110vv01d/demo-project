package org.h0110w.som.core.model.user;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Embeddable
@Data
public class PersonalData {
    private String firstName;
    private String lastName;
    private String middleName;
    private String info;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn)
//    @org.hibernate.annotations.CollectionId(
//            columns = @Column(name = "contact_id"),
//            type = @org.hibernate.annotations.Type(type = "long"),
//            generator = Generators.LONG_ID_GENERATOR)
    private Collection<Contact> contacts = new ArrayList<>();
}
