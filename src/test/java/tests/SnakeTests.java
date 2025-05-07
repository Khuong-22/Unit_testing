package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Snake;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SnakeTests {

    private static Snake snake;

    @BeforeAll
    public static void createAnimals() {
        snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON);
    }

    @Test
    @Order(1)
    @DisplayName("Snake Type Tests Domestic")
    public void animalTypeTests() {
        assertEquals(AnimalType.DOMESTIC, snake.getAnimalType(), "Snake Type Expected[" + AnimalType.DOMESTIC
                + "] Actual Type[" + snake.getAnimalType() + "]");
    }

    @Test
    @Order(2)
    @DisplayName("Snake Speak Hiss Tests")
    public void snakeGoesHissTest() {
        assertEquals("The snake goes hiss! hiss!", snake.speak(), "I was expecting hiss! hiss! But got " + snake.speak());
    }

    @Test
    @Order(3)
    @DisplayName("Snake Scales Hypoallergenic Test")
    public void snakeHypoallergenicTests() {
        assertEquals("The snake is hyperallergetic!", snake.snakeHypoallergenic(),
                "The snake should be hyperallergetic with scales!");
    }

    @Test
    @Order(4)
    @DisplayName("Snake Breed Test")
    public void breedTest() {
        assertEquals(Breed.BALL_PYTHON, snake.getBreed(), "Expected Ball Python breed! But got: " + snake.getBreed());
    }

    @Test
    @Order(5)
    @DisplayName("Snake Venomous Test - Non-venomous")
    public void nonVenomousTest() {
        assertFalse(snake.isVenomous(), "Ball Python should not be venomous!");
    }

    @Test
    @Order(6)
    @DisplayName("Snake Venomous Test - Venomous")
    public void venomousTest() {
        Snake venomousSnake = new Snake(AnimalType.WILD, Skin.SCALES, Gender.FEMALE, Breed.COPPERHEAD);
        assertTrue(venomousSnake.isVenomous(), "Copperhead should be venomous!");
    }

    @Test
    @Order(7)
    @DisplayName("Snake Wild Speak Test")
    public void snakeWildSpeakTest() {
        Snake wildSnake = new Snake(AnimalType.WILD, Skin.SCALES, Gender.FEMALE, Breed.COPPERHEAD);
        assertEquals("The snake goes ssss! ssss!", wildSnake.speak(), "Wild snake should make ssss! sound. But got " + wildSnake.speak());
    }

    @Test
    @Order(8)
    @DisplayName("Snake Cost Test")
    public void snakeCostTest() {
        Snake costSnake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON, new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), costSnake.getCost(), "Snake cost should be $200.00. But snake cost is " + costSnake.getCost());
    }

    @Test
    @Order(9)
    @DisplayName("Snake Store ID Test")
    public void snakeStoreIdTest() {
        Snake storeSnake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("200.00"), 1);
        assertEquals(1, storeSnake.getPetStoreId(), "Snake store ID should be 1. But ID is " + storeSnake.getPetStoreId());
    }

    @Test
    @Order(10)
    @DisplayName("Snake ToString Test")
    public void snakeToStringTest() {
        String expected = snake.toString();
        assertTrue(expected.contains("The snake is DOMESTIC"));
        assertTrue(expected.contains("The breed is BALL_PYTHON"));
        assertTrue(expected.contains("The snake is not venomous"));
        assertTrue(expected.contains("The snake is hyperallergetic"));
        assertTrue(expected.contains("The snake goes hiss! hiss!"));
    }
}
