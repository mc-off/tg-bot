package ru.mcoff;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class PhotoBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            switch (message_text) {
                case ("/start"): {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText(message_text);
                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } break;
                case ("/picAsync"): {
                    URL resource = getClass().getClassLoader().getResource("IMG_1179.JPG");
                    if (resource == null) {
                        throw new IllegalArgumentException("file not found!");
                    } else {
                        try {
                            File file = new java.io.File(resource.toURI());
                            SendDocument msg = new SendDocument()
                                    .setChatId(chat_id)
                                    .setDocument(file)
                                    .setCaption("Caption");
                            SendMessage textMessage = new SendMessage()
                                    .setChatId(chat_id)
                                    .setText("Text after photo");

                            Thread thread = new Thread() {
                                public void run(){
                                    try {
                                        Calendar calendar = new GregorianCalendar();
                                        calendar.setTime(new Date());
                                        execute(new SendMessage().setChatId(chat_id).setText("Start upload image in "+ calendar.getTime()));
                                        execute(msg);
                                        Calendar calendar1 = new GregorianCalendar();
                                        calendar1.setTime(new Date());
                                        execute(new SendMessage().setChatId(chat_id).setText("Finished in " + calendar1.getTime()));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            Thread thread2 = new Thread() {
                                public void run(){
                                    try {
                                        //sleep(1000);
                                        execute(textMessage);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                            thread2.start();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                } break;
                case ("/pic"): {
                    URL resource = getClass().getClassLoader().getResource("IMG_1179.JPG");
                    if (resource == null) {
                        throw new IllegalArgumentException("file not found!");
                    } else {
                        try {
                            File file = new java.io.File(resource.toURI());
                            SendDocument msg = new SendDocument()
                                    .setChatId(chat_id)
                                    .setDocument(file)
                                    .setCaption("Photo");
                            SendMessage textMessage = new SendMessage()
                                    .setChatId(chat_id)
                                    .setText("Sync text after photo");
                            try {
                                execute(msg); // Call method to send the photo
                                execute(textMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                } break;
                case ("/markup"): {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("Here is your keyboard");
                    // Create ReplyKeyboardMarkup object
                    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                    // Create the keyboard (list of keyboard rows)
                    List<KeyboardRow> keyboard = new ArrayList<>();
                    // Create a keyboard row
                    KeyboardRow row = new KeyboardRow();
                    // Set each button, you can also use KeyboardButton objects if you need something else than text
                    row.add("/pic");
                    // Add the first row to the keyboard
                    keyboard.add(row);
                    // Create another keyboard row
                    row = new KeyboardRow();
                    // Set each button for the second line
                    row.add("/picAsync");
                    // Add the second row to the keyboard
                    keyboard.add(row);
                    // Set the keyboard to the markup
                    keyboardMarkup.setKeyboard(keyboard);
                    // Add it to the message
                    message.setReplyMarkup(keyboardMarkup);
                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } break;
                case ("/hide"): {
                    SendMessage msg = new SendMessage()
                            .setChatId(chat_id)
                            .setText("Keyboard hidden");
                    ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                    msg.setReplyMarkup(keyboardMarkup);
                    try {
                        execute(msg); // Call method to send the photo
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } break;

            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Message contains photo
            // Set variables
            long chat_id = update.getMessage().getChatId();

            List<PhotoSize> photos = update.getMessage().getPhoto();
            String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            int f_width = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            int f_height = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
            SendPhoto msg = new SendPhoto()
                    .setChatId(chat_id)
                    .setPhoto(f_id);

            try {
                execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "PhotoBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "TOKEN_BOT";
    }
}
