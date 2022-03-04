package org.h0110w.som.core.model.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.h0110w.som.core.configuration.db.Generators;
import org.h0110w.som.core.model.user.User;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Task {
    @Id
    @GeneratedValue(generator = Generators.LONG_ID_GENERATOR)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(updatable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn
    private User responsible;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "watchedTasks")
    private Set<User> watchers = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinTable(name = "task_subtask",
            joinColumns = @JoinColumn(name = "super_task_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_task_id", nullable = false))
    @org.hibernate.annotations.OnDelete(action = OnDeleteAction.CASCADE)
    private Task superTask;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "superTask")
    private Set<Task> subTasks = new HashSet<>();

    public void assignResponsible(User user){
        this.responsible = user;
        user.getTasks().add(this);
    }

    public void attachToSuperTask(Task superTask){
        this.superTask = superTask;
        superTask.getSubTasks().add(this);
    }

    public void addWatcher(User user) {
        this.watchers.add(user);
        user.getWatchedTasks().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
//    @org.hibernate.annotations.CreationTimestamp
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
//    @org.hibernate.annotations.UpdateTimestamp
    private Date updated;

    @PrePersist
    protected void onCreate() {
        updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
