package ui;

import model.NationalPark;

import javax.swing.*;
import java.awt.*;

//Handles all the updating needed for the population window in the GamePanel UI
public class PopulationManager {

    private final JPanel counterPanel;
    private final NationalPark nationalPark;

    //EFFECTS: creates the PopulationManager object by initializing the counterPanel and the nationalPark
    public PopulationManager(JPanel counterPanel, NationalPark nationalPark) {
        this.counterPanel = counterPanel;
        this.nationalPark = nationalPark;
    }

    //REQUIRES: animalName to be in nationalPark.getAllTypes()
    //EFFECTS: updates the counter based on the current animals in getNationalPark.getPopulation() and the change of
    // animals in getNationalPark().getDeltaAnimals()
    public void updateCounter(String animalName) {
        JLabel counterLabel = getCounter();
        int pop = getNationalPark().getPopulation().get(animalName);
        int deltaPop = getNationalPark().getDeltaAnimals().get(animalName);
        if (deltaPop >= 0) {
            counterLabel.setText(pop + " (+" + (deltaPop) + ")");
        } else {
            counterLabel.setText(pop + " (" + (deltaPop) + ")");
        }
    }

    // --------------------- SETTERS AND GETTERS BELOW!

    //REQUIRES: panel must have one and only one JLabel
    //EFFECTS: a complex helper getter function that gets the label from a panel
    private JLabel getLabel(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                return (JLabel) component;
            }
        }
        return null;
    }

    //EFFECTS: a more complex getting the single label in counterPanel
    private JLabel getCounter() {
        return getLabel(getCounterPanel());
    }

    //EFFECTS: simple getter of counterPanel
    public JPanel getCounterPanel() {
        return counterPanel;
    }

    //EFFECTS: simple getter of nationalPark
    public NationalPark getNationalPark() {
        return nationalPark;
    }
}
