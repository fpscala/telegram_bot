package telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import protocols.StudentProtocol;

public class BotInitializer extends TelegramLongPollingBot {
  private final String botUserName;
  private final String botToken;
  private final String httpLink;
  private String user_id;
  private long chat_id;

  public BotInitializer(final String botUserName, final String botToken, final String httpLink) {
    this.botUserName = botUserName;
    this.botToken = botToken;
    this.httpLink = httpLink;
  }

  @Override
  public void onUpdateReceived(Update update) {
    SendMessage message = new SendMessage()
        .setParseMode("HTML")
        .setChatId(update.getMessage().getChatId())
        .setText("Hello!");
    try {
      execute(message); // Sending our message object to user
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getBotUsername() {
    return new BotInitializer(botUserName, botToken, httpLink).botUserName;
  }

  @Override
  public String getBotToken() {
    return new BotInitializer(botUserName, botToken, httpLink).botToken;
  }

  public String message(StudentProtocol.Student student) {
    String congratulation = " sizni tug`ilgan kuninggiz bilan chin qalbimdan muborakbod qilaman!";
    user_id = ""+ student.telegram_id()+ "";
    String mention = "<a href=\"tg://user?id=" + user_id + "\">" + student.first_name() + "</a>";
    SendMessage message = new SendMessage()
        .setParseMode(ParseMode.HTML)
        .setChatId("-214767533")
        .setText(mention + " " + congratulation);
    try {
      execute(message); // Sending our message object to user
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
    return "run";
  }
}
