package model;

import java.util.ArrayList;

//Just creates a rabbit, with its values, adjusting the values adjusts the rabbit's stats
public class Rabbit extends Animal {

    //EFFECTS: creates a rabbit based on the animal supertype
    public Rabbit() {
        super("rabbit", new ArrayList<>(), 1, 0, 3, 1);
    }

    //EFFECTS: returns a new copy of a rabbit
    public Animal giveBaby() {
        return new Rabbit();
    }
}
