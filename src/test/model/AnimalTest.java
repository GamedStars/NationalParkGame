package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


public class AnimalTest {

    private NationalPark nationalPark;
    private Animal wolf;
    private Animal rabbit;
    private Animal moose;
    private Animal bear;
    private Animal owl;

    @BeforeEach
    public void runBefore() {
        nationalPark = new NationalPark();
        wolf = new Wolf();
        rabbit = new Rabbit();
        moose = new Moose();
        bear = new Bear();
        owl = new Owl();

        for (int i = 0; i < 3; i++) {
            nationalPark.addAnimal("rabbit");
        }
        nationalPark.getAnimals().add(wolf);
        nationalPark.getAnimals().add(rabbit);
        //nationalPark.animals is a list with 4 rabbits, 1 wolf
    }

    @Test
    public void canReproduceTest() {
        assertFalse(wolf.canReproduce());
        assertFalse(rabbit.canReproduce());
        wolf.setCyclesBeforeReproduction(0);
        assertTrue(wolf.canReproduce());
        assertFalse(rabbit.canReproduce());
        rabbit.setCyclesBeforeReproduction(1);
        assertFalse(rabbit.canReproduce());
        rabbit.setCyclesBeforeReproduction(-1);
        assertFalse(rabbit.canReproduce());
        rabbit.setCyclesBeforeReproduction(0);
        assertTrue(rabbit.canReproduce());
    }

    @Test
    public void reproduceTest() {
        wolf.setCyclesBeforeReproduction(0);
        Animal babyWolf = wolf.reproduce();
        assertEquals("wolf", babyWolf.getType());
        assertNotEquals(0, wolf.getCyclesBeforeReproduction());

        rabbit.setCyclesBeforeReproduction(0);
        Animal babyRabbit = rabbit.reproduce();
        assertEquals("rabbit", babyRabbit.getType());
        assertNotEquals(0, rabbit.getCyclesBeforeReproduction());

        owl.setCyclesBeforeReproduction(0);
        Animal babyOwl = owl.reproduce();
        assertEquals("owl", babyOwl.getType());
        assertNotEquals(0, owl.getCyclesBeforeReproduction());

        bear.setCyclesBeforeReproduction(0);
        Animal babyBear = bear.reproduce();
        assertEquals("bear", babyBear.getType());
        assertNotEquals(0, bear.getCyclesBeforeReproduction());

        moose.setCyclesBeforeReproduction(0);
        Animal babyMoose = moose.reproduce();
        assertEquals("moose", babyMoose.getType());
        assertNotEquals(0, moose.getCyclesBeforeReproduction());
    }

    @Test
    public void hasEnoughFoodTest() {
        assertTrue(wolf.hasEnoughFood(nationalPark.getAnimals()));
        assertTrue(rabbit.hasEnoughFood(nationalPark.getAnimals()));
        nationalPark.setAnimals(new ArrayList<>());
        assertFalse(wolf.hasEnoughFood(nationalPark.getAnimals()));
        assertTrue(rabbit.hasEnoughFood(nationalPark.getAnimals()));
        wolf.setHunger(0);
        //if the wolf is not hungry then it doesn't need food
        assertTrue(wolf.hasEnoughFood(nationalPark.getAnimals()));
        assertTrue(rabbit.hasEnoughFood(nationalPark.getAnimals()));
        wolf.setHunger(3);
        nationalPark.addAnimal("wolf");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        nationalPark.addAnimal("rabbit");
        assertTrue(wolf.hasEnoughFood(nationalPark.getAnimals()));
        assertTrue(rabbit.hasEnoughFood(nationalPark.getAnimals()));

    }

    @Test
    public void eatTest() {
        assertTrue(rabbit.hasEnoughFood(nationalPark.getAnimals()));
        assertTrue(rabbit.isFed()); //rabbits are always fed, grass is plentiful
        assertEquals(5, nationalPark.getAnimalsSize());
        rabbit.eat(nationalPark.getAnimals());
        assertTrue(rabbit.isFed());
        assertEquals(5, nationalPark.getAnimalsSize());
        assertTrue(rabbit.hasEnoughFood(nationalPark.getAnimals()));

        assertTrue(wolf.hasEnoughFood(nationalPark.getAnimals()));
        assertFalse(wolf.isFed());
        wolf.eat(nationalPark.getAnimals());
        assertTrue(wolf.isFed());
        assertEquals(2, nationalPark.getAnimalsSize());
        assertTrue(wolf.hasEnoughFood(nationalPark.getAnimals()));
        //This is true because the wolf has eaten, he doesn't need more food therefore there is enough food for him
    }

    @Test
    public void otherEatTest() {
        List<Animal> fodder = new ArrayList<>();
        fodder.add(new Rabbit());
        fodder.add(new Rabbit());

        List<Animal> fedAnimals = new ArrayList<>();
        fedAnimals.add(new Rabbit());
        fedAnimals.add(new Rabbit());
        fedAnimals.add(new Rabbit());
        fedAnimals.add(new Rabbit());

        assertFalse(wolf.isFed());
        wolf.eat(fodder, fedAnimals);
        assertTrue(wolf.isFed());
        assertEquals(0, fodder.size());
        assertEquals(3, fedAnimals.size());
    }

    @Test
    public void eatThisTypeTest() {
        List<Animal> fodder = new ArrayList<>();
        fodder.add(new Wolf());
        fodder.add(new Rabbit());
        fodder.add(new Rabbit());
        fodder.add(new Moose());

        assertEquals(wolf.getMaxHunger(), wolf.getHunger());
        assertEquals(4, fodder.size());
        wolf.eatThisType(fodder, "rabbit");
        assertEquals(2, fodder.size());
        wolf.eatThisType(fodder, "moose");
        assertEquals(1, fodder.size());

        fodder = new ArrayList<>();
        fodder.add(new Rabbit());
        fodder.add(new Rabbit());
        fodder.add(new Rabbit());
        fodder.add(new Rabbit());

        wolf.setHunger(wolf.getMaxHunger());
        assertEquals(wolf.getMaxHunger(), wolf.getHunger());
        wolf.eatThisType(fodder, "rabbit");
        assertEquals(1, fodder.size());
    }

    @Test
    public void setMaxHungerTest() {
        wolf.setMaxHunger(0);
        assertEquals(0, wolf.getMaxHunger());
    }

    @Test
    public void setMaxReproductiveTimeTest() {
        wolf.setMaxReproductiveTime(10);
        assertEquals(10, wolf.getMaxReproductiveTime());
    }

    @Test
    public void setCostTest() {
        wolf.setCost(0);
        assertEquals(0, wolf.getCost());
    }

    @Test
    public void setProfitTest() {
        wolf.setProfit(0);
        assertEquals(0, wolf.getProfit());
    }

    @Test
    public void getFoodListTest() {
        List<String> wolfFoodList = List.of("rabbit", "moose");
        assertEquals(wolfFoodList, wolf.getFoodList());
    }
}
