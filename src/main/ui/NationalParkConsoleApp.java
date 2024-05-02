package ui;

import model.NationalPark;
import persistence.Load;
import persistence.Save;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

//This is the console app, handles all the user inputs and calls the relevant classes in the model program.
public class NationalParkConsoleApp {

    private final NationalPark nationalPark;
    private final Scanner input = new Scanner(System.in);
    private final String cyan = "\u001B[36m";
    private final String red = "\u001B[31m";
    private final String reset = "\u001B[0m";

    //EFFECTS: Starts the Console UI of the game
    public NationalParkConsoleApp() {
        this.nationalPark = new NationalPark();
        nationalPark.setMoney(10000); //REMOVE THIS LINE ONCE DONE WITH TESTING
        System.out.println("Welcome to your national park!\n");
        this.help();
        this.commands();
    }

    //EFFECTS: Simple output of all the commands
    private void help() {
        System.out.println("This is just a help screen to inform you of all the commands:");
        System.out.println("() means required parameters while [] means optional and | denotes other options.");
        System.out.println("'' quotes denote an exact parameter, as in that parameter should be typed as shown\n");
        //add a print here w all the animals
        System.out.println("buy (animalName) [quantity | 'full']: Buys an animal of amount quantity or until "
                + "you have no more money.");
        System.out.println("bal : Checks your balance");
        System.out.println("pop : Gets the population");
        System.out.println("next : Goes to the next day, you can also just press enter to go to the next day");
        System.out.println("exit : Quits the program");
        System.out.println();
    }

    //EFFECTS: accepts a user input and then handles it.
    @SuppressWarnings("methodlength")
    private void commands() {
        System.out.println("Day " + nationalPark.getCycle());
        System.out.print(">>> ");
        String command = input.nextLine();
        String[] split = command.split(" ");

        switch (split[0]) {
            case "buy":
                this.buy(split);
                break;

            case "pop":
                this.printStatistics();
                break;

            case "bal":
                System.out.println(nationalPark.getMoney());
                break;

            case "next":
            case "":
                nationalPark.nextCycle();
                break;

            case "save":
                try {
                    save(split[1]);
                } catch (FileNotFoundException e) {
                    System.out.println("Did not find file" + split[1] + ".json in the data folder.");
                }
                break;

            case "load":
                try {
                    load(split[1]);
                } catch (FileNotFoundException e) {
                    System.out.println("Did not find file" + split[1] + ".json in the data folder.");
                }
                break;

            case "exit":
                return;

            default:
                //! many should be a different exception?
                System.out.println("Invalid command ");
        }

        System.out.println();
        this.commands();
    }

    //EFFECTS: prints out the population in console to help with debugging
    public void printStatistics() {
        Map<String, Integer> currentPop = nationalPark.getPopulation();
        for (var key : currentPop.keySet()) {
            int delta = nationalPark.getDeltaAnimals().get(key);
            if (delta > 0) {
                System.out.println(key + ": " + currentPop.get(key)
                        +  cyan + " (+" + delta + ")" + reset);
            } else if (delta < 0) {
                System.out.println(key + ": " + currentPop.get(key)
                        +  red + " (" + delta + ")" + reset);
            } else {
                System.out.println(key + ": " + currentPop.get(key)
                        + " (" + delta + ")");
            }
        }
    }

    //EFFECTS: saves the current game state into the filename
    public void save(String filename) throws FileNotFoundException {
        Save save = new Save(filename);
        save.save(nationalPark);
    }

    //MODIFIES: nationalPark
    //EFFECTS: loads the savefile in filename into nationalPark
    public void load(String filename) throws FileNotFoundException {
        Load load = new Load(filename);
        load.loadInto(nationalPark);
    }

    //MODIFIES: nationalPark
    //EFFECTS: buys an animal
    // can throw three types of errors:
    // NotValidTypeAnimalException: The animal given is not a valid animal.
    // NotEnoughMoneyException: You do not have enough money to buy that many/that animal(s).
    // NumberFormatException: This is a java exception, might as well use it, no need to reinvent the wheel.
    public void buy(String[] command) {
//        if (!nationalPark.getAllTypes().contains(command[1])) {
//            throw new NotValidTypeAnimalException();
//        }
        if (command.length > 2) {
            if (command[2].equals("full")) {
                buy(command[1]);
            } else {
                buy(command[1], Integer.parseInt(command[2]));
            }
        } else {
            buy(command[1],1);
        }
        System.out.println("Buying animal: " + command[1]);
    }

    //EFFECTS: buys quantity amount of animals of type
    // can throw:
    // NotEnoughMoneyException: You do not have enough money to buy that many/that animal(s).
    // NumberIsZeroException: the quantity is 0 which makes no sense,
    // program should work without this exception but why would you.
    private void buy(String type, int quantity) {
        if (nationalPark.getMoney() - nationalPark.generateAnimal(type).getCost() * quantity >= 0) {
            for (;quantity > 0; quantity--) {
                nationalPark.buyAnimal(type);
            }
        }
    }

    //EFFECTS: keeps buying animals of type until the user can't afford it
    private void buy(String type) {
        while (nationalPark.getMoney() - nationalPark.generateAnimal(type).getCost() >= 0) {
            nationalPark.buyAnimal(type);
        }
    }

}
