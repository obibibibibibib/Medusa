package keystrokesmod.config;

import keystrokesmod.main.NotAName;
import keystrokesmod.main.Ravenb3;
import keystrokesmod.module.*;
import keystrokesmod.module.modules.HUD;
import keystrokesmod.module.modules.client.CommandLine;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConfigManager {
    private File configDirecotry;
    private File currentConfig;
    private String fileName;
    private String extension;
    public boolean loading;

    public ConfigManager() {
        this.loading = false;
        configDirecotry = new File(Minecraft.getMinecraft().mcDataDir, "keystrokes" + File.separator + "configs");
        if (!configDirecotry.exists()) {
            configDirecotry.mkdir();
        }
        this.fileName = "default";
        this.extension = "bplus";
        currentConfig = new File(configDirecotry, fileName + "." + extension);
        if (!currentConfig.exists()) {
            try {
                currentConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        //System.out.println("i save to " + this.currentConfig.getName());
        ArrayList<String> finalString = new ArrayList<String>();

        for(Module clientModule : NotAName.moduleManager.listofmods()){
            StringBuilder moduleAttributes = new StringBuilder();
            moduleAttributes.append("module:");
            moduleAttributes.append(clientModule.getName() + ":");
            moduleAttributes.append(clientModule.isEnabled() + ":");
            moduleAttributes.append(clientModule.getKeycode());

            finalString.add(moduleAttributes.toString());

            for (ModuleSettingsList moduleSetting : clientModule.getSettings()) {
                StringBuilder settingString = new StringBuilder();
                String base = "setting:" + clientModule.getName() + ":" + moduleSetting.getName();
                settingString.append(base);
                if (moduleSetting.mode.equalsIgnoreCase("slider")) {
                    ModuleSettingSlider setting = (ModuleSettingSlider) moduleSetting;

                    settingString.append(":" + moduleSetting.mode);
                    settingString.append(":" + setting.getInput());
                }
                else if (moduleSetting.mode.equalsIgnoreCase("tick")) {
                    ModuleSettingTick setting = (ModuleSettingTick) moduleSetting;

                    settingString.append(":" + moduleSetting.mode);
                    settingString.append(":" + setting.isToggled());
                } else if (moduleSetting.mode.equalsIgnoreCase("desc")) {
                    ModuleDesc setting = (ModuleDesc) moduleSetting;

                    settingString.append(":" + moduleSetting.mode);
                    settingString.append(":" + setting.getDesc());
                }

                if (settingString.length() > base.length())
                    finalString.add(settingString.toString());
            }
        }


        PrintWriter writer = null;
        try {
            writer = new PrintWriter(this.currentConfig);
            for (String line : finalString) {
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void load() throws FileNotFoundException {
        //System.out.println("iLOAD from " + this.currentConfig.getName());
        Scanner reader = new Scanner(this.currentConfig);
        while (reader.hasNextLine()) {
            String current = reader.nextLine();
            if(current.startsWith("module:")){

                String[] currentModule = current.split(":");
                Module module = NotAName.moduleManager.getModuleByName(currentModule[1]);

                if (module == null)
                    continue;

                try{
                    boolean toggled = Boolean.parseBoolean(currentModule[2]);
                    int keyBind = Integer.parseInt(currentModule[3]);
                    if (module.getName().equalsIgnoreCase("hud") && toggled){
                        Module hud = (HUD) module;
                        hud.enable();
                    }

                    if (module.getName().equalsIgnoreCase("Command line") && toggled){
                        Module cmd = (CommandLine) module;
                        cmd.disable();
                        cmd.onDisable();
                        continue;
                    }
                    module.setbind(keyBind);
                    if (toggled) {
                        module.enable();
                        module.onEnable();
                    } else {
                        module.disable();
                        module.onDisable();
                    }
                } catch (Exception hnfsaofsh) {
                    hnfsaofsh.printStackTrace();
                }

            }

            else if (current.startsWith("setting:")){

                String[] currentSetting = current.split(":");
                Module module = NotAName.moduleManager.getModuleByName(currentSetting[1]);

                if (module == null)
                    continue;

                ModuleSettingsList settingList = module.getSettingByName(currentSetting[2]);

                if (settingList == null)
                    continue;

                if (currentSetting[3].equalsIgnoreCase("tick")) {
                    ModuleSettingTick setting = (ModuleSettingTick) settingList;

                    boolean toggled = Boolean.parseBoolean(currentSetting[4]);
                    setting.setEnabled(toggled);
                } else if (currentSetting[3].equalsIgnoreCase("slider")) {
                    ModuleSettingSlider setting = (ModuleSettingSlider) settingList;

                    double value = Double.parseDouble(currentSetting[4]);
                    setting.setValue(value);
                } else if (currentSetting[3].equalsIgnoreCase("desc")) {
                    ModuleDesc setting = (ModuleDesc) settingList;

                    setting.setDesc(currentSetting[4]);
                }
            }
        }
    }

    public String getExtension() {
        return "." + extension;
    }

    public String getCurrentConfig() {
        return fileName;
    }

    public File getConfigDirecotry() {
        return configDirecotry;
    }

    public void loadConfig(String fileName) {
        this.loading = true;
        //System.out.println("no saving");
        this.fileName = fileName;
        this.currentConfig = new File(this.configDirecotry, fileName + "." + this.extension);
        try {
            this.load();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.loading = false;
        //System.out.println("yes saving");
    }

    public void saveConfig(String fileName) {
        String prevFileName = "";
        for (int bruh = 0; bruh <= this.fileName.length()-1; bruh++){
            prevFileName+=this.fileName.charAt(bruh);
        }
        this.fileName = fileName;
        this.currentConfig = new File(this.configDirecotry, fileName + "." + this.extension);
        if(!this.currentConfig.exists()) {
            try {
                this.currentConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.save();

        this.loadConfig(prevFileName);
    }

    public boolean removeConfig(String fileName) {
        boolean found = false;
        for (File config : this.listConfigs()) {
            if (config.getName().replace(this.getExtension(), "").equalsIgnoreCase(fileName)){
                found = true;
                File toDelete = new File(this.configDirecotry, fileName + this.getExtension());
                if(toDelete.delete()) {
                    this.loadConfig("default");
                    return true;
                }
                else
                    return false;
            }
        }
        return false;
    }

    public ArrayList<File> listConfigs() {
        ArrayList<File> proBlockGameCheater = new ArrayList<File>();
        for (File config : this.configDirecotry.listFiles()) {
            if (config.getName().endsWith(this.extension)) {
                proBlockGameCheater.add(config);
            }
        }

        return proBlockGameCheater;
    }
}
