# My Personal Project

## Task 1
My application idea is a game based on managing a national park. You can buy animals to put into your park and then each animal will dietary requirements that would need to be managed. Depending on the animals you have in your park, they will generate revenue. I am also planning to have some random events to spice the game up. My arbitary list will be all the animals in the park, while my important list of pre-defined length is the types of animals that can be purchased. On the other hand, I spoke to my TA, and I thought about also adding demographics to my game of populations (children like cuter animals, therefore if the city has more children and my park has more cute animals I will generate more revenue!). This also could be another list with all the demographics I would have in my city (children, adults, teenagers etc).
## Task 2
- **What will the application do?**
This will be a simulation/strategy based game that manages a national park with animals to create profit. The game is supposed to be relaxing and zen-like with no particular end.

- **Who will use it?**
People who wish just have fun playing management games and possibly people who enjoy looking to animals. I theorize it can be also used to very loosely and poorly model animal populations, with some adjustments to the numbers, so maybe it can be an interesting tool to undergraduates or high-school students?

- **Why is this project of interest to you?**
This project is of interest to me because I am avid fan of strategy and management games, animal populations have always been a hobby of mine since I enjoy reading about how they can form cyclic populations. The mix between mathematics, computer science and animal populations is the perfect unison of subjects that are of extreme interest to me.

## Task 3 (User Stories)

#### Implemented
- As a user, I want to be able to buy animals to put into my national park. (Adding X to Y)
- As a user, I want to be able to see how many of each animal type I have in my national park. (Viewing the list of Y)
- As a user, I want to be able to see how each animal type population has changed over a period of time. (Viewing the change in the list of Y)
- As a user, I want to be able to see how much money I have.
- As a user, I want to be able to buy multiple animals at once.
- As a user, I want to be able to use ALL my money to buy a specific animal.

- As a user, I want to be able to save the game manually.
- As a user, I want to be able to either load my previous save or start a new game.

*Note: In this program X is the Animal class, and Y is the NationalPark class that collects an arbitrary number of these items.*

## Documentation for the console
Hi TA (or whoever is grading my work, probably Youssef)!, hope you are having a good day, I am writing documentation here because I have no idea where else to write this stuff.
To run my program simply run the Main file, and it should give you a general documentation of how the program should work. But this is the proper documentation.

Just as a summary of the game. This game allows you to buy animals to keep in your national park where they give you money if you keep them alive.
The dilemma is how you are going to do this. As an example, if you buy certain predators, you will need to buy suitable preys to keep them alive. If you have too many predators, they decimate the preys population before starving to death themselves.
S

### Commands
The program has 5 commands, *Note: all commands should be without quotes*:
- "buy (animalName) [quantity | 'full']"

The buy command works by requiring an animalName which is one of ("rabbit", "wolf", "moose", "bear", "owl").

The other parameter that buy requires is the quantity which it accepts either a non-negative number (quantity >= 0) or the string "full" or even just empty.
Entering a integer quantity will buy that many animals of the provided animalName type.
"full" uses up all the money available to buy as much animals of the provided animalName type.
Entering nothing just buys single animal of the provided type.

- "pop"

This command just prints the current population of the national park to the console. It also prints to the side how the population has changed from one day to the next day.

- "bal"

The amount of money you have in your balance.

- "next" or ""

The two commands here do the same thing, either just press ENTER or type "next". This command just goes the next day. 

* For the more curious, the order the program does the tasks when moving from one day to the next is:
  * Increments the cycle counter;
  * Gets the current population;
  * Feeds all the animals;
  * If animals are not fed, those animals die (removed from the list, Java garbage collector deletes the instance because there should be no reference to the object);
  * Collect the profits made of the remaining animals;
  * Cycle each animal (this just resets their hunger and reducing their reproduction time);
  * Attempts to reproduce all animal;
  * Gets the new population;
  * Calculates the difference between the new and old population to calculate the change in population.

- "exit"

Ends the program

### Animal Information

This is just a table to help you test the program. It is to make sense of what values each animal has. These values are not realistic or final, I am using these values because,
they are useful for testing purposes. Lower reproduction time means we can see more animals and see if the animal reproduction is working and higher hunger number show if animal nutrition is working.

