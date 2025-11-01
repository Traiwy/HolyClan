package ru.traiwy.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.traiwy.enums.TypeClan;


@Getter
@Setter
public class ClanData {
    private int id;
    private String owner;
    private int level;
    private TypeClan typeClan;

    public ClanData(String owner, int level, TypeClan typeClan){
        this.owner = owner;
        this.level = level;
        this.typeClan = typeClan;
    }

}
