package tests;

import animals.AnimalType;
import animals.petstore.pet.attributes.Breed;
import animals.petstore.pet.attributes.Gender;
import animals.petstore.pet.attributes.Skin;
import animals.petstore.pet.types.Cat;
import animals.petstore.pet.types.Dog;
import animals.petstore.store.DuplicatePetStoreRecordException;
import animals.petstore.store.PetNotFoundSaleException;
import animals.petstore.store.PetStore;
import number.Numbers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import animals.petstore.pet.attributes.PetType;
import animals.petstore.pet.Pet;
import animals.petstore.pet.types.Snake;

public class PetStoreTest
{
    private static PetStore petStore;

    @BeforeEach
    public void loadThePetStoreInventory()
    {
        petStore = new PetStore();
        petStore.init();
    }

    @Test
    @DisplayName("Inventory Count Test")
    public void validateInventory()
    {
        assertEquals(6, petStore.getPetsForSale().size(),"Inventory counts are off!");
    }

    @Test
    @DisplayName("Print Inventory Test")
    public void printInventoryTest()
    {
        petStore.printInventory();
    }

    @Test
    @DisplayName("Sale of Poodle Remove Item Test")
    public void poodleSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        // Validation
        petStore.soldPetItem(poodle);
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

//    New test ---------------------

    @Test
    @DisplayName("Sale of cat Remove Item Test")
    public void catSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;
        Cat cat = new Cat(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.SPHYNX,
                new BigDecimal("100.00"), 2);

        petStore.soldPetItem(cat);
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Sale of Snake Remove Item Test")
    public void snakeSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("200.00"), 4);

        petStore.soldPetItem(snake);
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }

    @Test
    @DisplayName("Snake Not Found Exception Test")
    public void snakeNotFoundExceptionTest() {
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON);
        String expectedMessage = "The Pet is not part of the pet store!!";

        Exception exception = assertThrows(PetNotFoundSaleException.class, () -> {
            petStore.soldPetItem(snake);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Snake Duplicate Record Exception Test")
    public void snakeDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("200.00"), 4));
        Snake snake = new Snake(AnimalType.DOMESTIC, Skin.SCALES, Gender.MALE, Breed.BALL_PYTHON,
                new BigDecimal("200.00"), 4);

        String expectedMessage = "Duplicate Snake record store id [4]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () -> {
            petStore.soldPetItem(snake);
        });
        assertEquals(expectedMessage, exception.getMessage(), "DuplicateRecordExceptionTest was NOT encountered!");
    }

    @Test
    @DisplayName("Set Pet Store ID Test")
    public void setPetStoreIdTest() {
        // Create a snake using constructor matching Pet's constructor pattern
        Dog dog = new Dog(AnimalType.DOMESTIC, new BigDecimal("650.00"), Gender.MALE);

        // Verify initial store ID is 0
        assertEquals(0, dog.getPetStoreId(), "Initial pet store ID should be 0");

        // Change the store ID
        int newStoreId = 10;
        dog.setPetStoreId(newStoreId);

        // Verify the store ID was updated
        assertEquals(newStoreId, dog.getPetStoreId(), "Pet store ID should match the new value");
    }


    @Test
    @DisplayName("Add Pet Inventory Test")
    public void addPetInventoryTest() {
        petStore.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, new BigDecimal("650.00"), Gender.MALE));
        assertEquals(7, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
    }


    @Test
    @DisplayName("Unknown Pet Type Exception Test")
    public void unknownPetTypeExceptionTest() {
        // Create a custom pet that's not a Dog, Cat, or Snake
        Pet unknownPet = new Pet(PetType.BIRD, new BigDecimal("100.00"), Gender.MALE, 1) {
            @Override
            public String toString() {
                return "Unknown Pet";
            }
        };

        String expectedMessage = "Unknown pet type cannot be sold!";
        Exception exception = assertThrows(PetNotFoundSaleException.class, () -> {
            petStore.soldPetItem(unknownPet);
        });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test Init Add Duplicate Item")
    public void initAddDuplicateItemTest() {

        petStore = new PetStore();

        Dog duplicateDog = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        petStore.initAddDuplicateItem(duplicateDog);

        assertEquals(7, petStore.getPetsForSale().size(),
                "Inventory should have 7 items after adding duplicate");

        assertTrue(petStore.getPetsForSale().stream()
                        .anyMatch(p -> p instanceof Dog &&
                                ((Dog)p).getBreed() == Breed.POODLE &&
                                p.getPetStoreId() == 1),
                "Duplicate dog should be in inventory");

        String expectedMessage = "Duplicate Dog record store id [1]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () -> {
            petStore.soldPetItem(duplicateDog);
        });
        assertEquals(expectedMessage, exception.getMessage(),
                "Should throw duplicate record exception");
    }


    //-------------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Poodle Duplicate Record Exception Test")
    public void poodleDupRecordExceptionTest() {
        petStore.addPetInventoryItem(new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1));
        Dog poodle = new Dog(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        // Validation
        String expectedMessage = "Duplicate Dog record store id [1]";
        Exception exception = assertThrows(DuplicatePetStoreRecordException.class, () ->{
            petStore.soldPetItem(poodle);});
        assertEquals(expectedMessage, exception.getMessage(), "DuplicateRecordExceptionTest was NOT encountered!");

    }

    @Test
    @DisplayName("Sale of Sphynx Remove Item Test")
    public void sphynxSoldTest() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        // Validation
        assertEquals(inventorySize, petStore.getPetsForSale().size(), "Expected inventory does not match actual");
        assertEquals(sphynx.getPetStoreId(), removedItem.getPetStoreId(), "The cat items are identical");
    }




    /**
     * Limitations to test factory as it does not instantiate before all
     * @return list of {@link DynamicNode} that contains the test results
     * @throws DuplicatePetStoreRecordException if duplicate pet record is found
     * @throws PetNotFoundSaleException if pet is not found
     */
    @TestFactory
    @DisplayName("Sale of Sphynx Remove Item Test2")
    public Stream<DynamicNode> sphynxSoldTest2() throws DuplicatePetStoreRecordException, PetNotFoundSaleException {
        int inventorySize = petStore.getPetsForSale().size() - 1;

        Cat sphynx = new Cat(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);
        Cat removedItem = (Cat) petStore.soldPetItem(sphynx);

        // Validation
        List<DynamicNode> nodes = new ArrayList<>();
        List<DynamicTest> dynamicTests = Arrays.asList(
                dynamicTest("Inventory Check Size Test ", () -> assertEquals(inventorySize,
                        petStore.getPetsForSale().size())),
                dynamicTest("The cat objects match ", () -> assertEquals(sphynx.toString(),
                        removedItem.toString()))
        );
        nodes.add(dynamicContainer("Cat Item 2 Test", dynamicTests));//dynamicNode("", dynamicContainers);

        return nodes.stream();
    }
    
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, -10, 128, Integer.MIN_VALUE}) // six numbers
    void isNumberEven(int number)
    {
        assertTrue(Numbers.isEven(number));
    }

}