package test;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ya.praktikum.Bun;
import ya.praktikum.Burger;
import ya.praktikum.Ingredient;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class TestBurgerPriceCalculation {

    private static final float DELTA = 0.001f;

    private String testName;
    private float expectedPrice;
    private Ingredient[] testIngredients;
    private Burger burger;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"2 Булки(по 50 руб.) + соус(50 руб.)", 150.0f, new Ingredient[]{createMockIngredient(50.0f)}},
                {"2 Булки(по 50 руб.) + начинка(70 руб.)+ соус(50 руб.)", 220.0f,
                        new Ingredient[]{createMockIngredient(70.0f), createMockIngredient(50.0f)}},
                {"Только булки(по 50 руб.) без ингредиентов", 100.0f, new Ingredient[]{}},
                {"2 Булки(по 50 руб.) + две начинки (80 + 10 руб.) + соус (10 руб.)", 200.0f,
                        new Ingredient[]{createMockIngredient(80.0f), createMockIngredient(10.0f), createMockIngredient(10.0f)}}
        });
    }

    // Конструктор с параметрами — JUnit вызовет его для каждого набора данных
    public TestBurgerPriceCalculation(String testName, float expectedPrice, Ingredient[] testIngredients) {
        this.testName = testName;
        this.expectedPrice = expectedPrice;
        this.testIngredients = testIngredients;
    }

    private static Ingredient createMockIngredient(float price) {
        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getPrice()).thenReturn(price);
        return ingredient;
    }

    @Before
    public void setUp() {
        Bun mockBun = mock(Bun.class);
        when(mockBun.getPrice()).thenReturn(50.0f);

        burger = new Burger();
        burger.setBuns(mockBun);

        for (Ingredient ingredient : testIngredients) {
            burger.addIngredient(ingredient);
        }
    }

    @Test
    public void getPriceTest() {
        float actualPrice = burger.getPrice();
        assertEquals("Test case: " + testName, expectedPrice, actualPrice, DELTA);
    }
}
