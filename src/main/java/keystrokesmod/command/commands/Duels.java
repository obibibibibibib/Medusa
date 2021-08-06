package keystrokesmod.command.commands;

import keystrokesmod.CommandLine;
import keystrokesmod.ProfileUtils;
import keystrokesmod.URLUtils;
import keystrokesmod.ay;
import keystrokesmod.command.Command;
import keystrokesmod.main.Ravenb3;
import keystrokesmod.module.modules.minigames.DuelsStats;

public class Duels extends Command {
    public Duels()  {
        super("duels", "Fetches a player's stats", 1, 1,  new String[] {"Player name"},  new String[] {"d", "duel", "stat", "stats", "check"});
    }

    @Override
    public void onCall(String[] args) {
        if (URLUtils.k.isEmpty()) {
            CommandLine.print("&cAPI Key is empty!", 1);
            CommandLine.print("Use \"setkey [api_key]\".", 0);
            return;
        }
        String n;
        n = args[1];
        CommandLine.print("Retrieving data...", 1);
        Ravenb3.getExecutor().execute(() -> {
            int[] s = ProfileUtils.getHypixelStats(n, ProfileUtils.DM.OVERALL);
            if (s != null) {
                if (s[0] == -1) {
                    CommandLine.print("&c" + (n.length() > 16 ? n.substring(0, 16) + "..." : n) + " does not exist!", 0);
                } else {
                    double wlr = s[1] != 0 ? ay.round((double)s[0] / (double)s[1], 2) : (double)s[0];
                    CommandLine.print("&e" + n + " stats:", 1);
                    CommandLine.print("Wins: " + s[0], 0);
                    CommandLine.print("Losses: " + s[1], 0);
                    CommandLine.print("WLR: " + wlr, 0);
                    CommandLine.print("Winstreak: " + s[2], 0);
                    CommandLine.print("Threat: " + DuelsStats.gtl(s[0], s[1], wlr, s[2]).substring(2), 0);
                }
            } else {
                CommandLine.print("&cThere was an error.", 0);
            }

        });
    }
}
