package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ya.praktikum.Bun;
import ya.praktikum.Burger;
import ya.praktikum.Ingredient;
import ya.praktikum.IngredientType;

import static io.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestBurger {

    private Burger burger;

    @Mock
    private Bun mockBun;

    @Mock
    private Ingredient mockSauce;

    @Mock
    private Ingredient mockFilling;

    @Before
    public void setUp() {
        burger = new Burger();
    }

    @Test
    public void setBuns_UpdatesBurgerBunReference() {
        burger.setBuns(mockBun);
        assertEquals(mockBun, burger.bun);
    }

    @Test
    public void addIngredient_IncreasesSizeByOne() {
        burger.addIngredient(mockSauce);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void addIngredient_StoresCorrectInstance() {
        burger.addIngredient(mockSauce);
        assertEquals(mockSauce, burger.ingredients.get(0));
    }

    @Test
    public void removeIngredientReducesSize() {
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void removeIngredient_DecreasesListSizeByOne() {
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void moveIngredient_SwapsElementPositionsCorrectly() {
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        burger.moveIngredient(0, 1);

        assertEquals(mockFilling, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredient_PreservesMovedElementAtTargetIndex() {
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        burger.moveIngredient(0, 1);

        assertEquals(mockSauce, burger.ingredients.get(1));
    }

    @Test
    public void getPrice_IncludesBunsAndAllIngredients_CalculatesCorrectTotal() {
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockSauce.getPrice()).thenReturn(50f);
        when(mockFilling.getPrice()).thenReturn(75f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockSauce);
        burger.addIngredient(mockFilling);

        // ОР: цена = булка*2 + ингредиент1 + ингредиент2
        float expectedPrice = 100f * 2 + 50f + 75f; // = 325
        assertEquals(expectedPrice, burger.getPrice(), 0.001);
    }

    @Test
    public void getPrice_WithOnlyBuns_ReturnsDoubleBunPrice() {
        when(mockBun.getPrice()).thenReturn(100f);

        burger.setBuns(mockBun);

        float expectedPrice = 100f * 2;
        assertEquals(expectedPrice, burger.getPrice(), 0.001);
    }

    @Test
    public void getReceipt_WithBunAndSauce_GeneratesCorrectFormattedOutput() {
        when(mockBun.getName()).thenReturn("Флюоресцентная булка");
        when(mockBun.getPrice()).thenReturn(100f);
        when(mockSauce.getType()).thenReturn(IngredientType.SAUCE);
        when(mockSauce.getName()).thenReturn("Соус Spicy-X");
        when(mockSauce.getPrice()).thenReturn(50f);

        burger.setBuns(mockBun);
        burger.addIngredient(mockSauce);

        String receipt = burger.getReceipt();

        String expectedReceipt = String.format("(==== Флюоресцентная булка ====)%n" +
                "= sauce Соус Spicy-X =%n" +
                "(==== Флюоресцентная булка ====)%n" +
                "%n" +
                "Price: 250,000000%n");

        assertEquals(expectedReceipt, receipt);
    }

}