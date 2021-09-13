package telegramBot;

import com.github.sarxos.webcam.Webcam;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.ApagarOrdenador;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FishTelegramBot extends TelegramLongPollingBot {
    private long chat_id;
    private final String botToken;
    private final String botname;
    private long messageChat_Id;
    private boolean alive;
    private final Timer timer;
    private final int loopTimeTimer = 300; //seg
    private boolean secured;
    private ArrayList<String> securedString;
    private final String[] securedPassword = {"estado", "estado", "webcam", "estado", "webcam", "captura", "apagar"};

    public FishTelegramBot(long chat_id, String botname, String botToken) {
        this.chat_id = chat_id;
        this.botToken = botToken;
        this.botname = botname;
        this.alive = false;
        this.secured = false;
        this.securedString = new ArrayList<String>();
        this.timer = new Timer();
        mandarMensaje(" ### Iniciando ### ");
    }

    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            messageChat_Id = update.getMessage().getChatId();

            if (chat_id == messageChat_Id) {
                // IF TEXT IS /start
                if (message_text.equals("/" + "start") || message_text.equals("start")) {
                    // Create a message object object
                    SendMessage message = new SendMessage().setChatId(chat_id).setText("Opciones");
                    // Create ReplyKeyboardMarkup object
                    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                    // Create the keyboard (list of keyboard rows)
                    List<KeyboardRow> keyboard = new ArrayList();
                    // Create a keyboard row
                    KeyboardRow row = new KeyboardRow();
                    // Set each button, you can also use KeyboardButton objects if you need
                    // something else than text
                    row.add("estado");
                    row.add("cerrar");
                    row.add("apagar pc");
                    // Add the first row to the keyboard
                    keyboard.add(row);
//				// Create another keyboard row
                    row = new KeyboardRow();
                    row.add("alive");
                    row.add("captura");
                    row.add("webcam");
//				// Add the second row to the keyboard
                    keyboard.add(row);
                    // Set the keyboard to the markup
                    keyboardMarkup.setKeyboard(keyboard);
                    keyboardMarkup.setResizeKeyboard(true);
                    // Add it to the message
                    message.setReplyMarkup(keyboardMarkup);
                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    // BUTTON 1, 1
                } else if (message_text.equals("estado")) { //$NON-NLS-1$
                    if (!this.secured) {
                        mandarMensaje("Conectado: " + getBotUsername());
                    } else {
                        securedProtocol("estado");
                    }
                    // BUTTON 1, 2
                } else if (message_text.equals("alive")) { //$NON-NLS-1$
                    if (!this.secured) {
                        alive();
                    } else {
                        securedProtocol("alive");
                    }
                    // BUTTON 1, 3
                } else if (message_text.equals("captura")) { //$NON-NLS-1$
                    if (!this.secured) {
                        capturaPantalla();
                    } else {
                        securedProtocol("captura");
                    }
                    // BUTTON 1, 4
                } else if (message_text.equals("webcam")) { //$NON-NLS-1$
                    if (!this.secured) {
                        captureWebcam();
                    } else {
                        securedProtocol("webcam");
                    }
                    // BUTTON 1, 5
                } else if (message_text.equals("cerrar")) { //$NON-NLS-1$
                    cerrar();
                    // BUTTON 1, 6
                } else if (message_text.equals("apagar pc")) { //$NON-NLS-1$
                    if (!this.secured) {
                        capturaPantalla();
                        mandarMensaje("Apagando ordenador"); //$NON-NLS-1$
                        ApagarOrdenador.shutdown();
                    } else {
                        securedProtocol("apagar");
                    }
                }
                // IF TEXT IS
                else {
                    ;
                }
            } else {
                if (chat_id == -1) {
                    chat_id = messageChat_Id;
                    mandarMensaje("chatIdMessage"); //$NON-NLS-1$
                    mandarMensaje("" + messageChat_Id); //$NON-NLS-1$
                }
            }
        }
    }

    public void capturaPantalla() {
        try {
            // Captura pantalla
            Rectangle screenRect = new Rectangle(0, 0, 0, 0);
            for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                screenRect = gd.getDefaultConfiguration().getBounds();

                BufferedImage capture = new Robot().createScreenCapture(screenRect);
                //
                File file = File.createTempFile("ss-", "-tfb.jpg"); //$NON-NLS-1$ //$NON-NLS-2$
                ImageIO.write(capture, "jpg", file); //$NON-NLS-1$

                // send photo
                SendDocument msg = new SendDocument().setChatId(chat_id).setDocument(file);

                execute(msg); // Call method to send the photo
                file.delete();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void captureWebcam() {
        try {
            Webcam webcam = Webcam.getDefault();
            if (webcam == null || webcam.getName().contains("OBS")) {
                mandarMensaje("Camara no encontrada");
            } else {

                webcam.open();
                BufferedImage image = webcam.getImage();
                webcam.close();

                //
                File file = File.createTempFile("ss-", "-tfb.jpg"); //$NON-NLS-1$ //$NON-NLS-2$
                ImageIO.write(image, "jpg", file); //$NON-NLS-1$

                // send photo
                SendDocument msg = new SendDocument().setChatId(chat_id).setDocument(file);

                execute(msg); // Call method to send the photo
                file.delete();
            }
        } catch (IOException e) {
            mandarMensaje("Error...");
        } catch (TelegramApiException e) {
            mandarMensaje("Error de telegram");
        } catch (Exception e) {
            mandarMensaje("Error camara en uso");
        }
    }

    public void mandarMensaje(String mensaje) {
        SendMessage msg = new SendMessage().setChatId(chat_id).setText(mensaje);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void alive() {
        if (!this.alive) {
            mandarMensaje(" -- Activando alive -- ");
            this.alive = true;
            this.timer.schedule(new TimerTask() {
                public void run() {
                    if (!secured) {
                        mandarMensaje("Conectado...");
                    }
                }
            }, 0, this.loopTimeTimer * 1000);
        } else {
            this.alive = false;
            this.timer.cancel();
            mandarMensaje(" -- Desactivando alive -- ");
        }
    }

    public void cerrar() {
        if (!this.secured) {
            this.secured = true;
        } else {
            this.securedString = new ArrayList<String>();
        }
    }

    public boolean securedProtocol(String secret) {
        this.securedString.add(secret);
        if (this.securedString.size() == this.securedPassword.length) {
            for (int i = 0; i < this.securedPassword.length; i++) {
                if (!this.securedPassword[i].equals(this.securedString.get(i))) {
                    this.securedString = new ArrayList<String>();
                    return false;
                }
            }
            this.securedString = new ArrayList<String>();
            this.secured = false;
            mandarMensaje(" ### Iniciando ### ");
            return true;
        }
        return false;
    }

    public String getBotUsername() {
        // Se devuelve el nombre que dimos al bot al crearlo con el BotFather
        return this.botname;
    }

    @Override
    public String getBotToken() {
        // Se devuelve el token que nos generó el BotFather de nuestro bot
        return this.botToken;
    }

}
