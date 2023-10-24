# Red Badger Martian Robots Challenge

### Copyright:
- This project is a test task for an interview and is not intended for commercial use
- All rights reserved
- Author: Roman Matios (roma.matios@hey.com)

## System requirements
`Java 17, Docker`

## Building and running the programme
In order to run the project locally, please ensure you have a Docker daemon running
on your machine.


### Build the image container
`docker image build -t martian-robots .`

### Run the programme with container user-input enabled
`docker run -it martian-robots`

## User input hints
1) When you are done with the input, please press the following key combination to indicate that you are done: 
   - Windows / Linux / Docker: `Ctrl + D`
   - Mac OS: `Cmd + D`
   
This will trigger the programme to start the processing.

## Design decisions
- I've decided to use a `Command` enum to encapsulate the logic of the commands and make it easier to extend the 
  programme with new commands in the future.
  - `Command` enum contains an attribute `directionMultiplier` attribute. The idea is **IF** `B` (`directionMultiplier` = -1) command is added in the 
    future, the program would correctly handle backward movement. This is not tested since such command is not allowed per specification, but the logic is in place.
- I've decided to use a `Direction` enum to encapsulate the logic of the directions and make it easier to extend the 
  programme with new directions in the future.
- Currently if the robot remains on the grid after the last command is executed, it is not "persisted" on the board.
  - This means that the "next" robot can pass over the last robot. 
- `Grid` records both the position and all Orientations that led to robot being lost. For example, if position (1, 1) 
  was lost due to `N` orientation, `E`, `W` and `S` directions will be permitted, but `N` will be ignored.
- Limits on the `Grid` size (50 max) and `Robot` instructions (100 max) are enforced.

## Improvement ideas
1) I would most certainly rewrite `InputParser` to improve the code readability. 
   - In my opinion, it's the least readable class in the project.
   - Robot position and instruction could be parsed together, instead of separately.
   - This would alleviate the need to use `InputType` to keep track of the line sequence.
   
2) I would add a `B` `Command` for backward movement and tests to validate the logic of making that move. However, that would
have required adding additional logic to extract only "permitted" commands from `Command`.
   - Not a problem per-se, but I simply did not have enough time to implement it.
   
3) It would also be great to accept limits on the `Grid` size and `Robot` instructions as program input.
   - This would require additional validation logic and tests.
   - Another way would be pulling them from `env` variable or properties file.

4) have also considered storing commands inside `Robot` class. This would have allowed to remove the need for 
using a `Map<AbstractRobot, List<Command>>`. 
   - In the end, I've decided to keep the logic of the commands separate from the `Robot` class.
   - The only reason being: I thought that "in the real" world scenario - if the robot isn't lost on Mars, we NASA would
     want to send additional commands to it. :)

## Disclaimer
My apologies for larger than recommended time spent on the task if looking at the commit history. 
I was distracted by a few things outside of my control, but I've tried to keep the time spent on the task to a minimum.
Overall actual time spent on the task is around 3 hours.
 