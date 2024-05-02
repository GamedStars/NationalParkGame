package model;

import java.util.List;

//Just creates a wolf, with its values, adjusting the values adjusts the wolf's stats
public class Wolf extends Animal {

    //EFFECTS: creates a wolf based on the animal supertype
    public Wolf() {
        super("wolf", List.of("rabbit", "moose"), 1, 3, 7, 3);
    }

    //EFFECTS: returns a new copy of a wolf
    public Animal giveBaby() {
        return new Wolf();
    }
}
