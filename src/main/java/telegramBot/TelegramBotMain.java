package telegramBot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotMain extends Thread {
    private final long chat_id;
    private final String botName;
    private final String botToken;

    public TelegramBotMain(long chat_id, String botName, String botToken) {
        super();
        this.chat_id = chat_id;
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public void run() {
        // Se inicializa el contexto de la API
        ApiContextInitializer.init();

        // Se crea un nuevo Bot API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        FishTelegramBot fishBot = new FishTelegramBot(chat_id, botName, botToken);

        try {
            // Se registra el bot
            telegramBotsApi.registerBot(fishBot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}