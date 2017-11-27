package vexatos.iusdil.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vexatos.iusdil.Iusdil;
import vexatos.iusdil.reference.Config;
import vexatos.iusdil.util.EnchantUtils;

import javax.annotation.Nullable;
import java.util.ListIterator;
import java.util.Map;

/**
 * Taken from EnderIO which is in the public domain.
 * @author CrazyPants, Vexatos
 */
public class EnchantmentSoulbound extends IusdilEnchantment {

	public EnchantmentSoulbound() {
		super("soulbound", Rarity.VERY_RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
	}

	@Override
	public int getMaxEnchantability(int level) {
		return 60;
	}

	@Override
	public int getMinEnchantability(int level) {
		return 16;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	/**
	 * Taken from EnderIO which is in the public domain.
	 * @author CrazyPants, Vexatos
	 */
	public static class SoulboundHandler {

		public static final SoulboundHandler INSTANCE = new SoulboundHandler();

		private SoulboundHandler() {
		}

		/*
		 * This is called the moment the player dies and drops his stuff.
		 *
		 * We go early, so we can get our items before other mods put them into some
		 * grave. Also remove them from the list so they won't get duped. If the
		 * inventory overflows, e.g. because everything there and the armor is
		 * soulbound, let the remainder be dropped/graved.
		 */
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onPlayerDeath(PlayerDropsEvent evt) {
			if(evt.getEntityPlayer() == null || evt.getEntityPlayer() instanceof FakePlayer || evt.isCanceled()) {
				return;
			}
			if(evt.getEntityPlayer().world.getGameRules().getBoolean("keepInventory")) {
				return;
			}

			ListIterator<EntityItem> iter = evt.getDrops().listIterator();
			while(iter.hasNext()) {
				EntityItem ei = iter.next();
				ItemStack stack = ei.getItem();
				if(EnchantUtils.isEnchanted(Enchantments.soulbound, stack)) {
					removeIfApplicable(stack);
					if(addToPlayerInventory(evt.getEntityPlayer(), stack)) {
						iter.remove();
					}
				}
			}
		}

		private void removeIfApplicable(ItemStack stack) {
			if(Config.SingleUseSoulbound) {
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
				if(enchantments.get(Enchantments.soulbound) > 0) {
					enchantments.put(Enchantments.soulbound, -1);
				} else {
					enchantments.remove(Enchantments.soulbound);
				}
				EnchantmentHelper.setEnchantments(enchantments, stack);
			}
		}

		/*
		 * Do a second (late) pass. If any mod has added items to the list in the meantime, this gives us a chance to save them, too. If some gravestone mod has
		 * removed drops, we'll get nothing here.
		 */
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onPlayerDeathLate(PlayerDropsEvent evt) {
			onPlayerDeath(evt);
		}

		/*
		 * This is called when the user presses the "respawn" button. The original inventory would be empty, but onPlayerDeath() above placed items in it.
		 *
		 * Note: Without other death-modifying mods, the content of the old inventory would always fit into the new one (both being empty but for soulbound items in
		 * the old one) and the old one would be discarded just after this method. But better play it safe and assume that an overflow is possible and that another
		 * mod may move stuff out of the old inventory, too.
		 */
		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public void onPlayerClone(PlayerEvent.Clone evt) {
			if(!evt.isWasDeath() || evt.isCanceled()) {
				return;
			}
			if(evt.getOriginal() == null || evt.getEntityPlayer() == null || evt.getEntityPlayer() instanceof FakePlayer) {
				return;
			}
			if(evt.getEntityPlayer().world.getGameRules().getBoolean("keepInventory")) {
				return;
			}
			if(evt.getOriginal() == evt.getEntityPlayer()
				|| evt.getOriginal().inventory == evt.getEntityPlayer().inventory
				|| (evt.getOriginal().inventory.armorInventory == evt.getEntityPlayer().inventory.armorInventory && evt.getOriginal().inventory.mainInventory == evt.getEntityPlayer().inventory.mainInventory)) {
				Iusdil.log.warn("Player " + evt.getEntityPlayer().getName() + " just died and respawned in their old body. Did someone fire a PlayerEvent.Clone(death=true) "
					+ "for a teleportation? Supressing Soulbound enchantment for zombie player.");
				return;
			}

			addOrDropItems(evt.getOriginal(), evt.getEntityPlayer(), evt.getOriginal().inventory.armorInventory);
			addOrDropItems(evt.getOriginal(), evt.getEntityPlayer(), evt.getOriginal().inventory.mainInventory);
		}

		/*
		 * Do a second (late) pass and try to preserve any remaining items by spawning them into the world. They might end up nowhere, but if we do nothing they will
		 * be deleted. Note the dropping at the old location, because the new player object's location has not yet been set.
		 */
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onPlayerCloneLast(PlayerEvent.Clone evt) {
			if(!evt.isWasDeath() || evt.isCanceled()) {
				return;
			}
			if(evt.getOriginal() == null || evt.getEntityPlayer() == null || evt.getEntityPlayer() instanceof FakePlayer) {
				return;
			}
			if(evt.getEntityPlayer().world.getGameRules().getBoolean("keepInventory")) {
				return;
			}
			if(evt.getOriginal() == evt.getEntityPlayer()
				|| evt.getOriginal().inventory == evt.getEntityPlayer().inventory
				|| (evt.getOriginal().inventory.armorInventory == evt.getEntityPlayer().inventory.armorInventory && evt.getOriginal().inventory.mainInventory == evt.getEntityPlayer().inventory.mainInventory)) {
				return;
			}

			addOrDropItems(evt.getOriginal(), evt.getEntityPlayer(), evt.getOriginal().inventory.armorInventory);
			addOrDropItems(evt.getOriginal(), evt.getEntityPlayer(), evt.getOriginal().inventory.mainInventory);
		}

		private void addOrDropItems(EntityPlayer oldPlayer, EntityPlayer newPlayer, NonNullList<ItemStack> inv) {
			for(int i = 0; i < inv.size(); i++) {
				ItemStack stack = inv.get(i);
				if(EnchantUtils.isEnchanted(Enchantments.soulbound, stack, lv -> lv != 0)) {
					removeIfApplicable(stack);
					if(addToPlayerInventory(newPlayer, stack) || tryToSpawnItemInWorld(oldPlayer, stack)) {
						inv.set(i, ItemStack.EMPTY);
					}
				}
			}
		}

		private boolean tryToSpawnItemInWorld(@Nullable EntityPlayer entityPlayer, ItemStack item) {
			if(entityPlayer != null) {
				EntityItem entityitem = new EntityItem(entityPlayer.world, entityPlayer.posX, entityPlayer.posY + 0.5, entityPlayer.posZ, item);
				entityitem.setPickupDelay(40);
				entityitem.lifespan *= 5;
				entityitem.motionX = 0;
				entityitem.motionZ = 0;
				entityPlayer.world.spawnEntity(entityitem);
				return true;
			}
			return false;
		}

		private boolean addToPlayerInventory(@Nullable EntityPlayer entityPlayer, @Nullable ItemStack item) {
			if(item == null || entityPlayer == null) {
				return false;
			}
			if(item.getItem() instanceof ItemArmor) {
				ItemArmor arm = (ItemArmor) item.getItem();
				int index = arm.armorType.getIndex();
				if(entityPlayer.inventory.armorInventory.get(index).isEmpty()) {
					entityPlayer.inventory.armorInventory.set(index, item);
					return true;
				}
			}

			InventoryPlayer inv = entityPlayer.inventory;
			for(int i = 0; i < inv.mainInventory.size(); i++) {
				if(inv.mainInventory.get(i).isEmpty()) {
					inv.mainInventory.set(i, item.copy());
					return true;
				}
			}

			return false;
		}

	}
}
