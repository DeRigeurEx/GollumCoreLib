package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.items.ItemBuilding;

public class ModItems {

	public static ItemBuilding itemBuilding;
	
	public static void init() {
		ModItems.itemBuilding = (ItemBuilding)new ItemBuilding("ItemBuilding").setCreativeTab(ModGollumCoreLib.tabBuildingStaff);
	}
}
