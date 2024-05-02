package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This is the NationalPark, this contains a lot of Animals. It handles most of the feeding, reproducing and other
// things. It is also where money is stored and calculated based on each Animal.
public class NationalPark {

    private List<Animal> animals;
    private Map<String, Integer> deltaAnimals;
    private int money;
    //!!! NEW ANIMALS NEED TO BE ADDED HERE
    private final List<String> allTypes = List.of("rabbit", "wolf", "moose", "owl", "bear");
    private int cycle;

    //EFFECTS: initialize a national park with no animals.
    public NationalPark() {
        this.animals = new ArrayList<>();
        this.money = 15;
        this.cycle = 1;
        this.deltaAnimals = getPopulation();
    }

    //!!! NEW ANIMALS NEED TO BE ADDED HERE

    //EFFECTS: creates a new copy of the provided string, basically just returns an animal of the type string
    public Animal generateAnimal(String type)  {
        switch (type) {
            case "rabbit":
                return new Rabbit();

            case "wolf":
                return new Wolf();

            case "moose":
                return new Moose();

            case "owl":
                return new Owl();

            case "bear":
                return new Bear();

            default:
                return null;
        }
    }

    //MODIFIES: this
    //EFFECTS: just adds the animal object into this.animals
    public void addAnimal(Animal animal) {
        this.animals.add(animal);
    }

    //REQUIRES: type to be in this.allTypes
    //MODIFIES: this
    //EFFECTS: adds an animal to this.animals based on the provided String type
    public void addAnimal(String type) {
        addAnimal(generateAnimal(type));
    }

    //REQUIRES: this.money - Animal.cost >= 0
    //MODIFIES: this
    //EFFECTS: adds an animal to this.animals and then subtracts the Animal.cost from this.money
    public void buyAnimal(String type) {
        EventLog.getInstance().logEvent(new Event(type + " has been bought and added to the national park"));
        this.addAnimal(type);
        this.money -= this.animals.get(this.getAnimalsSize() - 1).getCost();
    }

    //MODIFIES: this
    //EFFECTS: feeds all the animals in the NationalPark
    public void feedAll() {
        this.animals = feedAll(this.animals, new ArrayList<>());
    }


    //REQUIRES: all animals in fodder have not been fed and all animals in fedAnimals is fed
    //MODIFIES: this, fodder
    //EFFECTS: calls feed with the first element of fodder (which is an animal, the first of the list)
    // and attempts to feed this animal, by passing the animal (first of the list), the accumulator (fedAnimals)
    // and the fodder (rest of the list) to feed. The feeding is all done once the fodder (rest of the list) is empty,
    // the program will then just return all the fedAnimals.

    //THREE PART TERMINATION ARGUMENT:
    //BASE CASE: fodder.isEmpty()
    //REDUCTION STEP: fodder.remove(0);
    //TERMINATION ARGUMENT: when we are calling feed(), we are removing one animal at a time everytime.
    // this animal may or may not be added to the fedAnimals accumulator but fodder keeps reducing size. At no point,
    // do we add more animals to fodder, we always remove at least one animal everytime feed() is called. In fact,
    // feed removes more animals if certain conditions are met.

    private List<Animal> feedAll(List<Animal> fodder, List<Animal> fedAnimals) {
        if (fodder.isEmpty()) {
            return fedAnimals;
        }
        Animal firstAnimal = fodder.get(0);
        fodder.remove(0);
        return feed(firstAnimal, fodder, fedAnimals);
    }

    //REQUIRES: !animal.isFed() and all animals in fodder have not been fed and all animals in fedAnimals have been fed
    //MODIFIES: fedAnimals
    //EFFECTS: this function uses the animal and attempts to eat enough food if there is enough.

    // The function should first check if there is enough food in just fodder, if so just eat the animals there,
    // if there was not enough food combine the list of fodder and fedAnimals and check if there is enough food now,
    // if there still not enough food, then don't add the animal to any list, this should remove all reference to
    // that animal, I did some research and Java garbage collector should just delete the instantiated object
    // preserving precious memory space and effectively killing the animal (death by starvation).
    // if there was enough food in the combined array, then the animal will just eat using the two arrays (more details
    // in the eat method).

    public List<Animal> feed(Animal animal, List<Animal> fodder, List<Animal> fedAnimals) {
        if (animal.hasEnoughFood(fodder)) {
            animal.eat(fodder);
            fedAnimals.add(animal);
            EventLog.getInstance().logEvent(new Event(animal.getType() + " has been fed"));
        } else if (animal.hasEnoughFood(combine(fodder, fedAnimals))) {
            animal.eat(fodder, fedAnimals);
            fedAnimals.add(animal);
            EventLog.getInstance().logEvent(new Event(animal.getType()
                    + " has been fed using some of the fed animals"));
        } else {
            EventLog.getInstance().logEvent(new Event(animal.getType()
                    + " has not been fed and has died"));
        }
        return feedAll(fodder, fedAnimals);
    }

