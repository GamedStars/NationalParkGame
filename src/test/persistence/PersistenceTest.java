package persistence;

import model.NationalPark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {

    private Save save;
    private Load load;
    private NationalPark nationalParkSave;
    private NationalPark nationalParkLoad;
    private Map<String, Integer> deltaAnimals;

    @BeforeEach
    public void runBefore() {
        nationalParkSave = new NationalPark();
        nationalParkLoad = new NationalPark();
        nationalParkSave.addAnimal("rabbit");
        nationalParkSave.addAnimal("rabbit");
        nationalParkSave.addAnimal("rabbit");
        nationalParkSave.addAnimal("wolf");
        nationalParkSave.addAnimal("wolf");
        nationalParkSave.addAnimal("wolf");
        nationalParkSave.setCycle(10);
        nationalParkSave.setMoney(100);
        deltaAnimals = new HashMap<>();
        int i = 0;
        for (String animalName : nationalParkSave.getAllTypes()) {
            deltaAnimals.put(animalName,i);
            i++;
        }
        nationalParkSave.setDeltaAnimals(deltaAnimals);
        save = new Save("testSave1");
        load = new Load("testSave1");
    }

    @Test
    public void testPersistence() {
        try {
            save.save(nationalParkSave);
            load.loadInto(nationalParkLoad);
            assertEquals(6, nationalParkLoad.getAnimalsSize());
            assertEquals(10, nationalParkLoad.getCycle());
            assertEquals(100, nationalParkLoad.getMoney());
            Map<String, Integer> currentDeltaAnimals = nationalParkLoad.getDeltaAnimals();
            for (String key : currentDeltaAnimals.keySet()) {
                assertEquals(deltaAnimals.get(key), currentDeltaAnimals.get(key));
            }
            Map<String, Integer> pop = nationalParkLoad.getPopulation();
            assertEquals(3, pop.get("rabbit"));
            assertEquals(3, pop.get("wolf"));
        } catch (FileNotFoundException e) {
            System.out.println("testSave1 not found? Missing a file that should exist from repo");
        }
    }

    @Test
    public void testFileNotFound() {
        try {
            Load invalidLoad = new Load("doesNotExist");
            invalidLoad.loadInto(nationalParkLoad);
            fail();
        } catch (FileNotFoundException e) {
            System.out.println("Pass!");
        }
    }

    @Test
    public void testGetterAndSetters () {
        save.setDestination("testSave2");
        load.setDestination("testSave3");
        assertEquals("testSave2", save.getDestination());
        assertEquals("testSave3", load.getDestination());
    }
}
