package cc.funkemunky.ZedPvP.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cc.funkemunky.ZedPvP.Core;

public class MiscUtils {
	
	public static Set<String> getConfigKeys(String string) {
		if(Core.getInstance().getConfig().getConfigurationSection(string).getKeys(false) != null) {
			return Core.getInstance().getConfig().getConfigurationSection(string).getKeys(false);
		}
		
		return null;
	}
	
	public static boolean isInt(final String sInt)
	{
		try
		{
			Integer.parseInt(sInt);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
	public static ItemStack createItem(Material material, int amount, String name, String... lore) {
		ItemStack thing = new ItemStack(material, amount);
		ItemMeta thingm = thing.getItemMeta();
		thingm.setDisplayName(Color.translate(name));
		List<String> loreList = new ArrayList<String>();
		for (String string : lore) {
			loreList.add(Color.translate(string));
		}
		thingm.setLore(loreList);
		thing.setItemMeta(thingm);
		return thing;
	}
	
	public static ItemStack WAND = createItem(Material.STONE_AXE, 1, Color.Green + "Cuboid Wand", new String[] {"", Color.White + "Left Click Block" + Color.Gray + " to " + Color.White +"Save Location 1" + Color.Gray + ".", Color.White + "Right Click Block" + Color.Gray + " to " + Color.White + "Save Location 2" + Color.Gray + ".", Color.White + "Left Click Air " + Color.Gray + "to clear selection."});

}
