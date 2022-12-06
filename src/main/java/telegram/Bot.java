package telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Bot {
    private final String token = "5478752759:AAHseyLVL43iq11mOfdwBaQU1Bljr2g5sOM";
    private final TelegramBot bot = new TelegramBot(token);
    private final Config config = Config.getInstance();
    private static final Logger log = LogManager.getRootLogger();
    public Bot() {
        config.load();
        getUpdates().forEach(this::onUpdate);
    }

    public void sendMessage(long chatId, String message, String ref) {
        bot.execute(new SendMessage(chatId, message+"\n"+"<a href='"+ref+"'>Ссылка на ордер</a>").parseMode(ParseMode.HTML));
        log.info("Уведомление в телеграм отправлено");
    }
    private List<Update> getUpdates(){
        GetUpdates getUpdates;
        if (config.getUpdateId()!=null &&!config.getUpdateId().equals("")){
            getUpdates = new GetUpdates().offset(Integer.parseInt(config.getUpdateId())+1);
        }
        else {
            getUpdates = new GetUpdates();
        }
        GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
        List<Update> updates = updatesResponse.updates();
        if(updates.size()>0){
            config.setUpdateId(updates.get(updates.size()-1).updateId().toString());
        }
        return updates;
    }
    public void onUpdate(Update update) {

        if (update.message().text().equals("/chatid")) {
            long chatId = update.message().chat().id();
            SendMessage message = new SendMessage(chatId, "Ваш chat id:<code>" + chatId + "</code>").parseMode(ParseMode.HTML);
            config.setChatId(String.valueOf(chatId));
            config.save();
            bot.execute(message);
        }
    }


}
