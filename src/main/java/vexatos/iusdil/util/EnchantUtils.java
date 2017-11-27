package vexatos.iusdil.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import vexatos.iusdil.enchantment.EnchantmentSoulbound;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Vexatos
 */
public class EnchantUtils {

	public static boolean isEnchanted(Enchantment ench, ItemStack stack) {
		return isEnchanted(ench, stack, 1);
	}

	public static boolean isEnchanted(Enchantment ench, ItemStack stack, int minLevel) {
		return EnchantmentHelper.getEnchantmentLevel(ench, stack) >= minLevel;
	}

	public static boolean isEnchanted(Enchantment ench, ItemStack stack, Predicate<Integer> pred) {
		return pred.test(EnchantmentHelper.getEnchantmentLevel(ench, stack));
	}

	public static void removeEnchantment(EnchantmentSoulbound ench, ItemStack stack) {
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		enchantments.remove(ench);
		EnchantmentHelper.setEnchantments(enchantments, stack);
	}
}
