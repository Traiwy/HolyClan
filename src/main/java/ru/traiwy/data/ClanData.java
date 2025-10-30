package ru.traiwy.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.traiwy.enums.TypeClan;


@Getter
@Setter
@AllArgsConstructor
public class ClanData {
    private int id;
    private String owner;
    private int level;
    private TypeClan typeClan;

}
