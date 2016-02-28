package net.yeahsaba.tanikyan.thr.race.skill;

import java.util.List;

import net.yeahsaba.tanikyan.thr.THRPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class THSkillNNG extends JavaPlugin {
	///移動スキル系
	//仙人の壁抜け
	public static void sennnin_passthough(Player pl, Plugin plugin){
		float pitch = pl.getLocation().getPitch();
		float yaw = pl.getLocation().getYaw();
		Location warploc = new Location (pl.getWorld(),pl.getLocation().getX() + pl.getLocation().getDirection().getX() * 2,pl.getLocation().getY() + pl.getLocation().getDirection().getY() * 2,pl.getLocation().getZ() + pl.getLocation().getDirection().getZ() * 2);
		if (pl.getWorld().getBlockAt(warploc).getType() != Material.AIR){
			pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_HIT, 2, 0);
		}else{
			pl.getWorld().playSound(pl.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 1);
			pl.getWorld().playEffect(pl.getLocation(), Effect.COLOURED_DUST, 1, 5);
			warploc.setPitch(pitch);
			warploc.setYaw(yaw);
			pl.teleport(warploc);
			plugin.getConfig().set("user." + pl.getUniqueId() + ".spilit", plugin.getConfig().getDouble("user." + pl.getUniqueId() + ".spilit") - 20);
			plugin.saveConfig();
		}
	}

	//攻撃スキル系
	//魔法
	//土魔法
	public static void magic_dirt(Player pl,Plugin plugin){
		pl.sendMessage(THRPlugin.thrpre + ChatColor.YELLOW + "土の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.PISTON_EXTEND, 1.0F, -1.0F);
		List<Entity> enemys = pl.getNearbyEntities(12.0D, 12.0D, 12.0D);
		for (Entity enemy : enemys) {
			if (((enemy instanceof LivingEntity)) && (enemy.isOnGround())){
				((LivingEntity)enemy).damage(25.0D);
				enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.HORSE_HIT, 1.0F, 0.0F);
			}
		}
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}, 60L);
	}

	//風魔法
	public static void magic_wind(Player pl, Plugin plugin){
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + "風の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
		pl.setVelocity(pl.getVelocity().add(new Vector(0.5D, 3.0D, 0.5D)));
		pl.setFallDistance(-40.0F);
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}, 60L);
	}

	//火魔法
	public static void magic_fire(Player pl, Plugin plugin, PlayerInteractEvent e) {
		pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "火の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.FIRE, 1, 0);
		Location location =pl.getEyeLocation();
		float pitch = location.getPitch() / 180.0F * 3.1415927F;
		float yaw=location.getYaw() / 180.0F * 3.1415927F ;
		double motX=-Math.sin(yaw) * Math.cos(pitch);
		double motZ=Math.cos(yaw) * Math.cos(pitch);
		double motY=-Math.sin(pitch);
		Vector velocity=new Vector(motX,motY,motZ).multiply(2D);
		@SuppressWarnings("deprecation")
		Snowball snowball=pl.throwSnowball();
		MetadataValue shooter = new FixedMetadataValue(plugin, pl.getUniqueId().toString()) ;
		MetadataValue fireeffect = new FixedMetadataValue(plugin, 30D) ;
		snowball.setMetadata("fireffect", fireeffect);
		snowball.setMetadata("mazyo-fireball", shooter);
		snowball.setVelocity(velocity);
		snowball.setFireTicks(300);
		MetadataValue usingmagic = new FixedMetadataValue(plugin, true) ;
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, false) ;
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		},80L);
	}

	//水魔法
	public static void magic_water(Player pl, Plugin plugin, PlayerInteractEvent e) {
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + "水の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.MAGMACUBE_JUMP, 1, 0);
		List<Entity> enemys=pl.getNearbyEntities(8D, 8D, 8D);
		enemys.add(pl);
		for (Entity enemy : enemys){
			if (enemy instanceof LivingEntity && enemy.isDead() == false){
				if (((LivingEntity) enemy).getHealth() + 12D > ((LivingEntity) enemy).getMaxHealth()){
					((LivingEntity) enemy).setHealth(((LivingEntity) enemy).getMaxHealth());
					enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.LEVEL_UP, 1, 2);
				}else{
					((LivingEntity) enemy).setHealth(((LivingEntity) enemy).getHealth() + 12D);
					enemy.getLocation().getWorld().playSound(enemy.getLocation(), Sound.LEVEL_UP, 1, 2);
				}
			}
		}
		MetadataValue usingmagic = new FixedMetadataValue(plugin, true) ;
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, false) ;
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}
		, 180L);
	}

	//雷魔法
	public static void magic_thunder(Player pl, Plugin plugin, PlayerInteractEvent e) {
		pl.sendMessage(THRPlugin.thrpre + ChatColor.DARK_PURPLE + "雷の魔法を唱えた！");
		Entity lightning1 = pl.getWorld().spawnEntity(e.getClickedBlock().getLocation().add(4D,0,0), EntityType.LIGHTNING);
		Entity lightning2 = pl.getWorld().spawnEntity(e.getClickedBlock().getLocation().add(-4D,0,0), EntityType.LIGHTNING);
		Entity lightning3 = pl.getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0,0,4D), EntityType.LIGHTNING);
		Entity lightning4 = pl.getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0,0,-4D), EntityType.LIGHTNING);
		MetadataValue lightningeffect = new FixedMetadataValue(plugin, 20D) ;
		MetadataValue shooter = new FixedMetadataValue(plugin, pl.getUniqueId().toString()) ;
		lightning1.setMetadata("lightningeffect", lightningeffect);
		lightning2.setMetadata("lightningeffect", lightningeffect);
		lightning3.setMetadata("lightningeffect", lightningeffect);
		lightning4.setMetadata("lightningeffect", lightningeffect);
		lightning1.setMetadata("shooter", shooter);
		lightning2.setMetadata("shooter", shooter);
		lightning3.setMetadata("shooter", shooter);
		lightning4.setMetadata("shooter", shooter);
		MetadataValue usingmagic = new FixedMetadataValue(plugin, true) ;
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, false) ;
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		},180L);
	}

	//治癒魔法
	public static void magic_heal(Player pl, Plugin plugin) {
		MetadataValue casting = new FixedMetadataValue(plugin, true) ;
		pl.setMetadata("casting", casting);
		pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "棒を構えた！");
		pl.getWorld().playSound(pl.getLocation(), Sound.ANVIL_LAND, 1, 1);
		pl.getWorld().playEffect(pl.getLocation(), Effect.WITCH_MAGIC, 1, 1);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				pl.sendMessage(THRPlugin.thrpre + ChatColor.YELLOW + "自己治癒を使った！");
				pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.ORB_PICKUP, 1, 1);
				if (pl.getHealth() + 8D > pl.getMaxHealth()){
					pl.setHealth(pl.getMaxHealth());
				}else{
					pl.setHealth(pl.getHealth() + 8D);
				}
				pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.LEVEL_UP, 1, 2);
				MetadataValue usingmagic = new FixedMetadataValue(plugin, true) ;
				pl.setMetadata("using-magic", usingmagic);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
					  MetadataValue usingmagic = new FixedMetadataValue(plugin, false) ;
					  pl.setMetadata("using-magic", usingmagic);
					  pl.sendMessage(THRPlugin.thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
					}
				}, 20L);
				MetadataValue casted = new FixedMetadataValue(plugin, false) ;
				pl.setMetadata("casting", casted);
			}
		});
	}

	////パッシブスキル系
	///エンティティ専用
	public static void sibito_deadattack(Player pl, Plugin plugin, EntityDamageByEntityEvent event){
		if (pl.getHealth() <= 20D){
			event.setDamage(event.getDamage() + 3D);
			event.getDamager().getWorld().playSound(pl.getLocation(), Sound.ZOMBIE_PIG_HURT, 1, 1);
			event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.TILE_BREAK, 49);
		}
	}

	public static void gennzinnsin_luckyattack(Player pl, final Plugin plugin, EntityDamageByEntityEvent event){
		if (Math.random() > 0.7 && plugin.getConfig().getInt("user." + pl.getUniqueId() + ".spilit") >= 5D){
			plugin.getConfig().set(plugin.getConfig().getString("user." + pl.getUniqueId() + ".spilit"),plugin.getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 5D);
			event.setDamage(event.getDamage() + 5D);
			pl.getWorld().playSound(pl.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
		}
	}

	///エンティティ・ブロック兼用
	public static void houraizin_reverselife_Entity(Player pl, Plugin plugin, EntityDamageByEntityEvent event){
		double reverse = Math.random();
		if (event.getDamage() >= pl.getHealth() && reverse > 0.6){
			plugin.getConfig().set(plugin.getConfig().getString("user." + pl.getUniqueId() + ".spilit"),plugin.getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 30D);
			pl.setHealth(pl.getMaxHealth());
			pl.sendMessage(THRPlugin.thrpre + ChatColor.AQUA + "貴方は不死の力を使い蘇った！！");
			pl.getWorld().playSound(pl.getLocation(), Sound.BLAZE_BREATH, 1, -1);
			event.setDamage(0D);
		}
	}

	public static void houraizin_reverselife_block(Player pl, Plugin plugin, EntityDamageEvent event){
		double reverse = Math.random();
		if (event.getDamage() >= pl.getHealth() && reverse > 0.6){
			plugin.getConfig().set(plugin.getConfig().getString("user." + pl.getUniqueId() + ".spilit"),plugin.getConfig().getInt("user." + pl.getUniqueId() + ".spilit") - 30D);
			pl.setHealth(pl.getMaxHealth());
			pl.sendMessage(THRPlugin.thrpre + ChatColor.AQUA + "貴方は不死の力を使い蘇った！！");
			pl.getWorld().playSound(pl.getLocation(), Sound.BLAZE_BREATH, 1, -1);
			event.setDamage(0D);
		}
	}
}
