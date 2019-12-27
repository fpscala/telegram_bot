package telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotInitializer extends TelegramLongPollingBot {
  private final String botUserName;
  private final String botToken;
  private final String httpLink;
  private long chat_id;

  public BotInitializer(final String botUserName, final String botToken, final String httpLink) {
    this.botUserName = botUserName;
    this.botToken = botToken;
    this.httpLink = httpLink;
  }

  @Override
  public void onUpdateReceived(Update update) {

    String data = "https://api.telegram.org/bot" + botToken + update.getMessage().getChatId();

    System.out.println(data);


    if (update.hasMessage() && update.getMessage().hasText()) {
      update.getUpdateId();
      SendMessage message = new SendMessage().setParseMode("HTML").setChatId("-214767533");
      chat_id = update.getMessage().getChatId();
      System.out.println(chat_id);
      String message_text = update.getMessage().getText();
      Long id = update.getMessage().getChat().getId();

      try {
        message.setText("123");
        execute(message); // Sending our message object to user
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
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

  public String massege(String massage) {
    return massage;
  }
}
