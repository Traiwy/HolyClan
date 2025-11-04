package ru.traiwy.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;


@UtilityClass
public class ClanPromptText {

    public static String onClanCreateText(){
        String text = """
                
                
                
                
                &6Придумайте название клана и введите его в чат.
                Название клана может содержать до 12 символов, включая символов цвета
                """;
        return text;
    }

    public static String onDeleteClanText() {
        String text = """
                
                
                
              
                Вы действительно желаете удалить клан?\n Если хотите удалить, то напишите ПОДТВЕРДИТЬ, /n Если нет, то ОТМЕНИТЬ
                
                """;
        return text;
    }
}
