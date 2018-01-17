package cc.funkemunky.ZedPvP.freeze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;


public class GUIManager {
    private static Inventory inv;
    private static ItemStack item;

    private static void createInv() {
        inv = Bukkit.createInventory(null, 9, Chat.c(Color.Red + "YOU HAVE BEEN FROZEN"));
        ItemStack admit = createAdmit("&e&lADMIT", new String[] {"", Color.White + Color.Italics + "Shift-Left-Click to admit!"});
        item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Chat.c("&cFROZEN"));

        List<String> lores = lore();
        List<String> temp = lore();
        lores.clear();

        for (String str : temp) {
            lores.add(Chat.c(str));
        }

        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);

        for (int i = 0; i < inv.getSize() - 1; i++) {
            inv.setItem(i, item);
        }
        
        inv.setItem(8, admit);
        
    }
	private static ItemStack createAdmit(String name, String... lore)
	{
	  ItemStack thing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
	  ItemMeta thingm = thing.getItemMeta();
	  thingm.setDisplayName(Color.translate(name));
	  thingm.setLore(Arrays.asList(lore));
	  thing.setItemMeta(thingm);
	  return thing;
	}

    public static void openInv(Player player) {
        player.openInventory(inv);
    }
    
	private static List<String> lore() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add(Color.translate("&ePlease download TeamSpeak @"));
		list.add(Color.translate("&fhttp://teamspeak.com"));
		list.add(Color.translate("&eThen join TeamSpeak @"));
		list.add(Color.translate("&fts.vanix.cc"));
		list.add("");
		return list;
	}

    public static Inventory getInv() {
        return inv;
    }

    public static void removeLock(Player p) {
        Core.getInstance().getInvLock().remove(p.getUniqueId());
        p.closeInventory();
    }

    public static void addLock(Player p) {
        createInv();
        openInv(p);
        Core.getInstance().getInvLock().add(p.getUniqueId());
    }

    public static boolean isLocked(Player p) {
        return Core.getInstance().getInvLock().contains(p.getUniqueId());
    }
}
