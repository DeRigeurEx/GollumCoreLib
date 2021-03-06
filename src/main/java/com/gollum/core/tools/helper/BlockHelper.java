package com.gollum.core.tools.helper;

import java.util.Random;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.registry.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockHelper implements IBlockHelper {
	
	// Pour chaque element natural. Utilise le fonctionnement naturel mais pas des helpers
	// Une sorte de config
	// Par defaut le helper vas enregistrer le block, charger des texture perso ...
	public boolean vanillaRegister      = false;
	public boolean vanillaTexture       = false;
	public boolean vanillaPicked        = false;
	public boolean vanillaDamageDropped = false;
	
	protected GollumMod mod;
	protected Block parent;
	protected String registerName;
	protected Class<? extends ItemBlock> itemBlockClass = ItemBlock.class;
	
	public BlockHelper (Block parent, String registerName) {
		this.parent       = parent;
		this.registerName = registerName;
		this.mod          = ModContext.instance().getCurrent();
		
		BlockRegistry.instance().add((IBlockHelper) this.parent);
		this.parent.setBlockName(this.registerName);
		
	}
	
	@Override
	public BlockHelper getGollumHelper() {
		return this;
	}
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 */
	@Override
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass) {
		this.itemBlockClass = itemClass;
		return this.parent;
	}
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register () {
		
		if(vanillaRegister) return;
		GameRegistry.registerBlock (this.parent , this.itemBlockClass , this.getRegisterName ());
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return registerName;
	}
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	@Override
	public Item getBlockItem () {
		return Item.getItemFromBlock(this.parent);
	}
	
	/**
	 * Libère les items de l'inventory
	 */
	@Override
	public void breakBlockInventory(World world, int x, int y, int z, Block oldBlock) {
		
		Random random = new Random();		
		TileEntity te = world.getTileEntity(x, y, z);
		
		if (te != null && te instanceof IInventory) {
			IInventory inventory = (IInventory)te;
			
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack itemStack = inventory.getStackInSlot(i);
				
				if (itemStack != null) {
					float f  = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityItem;
					
					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; world.spawnEntityInWorld(entityItem)) {
						int k1 = random.nextInt(21) + 10;
						
						if (k1 > itemStack.stackSize) {
							k1 = itemStack.stackSize;
						}
						
						itemStack.stackSize -= k1;
						entityItem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));
						float f3 = 0.05F;
						entityItem.motionX = (double) ((float) random.nextGaussian() * f3);
						entityItem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
						entityItem.motionZ = (double) ((float) random.nextGaussian() * f3);
						
						if (itemStack.hasTagCompound()) {
							entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
						}
					}
				}
			}
			
			world.func_147453_f(x, y, z, oldBlock);
		}
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	@Override
	public String getTextureKey () {
		return ((IBlockHelper)this.parent).getRegisterName().toLowerCase();
	}
	
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si useTextureKey est à false
	*
	* @param iconRegister
	* @param key
	* @return
	*/
	public IIcon loadTexture(IIconRegister iconRegister) {
		return this.loadTexture(iconRegister, "");
	}
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si useTextureKey est à false
	*
	* @param iconRegister
	* @param key
	* @return
	*/
	public IIcon loadTexture(IIconRegister iconRegister, String sufixe) {
		return this.loadTexture(iconRegister, sufixe, false);
	}
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si dontUseTextureKey est à false
	* 
	* @param iconRegister
	* @param key
	* @return
	*/
	public IIcon loadTexture(IIconRegister iconRegister, String sufixe, boolean dontUseTextureKey) {
		
		String key = (dontUseTextureKey) ?  sufixe : (((IBlockHelper)this.parent).getTextureKey ()+sufixe);
		String texture = this.mod.getModId().toLowerCase() + ":" + key;
		
		ModGollumCoreLib.log.debug ("Register icon " + texture + "\"");
		return iconRegister.registerIcon(texture);
	}
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		((IBlockHelper)this.parent).setIcon (this.loadTexture(iconRegister));
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public IBlockHelper setIcon (IIcon icon) {
		ModGollumCoreLib.log.warning("setIcon don't be call by helper. It's stub");
		((IBlockHelper)this.parent).setIcon(icon);
		return this;
	}
}