package model;

import java.util.List;

//Just creates a bear, with its values, adjusting the values adjusts the bear's stats
public class Bear extends Animal {

    //EFFECTS: creates a bear based on the animal supertype
    public Bear() {
        super("bear", List.of("rabbit", "moose"), 1, 5, 10, 5);
    }

    //EFFECTS: returns a new copy of a bear
    public Animal giveBaby() {
        return new Bear();
    }
}
