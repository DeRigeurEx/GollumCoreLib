package mods.gollum.core.tools.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;

public interface IBlockHelper {
	
	public BlockHelper getGollumHelper ();
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	public void registerIcons(IconRegister iconRegister);
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	public IBlockHelper setIcon (Icon icon);
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	public String getTextureKey ();
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 * @return 
	 */
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass);
	
}