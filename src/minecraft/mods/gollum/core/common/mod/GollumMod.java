package mods.gollum.core.common.mod;

import java.lang.annotation.Annotation;

import mods.gollum.core.common.context.ModContext;
import mods.gollum.core.common.i18n.I18n;
import mods.gollum.core.common.log.Logger;
import mods.gollum.core.tools.registry.BlockRegistry;
import mods.gollum.core.tools.registry.ItemRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class GollumMod {

	/**
	 * Gestion des logs
	 */
	public static Logger log;
	
	/**
	 * Gestion de l'i18n
	 */
	public static I18n i18n;

	private String modId;
	private String modName;
	private String version;
	private String minecraftVersion;

	private int mobId = 0;
	
	public GollumMod() {

		this.modId = "Error";
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {
				this.modId = ((Mod)annotation).modid();
			}
		}
		this.modName = "Error";
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {
				this.modName = ((Mod)annotation).name();
			}
		}
		
		this.version = "0.0.0 [DEV]";
		
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {;
			this.version = ((Mod)annotation).version();
			}
		}
		
		this.minecraftVersion = Loader.instance().getMinecraftModContainer().getVersion();
		
	}
	
	/**
	 * Renvoie le modID du MOD
	 */
	public String getModId () {
		return this.modId;
	}
	
	/**
	 * Renvoie le modName du MOD
	 */
	public String getModName () {
		return this.modName;
	}
	
	public int nextMobID() {
		return ++this.mobId ;
	}
	
	/**
	 * Renvoie la version du MOD
	 */
	public String getVersion () {
		return version;
	}
	
	/**
	 * Renvoie la version de Minecraft
	 */
	public String getMinecraftVersion () {
		return this.minecraftVersion;
	}
	
	public void handler (FMLPreInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		// Creation du logger
		this.log = new Logger();
		
		// Creation du logger
		this.i18n = new I18n();
		
		this.preInit(event);
		
		BlockRegistry.instance().registerAll();
		ItemRegistry.instance().registerAll();
	}
	
	public void handler (FMLInitializationEvent event) {
		this.init(event);
	}
	public void handler (FMLPostInitializationEvent event) {
		this.postInit(event);
		ModContext.instance ().pop();
	}
	
	public abstract void preInit (FMLPreInitializationEvent event);
	public abstract void init(FMLInitializationEvent event);
	public abstract void postInit(FMLPostInitializationEvent event);
	
}