package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NationalParkTest {

    private NationalPark nationalPark;

    @BeforeEach
    public void runBefore() {
        nationalPark = new NationalPark();
    }

    @Test
    public void addAnimalTest() {
        assertEquals(0, nationalPark.getAnimalsSize());
        nationalPark.addAnimal("rabbit");
        assertEquals(1, nationalPark.getAnimalsSize());
        nationalPark.addAnimal("wolf");
        assertEquals(2, nationalPark.getAnimalsSize());

        Map<String, Integer> population = nationalPark.getPopulation();
        assertEquals(1, (int) population.get("rabbit"));
        assertEquals(1, (int) population.get("wolf"));
    }

    @Test
    public void getPopulationTest() {
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("wolf");

        Map<String, Integer> population = nationalPark.getPopulation();

        assertEquals(4, (int) population.get("rabbit"));
        assertEquals(1, (int) population.get("wolf"));

    }

    @Test
    public void feedAllTest() {
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("wolf");

        assertEquals(5, nationalPark.getAnimalsSize());
        nationalPark.feedAll();
        assertEquals("rabbit", nationalPark.getAnimals().get(0).getType());
        assertEquals("wolf", nationalPark.getAnimals().get(1).getType());
        assertEquals(2, nationalPark.getAnimalsSize());
        for (Animal animal : nationalPark.getAnimals()) {
            assertTrue(animal.isFed());
        }
    }

    @Test
    public void feedTest() {
        Animal wolf = new Wolf();
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        List<Animal> fedAnimals = new ArrayList<>();
        assertFalse(wolf.isFed());
        nationalPark.feed(wolf, nationalPark.getAnimals(), fedAnimals);
        assertTrue(wolf.isFed());
        assertEquals(0, nationalPark.getAnimalsSize());
        assertEquals(1, fedAnimals.size());
    }

    @Test
    public void feedCombineTest() {
        Animal wolf = new Wolf();
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        List<Animal> fedAnimals = new ArrayList<>();
        fedAnimals.add(new Rabbit());

        assertFalse(wolf.isFed());

        nationalPark.feed(wolf, nationalPark.getAnimals(), fedAnimals);
        assertTrue(wolf.isFed());
        assertEquals(0, nationalPark.getAnimalsSize());
        assertEquals(1, fedAnimals.size());

    }

    @Test
    public void reproduceAllTest() {
        Animal rabbit1 = new Rabbit();
        rabbit1.setCyclesBeforeReproduction(0);
        Animal rabbit2 = new Rabbit();
        rabbit2.setCyclesBeforeReproduction(1);
        Animal rabbit3 = new Rabbit();
        rabbit3.setCyclesBeforeReproduction(0);
        nationalPark.addAnimal(rabbit1);
        nationalPark.addAnimal(rabbit2);
        nationalPark.addAnimal(rabbit3);
        nationalPark.reproduceAll();
        assertEquals(5,nationalPark.getAnimalsSize());
        for (Animal animal : nationalPark.getAnimals()) {
            assertEquals("rabbit", animal.getType());
        }
    }

    @Test
    public void updateDeltaAnimalsTest() {

    }

    @Test
    public void collectProfits() {
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        assertEquals((new Rabbit()).getProfit() * 3, nationalPark.collectProfits());
    }

    @Test
    public void nextCycleTest() {
        nationalPark.addAnimal("wolf");
        nationalPark.addAnimal("rabbit");
        //wolf will die because no food
        //we should get the profits of the rabbit
        //there should be one more rabbit
        int initialMoney = nationalPark.getMoney();
        nationalPark.nextCycle();
        assertEquals(2, nationalPark.getAnimalsSize());
        for (Animal animal : nationalPark.getAnimals()) {
            assertEquals("rabbit", animal.getType());
        }
        //This is * 1 because the profit of a newborn is not added, not a bug!
        assertEquals(initialMoney + (new Rabbit()).getProfit(), nationalPark.getMoney());
    }

    @Test
    public void generateAnimalTest() {
        assertEquals("rabbit", nationalPark.generateAnimal("rabbit").getType());
        assertEquals("wolf", nationalPark.generateAnimal("wolf").getType());
        assertEquals("moose", nationalPark.generateAnimal("moose").getType());
        assertEquals("owl", nationalPark.generateAnimal("owl").getType());
        assertEquals("bear", nationalPark.generateAnimal("bear").getType());
        assertNull(nationalPark.generateAnimal("animal"));
    }

    @Test
    public void buyAnimalTest() {
        int initialMoney = nationalPark.getMoney();
        nationalPark.buyAnimal("rabbit");
        assertEquals("rabbit", nationalPark.getAnimals().get(0).getType());
        assertEquals(initialMoney - (new Rabbit()).getCost(), nationalPark.getMoney());
    }

    @Test
    public void setMoneyTest() {
        nationalPark.setMoney(100);
        assertEquals(100, nationalPark.getMoney());
    }

    @Test
    public void setCycleTest() {
        nationalPark.setCycle(10);
        assertEquals(10, nationalPark.getCycle());
    }

    @Test
    public void getAllTypesTest() {
        List<String> allTypes = List.of("rabbit", "wolf", "moose", "owl", "bear");
        assertEquals(allTypes, nationalPark.getAllTypes());
    }

    @Test
    public void setDeltaAnimals() {
        Map<String, Integer> pop = new HashMap<>();
        pop.put("wolf", 10);
        pop.put("rabbit", 20);
        pop.put("moose", 30);
        nationalPark.setDeltaAnimals(pop);
        assertEquals(pop, nationalPark.getDeltaAnimals());
    }


}