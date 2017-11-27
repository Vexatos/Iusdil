package vexatos.iusdil.reference;

import net.minecraftforge.fml.common.Loader;

/**
 * @author Vexatos
 */
public class Mods {

	// The mod itself
	public static final String
		Iusdil = "iusdil",
		Iusdil_Name = "Instructions unclear, sword dropped into lava.";

	public static boolean isLoaded(String name) {
		return Loader.isModLoaded(name);
	}
}
