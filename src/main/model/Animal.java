package model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//This is an animal, it's an abstract class, since animal is an abstract concept. Other real "animals" should inherit
// this class.
public abstract class Animal {

    //setting these as final, since there is no need to change them during runtime.
    private final String type;
    private final List<String> foodList;
    private int hunger; //REQUIRES: hunger >= 0
    private int cyclesBeforeReproduction; //REQUIRES: cyclesBeforeReproduction >= 1
    private int maxReproductiveTime;
    private int maxHunger;
    private int cost; //REQUIRES: cost >= 1
    private int profit; //REQUIRES profit >= 0

    //REQUIRES: hunger >= 0, cyclesBeforeReproduction >= 1, cost >= 1, profit >= 0
    //EFFECTS: initializes an animal object
    public Animal(String type, List<String> foodList, int cyclesBeforeReproduction, int hunger, int cost, int profit) {
        this.type = type;
        this.foodList = foodList;
        this.cyclesBeforeReproduction = cyclesBeforeReproduction;
        this.maxReproductiveTime = cyclesBeforeReproduction;
        this.hunger = hunger;
        this.maxHunger = hunger;
        this.cost = cost;
        this.profit = profit;
    }

    //EFFECTS: returns whether this animal can reproduce or not
    public boolean canReproduce() {
        return cyclesBeforeReproduction == 0;
    }

    //EFFECTS: returns a new animal depending on the type of the animal
    public abstract Animal giveBaby();

    //REQUIRES: this.canReproduce()
    //EFFECTS: returns a new animal depending on the type of the animal and resets the counter
    public Animal reproduce() {
        EventLog.getInstance().logEvent(new Event(this.getType() + " has reproduced"));
        this.cyclesBeforeReproduction = this.maxReproductiveTime;
        return giveBaby();
    }

    //EFFECTS: Checks whether there is enough food for this animal in the list of animal
    public boolean hasEnoughFood(List<Animal> fodder) {
        int count = 0;
        for (Animal food : fodder) {
            if (this.hunger == count) {
                return true;
            }
            if (this.foodList.contains(food.getType())) {
                count += 1;
            }
        }
        return this.hunger == count;
    }

    //REQUIRES: this.hasEnoughFood(fodder)
    //MODIFIES: fodder, this
    //EFFECTS: removes a number of animals from the fodder list based on the food
    public void eat(List<Animal> fodder) {
        for (String food: this.foodList) {
            if (this.hunger == 0) {
                break;
            }
            eatThisType(fodder, food);
        }
    }

    //REQUIRES: this.hasEnoughFood(combine(fodder, fedAnimals)) and !this.hasEnoughFood(fodder)
    //MODIFIES: fodder, fedAnimals, this
    //EFFECTS: removes a number of animals from the fodder list and then removes more from fedAnimals
    public void eat(List<Animal> fodder, List<Animal> fedAnimals) {
        eat(fodder);
        eat(fedAnimals);
    }

    //REQUIRES: type in this.foodList, this.hasEnoughFood()
    //MODIFIES: fodder, this
    //EFFECTS: removes animals from fodder of String type, this.hunger number of times.
    public void eatThisType(List<Animal> fodder, String type) {
        List<Animal> removeList = new ArrayList<>();
        for (Animal food : fodder) {
            if (this.hunger == 0) {
                break;
            }
            if (food.getType().equals(type)) {
                removeList.add(food);
                EventLog.getInstance().logEvent(new Event(this.getType() + " has eaten a " + food.getType()));
                this.hunger -= 1;
            }
        }
        fodder.removeAll(removeList);
    }

    //EFFECTS: checks whether the animal is fed.
    public boolean isFed() {
        return this.hunger == 0;
    }

    //REQUIRES: !this.canReproduce()
    //MODIFIES: this
    //EFFECTS: cycles the animal, so decrement any values that need decrementing and reset any other values.
    // reset its hunger, so it has to eat tomorrow and decrease the cycles by 1 approaching reproduction.
    public void cycle() {
        this.hunger = this.maxHunger;
        this.cyclesBeforeReproduction -= 1;
    }

    //EFFECTS: converts an animal object into json and returns it.
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.type);
        jsonObject.put("hunger", this.hunger);
        jsonObject.put("cyclesBeforeReproduction", this.cyclesBeforeReproduction);
        jsonObject.put("maxReproductiveTime", this.maxReproductiveTime);
        jsonObject.put("maxHunger", this.maxHunger);
        jsonObject.put("cost", this.cost);
        jsonObject.put("profit", this.profit);
        return jsonObject;
    }

    // --------------------- SIMPLE SETTERS AND GETTERS BELOW!

    //EFFECTS: simple getter of type
    public String getType() {
        return this.type;
    }

    //EFFECTS: simple getter of foodList
    public List<String> getFoodList() {
        return this.foodList;
    }

    //EFFECTS: simple getter of hunger
    public int getHunger() {
        return this.hunger;
    }

    //EFFECTS: simple getter of maxHunger
    public int getMaxHunger() {
        return maxHunger;
    }

    //EFFECTS: simple getter of cyclesBeforeReproduction
    public int getCyclesBeforeReproduction() {
        return this.cyclesBeforeReproduction;
    }

    //EFFECTS: simple getter of maxReproductiveTime
    public int getMaxReproductiveTime() {
        return this.maxReproductiveTime;
    }

    //EFFECTS: simple getter of cost
    public int getCost() {
        return this.cost;
    }

    //EFFECTS: simple getter of profit
    public int getProfit() {
        return this.profit;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of hunger
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of maxHunger
    public void setMaxHunger(int maxHunger) {
        this.maxHunger = maxHunger;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of cyclesBeforeReproduction
    public void setCyclesBeforeReproduction(int cyclesBeforeReproduction) {
        this.cyclesBeforeReproduction = cyclesBeforeReproduction;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of maxReproductiveTime
    public void setMaxReproductiveTime(int maxReproductiveTime) {
        this.maxReproductiveTime = maxReproductiveTime;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of cost
    public void setCost(int cost) {
        this.cost = cost;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of profit
    public void setProfit(int profit) {
        this.profit = profit;
    }
}
