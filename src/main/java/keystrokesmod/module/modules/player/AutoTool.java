package keystrokesmod.module.modules.player;

import keystrokesmod.ay;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleSettingSlider;
import keystrokesmod.module.ModuleSettingTick;
import keystrokesmod.module.modules.combat.AutoClicker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class AutoTool extends Module {
    private ModuleSettingTick hotkeyBack;
    private ModuleSettingTick doDelay;
    private ModuleSettingSlider minDelay;
    private ModuleSettingSlider maxDelay;
    public static boolean isBusy;
    private double startWaitTime;
    private boolean isWaiting;
    public static int previousSlot;
    public static boolean justFinishedMining, mining;
    //public static List<Block> pickaxe = Arrays.asList(ItemBlock.class, BlockIce.class);

    public AutoTool() {
        super("Auto Tool", category.player, 0);

        this.registerSetting(hotkeyBack = new ModuleSettingTick("Hotkey back", true));
        this.registerSetting(doDelay = new ModuleSettingTick("Random delay", true));
        this.registerSetting(minDelay = new ModuleSettingSlider("Min delay", 100, 0, 3000, 5));
        this.registerSetting(maxDelay = new ModuleSettingSlider("Max delay", 390, 0, 3000, 5));
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (!ay.isPlayerInGame() || mc.currentScreen != null)
            return;

        //System.out.println(mc.currentScreen);

        if(Mouse.isButtonDown(0)) {
            if(AutoClicker.autoClickerEnabled) {
                if(!AutoClicker.breakBlocks.isToggled()) {
                    return;
                }
            }

            BlockPos lookingAtBlock = mc.objectMouseOver.getBlockPos();
            if (lookingAtBlock != null) {
                Block stateBlock = mc.theWorld.getBlockState(lookingAtBlock).getBlock();
                if (stateBlock != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block) {
                    if(!mining) {
                        previousSlot = ay.getCurrentPlayerSlot();
                        mining = true;
                    }
                    int index = -1;
                    double speed = 1;


                    for (int slot = 0; slot <= 8; slot++) {
                        ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
                        if(itemInSlot != null && itemInSlot.getItem() instanceof ItemTool) {
                            BlockPos p = mc.objectMouseOver.getBlockPos();
                            Block bl = mc.theWorld.getBlockState(p).getBlock();

                            if(itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                                speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                                index = slot;
                            }
                        }
                        else if(itemInSlot != null && itemInSlot.getItem() instanceof ItemShears) {
                            BlockPos p = mc.objectMouseOver.getBlockPos();
                            Block bl = mc.theWorld.getBlockState(p).getBlock();

                            if(itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed) {
                                speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                                index = slot;
                            }
                        }
                    }

                    if(index == -1 || speed <= 1.1 || speed == 0) {
                        return;
                    } else {
                        ay.hotkeyToSlot(index);
                    }






                }
                else{
                    return;
                }
            }
            else {
                return;
            }


        }
        else {
            if(mining)
            finishMining();
        }
/*
        BlockPos p = mc.objectMouseOver.getBlockPos();
        if (p != null) {
            Block bl = mc.theWorld.getBlockState(p).getBlock();
            if (bl != Blocks.air && !(bl instanceof BlockLiquid)) {}
        }*/
        //hotkeyToPickAxe();


    }

    public static void hotkeyToPickAxe() {
        for (int slot = 0; slot <= 8; slot++) {
            ItemStack itemInSlot = mc.thePlayer.inventory.getStackInSlot(slot);
            if(itemInSlot != null && itemInSlot.getItem() instanceof ItemPickaxe) {
                BlockPos p = mc.objectMouseOver.getBlockPos();
                Block bl = mc.theWorld.getBlockState(p).getBlock();
                System.out.println(itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()));
            }
        }
    }

    public void finishMining(){
        ay.hotkeyToSlot(previousSlot);
        justFinishedMining = false;
        mining = false;
    }
}
