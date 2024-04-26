package grimoire.grimoire.data;

import grimoire.grimoire.Grimoire;

import java.util.ArrayList;

import static grimoire.grimoire.data.LoadFile.save;

public class GrimoireData {
    private static final int MAX_PlayerNum = 20;
    public static int PlayerNum = 12;
    public static int MyselfSeatNum=0;
    public static boolean[] PlayerDeath = new boolean[MAX_PlayerNum];
    public static String[] PlayerNickName = new String[MAX_PlayerNum];
    public static String[] PlayerJob = new String[MAX_PlayerNum+3];
    public static  ArrayList<ArrayList<String>> PlayerDescription =new ArrayList<>();
    public static int animationTimer = 20;
    public static String Current_Script = null;
    public static boolean isDisplayAbsentIdentity =true;
    public static boolean isDisplayMySelfSeat =true;

    public static void PlayerNumChange(int num){//参与游戏玩家人数修改
        if(num == 1 || num == -1){
        if(0<=PlayerNum && PlayerNum<=MAX_PlayerNum){
            PlayerNum += num;
        }
        if(PlayerNum>MAX_PlayerNum){PlayerNum = MAX_PlayerNum;}
        if(PlayerNum<0){PlayerNum = 0;}
        save(Grimoire.GrimoireFile);
        }else{
            PlayerNum = Math.max(num, 0);
            PlayerNum = Math.min(PlayerNum, MAX_PlayerNum);
        }

        if(MyselfSeatNum+1>PlayerNum){MyselfSeatNum = PlayerNum-1;}
    }

    public static void MyselfSeatNumChange(int num){//自身座位修改
        if(0<=MyselfSeatNum && MyselfSeatNum<=(PlayerNum-1)){
            MyselfSeatNum += num;
        }
        if(MyselfSeatNum+1>PlayerNum){MyselfSeatNum = PlayerNum-1;}
        if(MyselfSeatNum<0){MyselfSeatNum = 0;}
        save(Grimoire.GrimoireFile);
    }

    public static void PlayerNickNameChange(int num,String NickName){//制定玩家昵称修改
        PlayerNickName[num] = NickName;
    }

    public static void PlayerJobChange(int PlayerNum,String Job){//指定玩家职业修改
        PlayerJob[PlayerNum] = Job;
        save(Grimoire.GrimoireFile);
    }
    public static void PlayerJobChange(int PlayerNum){//指定玩家职业清除
        PlayerJob[PlayerNum] = null;
        save(Grimoire.GrimoireFile);
    }
    public static void PlayerDescriptionAdd(int PlayerNum,String Description){//指定玩家描述增加
        PlayerDescription.get(PlayerNum).add(Description);
        save(Grimoire.GrimoireFile);
    }
    public static void PlayerDescriptionDelete(int PlayerNum,int Num){//删除指定玩家指定描述
        PlayerDescription.get(PlayerNum).remove(Num);
        save(Grimoire.GrimoireFile);
    }
    public static void PlayerDeathChange(int PlayerNum){//改变指定玩家死亡状态
        PlayerDeath[PlayerNum] =! PlayerDeath[PlayerNum];
        save(Grimoire.GrimoireFile);
    }
    public static void PlayerDeathChange(int PlayerNum,boolean Death){//改变指定玩家死亡状态
        PlayerDeath[PlayerNum] =Death;
        save(Grimoire.GrimoireFile);
    }

    public static void tickTimer() {
        if (animationTimer > 0) {
            animationTimer -= 5;
        }
    }
    public static void setScript(String scp) {
        Current_Script = scp;
        save(Grimoire.GrimoireFile);
    }

    public static void create(){
        Current_Script = null;
        PlayerNum = 12;
        MyselfSeatNum = 0;
        isDisplayAbsentIdentity =true;
        isDisplayMySelfSeat =true;
        for(int i = 0; i < MAX_PlayerNum; i++) {
            PlayerDeath[i] = false;
            PlayerJob[i] = null;
            PlayerNickName[i] = null;
            PlayerDescription.add(new ArrayList<String>());
        }
        for(int i = 20; i < 23; i++) {
            PlayerJob[i] = null;
        }
    }
    public static void clear() {
        PlayerNum = 12;
        for(int i = 0; i < MAX_PlayerNum; i++) {
            PlayerDeath[i] = false;
            PlayerJob[i] = null;
            if(PlayerDescription.get(i)!=null){
            PlayerDescription.get(i).clear();
            }else{
                PlayerDescription.set(i,new ArrayList<>());
            }
        }
        for(int i = 20; i < 23; i++) {
            PlayerJob[i] = null;
        }
        save(Grimoire.GrimoireFile);
    }
}
