package org.h0110w.som.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h0110w.som.core.configuration.db.Generators;
import org.h0110w.som.core.model.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "user_groups")
public class Group {
    @Id
    @GeneratedValue(generator = Generators.UUID_GENERATOR)
    private UUID id;

    private String name;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private User lead;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "group")
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
        user.setGroup(this);
    }

    public void assignLead(User user) {
        lead = user;
        user.setControlledGroup(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id.equals(group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
