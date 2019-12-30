package telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import protocols.StudentProtocol;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BotInitializer extends TelegramLongPollingBot {
  private final String botUserName;
  private final String botToken;
  private final String httpLink;

  public BotInitializer(final String botUserName, final String botToken, final String httpLink) {
    this.botUserName = botUserName;
    this.botToken = botToken;
    this.httpLink = httpLink;
  }

  @Override
  public void onUpdateReceived(Update update) {
    SendMessage message = new SendMessage()
        .setParseMode("HTML")
        .setChatId("-214767533")
        .setText("Hello!");
    System.out.println(update.getMessage().getChatId());
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
    String[] congratulation;
    congratulation = new String[3];
    congratulation[0] = "Sizni qutlug' " + (2019 - Integer.parseInt(convertToStrDate(student.birthDay()))) + " yoshingiz bilan \n" +
        " shogirtlaringiz nomidan tabriklayman! Ilohim bundanda katta marralarni zapt eting!\n" +
        "Sizga sog'lik, Baxt, Omad va dunyodagi eng ezgu tilaklarni tilab qolaman xurmat bilan Maftunbek!";
    congratulation[1] = " Поздравляем вас с " + (2019 - Integer.parseInt(convertToStrDate(student.birthDay()))) + "-летием от имени ваших учеников!" +
        " добиться еще большего успеха!\n" +
        " Желаю вам крепкого здоровья, счастья, удачи и наилучших пожеланий на свете c уважением Maftunbek!";
    congratulation[2] = " Congratulations on your " + (2019 - Integer.parseInt(convertToStrDate(student.birthDay()))) + "th birthday on behalf of your students!" +
        " achieve even greater success! \n" +
        "I wish you good health, happiness, good luck and best wishes in the world with respect Maftunbek!";
    String mention = "<a href=\"tg://user?id=" + student.telegram_id() + "\">" + student.first_name() + " " + student.last_name() + "</a>";
    for (int i = 0; i < congratulation.length; i++) {
      SendMessage message = new SendMessage()
          .setParseMode(ParseMode.HTML)
          .setChatId("-1001397860592")
          .setText(mention + " " + congratulation[i]);

      try {
        execute(message); // Sending our message object to user
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
    return "run";
  }

  private String convertToStrDate(Date date) {
    String d = new SimpleDateFormat("YYYY").format(date);
    return d;
  }
}
