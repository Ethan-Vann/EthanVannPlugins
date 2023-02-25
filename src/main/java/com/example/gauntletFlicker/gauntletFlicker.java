package com.example.gauntletFlicker;

import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.WidgetPackets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.HeadIcon;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Set;

import static net.runelite.api.Varbits.QUICK_PRAYER;

@PluginDescriptor(
        name = "Gauntlet Flicker and Switcher",
        description = "",
        tags = {"ethan"}
)
@PluginDependency(PacketUtilsPlugin.class)
@Slf4j
public class gauntletFlicker extends Plugin {
    @Inject
    Client client;
    @Inject
    ItemManager manager;
    @Inject
    MousePackets mousePackets;
    @Inject
    WidgetPackets widgetPackets;
    String updatedWeapon = "";
    @Inject
    NPCPackets npcPackets;
    boolean forceTab = false;
    Set<Integer> HUNLLEF_IDS = Set.of(NpcID.CORRUPTED_HUNLLEF,NpcID.CORRUPTED_HUNLLEF_9036,
            NpcID.CORRUPTED_HUNLLEF_9037,NpcID.CORRUPTED_HUNLLEF_9038,NpcID.CRYSTALLINE_HUNLLEF,
            NpcID.CRYSTALLINE_HUNLLEF_9022,NpcID.CRYSTALLINE_HUNLLEF_9023,NpcID.CRYSTALLINE_HUNLLEF_9024);
    boolean isRange = true;
    private int quickPrayerWidgetID = WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getPackedId();
    private void togglePrayer()
    {
        mousePackets.queueClickPacket(0, 0);
        widgetPackets.queueWidgetActionPacket(1,quickPrayerWidgetID, -1,-1);
    }
    @Subscribe
    public void onGameTick(GameTick e){
        if(client.getLocalPlayer().isDead()||client.getLocalPlayer().getHealthRatio()==0){
            forceTab =false;
            return;
        }
        if(client.getGameState()!= GameState.LOGGED_IN){
            forceTab =false;
            return;
        }
        Item weapon = null;
        try
        {
            weapon = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
        }catch (NullPointerException ex){
            //todo
        }
        String name = "";
        if(weapon!=null)
        {
            ItemComposition itemComp =
                    manager.getItemComposition(client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.WEAPON.getSlotIdx()).getId());
          name = itemComp.getName()==null?"":itemComp.getName();
        }
        NPC hunllef = client.getNpcs().stream().filter(x->HUNLLEF_IDS.contains(x.getId())).findFirst().orElse(null);
        if(client.getVarbitValue(9177) != 1){
            forceTab =false;
            return ;
        }
        if(client.getVarbitValue(9178) != 1){
            isRange = true;
            forceTab =false;
            updatedWeapon = "";
            return;
        }
        if(forceTab){
            System.out.println("forcing tab");
            client.runScript(915, 3);
            forceTab = false;
        }
        if(client.getWidget(5046276)==null){
            mousePackets.queueClickPacket();
            widgetPackets.queueWidgetAction(client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB),"Setup");
            forceTab = true;
        }
        if(isRange&&!isQuickPrayerActive(QuickPrayer.PROTECT_FROM_MISSILES)){
            mousePackets.queueClickPacket();
            widgetPackets.queueWidgetActionPacket(1,5046276, -1, 13); //quickPrayer range
        }else if(!isRange&&!isQuickPrayerActive(QuickPrayer.PROTECT_FROM_MAGIC)){
            mousePackets.queueClickPacket();
            widgetPackets.queueWidgetActionPacket(1,5046276, -1, 12); //quickPrayer magic
        }
        if(hunllef!=null){
            if(getHeadIcon(hunllef)== HeadIcon.MAGIC &&(!name.contains("bow")&&!name.contains("halberd"))){
                Widget bow = getItem("*bow*");
                Widget halberd = getItem("*halberd*");
                if(bow!=null) {
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetAction(bow,"Wield");
                    updatedWeapon = "bow";
                }else if(halberd!=null){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetAction(halberd,"Wield");
                    updatedWeapon = "halberd";
                }
            }
            if(getHeadIcon(hunllef)== HeadIcon.RANGED &&(!name.contains("staff")&&!name.contains("halberd"))){
                Widget staff = getItem("*staff*");
                Widget halberd = getItem("*halberd*");
                if(staff!=null) {
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetAction(staff,"Wield");
                    updatedWeapon = "staff";
                }else if(halberd!=null){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetAction(halberd,"Wield");
                    updatedWeapon = "halberd";
                }
            }
            if(getHeadIcon(hunllef)== HeadIcon.MELEE && (!name.contains("staff")&&!name.contains("bow"))){
                Widget staff = getItem("*staff*");
                Widget bow = getItem("*bow*");
                if(staff!=null) {
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetAction(staff,"Wield");
                    updatedWeapon = "staff";
                }else if(bow!=null){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetAction(bow,"Wield");
                    updatedWeapon = "bow";
                }
            }
            String weaponTesting = updatedWeapon.isEmpty()?name:updatedWeapon;
            if(weaponTesting.contains("bow")){
                if(rigourUnlocked()&&!isQuickPrayerActive(QuickPrayer.RIGOUR)){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetActionPacket(1,5046276, -1, 24); //quickPrayer rigour
                }else if(!rigourUnlocked()&&!isQuickPrayerActive(QuickPrayer.EAGLE_EYE)){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetActionPacket(1,5046276, -1, 22); //quickPrayer eagle eye
                }
            }else if(weaponTesting.contains("staff")){
                if(auguryUnlucked()&&!isQuickPrayerActive(QuickPrayer.AUGURY)){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetActionPacket(1,5046276, -1, 27); //quickPrayer augury
                }else if(!auguryUnlucked()&&!isQuickPrayerActive(QuickPrayer.MYSTIC_MIGHT)){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetActionPacket(1,5046276, -1, 23); //quickPrayer mystic might
                }
            }else if(weaponTesting.contains("halberd")){
                if(pietyUnlocked()&&!isQuickPrayerActive(QuickPrayer.PIETY)){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetActionPacket(1,5046276, -1, 26); //quickPrayer augury
                }else if(!pietyUnlocked()&&!isQuickPrayerActive(QuickPrayer.ULTIMATE_STRENGTH)){
                    mousePackets.queueClickPacket();
                    widgetPackets.queueWidgetActionPacket(1,5046276, -1, 10); //quickPrayer mystic might
                    if(!isQuickPrayerActive(QuickPrayer.INCREDIBLE_REFLEXES)){
                        mousePackets.queueClickPacket();
                        widgetPackets.queueWidgetActionPacket(1,5046276, -1, 11); //quickPrayer augury
                    }
                }
            }
        }
        if(isQuickPrayerEnabled()){
            togglePrayer();
        }
        togglePrayer();
    }
    public boolean rigourUnlocked(){
     return !(client.getVarbitValue(5451) == 0);
    }
    public boolean pietyUnlocked(){
        return client.getVarbitValue(3909)==8;
    }
    public boolean auguryUnlucked(){
        return !(client.getVarbitValue(5452) == 0);
    }
    public boolean isQuickPrayerEnabled(){
        return client.getVarbitValue(QUICK_PRAYER)==1;
    }
    public Widget getItem(String str)
    {
        Widget[] items = client.getWidget(WidgetInfo.INVENTORY).getDynamicChildren();
        for (int i = 0; i < items.length; i++)
        {
            if (WildcardMatcher.matches(str.toLowerCase(), Text.removeTags(items[i].getName()).toLowerCase()))
            {
                return items[i];
            }
        }
        return null;
    }
    @SneakyThrows
    public HeadIcon getHeadIcon(NPC npc) {
        Method getHeadIconArrayMethod = null;
        for (Method declaredMethod : npc.getComposition().getClass().getDeclaredMethods())
        {
            if(declaredMethod.getReturnType()== short[].class&&declaredMethod.getParameterTypes().length==0){
                getHeadIconArrayMethod = declaredMethod;
            }
        }

        if (getHeadIconArrayMethod == null) {
            return null;
        }
        getHeadIconArrayMethod.setAccessible(true);
        short[] headIconArray = (short[]) getHeadIconArrayMethod.invoke(npc.getComposition());
        if (headIconArray == null || headIconArray.length == 0) {
            return null;
        }
        return HeadIcon.values()[headIconArray[0]];
    }
    public boolean isQuickPrayerActive(QuickPrayer prayer)
    {
        if ((client.getVarbitValue(4102) & (int) Math.pow(2, prayer.getIndex())) == Math.pow(2, prayer.getIndex()))
        {
            return true;
        }
        return false;
    }
    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        if (event.getMenuOption().toLowerCase().contains("pass")){
            isRange = true;
        }
    }
    @Override
    protected void startUp(){
        isRange = true;
        forceTab =false;
        updatedWeapon = "";
    }
    @Subscribe
    public void onAnimationChanged(AnimationChanged e){
        if(e.getActor()==null){
            return;
        }
        if (e.getActor().getAnimation() == 8754) {
            isRange = false;
        }
        if (e.getActor().getAnimation() == 8755) {
            isRange = true;
        }
    }
    private boolean isHunllefVarbitSet() {
        return client.getVarbitValue(9177) == 1;
    }
}