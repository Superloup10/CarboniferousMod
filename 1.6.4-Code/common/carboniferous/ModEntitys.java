package carboniferous;

import carboniferous.creativetab.CreativeTabCarboniferous;
import carboniferous.entity.*;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntitys {

	public static void init() {
		//Normal Creatures
		registerMob(EntityDragonfly.class, "carboniferous.dragonfly", EntityRegistry.findGlobalUniqueEntityId(), 0x666600, 0x003300);
		registerMob(EntityBrachiopod.class, "carboniferous.brachiopod", EntityRegistry.findGlobalUniqueEntityId(), 0xCCCC66, 0x999966);
		registerMob(EntityAmmonite.class, "carboniferous.ammonite", EntityRegistry.findGlobalUniqueEntityId(), 0xFFFFCC, 0xCC0000);
		registerMob(EntityDendrerpeton.class, "carboniferous.dendrerpeton", EntityRegistry.findGlobalUniqueEntityId(), 13056, 52224);
		registerMob(EntityMesothelae.class, "carboniferous.mesothelae", EntityRegistry.findGlobalUniqueEntityId(), 1447446, 10489616);
		registerMob(EntityOrthacanthus.class, "carboniferous.orthacanthus", EntityRegistry.findGlobalUniqueEntityId(), 4996656, 12369084);
		registerMob(EntityCrassigyrinus.class, "carboniferous.crassigyrinus", EntityRegistry.findGlobalUniqueEntityId(), 13056, 4996656);
		registerMob(EntityCrab.class, "carboniferous.crab", EntityRegistry.findGlobalUniqueEntityId(), 0xFF6600, 0xCC3300);
		registerMob(EntityDimetrodon.class, "carboniferous.dimetrodon", EntityRegistry.findGlobalUniqueEntityId(), 0x669933, 0xCC9900);
		//Boss-like Creatures
		registerMob(EntityGiantMeganeura.class, "carboniferous.giantMeganeura", EntityRegistry.findGlobalUniqueEntityId(), 0x00CC66, 0xFF3333);
		//Entitys
		//registerEntity(EntitySpear.class, "carboniferous.spear", EntityRegistry.findGlobalUniqueEntityId());
		
		//Old Mod
		/**
		 * 	this.registerCreature(EntityHadrosaur.class, "Hadro", EntityRegistry.findGlobalUniqueEntityId(), 0x999900, 0x996600);
    	this.registerCreature(EntityDragonFly.class, "Drago", EntityRegistry.findGlobalUniqueEntityId(), 0x666600, 0x003300);
    	this.registerCreature(EntityCrab.class, "Crab", EntityRegistry.findGlobalUniqueEntityId(), 0xFF6600, 0xCC3300);
    	this.registerCreature(EntityAmmonite.class, "Ammon", EntityRegistry.findGlobalUniqueEntityId(), 0xFFFFCC, 0xCC0000);
    	this.registerCreature(EntityCeratosaurus.class, "Cerat", EntityRegistry.findGlobalUniqueEntityId(), 0x339900, 0x003300);
    	this.registerCreature(EntityVelociraptor.class, "Veloc", EntityRegistry.findGlobalUniqueEntityId(), 0x003300, 0x990000);
    	this.registerCreature(EntityDodo.class, "Dodo", EntityRegistry.findGlobalUniqueEntityId(), 0x663300, 0xFFFFFF);
    	this.registerCreature(EntityPachycepalosaurus.class, "Pachy", EntityRegistry.findGlobalUniqueEntityId(), 0xFF9900, 0x000000);
    	this.registerCreature(EntityGallinimus.class, "Galli", EntityRegistry.findGlobalUniqueEntityId(), 0x663300, 0x996600);
    	this.registerCreature(EntityEdaphosaurus.class, "Edaph", EntityRegistry.findGlobalUniqueEntityId(), 0x336600, 0x660000);
    	this.registerCreature(EntityAnkylosaurus.class, "Ankyl", EntityRegistry.findGlobalUniqueEntityId(), 330000, 0x663300);
    	this.registerCreature(EntityIchthyosaurus.class, "Ichth", EntityRegistry.findGlobalUniqueEntityId(), 0xFFFFFF, 0x33CCCC);
    	this.registerCreature(EntityTarSlime.class, "TarSl", EntityRegistry.findGlobalUniqueEntityId(), 1447446, 0);
    	//this.registerCreature(EntityCaveman.class, "Caveman", EntityRegistry.findGlobalUniqueEntityId(), 17446, 2);
		 */
	}
	
	public static void registerEntity(Class entityClass, String saveName, int id) {
	    EntityRegistry.registerGlobalEntityID(entityClass, saveName, id);
	    EntityRegistry.registerModEntity(entityClass, saveName, id, CarboniferousMod.instance, 120, 1, true);
	}
	
	public static void registerMob(Class entityClass, String saveName, int id, int backgroundEggColour, int foregroundEggColour) {
	    EntityRegistry.registerGlobalEntityID(entityClass, saveName, id, backgroundEggColour, foregroundEggColour);
	    EntityRegistry.registerModEntity(entityClass, saveName, id, CarboniferousMod.instance, 120, 1, true);
	    CreativeTabCarboniferous.spawnersList.add(id);
	}
}
