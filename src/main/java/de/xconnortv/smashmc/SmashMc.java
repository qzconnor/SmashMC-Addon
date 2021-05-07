package de.xconnortv.smashmc;

import de.xconnortv.smashmc.elements.ButtonElement;
import de.xconnortv.smashmc.module.TimerModule;
import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SmashMc extends LabyModAddon {
    private boolean enabled;
    private ScheduledExecutorService thread;
    private int time;
    private TimerModule module;
    private static boolean round;
    private String sound;
    private int volume;
    private int pitch;
    @Override
    public void onEnable() {
        module = new TimerModule();
        api.registerModule(module);
        api.getEventManager().register((unformat, format) -> {
            if(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1) != null){
                if(!StringUtils.stripControlCodes(Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName()).contains("Smash | Leben")){
                    if(enabled && (format.equals("● Smash ➥ Das Spiel beginnt jetzt!"))){
                        time = 30;
                        module.setTime(time);
                        round = true;
                        repeat();
                    }else if(enabled && (format.contains("hat das Spiel gewonnen!"))){
                        reset();
                    }
                }
            }

            return false;
        });
        api.getEventManager().register((MessageSendEvent) s -> {
            if (s.equals("/hub") || s.equals("/l") || s.equals("/lobby")){
                reset();
            }
            return false;
        });
        api.getEventManager().registerOnQuit(serverData -> reset());
    }

    public void repeat(){
        thread = Executors.newSingleThreadScheduledExecutor();
        thread.scheduleAtFixedRate(() -> {
            if(time >= 2){
                time--;
            }else{

                Minecraft.getMinecraft().thePlayer.playSound(sound, volume, pitch);

                time = 30;
            }
            module.setTime(time);

        }, 2, 1, TimeUnit.SECONDS);
    }


    public void reset(){
        round = false;
        if(thread != null){
            thread.shutdown();
        }
        time = 30;
        module.setTime(time);
    }


    @Override
    public void loadConfig() {
        this.enabled = getConfig().has("enabled") ? getConfig().get("enabled").getAsBoolean() : false;
        this.sound = getConfig().has("sound") ? getConfig().get("sound").getAsString() : "note.harp";
        this.volume = getConfig().has("volume") ? getConfig().get("volume").getAsInt() : 1;
        this.pitch = getConfig().has("pitch") ? getConfig().get("pitch").getAsInt() : 1;
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement("§cCreator: xConnorTV#8811"));
        list.add(new HeaderElement("Settings"));
        list.add(new BooleanElement("Enabled", new ControlElement.IconData(Material.MAGMA_CREAM), aBoolean -> setEnabled(aBoolean), enabled));
        list.add(new HeaderElement("Sound Settings"));
        list.add(new StringElement("Sound", new ControlElement.IconData(Material.NOTE_BLOCK), sound, s -> {
            sound = s;
            getConfig().addProperty("sound", s);
            saveConfig();
        }));
        list.add(new HeaderElement("§cPlease Choose only Valid Sounds!"));
        list.add(new SliderElement("Volume", new ControlElement.IconData(Material.SNOW_BALL), volume).setRange(0,5).addCallback(i ->{
            volume = i;
            getConfig().addProperty("volume", volume);
            saveConfig();
        }));
        list.add(new SliderElement("Pitch", new ControlElement.IconData(Material.SNOW_BALL), pitch).setRange(0,5).addCallback(i ->{
            pitch = i;
            getConfig().addProperty("pitch", pitch);
            saveConfig();
        }));
        list.add(new ButtonElement("Sounds", () -> LabyMod.getInstance().openWebpage("https://minecraft.fandom.com/wiki/Sounds.json/Bedrock_Edition_values", true)));
    }

    public static boolean isRound() {
        return round;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        getConfig().addProperty("enabled", enabled);
        saveConfig();
    }
}
