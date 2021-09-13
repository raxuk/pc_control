package main;

import telegramBot.TelegramBotMain;

public class main {
    private static long chat_id = 1; //TELEGRAM USER ID
    private static String botName = System.getProperty("user.name"); //$NON-NLS-1$
    private static String botToken = ""; //TELEGRAM BOT API
    static TelegramBotMain tb;

    public static void main(String[] args) {
        startTelegramBot();
    }

    public static void startTelegramBot() {
        tb = new TelegramBotMain(chat_id, botName, botToken);
        tb.start();
    }
}
