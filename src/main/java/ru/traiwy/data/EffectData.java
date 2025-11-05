package ru.traiwy.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.traiwy.enums.EffectType;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class EffectData {
    private int clanId;
    private EffectType effectType;
    private int active;
    private int level;
    private LocalDateTime startDateTime;
    private LocalDateTime endDataTime;


}
