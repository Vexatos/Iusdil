package vexatos.iusdil.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vexatos.iusdil.reference.Config;
import vexatos.iusdil.reference.Mods;

/**
 * @author Vexatos
 */
public class Enchantments {

	public static EnumEnchantmentType ANYTHING;

	public static EnchantmentSoulbound soulbound;
	public static EnchantmentSticky sticky;

	public static void init() {
		ANYTHING = EnumHelper.addEnchantmentType(Mods.Iusdil + ":anything", item -> true);

		if(Config.EnableSoulbound) {
			soulbound = new EnchantmentSoulbound();
			GameRegistry.findRegistry(Enchantment.class).register(soulbound);
			MinecraftForge.EVENT_BUS.register(EnchantmentSoulbound.SoulboundHandler.INSTANCE);
		}

		if(Config.EnableSticky) {
			sticky = new EnchantmentSticky();
			GameRegistry.findRegistry(Enchantment.class).register(sticky);
			MinecraftForge.EVENT_BUS.register(EnchantmentSticky.StickyHandler.INSTANCE);
		}
	}
}
