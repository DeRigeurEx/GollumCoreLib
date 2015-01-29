package mods.gollum.core.client.gui.config.entry;

import java.util.Collection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.type.BuildingConfigType;
import mods.gollum.core.common.config.type.BuildingConfigType.Group;
import mods.gollum.core.common.config.type.BuildingConfigType.Group.Building;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;

public class BuildingEntry extends ConfigEntry {
	
	private BuildingConfigType value;

	private int currentGroup = 0;
	private int currentBuilding = 0;

	private ConfigEntry group;
	private ConfigEntry globalSpawnRate;
	private ConfigEntry building;
	
	public BuildingEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.value = (BuildingConfigType) configElement.getValue();
		
		parent.setSlotHeight (200);
		
		this.init ();
	}
	
	private void init() {
		
		final BuildingEntry _this = this;
		
		Object[] valuesGO = this.value.lists.keySet().toArray();
		String[] valuesGS = new String[valuesGO.length];
		for (int i = 0; i < valuesGO.length; i++) {
			valuesGS[i] = (String)valuesGO[i];
		}
		
		this.group = this.createSubEntry(
			"group",
			this.getCurrentGroupName(),
			this.getCurrentDefaultGroupName(),
			0,
			new JsonConfigProp().type(ConfigProp.Type.LIST_INLINE).validValues(valuesGS)
		);
		this.group.btResetIsVisible = false;
		this.group.btUndoIsVisible  = false;
		this.group.formatedLabel    = false;
			
		this.group.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				
				if (type == Type.CHANGE) {
					
					Group oldGroup = _this.getCurrentGroup();
					oldGroup.globalSpawnRate = (Integer) _this.globalSpawnRate.getValue();
					
					_this.setCurrentGroup((String) _this.group.getValue());
					Group groupC = _this.getCurrentGroup();
					Group groupD = _this.getCurrentDefaultGroup();
					Group groupO = _this.getCurrentOldGroup();
					_this.globalSpawnRate.setValue(groupC.globalSpawnRate);
					_this.globalSpawnRate.configElement.setValue(groupO.globalSpawnRate);
					_this.globalSpawnRate.configElement.setDefaultValue(groupD.globalSpawnRate);
					
					Object[] valuesBO = groupC.buildings.keySet().toArray();
					String[] valuesBS = new String[valuesBO.length];
					for (int i = 0; i < valuesBO.length; i++) {
						valuesBS[i] = (String)valuesBO[i];
					}
					
					((ListInlineEntry)_this.building).values = valuesBS;
					_this.currentBuilding = 0;
					
					_this.building.setValue(_this.getCurrentBuildingName());
					_this.building.configElement.setValue(groupO.globalSpawnRate);
					_this.building.configElement.setDefaultValue(groupD.globalSpawnRate);
					
				} 
			}
			
		});
		
		this.globalSpawnRate = this.createSubEntry(
			"globalSpawnRate",
			this.getCurrentGroup().globalSpawnRate,
			this.getCurrentDefaultGroup().globalSpawnRate,
			1,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("0").maxValue("10")
		);
		this.globalSpawnRate.btResetIsVisible = true;
		this.globalSpawnRate.btUndoIsVisible  = true;
		
		this.globalSpawnRate.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				Group group = _this.getCurrentGroup();
				group.globalSpawnRate = (Integer) _this.globalSpawnRate.getValue();
			}
			
		});
		
		Group group = _this.getCurrentGroup();
		
		Object[] valuesBO = group.buildings.keySet().toArray();
		String[] valuesBS = new String[valuesBO.length];
		for (int i = 0; i < valuesBO.length; i++) {
			valuesBS[i] = (String)valuesBO[i];
		}
		
		this.building = this.createSubEntry(
				"building",
				this.getCurrentBuildingName(),
				this.getCurrentBuildingGroupName(),
				1,
				new JsonConfigProp().type(ConfigProp.Type.LIST_INLINE).validValues(valuesBS)
			);
		this.building.btResetIsVisible = false;
		this.building.btUndoIsVisible  = false;
		this.building.formatedLabel    = false;
				
		this.building.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (type == Type.CHANGE) {
					Building building = _this.getCurrentBuilding();
				}
			}
			
		});
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		this.parent.controlWidth -= 7;
		
		this.parent.labelX -= 10;
		this.group.drawEntry(0, x, y     , listWidth, 22, tessellator, mouseX, mouseY, isSelected);
		this.parent.labelX += 10;
		
		drawRec(this.parent.labelX - 10, y);
		
		this.globalSpawnRate.drawEntry(1, x, y + 22, listWidth, 22, tessellator, mouseX, mouseY, isSelected);
		this.building       .drawEntry(2, x, y + 44, listWidth, 22, tessellator, mouseX, mouseY, isSelected);
		
		
		drawRec(this.parent.labelX, y + 44);
		
		this.parent.labelX += 10;
		
		this.parent.labelX -= 10;
		
		this.parent.controlWidth += 3;
	}

	private void drawRec(int x, int y) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		this.parent.parent.drawGradientRect(
			x, 
			y + 20, 
			this.parent.controlX + this.parent.controlWidth+3, 
			y + 1000, 
			0x0EFFFFFF, 
			0x0EFFFFFF
		);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}
	
	@Override
	public int getLabelWidth() {
		if (!this.getLabelDisplay()) {
			return 0;
		}
		
		int[] sizes = new int[] {
			mc.fontRenderer.getStringWidth(this.tradIfExist("group")),
			mc.fontRenderer.getStringWidth(this.tradIfExist("globalSpawnRate")),
			mc.fontRenderer.getStringWidth(this.tradIfExist("building")),
		};
		
		int size = 0;
		for (int s : sizes) {
			size = Math.max(size, s);
		}
		
		return size;
	}
	
	public void setCurrentGroup (String name) {
		int i = 0;
		for (String group : this.value.lists.keySet()) {
			if (group.equals(name)) {
				this.currentGroup = i;
				return;
			}
			i++;
		}
		return;
	}
	
	public String getCurrentGroupName () {
		return this.getCurrentGroupName(this.value.lists.keySet());
	}
	public String getCurrentDefaultGroupName () {
		return this.getCurrentGroupName(((BuildingConfigType)this.configElement.getDefaultValue()).lists.keySet());
	}
	public String getCurrentOldGroupName () {
		return this.getCurrentGroupName(((BuildingConfigType)this.configElement.getValue()).lists.keySet());
	}
	private String getCurrentGroupName (Collection<String> collection) {
		int i = 0;
		for (String group : collection) {
			if (i == this.currentGroup) {
				return group;
			}
			i++;
		}
		return null;
	}
	
	public Group getCurrentGroup () {
		return this.getCurrentGroup(this.value.lists.values());
	}
	public Group getCurrentDefaultGroup () {
		return this.getCurrentGroup(((BuildingConfigType)this.configElement.getDefaultValue()).lists.values());
	}
	public Group getCurrentOldGroup () {
		return this.getCurrentGroup(((BuildingConfigType)this.configElement.getValue()).lists.values());
	}
	private Group getCurrentGroup (Collection<Group> collection) {
		int i = 0;
		for (Group group : collection) {
			if (i == this.currentGroup) {
				return group;
			}
			i++;
		}
		return null;
	}
	
	public String getCurrentBuildingName () {
		return this.getCurrentBuildingName(this.getCurrentGroup().buildings.keySet());
	}
	public String getCurrentBuildingGroupName () {
		return this.getCurrentBuildingName(this.getCurrentDefaultGroup().buildings.keySet());
	}
	public String getCurrentOldBuildingName () {
		return this.getCurrentBuildingName(this.getCurrentOldGroup().buildings.keySet());
	}
	public String getCurrentBuildingName (Collection<String> collection) {
		int i = 0;
		for (String building : collection) {
			if (i == this.currentBuilding) {
				return building;
			}
			i++;
		}
		return null;
	}
	
	public Building getCurrentBuilding () {
		return this.getCurrentBuilding(this.getCurrentGroup().buildings.values());
	}
	public Building getCurrentDefaultBuilding () {
		return this.getCurrentBuilding(this.getCurrentDefaultGroup().buildings.values());
	}
	public Building getCurrentOldBuilding () {
		return this.getCurrentBuilding(this.getCurrentOldGroup().buildings.values());
	}
	private Building getCurrentBuilding (Collection<Building> collection) {
		int i = 0;
		for (Building building : collection) {
			if (i == this.currentBuilding) {
				return building;
			}
			i++;
		}
		return null;
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		return this.value;
	}

	@Override
	public ConfigEntry setValue(Object value) {
		if (value instanceof BuildingConfigType) {
			this.value = (BuildingConfigType) ((BuildingConfigType)value).clone();
		}
		return super.setValue(value);
	}
}
