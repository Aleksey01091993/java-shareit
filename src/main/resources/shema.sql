create table public.booking
(
    id         bigint primary key not null default nextval('booking_id_seq'::regclass),
    end_time   timestamp(6) without time zone,
    start_time timestamp(6) without time zone,
    status     character varying(255),
    booker_id  bigint,
    item_id    bigint,
    foreign key (booker_id) references public.users (id)
        match simple on update no action on delete no action,
    foreign key (item_id) references public.item (id)
        match simple on update no action on delete no action
);

create table public.comments
(
    id           bigint primary key not null default nextval('comments_id_seq'::regclass),
    created_time timestamp(6) without time zone,
    item_id      bigint,
    text         character varying(255),
    author_id    bigint,
    foreign key (author_id) references public.users (id)
        match simple on update no action on delete no action
);

create table public.item
(
    id          bigint primary key not null default nextval('item_id_seq'::regclass),
    available   boolean,
    description character varying(255),
    name        character varying(255),
    owner_id    bigint,
    foreign key (owner_id) references public.users (id)
        match simple on update no action on delete no action
);

create table public.item_comments
(
    item_id     bigint not null,
    comments_id bigint not null,
    foreign key (item_id) references public.item (id)
        match simple on update no action on delete no action,
    foreign key (comments_id) references public.comments (id)
        match simple on update no action on delete no action
);
create unique index uk_rjhhf8wbmd564pr5ty0kkvgfw on item_comments using btree (comments_id);

create table public.item_request
(
    id           bigint primary key not null default nextval('item_request_id_seq'::regclass),
    create_time  timestamp(6) without time zone,
    description  character varying(255),
    requestor_id bigint,
    foreign key (requestor_id) references public.users (id)
        match simple on update no action on delete no action
);
create unique index uk_1qsp14ugfofgo0q3ojuwk5w5u on item_request using btree (requestor_id);

create table public.users
(
    id    bigint primary key not null default nextval('users_id_seq'::regclass),
    email character varying(255),
    name  character varying(255)
);
create unique index uk_6dotkott2kjsp8vw4d0m25fb7 on users using btree (email);

