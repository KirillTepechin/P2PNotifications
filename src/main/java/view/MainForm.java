package view;

import binance.BinanceParser;
import com.google.gson.JsonObject;
import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.math.BigDecimal;

public class MainForm{
    private JPasswordField passwordFieldApi;
    private JPasswordField passwordFieldSecret;
    private JTextField textFieldDiff;
    private JPanel panel;
    private JPanel buttonPanel;
    private JToggleButton toggleButton;
    private JTextArea textAreaLog;
    private JTextField textFieldChatId;
    private JButton buttonSaveConfig;
    private JCheckBox checkBoxSafe;
    private JComboBox comboBoxFreq;
    private JScrollPane scroll;
    private final BinanceParser binanceParser = new BinanceParser(); // Скрытый файл
    private Thread thread = new Thread(binanceParser);
    private final Config config = Config.getInstance();
    private static final Logger log = LogManager.getRootLogger();

    public MainForm(){
        init();
        toggleButton.addItemListener(e -> {
            if (toggleButton.isSelected()){
                if(textFieldDiff.getText().equals("") || textFieldChatId.getText().equals("")
                        || String.valueOf(passwordFieldApi.getPassword()).equals("") ){
                    JOptionPane.showMessageDialog(panel, "Заполните необходимые поля","", JOptionPane.ERROR_MESSAGE);
                    toggleButton.setSelected(false);
                    return;
                }

                toggleButton.setText("OFF");
                try {
                    binanceParser.setFreq(comboBoxFreq.getSelectedItem());
                    binanceParser.setDiff(new BigDecimal(textFieldDiff.getText()));
                    binanceParser.setChatId(Long.parseLong(textFieldChatId.getText()));
                    binanceParser.setApiKey(String.valueOf(passwordFieldApi.getPassword()));
                    binanceParser.setSecretKey(String.valueOf(passwordFieldSecret.getPassword()));
                }
                catch (IllegalArgumentException ex){
                    log.warn("Ошибка формата введенных значений: "+ex.getMessage());
                    JOptionPane.showMessageDialog(panel, "Неправильный формат введенных значений","", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(checkBoxSafe.isSelected()){
                    binanceParser.setSafe(true);
                    log.info("Запуск в безопасном режиме");
                }

                thread = new Thread(binanceParser);
                thread.start();
            }
            else{
                toggleButton.setText("ON");
                thread.interrupt();
                log.info("Бинанс бот принудительно остановлен");
            }
        });

        buttonSaveConfig.addActionListener(e -> {
            config.setApi(String.copyValueOf(passwordFieldApi.getPassword()));
            config.setSecret(String.copyValueOf(passwordFieldSecret.getPassword()));
            config.setChatId(textFieldChatId.getText());

            if(config.getApi().length()==0 || config.getSecret().length()==0 ||
                    config.getChatId().length()==0)
            {
                int result = JOptionPane.showConfirmDialog(
                        panel,
                        "Некоторые поля пустые, все равно сохранить?",
                        "Предупреждение",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.YES_OPTION)
                {
                    config.save();
                }
            }
            else{
                config.save();
            }
            log.info("Настройки сохранены");
        });
    }


    public JPanel getPanel(){
        return panel;
    }
    private void init(){
        DefaultCaret caret = (DefaultCaret)textAreaLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        binanceParser.setAreaLogger(textAreaLog);
        toggleButton.setText("ON");

        BigDecimal freq = new BigDecimal("0");
        for (int i = 0; i < 11; i++) {
            comboBoxFreq.addItem(freq);
            freq = freq.add(new BigDecimal("0.1"));
        }
        comboBoxFreq.addItem(new BigDecimal("1.5"));
        comboBoxFreq.addItem(new BigDecimal("2"));
        loadUserData();
    }

    private void loadUserData() {
        JsonObject loadJson = config.load();
        try {
            passwordFieldApi.setText(loadJson.get("api").getAsString());
            passwordFieldSecret.setText(loadJson.get("secret").getAsString());
            textFieldChatId.setText(loadJson.get("chat_id").getAsString());

            log.info("Настройки загружены в форму");
        }
        catch (Exception ex){
            log.warn("Ошибка при попытке загрузки настроек на форму: "+ ex.getMessage());
        }

    }
}
