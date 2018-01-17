package cc.funkemunky.ZedPvP.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cc.funkemunky.ZedPvP.Core;
import cc.funkemunky.ZedPvP.utils.Color;

public class RubiesGUI {
	
	private Inventory mainPage = Bukkit.createInventory(null, 27, Color.Gold + "Ruby Shop");
	private Inventory keysPage = Bukkit.createInventory(null, 27, Color.Gold + "Ruby Key Shop");
	private ItemStack back;
	public RubiesGUI() {
		back = createItem(Material.REDSTONE, 1, Color.Red + "Go back", new String[0]);
		
		mainPage.setItem(11, createItem(Material.TRIPWIRE_HOOK, 1, Color.Yellow + "Key Shop"));
		mainPage.setItem(15, createItem(Material.GOLD_INGOT, 1, Color.Yellow + "Money Shop"));
		
		keysPage.setItem(13, createItem(Material.BARRIER, 1, Color.Red + "Not available yet."));
		keysPage.setItem(26, back);
	}
	
	public void openKeysGUI(Player player) {
		player.openInventory(keysPage);
	}
	
	public void openMainGUI(Player player) {
		player.openInventory(mainPage);
	}
	
	public void openMoneyGUI(Player player) {
		Inventory moneyPage = Bukkit.createInventory(null, 27, Color.Gold + "Ruby Money Shop");
		moneyPage.setItem(9, createItem(Material.EMERALD, 1, Color.Green + "500", new String[] {"", Color.Gray + Color.Italics + "Costs " + Color.Red + Color.Italics + "1 Ruby", "", Color.Gray + Color.Italics + "You have " + Core.getInstance().getRubyManager().getRubies(player) + " rubies."}));
		moneyPage.setItem(11, createItem(Material.EMERALD, 1, Color.Green + "1000", new String[] {"", Color.Gray + Color.Italics + "Costs " + Color.Red + Color.Italics + "2 Rubies", "", Color.Gray + Color.Italics + "You have " + Core.getInstance().getRubyManager().getRubies(player) + " rubies."}));
		moneyPage.setItem(13, createItem(Material.EMERALD, 1, Color.Green + "5000", new String[] {"", Color.Gray + Color.Italics + "Costs " + Color.White + Color.Italics + "10 Rubies", "", Color.Gray + Color.Italics + "You have " + Core.getInstance().getRubyManager().getRubies(player) + " rubies."}));
		moneyPage.setItem(15, createItem(Material.EMERALD, 1, Color.Green + "10000", new String[] {"", Color.Gray + Color.Italics + "Costs " + Color.White + Color.Italics + "20 Rubies", "", Color.Gray + Color.Italics + "You have " + Core.getInstance().getRubyManager().getRubies(player) + " rubies."}));
		moneyPage.setItem(26, back);
		player.openInventory(moneyPage);
	}
	
	public ItemStack createItem(Material material, int amount, String name, String... lore) {
		ItemStack thing = new ItemStack(material, amount);
		ItemMeta thingm = thing.getItemMeta();
		thingm.setDisplayName(Color.translate(name));
		List<String> loreList = new ArrayList<String>();
		for(String string : lore) {
			loreList.add(Color.translate(string));
		}
		thingm.setLore(loreList);
		thing.setItemMeta(thingm);
		return thing;
	}
	
	public boolean hasSameName(ItemStack item, String name) {
		if(item.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
}
