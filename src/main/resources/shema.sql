create table booking
(
    booker_id  bigint,
    end_time   timestamp(6),
    id         bigserial not null,
    item_id    bigint,
    start_time timestamp(6),
    status     varchar(255) check (status in ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED')),
    primary key (id)
);

create table comments
(
    author_id    bigint,
    created_time timestamp(6),
    id           bigserial not null,
    item_id      bigint,
    text         varchar(255),
    primary key (id)
);

create table item
(
    available   boolean,
    id          bigserial not null,
    owner_id    bigint,
    description varchar(255),
    name        varchar(255),
    primary key (id)
);

create table item_comments
(
    comments_id bigint not null unique,
    item_id     bigint not null
);

create table item_request
(
    create_time  timestamp(6),
    id           bigserial not null,
    requestor_id bigint unique,
    description  varchar(255),
    primary key (id)
);

create table users
(
    id    bigserial not null,
    email varchar(255) unique,
    name  varchar(255),
    primary key (id)
);

alter table if exists booking
    add constraint FKf8qqd65qc077mrbdog4r9ldc3
        foreign key (booker_id)
            references users;

alter table if exists booking
    add constraint FKikwxul5wtb263vqoggtklsy5w
        foreign key (item_id)
            references item;

alter table if exists comments
    add constraint FKn2na60ukhs76ibtpt9burkm27
        foreign key (author_id)
            references users;

alter table if exists item
    add constraint FKquc2sh4rh6sc3rcffk9jxs2sg
        foreign key (owner_id)
            references users;

alter table if exists item_comments
    add constraint FKkh5r00mtikrgnggc5skiu4yjj
        foreign key (comments_id)
            references comments;

alter table if exists item_comments
    add constraint FKd0q60hv195l3lbow9bny6637d
        foreign key (item_id)
            references item;

alter table if exists item_request
    add constraint FKns8uiypw7utnbnbdci8nn738a
        foreign key (requestor_id)
            references users;
