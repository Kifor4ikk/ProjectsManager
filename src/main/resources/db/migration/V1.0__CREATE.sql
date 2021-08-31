
create schema IF NOT EXISTS project_manager;

SET search_path TO PROJECT_MANAGER, public;

create table IF NOT EXISTS user_entity
(
	ID bigserial not null,
	name varchar(25) not null,
	email varchar not null unique,
	password varchar not null,
	role smallint not null,
	status smallint not null,
	constraint pk_user_entity_ID primary key (ID)
);

create table IF NOT EXISTS project_entity
(
	ID bigserial not null,
	adminId bigint not null,
	name varchar(25) not null unique,
	accessCode varchar(10) not null unique,
	status smallint not null,
	constraint pk_project_entity_ID primary key (ID)
);

create table IF NOT EXISTS task_entity
(
	ID bigserial not null,
	projectid bigserial not null,
	name varchar(25) not null,
	body varchar(1000) not null,
	status smallint not null,
	FOREIGN key (projectId) references project_entity(id),
	constraint pk_task_entity_ID primary key (ID)
);

create table IF NOT EXISTS user_project
(
	userId bigint not null,
	projectId bigint not null,
	CONSTRAINT fk__user_project_to_user_entity FOREIGN KEY (userId)
	REFERENCES user_entity (id) ON DELETE CASCADE ON UPDATE cascade,
	CONSTRAINT fk__user_project_to_project_entity FOREIGN KEY (projectId)
	REFERENCES project_entity (id) ON DELETE CASCADE ON UPDATE CASCADE
);