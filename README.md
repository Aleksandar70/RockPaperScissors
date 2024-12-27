# Rock, Paper, Scissors Game

This project implements a Rock, Paper, Scissors game as a web service using Spring Boot. The application allows users to start a new game, make moves, retrieve overall game statistics, retrieve the game details and terminate the game. The game logic includes a basic implementation to predict and respond to user moves, taking advantage of common patterns in human decision-making.

## Features
* Start a new game session
* Make moves (Rock, Paper, Scissors)
* Retrieve the specific game statistics (wins, losses, draws)
* Retrieve details of a specific game session
* Terminate the game

## Technologies
* Java 17
* Spring Boot 3.x
* JUnit 5
* Mockito

## Getting Started
### Prerequisites
* Java 17 or higher
* Maven or Gradle for build management

### Installation

1. Build the Project:

```./gradlew build```

2. Run the Application:

```./gradlew bootRun```

The application will start on http://localhost:8080 by default.

## API Endpoints
### Start a New Game
* Endpoint: **POST /game/start**
* Description: Starts a new game session.
* Response:
```
{
    "valid": true,
    "message": "Game started! Your game ID is: d2672036-466b-4985-8ea0-5985a4d3d801"
}
```

### Make a Move
* Endpoint: **POST /game/move**
* Parameters:
  * **gameId**: The unique ID of the game session.
  * **move**: The user's move (**ROCK**, **PAPER**, or **SCISSORS**).
* Description: Processes the user's move and returns the result of the round.
* Example Request:

```curl -X POST "http://localhost:8080/game/move" -d "gameId=<gameId>&move=ROCK"```

* Response:

```
{
    "valid": true,
    "message": "You played ROCK. Computer played ROCK. Result: DRAW. Current stats: {wins=0, draws=1, losses=0}"
}
```

### Get Game Statistics
* Endpoint: **GET /game/stats/{gameId}**
* Description: Retrieves overall game statistics (wins, losses, draws) by **gameId**.
* Response:
```
 {
    "valid": true,
    "message": "{wins=0, draws=0, losses=0}"
}
```
### Get Game Details
* Endpoint: **GET /game/{gameId}**
* Description: Retrieves details of the game session specified by the **gameId**.
* Response:
```
{
    "valid": true,
    "message": "userMoves=[ROCK], computerMoves=[ROCK], results=[DRAW]"
}
```

### Terminate the Game
* Endpoint: **DELETE /terminate/{gameId}**
* Description: Terminates the game with the given **gameId**.
* Response:

```
{
    "valid": true,
    "message": "Game terminated successfully."
}
```

## Testing
To run the unit tests for the project:

```./gradlew test```

The tests validate the functionality of the GameController and GameService classes, ensuring that the endpoints work correctly and handle edge cases.

## Contributing
Contributions are welcome! If you have any ideas for improvements or find any issues.