    //EFFECTS: combines the two arrays into a new array
    //PS: Not doing something like list1.addAll(list2) because although it seems like it has the same effect, it alters
    // the list. I still want to be able to use list1 and list2 separately but I also would like a combined list,
    // hence this function.
    private List<Animal> combine(List<Animal> list1, List<Animal> list2) {
        List<Animal> returnList = new ArrayList<>();
        returnList.addAll(list1);
        returnList.addAll(list2);
        return returnList;
    }

    //MODIFIES: this
    //EFFECTS: adds a new animal of the same type of each animal that is able to reproduce
    public void reproduceAll() {
        List<Animal> addList = new ArrayList<>();
        for (Animal animal : this.animals) {
            if (animal.canReproduce()) {
                addList.add(animal.reproduce());
            }
        }
        this.animals.addAll(addList);
    }

    //EFFECTS: a more complex getter of animals, gives the population of each type of animal in this.animals
    public Map<String, Integer> getPopulation() {
        Map<String, Integer> map = new HashMap<>();
        for (String animalType : this.allTypes) {
            map.put(animalType, count(animalType));
        }
        return map;
    }

    //MODIFIES: this
    //EFFECTS: subtracts the new population from the old population for each type of animal to show how the
    // population has changed.
    public void updateDeltaAnimals(Map<String, Integer> oldPop, Map<String, Integer> newPop) {
        for (var key : oldPop.keySet()) {
            this.deltaAnimals.put(key, newPop.get(key) - oldPop.get(key));
        }
    }

    //EFFECTS: counts the amount of animals of animalType in this.animals
    private int count(String animalType) {
        int count = 0;
        for (Animal animal : this.animals) {
            if (animal.getType().equals(animalType)) {
                count++;
            }
        }
        return count;
    }

    //EFFECTS: sums up all the revenue made from all the animals
    //!!! rename this stupid name to something else lol
    public int collectProfits() {
        int sum = 0;
        for (Animal animal : this.animals) {
            sum += animal.getProfit();
        }
        return sum;
    }

    //MODIFIES: this
    //EFFECTS: cycles the day, gets the current population, feeds all the animals, collect the profits,
    // cycles each animal, reproduces all animals, gets the new population, calculates the change in population.
    public void nextCycle() {
        this.cycle += 1;
        Map<String, Integer> oldPop = getPopulation();
        this.feedAll();
        this.money += this.collectProfits();
        //reset their hunger and decrement their reproduction time
        for (Animal animal : this.animals) {
            animal.cycle();
        }
        //changing the order has great affects!
        this.reproduceAll();
        Map<String, Integer> newPop = getPopulation();
        updateDeltaAnimals(oldPop, newPop);
        EventLog.getInstance().logEvent(new Event("The day has been cycled, it now cycle " + this.cycle));
    }

    //EFFECTS: reads all the data in the national park and turns it into a JSON format.
    public JSONObject toJson() {
        JSONObject returnJsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (Animal animal : this.animals) {
            jsonArray.put(animal.toJson());
        }
        returnJsonObject.put("animals", jsonArray);

        JSONObject jsonObject = new JSONObject();
        for (var key : this.deltaAnimals.keySet()) {
            jsonObject.put(key, this.deltaAnimals.get(key));
        }
        returnJsonObject.put("deltaAnimals", jsonObject);

        returnJsonObject.put("money", this.money);
        returnJsonObject.put("cycle", this.cycle);

        return returnJsonObject;
    }

    // --------------------- SETTERS AND GETTERS BELOW!

    //EFFECTS: simple getter of animals
    public List<Animal> getAnimals() {
        return this.animals;
    }

    //EFFECTS: returns the size of the list of animals (how many animals do we have in our national park?)
    public int getAnimalsSize() {
        return this.animals.size();
    }

    //EFFECTS: simple getter of money
    public int getMoney() {
        return this.money;
    }

    //EFFECTS: simple getter of cycle
    public int getCycle() {
        return this.cycle;
    }

    //EFFECTS: simple getter of allTypes
    public List<String> getAllTypes() {
        return this.allTypes;
    }

    //EFFECTS: simple getter of deltaAnimals
    public Map<String, Integer> getDeltaAnimals() {
        return this.deltaAnimals;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of animals
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    //MODIFIES: this
    //EFFECT: simple setter of money
    public void setMoney(int money) {
        this.money = money;
    }

    //MODIFIES: this
    //EFFECT: simple setter of cycle
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    //MODIFIES: this
    //EFFECT: simple setter of setDeltaAnimals
    public void setDeltaAnimals(Map<String, Integer>  deltaAnimals) {
        this.deltaAnimals = deltaAnimals;
    }
}
