package model;

import java.util.ArrayList;

//Just creates a moose, with its values, adjusting the values adjusts the moose's stats
public class Moose extends Animal {

    //EFFECTS: creates a moose based on the animal supertype
    public Moose() {
        super("moose", new ArrayList<>(), 1, 0, 5, 1);
    }

    //EFFECTS: returns a new copy of a moose
    public Animal giveBaby() {
        return new Moose();
    }
}
