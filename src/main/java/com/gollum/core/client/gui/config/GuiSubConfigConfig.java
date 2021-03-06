package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.gollum.core.client.gui.config.element.CategoryElement;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.client.gui.config.element.SubConfigElement;
import com.gollum.core.client.gui.config.element.SubGuiElement;
import com.gollum.core.client.gui.config.entry.SubConfigEntry;
import com.gollum.core.common.config.ConfigLoader;
import com.gollum.core.common.config.ConfigLoader.ConfigLoad;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class GuiSubConfigConfig extends GuiConfig {
	
	private ArrayList<ConfigLoad> subConfigLoaded;
	private SubConfigEntry subConfigEntry;
	
	public GuiSubConfigConfig(SubConfigEntry subConfigEntry) {
		super(subConfigEntry.parent.parent);

		this.titleLine2   = subConfigEntry.getLabel();
		
		this.subConfigEntry = subConfigEntry;
	}
	
	@Override
	protected void initConfigElement() {
		
		this.subConfigLoaded = ConfigLoader.getSubConfig(this.mod);
		
		for (ConfigLoad configLoad : this.subConfigLoaded) {
			configElements.add(new CategoryElement(this.subConfigEntry.getLabel(), configLoad, configLoad.config.getFileName()));
		}
	}
	
	@Override
	public void displayParent() {
		
		boolean mcRestart = this.entryList.requiresMcRestart();
		boolean wRestart  = this.entryList.requiresWorldRestart();
		
		ConfigChangedEvent event = new OnConfigChangedEvent(this.getMod().getModId(), this.subConfigEntry.getName(), wRestart, mcRestart);
		FMLCommonHandler.instance().bus().post(event);
		if (!event.getResult().equals(Result.DENY)) {
			this.saveValue ();
			FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(this.getMod().getModId(), this.subConfigEntry.getName(), wRestart, mcRestart));
			
			if (mcRestart) {
				this.mc.displayGuiScreen(new GuiMessageDialog(this.getParent(), "fml.configgui.gameRestartTitle", new ChatComponentText(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
				return;
			}
		}
		super.displayParent();
	}
	
	@Override
	public void saveValue() {
		
		log.info("Save configuration "+this.getMod().getModId()+" > "+this.subConfigEntry.getLabel());
		
		for (Entry<String, Object> entry : this.entryList.getValues().entrySet()) {
			String fileName = entry.getKey();
			for (ConfigLoad configLoad : this.subConfigLoaded) {
				
				if (fileName.equals(configLoad.config.getFileName())) {
					log.info("Save file: "+fileName);
					configLoad.saveValue((LinkedHashMap<String, Object>) entry.getValue());
					new ConfigLoader(configLoad.config, false).writeConfig();
				}
			}
		}
		
	}
	
}
