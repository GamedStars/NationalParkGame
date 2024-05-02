package persistence;

import model.NationalPark;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//This class just handles all the saving and writing to JSON files
public class Save {

    private String destination;
    private PrintWriter writer;

    //EFFECTS: creates a data location with the file name, destination.
    public Save(String destination) {
        this.destination = "data/" + destination + ".json";
    }

    //MODIFIES: this
    //EFFECTS: Opens the file with the relative dir this.dest
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(this.destination);
    }

    //MODIFIES: the JSON file located in the this.dest, this
    //EFFECTS: Saves the data in nationalPark and then closes the file.
    public void save(NationalPark nationalPark) throws FileNotFoundException {
        this.open();
        JSONObject json = nationalPark.toJson();
        writer.print(json);
        writer.close();
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
