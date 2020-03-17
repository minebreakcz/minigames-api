create table players
(
    id                 bigint auto_increment primary key,
    uuid               binary(16)           not null,
    last_name          varchar(16)          not null,
    constraint users_uuid_uindex unique (uuid)
);

create table minigames
(
    id       int auto_increment primary key,
    name     varchar(32) not null,
    category varchar(32) null,
    constraint minigame_name_uindex unique (name)
);

create table currency
(
    id   int auto_increment primary key,
    name varchar(32) not null
);

create table player_currency
(
    id          bigint auto_increment primary key,
    player_id   bigint           not null,
    currency_id int              not null,
    amount      bigint default 0 not null,
    constraint currency_player_id_currency_id_uindex unique (player_id, currency_id),
    constraint currency_currency_types_id_fk foreign key (currency_id) references currency (id),
    constraint currency_users_player_id_fk foreign key (player_id) references players (id)
);


