package org.h0110w.som.core.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h0110w.som.core.model.Group;
import org.h0110w.som.core.model.tasks.Task;
import org.h0110w.som.core.model.user_account.UserAccount;
import org.h0110w.som.core.service.UsersService;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

/**
 * represents person who is using an app
 * cant exist without userAccount
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private UUID id;

    @MapsId
    @OneToOne(optional = false)
    @org.hibernate.annotations.OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount account;

    private String displayName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "group_users",
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn(nullable = false))
    private Group group;

    @OneToOne(mappedBy = "lead")
    private Group controlledGroup;

    private String roleName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responsible")
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "task_watcher",
            joinColumns = @JoinColumn(name = "watcher_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id", nullable = false))
    private Set<Task> watchedTasks = new HashSet<>();

    private PersonalData personalData = new PersonalData();

    public User(UserAccount account) {
        this.account = account;
        this.displayName = UsersService.UNNAMED;
    }

    public void assignTask(Task task) {
        tasks.add(task);
        task.setResponsible(this);
    }

    public void watchTask(Task task) {
        watchedTasks.add(task);
        task.getWatchers().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
