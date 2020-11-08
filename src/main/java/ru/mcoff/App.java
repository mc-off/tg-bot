package ru.mcoff;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new PhotoBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("PhotoBot successfully started!");
       // System.out.println( "Hello World!" );
    }
}
