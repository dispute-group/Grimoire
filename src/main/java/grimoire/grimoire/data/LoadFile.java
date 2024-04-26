package grimoire.grimoire.data;

import grimoire.grimoire.gui.AbsenceAndMyselfShow;
import grimoire.grimoire.gui.widgets.newJobImageLoad;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static grimoire.grimoire.data.GrimoireData.*;

public class LoadFile {
    public static final String path = "config\\script";
    public static ArrayList<File> Script;
    public static String[] FirstNight = null;
    public static String[] EachNight = null;
    public static ArrayList<ArrayList<String>> AllName;
    public static HashMap<String, String> AllIntroduction;
    public static HashMap<String,File> AllJobPath;
    public static HashMap<String, newJobImageLoad> AllLoadImage;
    public static HashMap<Integer,Integer> FirstNightPlayerNum;
    public static HashMap<Integer,Integer> EachNightPlayerNum;

    public static ArrayList<File> getScript() {//获得所有剧本列表
        ArrayList<File> script = new ArrayList<File>();
        File file = new File(path);
        if(file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < Objects.requireNonNull(file.list()).length; i++) {
                    if (files != null && files[i].isDirectory()) {
                        script.add(files[i].getAbsoluteFile());
                    }
                }
            }
        }else{
            file.mkdir();
            new File("script","请在script文件下添加您的剧本文件").mkdir();
        }

        return script;
    }

    public static void getNightSheet(String script) {
        File scriptfile = new File(path,script);
        File NightSheetFile = new File(scriptfile,"行动次序.txt");
//        FirstNight = new ArrayList<>();
//        EachNight = new ArrayList<>();
        if(NightSheetFile.exists()){
            try{
                BufferedReader In = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(NightSheetFile)));
                        String tmp = null;
                        String[] StringTmp = null;
                        while ((tmp = In.readLine()) != null) {
                            if(tmp.contains("首夜行动次序:")){
                                tmp = tmp.replace("首夜行动次序:", "");
                                FirstNight = tmp.split("->");

                            }
                            if(tmp.contains("其他夜行动次序:")){
                                tmp = tmp.replace("其他夜行动次序:", "");
                                EachNight = tmp.split("->");
                            }
                        }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public static void getEveryNightPlayerNum() {
        FirstNightPlayerNum = new HashMap<>();
        EachNightPlayerNum = new HashMap<>();
        int FirstNightNum = 1;
        int EachNightNum = 1;
        boolean isExist =false;
        if(FirstNight != null){
        for(String s : FirstNight){
                for(int i = 0 ;i < PlayerJob.length;i++ ) {
                    if(s.equals(PlayerJob[i])){
                        FirstNightPlayerNum.put(i,FirstNightNum);
                        isExist = true;
                    }
                }
                if(isExist) {
                    FirstNightNum++;
                    isExist = false;
                }
            }
        }
        if(EachNight != null){
            for(String s : EachNight){
                for(int i = 0 ;i < PlayerJob.length;i++ ) {
                    if(s.equals(PlayerJob[i])){
                        EachNightPlayerNum.put(i,EachNightNum);
                        isExist = true;
                    }
                }
                if(isExist) {
                    EachNightNum++;
                    isExist = false;
                }
            }
        }


    }

    public static ArrayList<ArrayList<String>> getName(String script) {//获取文件夹下的每一个文件的列表
        if(script!=null) {
            File scriptfile = new File(path, script);
            File Introduction = new File(scriptfile, "介绍.txt");      //初始化Introduction.txt文件位置
            ArrayList<ArrayList<String>> Names = new ArrayList<>();
            ArrayList<String> Name = new ArrayList<>();
            if (Introduction.exists()) {
                try {
                    BufferedReader In = new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(Introduction)));
                    String tmp = null;
                    int tmpIndex = -1;
                    while ((tmp = In.readLine()) != null) {
                        if (tmp.contains("---[") && tmp.contains("]---")) {
                            tmpIndex++;
                            Names.add(Name);
                            Name = new ArrayList<>();
                        } else {
                            Name.add(tmp.split("->")[0]);
                        }
                    }
                    Names.add(Name);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return Names;
            } else {
                if (scriptfile.isDirectory()) {
                    Names = new ArrayList<>();
                    String[] filesName = new String[]{"状态", "镇民", "外来者", "爪牙", "恶魔", "旅行者"};
                    for (String f : filesName) {
                        File[] f1 = new File(scriptfile, f).listFiles();
                        if (f1 != null) {
                            for (File f2 : f1) {
                                Name.add(f2.getName().substring(0, f2.getName().lastIndexOf('.')));
                            }
                        }
                        Names.add(Name);
                        Name = new ArrayList<>();
                    }
                }

                return Names;
            }
        }else{return null;}
    }


    public static HashMap<String, String> getIntroduction(String script,ArrayList<ArrayList<String>> Names) {   //在给出的剧本文件夹内寻找Introduction文件如果文件存在则打开文件并分割文件内容然后返回一个hashmap数组 如果不存在则输出新建一个Introduction文件初始化并返回。
        if(script!=null) {
        File scriptfile = new File(path,script);
        File Introduction = new File(scriptfile, "介绍.txt");      //初始化Introduction.txt文件位置
        ArrayList<String> content = new ArrayList<>();                      //初始化输出内容中转数组\
        ArrayList<String> add = new ArrayList<>();
        String[] cutcontent;                                                //初始化分割内容数组
        HashMap<String, String> ret = new HashMap<>();                      //初始化返回hashmap数组
//        ArrayList<String> Class = getClassName(script);
        try {
            if (scriptfile.isDirectory()) {
                if (!Introduction.exists()) {//如果文件不存在则新建Introduction文件
                    Introduction.createNewFile();//新建文件
                    if (Names != null) {
                        for (int i = 0; i < Names.size(); i++) {
                            switch (i) {
                                case 0:
                                    add.add("---[状态]---");
                                    break;
                                case 1:
                                    add.add("---[镇民]---");
                                    break;
                                case 2:
                                    add.add("---[外来者]---");
                                    break;
                                case 3:
                                    add.add("---[爪牙]---");
                                    break;
                                case 4:
                                    add.add("---[恶魔]---");
                                    break;
                                case 5:
                                    add.add("---[旅行者]---");
                                    break;
                            }
                            for (String file : Names.get(i)) {
                                add.add(file + "->请在此处输入" + file + "的技能描述");//提取没有后缀名的文件名称并添加在add数组
                            }

                        }
                    }

                    PrintWriter out = new PrintWriter(//
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            new FileOutputStream(Introduction))));

                    for (String line : add) {
                        out.println(line);
                    }
                    out.close();
                }
            }


            BufferedReader In = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(Introduction)));
            String tmp = null;
            while ((tmp = In.readLine()) != null) {
                if (tmp.contains("->")) {
                    content.add(tmp);

                }
            }
            for (String s : content) {
                cutcontent = s.split("->");
                ret.put(cutcontent[0], cutcontent[1]);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ret;
        }else {return null;}
    }//获得所有职业以及技能介绍的hashmap

    public static HashMap<String,File> getAllJobPath(String script){
        if(script!=null) {
        File scriptfile = new File(path,script);
        HashMap<String,File> ret = new HashMap<>();
        if(scriptfile.isDirectory()) {
            File[] files1 = scriptfile.listFiles();
            for(File f1 : files1){
                if(f1.isDirectory()){
                    File[] files2 =  f1.listFiles();
                    for(File f2 : files2){
                        ret.put(f2.getName().substring(0, f2.getName().lastIndexOf('.')),f2.getAbsoluteFile());
                    }
                }
            }
        }
        return ret;
        }else {return null;}
    }//获得所有职业名称对应图标的hashmap

    private static HashMap<String, newJobImageLoad> getLoadImage(){
        if(AllName!=null) {
            HashMap<String, newJobImageLoad> ret = new HashMap<>();
            for (ArrayList<String> jobNames : AllName) {
                if (jobNames != null) {
                    for (String jobName : jobNames) {
                        if (AllJobPath.get(jobName) != null) {
                            newJobImageLoad retJobImage = new newJobImageLoad(AllJobPath.get(jobName));
                            ret.put(jobName, retJobImage);
                        }
                    }
                }
            }

            return ret;
        }else {return null;}
    }

    public static void load(File file) {
        if(!file.exists()){
            GrimoireData.create();
            save(file);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            GrimoireData.PlayerNum = ois.read();
            GrimoireData.MyselfSeatNum = ois.read();
            GrimoireData.Current_Script = (String)ois.readObject();
            GrimoireData.isDisplayAbsentIdentity = ois.readBoolean();
            GrimoireData.isDisplayMySelfSeat = ois.readBoolean();

            for(int i = 0 ;i<GrimoireData.PlayerNum;i++){
                PlayerDeath[i]  = ois.readBoolean();
                PlayerJob[i] = (String)ois.readObject();
                PlayerNickName[i] = (String)ois.readObject();
            }
            for(int i = 20;i<23;i++){
                PlayerJob[i] = (String)ois.readObject();
            }
            GrimoireData.PlayerDescription = (ArrayList<ArrayList<String>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Script = getScript();
        AllName = getName(GrimoireData.Current_Script);
        AllIntroduction = getIntroduction(GrimoireData.Current_Script,AllName);
        AllJobPath = getAllJobPath(GrimoireData.Current_Script);
        AllLoadImage = getLoadImage();
        getNightSheet(GrimoireData.Current_Script);
    }


    public static void save(File file) {
        if(!new File(path).exists()){
            new File(path).mkdir();
        }
        if (file.exists()) {
            file.delete();
        }

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.write(PlayerNum);
            oos.write(MyselfSeatNum);
            oos.writeObject(Current_Script);
            oos.writeBoolean(isDisplayAbsentIdentity);
            oos.writeBoolean(isDisplayMySelfSeat);

            for(int i = 0 ;i<PlayerNum;i++){
                oos.writeBoolean(PlayerDeath[i]);
                oos.writeObject(PlayerJob[i]);
                oos.writeObject(PlayerNickName[i]);
            }
            for(int i = 20;i<23;i++){

                oos.writeObject(PlayerJob[i]);
            }
            oos.writeObject(PlayerDescription);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AbsenceAndMyselfShow.Allimage = new CompletableFuture[4];
        AbsenceAndMyselfShow.texture = new DynamicTexture[4];
    }


}
