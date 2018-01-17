package anticheat.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import anticheat.Keaton;
import anticheat.detections.Checks;
import anticheat.detections.ChecksType;
import anticheat.utils.Color;

public class GUI {

	public Inventory mainGUI;
	public Inventory checksBannableGUI;
	public Inventory checksToggleGUI;
	public Inventory checksSetToggleGUI;
	public Inventory checksSetBannableGUI;
	public ItemStack backButton;
	String color;

	public GUI() {

		mainGUI = Bukkit.createInventory(null, 27, Color.Gold + Color.Bold + "Keaton AntiCheat");
		checksBannableGUI = Bukkit.createInventory(null, 9, Color.Dark_Gray + "Choose a type to set bannable.");
		checksToggleGUI = Bukkit.createInventory(null, 9, Color.Dark_Gray + "Choose a type to toggle.");
		
		backButton = createItem(Material.ARROW, 1, "&cBack", new String[] {"", "&fLeft Click &7to go back to previous page."});
		

		loadMainGUIItems();
	}

	private void loadMainGUIItems() {
		mainGUI.setItem(9, createItem(Material.BOOK, 1, "&6Toggle Bannable Checks", new String[] {"", "&fLeft Click &7to open &fGUI Editor", "&7for &fToggling Bans&7."}));
		mainGUI.setItem(11, createItem(Material.BOOK, 1, "&6Toggle Checks", new String[] {"", "&fLeft Click &7to open &fGUI Editor", "&7for &fEnabling/Disabling Checks&7."}));
		mainGUI.setItem(13, createItem(Material.ENCHANTED_BOOK, 1, "&cKeaton Info", new String[] {"", "", "&7You are currently using &6Keaton v" + Keaton.getAC().getDescription().getVersion(), "&7by &ffunkemunky &7and &fXTasyCode&7.", "&7Any questions or concerns?", "&fShift Left Click &7to receive an &fInvite to the Keaton Discord&7."}));
		mainGUI.setItem(15, createItem(Material.BOOK, 1, "&6Reload Keaton", new String[] {"", "&fLeft Click &7to &fReload Keaton&7."}));
		mainGUI.setItem(17, createItem(Material.BOOK, 1, "&6Reset Violations", new String[] {"", "&fLeft Click &7to", "&fReset all player Violations&7."}));
	}
	
	public void openMainGUI(Player player) {
		player.openInventory(mainGUI);
	}
	
	public void openChecksBannableGUI(Player player) {
		checksBannableGUI.setItem(0, backButton);
		checksBannableGUI.setItem(2, createItem(Material.REDSTONE_BLOCK, 1, "&cCombat"));
		checksBannableGUI.setItem(4, createItem(Material.REDSTONE_BLOCK, 1, "&cMovement"));
		checksBannableGUI.setItem(6, createItem(Material.REDSTONE_BLOCK, 1, "&cMiscellaneous"));
		checksBannableGUI.setItem(8, backButton);
		
		player.openInventory(checksBannableGUI);
	}
	
	public void openBannableChecks(Player player, ChecksType type) {
		checksSetBannableGUI = Bukkit.createInventory(null, 45, Color.Dark_Gray + "Toggle Bans for: " + Color.Gold + type.getName());
		int i = 0;
		for(Checks check : Keaton.getAC().getChecks().getDetections()) {
			if(check.getType() == type) {
				if(i != 44) {
					checksSetBannableGUI.setItem(i, createItem(check.isBannable() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, i + 1, check.isBannable() ? Color.Green + check.getName() : Color.Red + check.getName()));
					i++;
				}
			}
		}
		checksSetBannableGUI.setItem(44, backButton);
		player.openInventory(checksSetBannableGUI);
	}
	
	public void openChecksToggleGUI(Player player) {
		checksToggleGUI.setItem(0, backButton);
		checksToggleGUI.setItem(2, createItem(Material.REDSTONE_BLOCK, 1, "&cCombat"));
		checksToggleGUI.setItem(4, createItem(Material.REDSTONE_BLOCK, 1, "&cMovement"));
		checksToggleGUI.setItem(6, createItem(Material.REDSTONE_BLOCK, 1, "&cMiscellaneous"));
		checksToggleGUI.setItem(8, backButton);
		
		player.openInventory(checksToggleGUI);
	}
	
	public void openToggleChecks(Player player, ChecksType type) {
		checksSetToggleGUI = Bukkit.createInventory(null, 45, Color.Dark_Gray + "Toggle Checks for: " + Color.Gold + type.getName());
		int i = 0;
		for(Checks check : Keaton.getAC().getChecks().getDetections()) {
			if(check.getType() == type) {
				if(i != 44) {
					checksSetToggleGUI.setItem(i, createItem(check.getState() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, i + 1, check.getState() ? Color.Green + check.getName() : Color.Red + check.getName()));
					i++;
				}
			}
		}
		checksSetToggleGUI.setItem(44, backButton);
		player.openInventory(checksSetToggleGUI);
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