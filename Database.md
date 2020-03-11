# Database

## minigame_players

| Name | Type | Null? | Description |
|------|------|:-----:|-------------|
| player_id | UUID | ✖ | Player's uuid |
| minigame | String | ✖ | Codename of the minigame |
| statistics | JSON String | ✔ |  |
| kits | String Array | ✔ | Unlocked kits, separated by ';' character |
| active_kit | String | ✔ | Selected kit | 

```mysql
create table minigame_players
(
    player_id  binary(16)                   not null,
    minigame   varchar(32)                  not null,
    statistics longtext collate utf8mb4_bin null,
    kits       text                         null,
    active_kit tinytext                     null,
    primary key (player_id, minigame)
);
```

## minigame_currency

| Name | Type | Null? | Description |
|------|------|:-----:|-------------|
| player_id | UUID | ✖ | Player's uuid |
| currency | String | ✖ | Type / Name of the currency |
| amount | long | ✖ |  |

```mysql
create table minigame_currency
(
    playerid binary(16)  not null,
    currency varchar(32) not null,
    amount   mediumtext  null,
    primary key (playerid, currency)
);
```
