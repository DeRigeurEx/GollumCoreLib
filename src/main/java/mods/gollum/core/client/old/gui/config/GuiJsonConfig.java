package mods.gollum.core.client.old.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import akka.dispatch.sysmsg.Create;

import com.ibm.icu.text.DisplayContext.Type;

import mods.gollum.core.client.old.gui.config.entry.GollumCategoryEntry;
import mods.gollum.core.client.old.gui.config.entry.JsonEntry;
import mods.gollum.core.client.old.gui.config.properties.FieldProperty;
import mods.gollum.core.client.old.gui.config.properties.JsonProperty;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.GuiConfigEntries.SelectValueEntry;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;

public class GuiJsonConfig extends GuiGollumConfig {
	
	public GuiJsonConfig(GuiConfig parent, JsonEntry entry, Json value, Json defaultValue) {
		super(parent, getFields (parent, value, defaultValue), entry);
	}
	
	private static List<IConfigElement> getFields(GuiConfig parent, Json value, Json defaultValue) {
		
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		if (value.isObject()) {
			
			for(Entry<String, Json> entry : value.allChildWithKey()) {
				
				JsonProperty prop      = new JsonProperty (getMod(parent), entry.getKey(), entry.getValue(), defaultValue.child(entry.getKey()));
				IConfigElement element = prop.createConfigElement ();
				
				if (element != null) {
					fields.add(element);
				}
			}
			
		}
		
		if (value.isArray()) {
			
			for(Entry<String, Json> entry : value.allChildWithKey()) {
				
				JsonProperty prop      = new JsonProperty (getMod(parent), entry.getKey(), entry.getValue(), defaultValue.child(entry.getKey()));
				IConfigElement element = prop.createConfigElement ();
				
				if (element != null) {
					fields.add(element);
				}
			}
		}
		
		return fields;
	}

	public void saveChanges() {
		Json value = (Json) ((JsonEntry)this.entry).getJsonValue().clone();
		if (value.isObject()) {
			
			for (IConfigEntry entry : this.entryList.listEntries) {
				
				Json newValue = null;
				
				if (value.containsKey(entry.getName())) {
					newValue = (Json) value.child(entry.getName()).clone();
				} else {
					// TODO no implment add new value
					newValue = Json.create("");
				}
				
				newValue.setValue(entry.getCurrentValue());
				value.add(entry.getName(), newValue);
			}
		}
		if (value.isArray()) {
			
			value.clear ();
			
			for (int i = 0; i < this.entryList.listEntries.size(); i++) {
				
				IConfigEntry entry = this.entryList.listEntries.get(i);
				Json newValue = (Json) value.child(entry.getName()).clone();
				
				newValue.setValue(entry.getCurrentValue());
				value.add(newValue);
			}
		}
		
		this.entry.setValueFromChildScreen(value);
	}
	
	protected boolean doneAction() {
		this.saveChanges ();
		return super.doneAction();
	}
}