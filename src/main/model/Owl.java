package model;

import java.util.ArrayList;
import java.util.List;

//Just creates an owl, with its values, adjusting the values adjusts the owl's stats
public class Owl extends Animal {

    //EFFECTS: creates an owl based on the animal supertype
    public Owl() {
        super("owl", List.of("rabbit"), 1, 1, 8, 2);
    }

    //EFFECTS: returns a new copy of a owl
    public Animal giveBaby() {
        return new Owl();
    }
}

