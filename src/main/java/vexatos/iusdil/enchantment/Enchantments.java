package vexatos.iusdil.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vexatos.iusdil.reference.Config;

/**
 * @author Vexatos
 */
public class Enchantments {

	public static EnchantmentSoulbound soulbound;
	public static EnchantmentSticky sticky;

	public static void init() {
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
