package vexatos.iusdil;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import vexatos.iusdil.enchantment.Enchantments;
import vexatos.iusdil.reference.Config;
import vexatos.iusdil.reference.Mods;

@Mod(modid = Mods.Iusdil, name = Mods.Iusdil_Name, version = "@VERSION@")
public class Iusdil {

	@Instance
	public static Iusdil instance;

	public static Config config;
	public static Logger log;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		log = e.getModLog();
		config = new Config(new Configuration(e.getSuggestedConfigurationFile()));
		config.load();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		Enchantments.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		config.save(); // generate config file if it doesn't exist yet.
	}

	/*@EventHandler
	public void onServerStart(FMLServerAboutToStartEvent e) {

	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent e) {

	}

	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent e) {
		// NO-OP
	}*/

	/*private final Set<Runnable> pending = new HashSet<>();

	public void schedule(Runnable r) {
		synchronized(pending) {
			pending.add(r);
		}
	}

	@SubscribeEvent
	public void onTick(ServerTickEvent e) {
		if(e.phase == TickEvent.Phase.START) {
			final Runnable[] pending;
			synchronized(this.pending) {
				pending = this.pending.isEmpty() ? null : this.pending.toArray(new Runnable[0]);
				this.pending.clear();
			}
			if(pending != null) {
				for(Runnable r : pending) {
					try {
						r.run();
					} catch(Throwable t) {
						Iusdil.log.warn("Error in scheduled tick action.", t);
					}
				}
			}
		}
	}*/
}
