package keystrokesmod.module.modules.render;

import keystrokesmod.ay;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleDesc;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Fullbright extends Module {
    private ModuleDesc description;
    private float defaultGamma, clientGamma;

    public Fullbright() {
        super("Fullbright", category.render, 0);

        this.registerSetting(description = new ModuleDesc("No more darkness!"));
        this.clientGamma = 10000;
    }

    @Override
    public void onEnable() {
        this.defaultGamma = mc.gameSettings.gammaSetting;
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onEnable();
        mc.gameSettings.gammaSetting = this.defaultGamma;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (!ay.isPlayerInGame()) {
            onDisable();
            return;
        }

        if (mc.gameSettings.gammaSetting != clientGamma)
            mc.gameSettings.gammaSetting = clientGamma;
    }
}
