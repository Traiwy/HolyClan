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
    private String clanName;
    private long balance;
    private int point;
    private int level;
    private TypeClan typeClan;

    public ClanData(String owner, String clanName,long balance, int point ,int level, TypeClan typeClan){
        this.owner = owner;
        this.clanName = clanName;
        this.balance = balance;
        this.point = point;
        this.level = level;
        this.typeClan = typeClan;
    }

}
