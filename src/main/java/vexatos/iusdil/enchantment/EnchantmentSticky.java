package vexatos.iusdil.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vexatos.iusdil.util.EnchantUtils;

/**
 * @author Vexatos
 */
public class EnchantmentSticky extends IusdilEnchantment {

	public EnchantmentSticky() {
		super("sticky", Enchantment.Rarity.RARE, Enchantments.ANYTHING, EntityEquipmentSlot.values());
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 60;
	}

	@Override
	public int getMinEnchantability(int level) {
		return 0;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	/**
	 * @author Vexatos
	 */
	public static class StickyHandler {

		public static final StickyHandler INSTANCE = new StickyHandler();

		private StickyHandler() {
		}

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onItemToss(ItemTossEvent e) {
			if(e.getPlayer() == null || e.getEntityItem() == null
				|| e.getEntityItem().getItem().isEmpty()
				|| e.getPlayer() instanceof FakePlayer
				|| e.isCanceled()) {
				return;
			}
			EntityPlayer player = e.getPlayer();
			ItemStack stack = e.getEntityItem().getItem();
			if(EnchantUtils.isEnchanted(Enchantments.sticky, stack)) {
				if(player.inventory.addItemStackToInventory(stack)) {
					e.setCanceled(true);
				}
			}
		}
	}
}
