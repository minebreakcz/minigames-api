# Database

## minigame_players

| Name | Type | Null? | Description |
|------|------|:-----:|-------------|
| player_id | UUID | ✖ | Player's uuid |
| minigame | String | ✖ | Codename of the minigame |
| statistics | JSON String | ✔ |  |
| kits | String Array | ✔ | Unlocked kits, separated by ';' character |
| active_kit | String | ✔ | Selected kit | 

## minigame_currency

| Name | Type | Null? | Description |
|------|------|:-----:|-------------|
| player_id | UUID | ✖ | Player's uuid |
| currency | String | ✖ | Type / Name of the currency |
| amount | long | ✖ |  |
