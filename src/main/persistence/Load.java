package persistence;

import model.Animal;
import model.NationalPark;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//handles all the loading of the json file by reading the file into string and then converting it into json
public class Load {

    private String destination;

    //EFFECTS: creates a data location with the file name, destination.
    public Load(String destination) {
        this.destination = "data/" + destination + ".json";
    }

    //MODIFIES: nationalPark
    //EFFECTS: reads a file into string and then converts that string into a JSONObject
    public void loadInto(NationalPark nationalPark) throws FileNotFoundException {
        File file = new File(this.destination);
        Scanner myReader = new Scanner(file);
        StringBuilder fileContent = new StringBuilder();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            fileContent.append(data);
        }
        JSONObject jsonObject = new JSONObject(fileContent.toString());
        this.loadNationalPark(jsonObject, nationalPark);
    }

    //MODIFIES: nationalPark
    //EFFECTS: loads the contents of the jsonObject into nationalPark
    private void loadNationalPark(JSONObject jsonObject, NationalPark nationalPark) {
        JSONArray jsonArray = jsonObject.getJSONArray("animals");
        this.loadAnimals(jsonArray, nationalPark);

        JSONObject jsonDeltaAnimals = jsonObject.getJSONObject("deltaAnimals");
        HashMap<String, Integer> deltaAnimals = new HashMap<>();
        for (String animalKey : nationalPark.getAllTypes()) {
            deltaAnimals.put(animalKey, jsonDeltaAnimals.getInt(animalKey));
        }
        nationalPark.setDeltaAnimals(deltaAnimals);

        nationalPark.setMoney(jsonObject.getInt("money"));
        nationalPark.setCycle(jsonObject.getInt("cycle"));
    }

    //MODIFIES: nationalPark
    //EFFECTS: creates the animals and then loads them into the nationalPark
    private void loadAnimals(JSONArray jsonArray, NationalPark nationalPark) {
        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Animal animal = nationalPark.generateAnimal(jsonObject.getString("type"));
            animal.setHunger(jsonObject.getInt("hunger"));
            animal.setCyclesBeforeReproduction(jsonObject.getInt("cyclesBeforeReproduction"));
            animal.setMaxReproductiveTime(jsonObject.getInt("maxReproductiveTime"));
            animal.setMaxHunger(jsonObject.getInt("maxHunger"));
            animal.setCost(jsonObject.getInt("cost"));
            animal.setProfit(jsonObject.getInt("profit"));
            animals.add(animal);
        }
        nationalPark.setAnimals(animals);
    }

    // --------------------- SETTERS AND GETTERS BELOW!

    //EFFECTS: simple getter of destination
    public String getDestination() {
        return destination;
    }

    //MODIFIES: this
    //EFFECTS: simple setter of destination
    public void setDestination(String destination) {
        this.destination = destination;
    }
}