package org.example;

import org.example.pages.HomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class InvalidInputTest extends PlaywrightTestBase {


    private HomePage home;


    @BeforeEach
    void setUp() {
        home = new HomePage(page).open();
    }

// Scrum 35 - Testfall 169: Kontrollera att felmeddelande visas vid ogiltigt tecken
    // Vahid

    @Test
    public void shouldShowErrorWhenEnteringInvalidCharacterInName() {


        home.competitors().add("?");

        assertThat(home.results().errorBanner()).isVisible();
        assertThat(home.results().errorBanner()).containsText("Name must contain valid letters");




    }



@Test
public void shouldShowErrorWhenEnteringSpaceInName() {

        home.competitors().add(" ");

        assertThat(home.results().errorBanner()).isVisible();
        assertThat(home.results().errorBanner()).containsText("Name must contain valid letters");
}



}

