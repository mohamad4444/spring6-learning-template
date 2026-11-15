-- MySQL variant of the consolidated schema. Use with the mysql profile.
create table app_user (
    id binary(16) primary key,
    first_name varchar(255), last_name varchar(255),
    email varchar(255) not null unique, password varchar(255)
);
create table advanced_user_entity_roles (
    advanced_user_entity_id binary(16) not null,
    roles varchar(255),
    foreign key (advanced_user_entity_id) references app_user(id)
);
create table jpa_note (id bigint auto_increment primary key, note varchar(255), tag varchar(255));
create table secured_note (id bigint auto_increment primary key, note varchar(255), tag varchar(255));
create table advanced_note (id bigint auto_increment primary key, note varchar(255), tag varchar(255), creator_id binary(16));
create table users (id bigint auto_increment primary key, username varchar(255), password varchar(255), role varchar(255));
create table benutzer (
    id integer auto_increment primary key,
    vorname varchar(255), nachname varchar(255), benutzername varchar(255),
    password_hash varbinary(255) not null, password_salt varbinary(255) not null
);
create table notiz (
    id integer auto_increment primary key,
    datum datetime(6), inhalt varchar(255), benutzer_id integer,
    foreign key (benutzer_id) references benutzer(id)
);
create table task (
    id binary(16) primary key, title varchar(120) not null, description varchar(2000) not null,
    status enum('TODO', 'IN_PROGRESS', 'DONE') not null, owner varchar(255) not null,
    version bigint not null, created_at datetime(6) not null, updated_at datetime(6) not null
);
create index idx_task_owner_status on task(owner, status);
