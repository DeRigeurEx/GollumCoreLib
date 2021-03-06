package com.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.gollum.core.utils.math.Integer3d;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Building {
	
	public static final int ROTATED_0  = 0;
	public static final int ROTATED_90 = 1;
	public static final int ROTATED_180 = 2;
	public static final int ROTATED_270 = 3;
	public static final int ROTATED_360 = 4;
	
	public static class Unity3D implements Comparable {
		
		private Building building;
		private Integer3d position3d;
		public Unity unity;
		
		
		public Unity3D (Building building, Unity unity, int x, int y, int z) {
			this(building, unity, new Integer3d(x, y, z));
		}
		
		private Unity3D (Building building, Unity unity, Integer3d position3d) {
			this.building   = building;
			this.unity      = unity;
			this.position3d = position3d;
		}
		
		public int x(int rotate) {
			return (rotate == ROTATED_90 || rotate == ROTATED_270) ? building.maxX(rotate) - this.position3d.z - 1 : this.position3d.x;
		}
		public int y(int rotate) {
			return this.position3d.y;
		}
		public int z(int rotate) {
			return (rotate == ROTATED_90 || rotate == ROTATED_270) ? this.position3d.x : building.maxZ(rotate) - this.position3d.z - 1;
		}

		@Override
		public boolean equals (Object o) {
			return this.position3d.equals(((Unity3D)o).position3d);
		}
		
		@Override
		public int compareTo(Object o) {
			return this.position3d.compareTo(((Unity3D)o).position3d);
		}
		
	}
	
	
	
	/**
	 * Un element de lamatrice building
	 */
	public static class Unity {
		
		public static final int ORIENTATION_NONE   = 0;
		public static final int ORIENTATION_UP     = 1;
		public static final int ORIENTATION_DOWN   = 2;
		public static final int ORIENTATION_LEFT   = 3;
		public static final int ORIENTATION_RIGTH  = 4;
		public static final int ORIENTATION_TOP_HORIZONTAL    = 5;
		public static final int ORIENTATION_BOTTOM_HORIZONTAL = 6;
		public static final int ORIENTATION_TOP_VERTICAL      = 7;
		public static final int ORIENTATION_BOTTOM_VERTICAL   = 8;
		
		/**
		 * Contenu d'un objet (des Item uniquement pour le moment)
		 */
		static public class Content {
			
			public static final int TYPE_ITEM  = 0;
			public static final int TYPE_BLOCK = 1;
			
			public Item item = null;
			public int  min = 1;
			public int  max = 1;
			public int  metadata = -1;
			public int  type;
			
		}
		
		public Block block     = null;
		public int metadata    = 0;
		public int orientation = Unity.ORIENTATION_NONE;
		public boolean after   = false;
		public ArrayList<ArrayList<Content>> contents = new ArrayList();
		public HashMap<String, String> extra = new HashMap<String, String>();
		
	}
	
	public static class DimentionSpawnInfos {

		public int spawnRate = 0;
		public int spawnHeight = 0;
		public ArrayList<Block> blocksSpawn = new ArrayList<Block>();
		

		public DimentionSpawnInfos() {
		}
		
		public DimentionSpawnInfos(int spawnRate, int spawnHeight, ArrayList<Block> blocksSpawn) {
			this.spawnRate   = spawnRate;
			this.spawnHeight = spawnHeight;
			this.blocksSpawn = blocksSpawn;
		}
		
	}
	
	public static class SubBuilding {
		public int x = 0;
		public int y = 0;
		public int z = 0;
		public int orientation;
		public Building building = new Building();
		
		public void synMax(Building building) {
			this.building.maxX = building.maxX;
			this.building.maxY = building.maxY;
			this.building.maxZ = building.maxZ;
		}
	}
	public static class ListSubBuildings extends ArrayList<SubBuilding>{}
	public static class GroupSubBuildings extends ArrayList<ListSubBuildings>{
		
		public void add(SubBuilding subBuilding) {
			ListSubBuildings listSubBuildings = new ListSubBuildings();
			listSubBuildings.add(subBuilding);
			this.add (listSubBuildings);
		}
	}
	
	private int maxX;
	private int maxY;
	private int maxZ;
	
	public int height = 0;
	public String name = "random";
	public String modId = "";
	public HashMap<Integer, DimentionSpawnInfos> dimentionsInfos = new HashMap<Integer, DimentionSpawnInfos>();
	
	/**
	 * Liste des block de la constuction
	 */
	public TreeSet<Unity3D> unities = new TreeSet<Unity3D>();
	
	
	/**
	 * Liste des blocks posés aléatoirements
	 */
	private ArrayList<GroupSubBuildings> randomGroupSubBuildings = new ArrayList<GroupSubBuildings>();
	
	public Building(String name, String modId) {
		this.name = name;
		this.modId = modId;
	}

	public Building() {
	}
	
	public int maxX() { return maxX; }
	public int maxY() { return maxY; }
	public int maxZ() { return maxZ; }
	public int maxX(int rotate) { return (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) ? maxZ : maxX; }
	public int maxZ(int rotate) { return (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) ? maxX : maxZ; }

	public void setNull (int x, int y, int z) {
		
		maxX = Math.max(maxX, x+1);
		maxY = Math.max(maxY, y+1);
		maxZ = Math.max(maxZ, z+1);
		
		Unity3D unity3D = new Unity3D(this, null, x, y, z);
		if (this.unities.contains(unity3D)) {
			this.unities.remove(unity3D);
		}
	}
	
	public void set (int x, int y, int z, Unity unity) {
		
		this.setNull(x, y, z);
		
		if (unity != null) {
			this.unities.add (new Unity3D(this, unity, x, y, z));
		}
	}
	
	public void addRandomBuildings(GroupSubBuildings groupSubBuildings) {
		this.randomGroupSubBuildings.add(groupSubBuildings);
	}

	public void addBuilding(SubBuilding subBuilding) {
		GroupSubBuildings groupSubBuildings = new GroupSubBuildings();
		groupSubBuildings.add(subBuilding);
		this.addRandomBuildings (groupSubBuildings);
	}
	
	/**
	 * Renvoie la liste des groupes
	 * @return
	 */
	public ArrayList<GroupSubBuildings> getRandomGroupSubBuildings () {
		return this.randomGroupSubBuildings;
	}
	
}
