package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Config {
    private static Config instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String api;
    private static String secret;
    private static String chatId;
    private static String updateId;
    private static final Logger log = LogManager.getRootLogger();

    public Config() {
        api = "";
        secret = "";
        chatId = "";
        updateId = "";
    }

    public void save(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api", api);
        jsonObject.addProperty("secret", secret);
        jsonObject.addProperty("chat_id", chatId);
        jsonObject.addProperty("update_id", updateId);
        String config = gson.toJson(jsonObject);
        try {
            //debug
            //FileWriter fw = new FileWriter(Path.of(System.getProperty("user.dir")).getParent() +"/config.json");
            //prod
            FileWriter fw = new FileWriter(Path.of(System.getProperty("user.dir")) +"/config.json");

            fw.write(config);
            fw.close();
            log.info("Настройки успешно записанны в файл");
        } catch (IOException ex) {
            log.warn("Ошибка во время записи в файл: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
    public JsonObject load(){
        try {
            //debug
            //FileReader fr = new FileReader(Path.of(System.getProperty("user.dir")).getParent() +"/config.json");
            //prod
            FileReader fr = new FileReader(Path.of(System.getProperty("user.dir")) +"/config.json");

            Scanner scan = new Scanner(fr);

            StringBuilder string = new StringBuilder();
            while (scan.hasNextLine()) {
                string.append(scan.nextLine());
            }
            fr.close();

            JsonObject jsonObject = JsonParser.parseString(string.toString()).getAsJsonObject();
            setApi(jsonObject.get("api").getAsString());
            setSecret(jsonObject.get("secret").getAsString());
            setChatId(jsonObject.get("chat_id").getAsString());
            setUpdateId(jsonObject.get("update_id").getAsString());

            return jsonObject;
        } catch (IOException ex) {
            log.warn("Ошибка во время загрузки данных из файла: "+ex.getMessage());
            ex.printStackTrace();
        }
        return new JsonObject();
    }
    public static Config getInstance()
    {
        if (instance == null)
        {
            instance = new Config();
        }
        return instance;
    }


    public String getApi() {
        return api;
    }
    public void setApi(String api) {
        Config.api = api;
    }
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        Config.secret = secret;
    }
    public String getChatId() {
        return chatId;
    }
    public void setChatId(String chatId) {
        Config.chatId = chatId;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        Config.updateId = updateId;
    }
}
