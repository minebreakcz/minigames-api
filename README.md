# Minigame API

Simple API for developing minigames.

Do not forget to call events on state change and game start/end.

## Database

There is `config.yml` with existing database made for testing.

If someone will abuse it, it will be removed and you will have to use your own.
The username and password are not used anywhere else.

## Content

- Chest Menu (`ItemMenu`)
- Items
  - `ItemBuilder` (use as `new ItemBuilder(Material)...build()`)
  - `PlayerHead`
- Chat
  - `ComponentBuilder` (use as `ComponentBuilder.translate("...")...build()`)
  - `ChatInfo` (use as `ChatInfo.ERROR.send(player, message)`)

If you want to add more, just create pull request.

## Usage

Clone this repository locally and run `maven install`

To your `maven` plugin, add this to your dependencies in `pom.xml`:
```xml
<dependency>
    <groupId>net.graymadness</groupId>
    <artifactId>minigame_api</artifactId>
    <version>1.2</version>
    <scope>provided</scope>
</dependency>
```