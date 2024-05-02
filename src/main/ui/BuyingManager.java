package ui;

import model.NationalPark;

import javax.swing.*;
import java.awt.*;

//Handles all the buying and other functions of the buy window in the GamePanel UI
public class BuyingManager {

    private final JPanel counterPanel;
    private final JPanel leftPanel;

    private final NationalPark nationalPark;

    //EFFECTS: creates the BuyingManager object by initializing the different panels and the nationalPark
    public BuyingManager(JPanel counterPanel, JPanel leftPanel, NationalPark nationalPark) {
        this.counterPanel = counterPanel;
        this.leftPanel = leftPanel;
        this.nationalPark = nationalPark;
    }

    //REQUIRES: animalName to be in nationalPark.getAllTypes()
    //EFFECTS: increases the counter of how many animals we are buying to one or the max amount possible,
    // the counter cannot be more than the max amount possible
    public void incrementCounter(String animalName, boolean setMax) {
        JLabel counter = getCounterLabel();
        int animalCost = getNationalPark().generateAnimal(animalName).getCost();
        int increment = Integer.parseInt(counter.getText()) + 1;
        if (setMax || increment * animalCost > getNationalPark().getMoney()) {
            increment = getNationalPark().getMoney() / animalCost;
            counter.setText(String.valueOf(increment));
        } else {
            counter.setText(String.valueOf(increment));
        }
        updateRunningCost(increment * animalCost);
    }

    //REQUIRES: animalName to be in nationalPark.getAllTypes()
    //EFFECTS: decrements the counter by one or to zero, the counter cannot be less than 0
    public void decrementCounter(String animalName, boolean setZero) {
        JLabel counter = getCounterLabel();
        int animalCost = getNationalPark().generateAnimal(animalName).getCost();
        int newNumber = Integer.parseInt(counter.getText()) - 1;
        if (setZero || newNumber <= 0) {
            counter.setText("0");
            updateRunningCost(0);
        } else {
            counter.setText(String.valueOf(newNumber));
            updateRunningCost(newNumber * animalCost);
        }
    }

    //REQUIRES: cost >= 0
    //EFFECTS: updates the running cost to know how much the user will spend on these animals
    private void updateRunningCost(int cost) {
        JLabel runningCost = getLeftLabel();
        runningCost.setText(String.valueOf(cost));
    }

    //EFFECTS: resets the counters to 0.
    public void reset() {
        JLabel counter = getCounterLabel();
        counter.setText("0");
        JLabel leftLabel = getLeftLabel();
        leftLabel.setText("0");
    }

    //REQUIRES: animalName to be in nationalPark.getAllTypes()
    //EFFECTS: buys the animalName
    public void buy(String animalName) {
        JLabel counter = getCounterLabel();
        int quantity = Integer.parseInt(counter.getText());
        for (int i = 0; i < quantity; i++) {
            getNationalPark().buyAnimal(animalName);
        }
    }

    // --------------------- SETTERS AND GETTERS BELOW!

    //EFFECTS: simple getter of nationalPark
    public NationalPark getNationalPark() {
        return nationalPark;
    }

    //EFFECTS: simple getter of leftPanel
    public JPanel getLeftPanel() {
        return leftPanel;
    }

    //EFFECTS: simple getter of counterPanel
    public JPanel getCounterPanel() {
        return counterPanel;
    }

    //EFFECTS: a more complex getting the single label in counterPanel
    private JLabel getCounterLabel() {
        return getLabel(getCounterPanel());
    }

    //EFFECTS: a more complex getting the single label in leftPanel
    private JLabel getLeftLabel() {
        return getLabel(getLeftPanel());
    }

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
}
