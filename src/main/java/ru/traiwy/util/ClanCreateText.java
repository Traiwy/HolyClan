package ru.traiwy.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClanCreateText {

    public static String onClanCreateText(){
        String text = """
                
                
                
                
                &6Придумайте название клана и введите его в чат.
                Название клана может содержать до 12 символов, включая символов цвета
                """;
        return text;
    }
}
