package vexatos.iusdil.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import vexatos.iusdil.reference.Mods;

/**
 * @author Vexatos
 */
public abstract class IusdilEnchantment extends Enchantment {

	public final String NAME;

	protected IusdilEnchantment(String name, Rarity rarity, EnumEnchantmentType type, EntityEquipmentSlot[] slots) {
		super(rarity, type, slots);
		this.NAME = name;
		setName(Mods.Iusdil + "." + NAME);
		setRegistryName(NAME);
	}
}
