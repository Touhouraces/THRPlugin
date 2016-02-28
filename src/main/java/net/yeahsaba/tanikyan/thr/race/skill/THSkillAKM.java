package net.yeahsaba.tanikyan.thr.race.skill;

import java.util.List;

import net.yeahsaba.tanikyan.thr.THRPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class THSkillAKM extends JavaPlugin {
	private static String thrpre = THRPlugin.thrpre;
	//移動スキル系
	//吸血鬼カモフラージュ
	public static void kyuuketuki_vamp(Player pl, Plugin plugin){
		pl.sendMessage(thrpre + ChatColor.GRAY + "バンプカモフラージュを唱えた！！");
		pl.getWorld().playSound(pl.getLocation(), Sound.BAT_IDLE, 1.0F, 0.0F);
		pl.getWorld().playEffect(pl.getLocation(), Effect.SMOKE, 1, 1);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue casted = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
				pl.setMetadata("casting", casted);
				MetadataValue batman = new FixedMetadataValue(plugin, pl.getUniqueId());
				pl.setMetadata("batman", batman);
				pl.setGameMode(GameMode.SPECTATOR);
				pl.getWorld().playSound(pl.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 0.0F);
				pl.sendMessage(thrpre + ChatColor.RED + "あなたは蝙蝠になった！！");
				Entity bat = pl.getWorld().spawnEntity(pl.getEyeLocation(), EntityType.BAT);
				MetadataValue invincible = new FixedMetadataValue(plugin, pl.getUniqueId());
				bat.setMetadata("invincible", invincible);
				pl.setMetadata("using-magic", usingmagic);
			}
		}, 20L);
	}

	//攻撃スキル系
	//紅魔法
	public static void akuma_red_magic(Player pl, Plugin plugin){
		pl.sendMessage(thrpre + ChatColor.DARK_RED + "紅の魔法を唱えた！");
		pl.getLocation().getWorld().playSound(pl.getLocation(), Sound.WITHER_SPAWN, 1.0F, 2.0F);
		List<Entity> enemys = pl.getNearbyEntities(9.0D, 9.0D, 9.0D);
		for (Entity enemy : enemys) {
			if (((enemy instanceof LivingEntity)) && (enemy.isOnGround())){
				((LivingEntity)enemy).damage(5.0D);
				((LivingEntity)enemy).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 3));
				enemy.getWorld().playEffect(enemy.getLocation(), Effect.TILE_DUST, 12);
			}
		}
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}, 100L);
	}

	//鬼の埋め落とし
	public static void oni_kairiki(Player pl, Plugin plugin, PlayerInteractEntityEvent event, LivingEntity entity){
		entity.getWorld().playSound(event.getRightClicked().getLocation(), Sound.DONKEY_ANGRY, 1, -1);
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		final int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				entity.getVelocity().setY(-5);
			}
		},0,1L);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				plugin.getServer().getScheduler().cancelTask(task);
				Player pl = event.getPlayer();
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(thrpre + ChatColor.BLUE + "詠唱のクールダウンが終わりました");
			}
		}, 100L);
	}

	//吸血鬼の吸血
	public static void kyuuketuki_drain(Player pl, Plugin plugin, PlayerInteractEntityEvent event, LivingEntity entity){
		Entity target = event.getRightClicked();
		if (pl.getLocation().distanceSquared(target.getLocation()) >= 40.0D){
			pl.getWorld().playSound(pl.getLocation(), Sound.SPIDER_IDLE, 2.0F, 1.0F);
			pl.sendMessage(thrpre + ChatColor.BLUE + "しかし逃げられてしまった！！");
		}else{
			pl.sendMessage(thrpre + ChatColor.DARK_RED + "あなた吸血した！");
			target.getWorld().playSound(pl.getLocation(), Sound.SPIDER_DEATH, 2.0F, 1.0F);
			target.getWorld().playEffect(pl.getLocation(), Effect.TILE_BREAK, 1, 152);
			if (((LivingEntity)target).getHealth() - 30.0D >= 0.0D) {
				((LivingEntity)target).setHealth(((LivingEntity)target).getHealth() - 30.0D);
			}else{
				((LivingEntity)target).setHealth(0.0D);
			}
			if (pl.getHealth() > pl.getMaxHealth() - 30.0D){
				pl.setHealth(pl.getMaxHealth());
			}else{
				pl.setHealth(30.0D + pl.getHealth());
			}
		}
	}

	///パッシブ系
	//ダメージ系
	public static void akuma_dark_attack(Player pl, Plugin plugin, EntityDamageByEntityEvent event){
		if (pl.getLocation().getBlock().getLightLevel() < 8){
			event.setDamage(event.getDamage() + 1.0D);
			event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.TILE_BREAK, 152);
		}
	}

	public static void kyuuketuki_shadow_attack(Player pl, Plugin plugin, EntityDamageByEntityEvent event){
		if (pl.getLocation().getBlock().getLightLevel() < 4){
			event.setDamage(event.getDamage() + 1.0D);
			event.getDamager().getWorld().playSound(event.getEntity().getLocation(), Sound.BAT_HURT,1,-1);
		}
	}

	public static void oni_closed_attack(Player pl, Plugin plugin,EntityDamageByEntityEvent event){
		if (pl.getLocation().distanceSquared(event.getEntity().getLocation()) < 12){
			event.setDamage(event.getDamage() + 2.0D);
			event.getDamager().getWorld().playEffect(event.getEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
		}
	}

	//防御系
	public static void akuma_antiheat_body(Player pl, Plugin plugin,EntityDamageEvent event){
		if (event.getCause() == DamageCause.FIRE_TICK) event.setCancelled(true);
	}

	//防御系
	public static void kyuuketuki_antiallfire_body(Player pl, Plugin plugin,EntityDamageEvent event){
		if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.LAVA) event.setCancelled(true);
	}
}
