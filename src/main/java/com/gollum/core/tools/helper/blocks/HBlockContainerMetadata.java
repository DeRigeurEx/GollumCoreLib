package com.gollum.core.tools.helper.blocks;

import java.util.List;
import java.util.TreeSet;

import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.BlockMetadataHelper;
import com.gollum.core.tools.helper.IBlockMetadataHelper;
import com.gollum.core.tools.helper.items.HItemBlockMetadata;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class HBlockContainerMetadata extends HBlockContainer implements IBlockMetadataHelper {

	private BlockMetadataHelper helperMetadata;

	public HBlockContainerMetadata(String registerName, Material material) {
		super(registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockContainerMetadata(String registerName, Material material, int listSubBlock[]) {
		super(registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName, listSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockContainerMetadata(String registerName, Material material, int numberSubBlock) {
		super(registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName, numberSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	@Override
	public BlockMetadataHelper getGollumHelperMetadata () {
		return helperMetadata;
	}
	
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		this.helperMetadata.getSubBlocks(item, ctabs, list);
	}
	
	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int damage) {
		return (helper.vanillaDamageDropped) ? super.damageDropped(damage) : this.helperMetadata.damageDropped (damage);
	}
	
	/**
	 * Called when a user uses the creative pick block button on this block
	 * 
	 * @param target
	 *            The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing
	 *         should be added.
	 */
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return (helper.vanillaPicked) ? super.getPickBlock(target, world, x, y, z) : helperMetadata.getPickBlock(super.getPickBlock(null, world, x, y, z), world, x, y, z);
	}

	/**
	 * Liste des metadata enabled pour le subtype
	 */
	public TreeSet<Integer> listSubEnabled () {
		return this.helperMetadata.listSubEnabled();
	}
	
	public int getEnabledMetadata (int dammage) {
		return this.helperMetadata.getEnabledMetadata(dammage);
	}

	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		if (helper.vanillaTexture) super.registerBlockIcons(iconRegister); else helperMetadata.registerBlockIcons(iconRegister);
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return (helper.vanillaTexture) ? super.getIcon(side, metadata) : this.helperMetadata.getIcon(side, metadata);
	}
}
