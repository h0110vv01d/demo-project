create sequence log_events_id_seq start 1 increment 1;
create sequence long_id_generator start 1 increment 1;

create table group_users (
                             group_id uuid not null,
                             account_id uuid not null,
                             primary key (account_id)
);

create table log_events (
                            id int8 not null,
                            event_type varchar(255),
                            info json default '{}' not null,
                            stamp timestamp,
                            primary key (id)
);

create table task_subtask (
                              sub_task_id int8 not null,
                              super_task_id int8 not null,
                              primary key (super_task_id)
);

create table task_watcher (
                              watcher_id uuid not null,
                              task_id int8 not null,
                              primary key (watcher_id, task_id)
);

create table tasks (
                       id int8 not null,
                       created timestamp,
                       description text,
                       name varchar(255),
                       status varchar(255),
                       updated timestamp,
                       created_by_account_id uuid,
                       responsible_account_id uuid,
                       primary key (id)
);

create table user_accounts (
                               id uuid not null,
                               built_in boolean,
                               login varchar(255),
                               user_account_status varchar(255),
                               user_account_type varchar(255),
                               primary key (id)
);

create table user_contacts (
                               user_account_id uuid not null,
                               description text,
                               info varchar(255),
                               type varchar(255)
);

create table user_groups (
                             id uuid not null,
                             name varchar(255),
                             lead_account_id uuid,
                             primary key (id)
);

create table users (
                       display_name varchar(255),
                       first_name varchar(255),
                       info varchar(255),
                       last_name varchar(255),
                       middle_name varchar(255),
                       role_name varchar(255),
                       account_id uuid not null,
                       primary key (account_id)
);

alter table group_users
    add constraint FKpnfjqos3rlmgpiu1ofjvs8lrb
        foreign key (group_id)
            references user_groups;

alter table group_users
    add constraint FK7j69dh5lcp6hk3htw93rriple
        foreign key (account_id)
            references users;

alter table task_subtask
    add constraint FKw1x41dr8gwss9o9rn8kyfr0g
        foreign key (sub_task_id)
            references tasks
            on delete cascade;

alter table task_subtask
    add constraint FKlyxdhcso4utoqytr7symgdvw0
        foreign key (super_task_id)
            references tasks;

alter table task_watcher
    add constraint FKskisuyqpfljao5gr7jaldyelq
        foreign key (task_id)
            references tasks;

alter table task_watcher
    add constraint FK83v05tfr5r14727tyn2sa8hbi
        foreign key (watcher_id)
            references users;

alter table tasks
    add constraint FKjpyd9ngq3pkibocx3ikh7ivo9
        foreign key (created_by_account_id)
            references users;

alter table tasks
    add constraint FK350i12cbij9ne747l9atfcm7p
        foreign key (responsible_account_id)
            references users;

alter table user_contacts
    add constraint FKceroykvy45m6vakkdjnpbhece
        foreign key (user_account_id)
            references users;

alter table user_groups
    add constraint FK9wj8k5q27vkvbiapodvtch8p4
        foreign key (lead_account_id)
            references users;

alter table users
    add constraint FKbf6v1xd7i9ghwmlfawwnmibeg
        foreign key (account_id)
            references user_accounts
            on delete cascade;