| (String) type | (ListOfString) foodList    | (int) cyclesBeforeReproduction | (int) hunger | (int) cost | (int) profit |
|---------------|----------------------------|--------------------------------|--------------|------------|--------------|
| "rabbit"      | new ArrayList<>()          | 1                              | 0            | 3          | 1            |
| "wolf"        | List.of("rabbit", "moose") | 1                              | 3            | 7          | 3            |
| "moose"       | new ArrayList<>()          | 1                              | 0            | 5          | 1            |
| "owl"         | List.of("rabbit")          | 1                              | 1            | 7          | 2            |
| "bear"        | List.of("rabbit", "moose") | 1                              | 5            | 10         | 5            |

- String type: This is just the animal type
- ListOfString foodList: This is the food that the animal can eat, empty list means this animal doesn't eat anything and therefore does not need any food. **Animals prioritize eating prey in the order shown in the (ListOfString) foodList column in the table above. Eg, if in a park there is rabbits, moose and wolves; the wolves will prioritize eating rabbits before the moose.**
- int cyclesBeforeReproduction: This is the number of days before reproduction, 1 means this animal is able to reproduce every cycle, 2 means every 2 cycles.
- int hunger: This is number of animals this animal needs to eat before being satisfied (if an animal doesn't have enough food it will die).
- int cost: This is the cost to buy the animal
- int profit: This is the amount of money the animal returns if the animal is alive till the next day.

# Instructions for Grader

- You can generate the first required action related to the user story "adding multiple Xs to a Y" by... simply clicking on the "Buy an animal!" button and then clicking the "+" or "-" button, this keeps a running cost total in the bottom left on the panel in red.
- The second action is in the "Population" button that will by default on you clicked it open the population of rabbits in your park. You can go to the next cycle and see how the animals reproduce and the population changes.
- You can locate my visual component by simply clicking on the "Buy an animal!" button.
- You can save the state of my application by clicking on the save button near the top right of the window and entering a filename you wish to save the program in.
- You can reload the state of my application by clicking on the load button near the top right of the window and entering the filename you saved the program in, and now you wish to load it.

## References and links for the pictures used.
- All resources used are of free license from the website.
- Rabbit icon: https://www.flaticon.com/free-icon/rabbit_7441511
- Wolf icon: https://www.flaticon.com/free-icon/wolf_3359935
- Moose icon: https://www.flaticon.com/free-icon/moose_6199471
- Owl icon: https://www.flaticon.com/free-icon/owl_1888692
- Bear icon: https://www.flaticon.com/free-icon/bear_3093871

# Phase 4: Task 2

rabbit has been bought and added to the national park
rabbit has been fed
rabbit has reproduced
The day has been cycled, it now cycle 2
rabbit has been fed
rabbit has been fed
rabbit has reproduced
rabbit has reproduced
The day has been cycled, it now cycle 3
wolf has been bought and added to the national park
wolf has been bought and added to the national park
rabbit has been fed
rabbit has been fed
rabbit has been fed
rabbit has been fed
wolf has eaten a rabbit
wolf has eaten a rabbit
wolf has eaten a rabbit
wolf has been fed using some of the fed animals
wolf has not been fed and has died
rabbit has reproduced
wolf has reproduced
The day has been cycled, it now cycle 4

## Phase 4: Task 3
Looking at the UML Class Diagram I just realized that, there is a lot of unseen dependencies in my program.
Ideally, these dependencies would be their own associations. Additionally, we can see that the BuyingManager and PopulationManager classes
have an association of NationalPark, in hindsight it would be better to pass this as a parameter through GamePanel since we are using the same NationalPark.
This would reduce coupling in my program.

Reading the program in GamePanel I feel more code could be pulled out into their own classes, currently the GamePanel handles the creation of panels, adding of buttons, adding of functionality of the buttons. In reality, only the button functionality needs to know about the NationalPark. So we would improve GamePanel by breaking down it into separate classes, one to create the JPanels themselves, one to add the buttons, and one to add the functionality of each button. This would improve the cohesion of the program.