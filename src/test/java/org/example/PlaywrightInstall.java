package org.example;

import com.microsoft.playwright.CLI;

public class PlaywrightInstall {
    public static void main(String[] args) {
        try {
            CLI.main(new String[]{"install"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
