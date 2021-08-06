package keystrokesmod.command.commands;

import keystrokesmod.CommandLine;
import keystrokesmod.URLUtils;
import keystrokesmod.command.Command;
import keystrokesmod.main.BlowsyConfigManager;
import keystrokesmod.main.Ravenb3;

public class SetKey extends Command {
    public SetKey() {
        super("setkey", "Sets hypixel's API key<br>To get a new key, run<br>`/api new`", 1, 1, new String[] {"apikey"}, new String[] {"apikey"});
    }

    @Override
    public void onCall(String[] args) {
        if(args == null) {
            this.incorrectArgs();
            return;
        }

        if (args.length - 1 > this.getMaxArgs() || args.length - 1 < this.getMinArgs()){
            this.incorrectArgs();
            return;
        }

        CommandLine.print("§3Setting...", 1);
        String n;
        n = args[1];
        Ravenb3.getExecutor().execute(() -> {
            if (URLUtils.isHypixelKeyValid(n)) {
                URLUtils.k = n;
                CommandLine.print("&a" + "success!", 0);
                BlowsyConfigManager.saveCheatSettingsToConfigFile();
            } else {
                CommandLine.print("&c" + "Invalid key.", 0);
            }

        });
    }
}
