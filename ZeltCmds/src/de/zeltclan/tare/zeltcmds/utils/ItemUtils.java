package de.zeltclan.tare.zeltcmds.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ItemUtils {
	public static ItemStack getItemStack(final String p_item) {
		ItemStack itemstack = null;
		if (p_item.matches("[0-9]+")) {
			if (Material.getMaterial(Integer.parseInt(p_item)) != null) {
				itemstack = new MaterialData(Integer.parseInt(p_item)).toItemStack();
			}
		} else if (p_item.matches("[0-9]+:[0-9]+")) {
			String[] split = p_item.split(":");
			if (Material.getMaterial(Integer.parseInt(split[0])) != null) {
				itemstack = new MaterialData(Integer.parseInt(split[0]), (byte) Integer.parseInt(split[1])).toItemStack();
			}
		} else {
			Material material = Material.matchMaterial(p_item);
			if (material != null) {
				itemstack = new ItemStack(material);
			}
		}
		return itemstack;
	}
}
