package vexatos.iusdil.reference;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Vexatos
 */
public class Config {

	private Configuration config;

	public static boolean EnableSoulbound = true;
	public static boolean SingleUseSoulbound = true;

	public static boolean EnableSticky = true;

	public Config(Configuration configuration) {
		config = configuration;
		config.load();
	}

	public void load() {
		EnableSoulbound = config.getBoolean("enable", "soulbound", true, "Enable the Soulbound enchantment.");
		SingleUseSoulbound = config.getBoolean("singleUseSoulbound", "soulbound", true, "Soulbound enchantment vanishes after being activated.");

		EnableSticky = config.getBoolean("enable", "sticky", true, "Enable the Sticky enchantment.");
	}

	public void save() {
		config.save();
	}

}
