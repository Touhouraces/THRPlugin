package net.yeahsaba.tanikyan.thr.race.skill;

import net.yeahsaba.tanikyan.thr.THRPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class THSkillYUZ extends JavaPlugin {
	//召喚スキル系
	//狼召喚(妖獣系)
	public static void yuz_summon_wolf(Player pl, Plugin plugin, PlayerInteractEvent event){
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GRAY + "狼の群れを呼び出すウオオオーン");
		pl.getWorld().playSound(pl.getLocation(), Sound.WOLF_WHINE, 4.0F, -1.0F);
		pl.getWorld().playEffect(pl.getLocation(), Effect.BLAZE_SHOOT, 1, 1);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				int n = 0;
				while (n < 3){
					Entity wolf = pl.getWorld().spawnEntity(pl.getLocation(), EntityType.WOLF);
					MetadataValue tamedwolf = new FixedMetadataValue(plugin, Boolean.valueOf(true));
					wolf.setMetadata("tamedwolf", tamedwolf);
					MetadataValue wolfowner = new FixedMetadataValue(plugin, pl.getUniqueId());
					wolf.setMetadata("wolfowner", wolfowner);
					n++;
				}
				pl.getWorld().playSound(pl.getLocation(), Sound.WOLF_BARK, 1.0F, 1.0F);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.GOLD + "「ウオン」「ウオン」「ウオン」");
			}
		}, 40L);
	}

	//猫召喚(式)
	public static void siki_summon_oc(Player pl, Plugin plugin, PlayerInteractEvent event){
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GRAY + "愛くるしい猫を呼び出すニャア");
		pl.getWorld().playSound(pl.getLocation(), Sound.CAT_MEOW, 4.0F, -1.0F);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				int n = 0;
				while (n < 3){
					Entity cat = pl.getWorld().spawnEntity(pl.getLocation(), EntityType.OCELOT);
					MetadataValue tamedcat = new FixedMetadataValue(plugin, Boolean.valueOf(true));
					cat.setMetadata("tamedcat", tamedcat);
					MetadataValue catowner = new FixedMetadataValue(plugin, pl.getUniqueId());
					cat.setMetadata("catowner", catowner);
					n++;
				}
				pl.getWorld().playSound(pl.getLocation(), Sound.CAT_PURREOW, 1.0F, 1.0F);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.GOLD + "「ニャア」「ニャア」「ニャア」");
			}
		}, 40L);
	}

	//攻撃スキル系
	//霊力消費で強化
	public static void youzyu_gainenergy(Player pl, Plugin plugin, PlayerInteractEvent event){
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + "霊力で自身の能力増強を図った！");
		pl.getWorld().playSound(pl.getLocation(), Sound.BLAZE_BREATH, 1.0F, 1.0F);
		pl.getWorld().playEffect(pl.getLocation(), Effect.MOBSPAWNER_FLAMES, 1, 1);
		double ram = Math.random();
		pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + ram);
		if (ram < 0.1D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.GRAY + "失敗！");
		}else if (ram < 0.2D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + "移動の速度がさらに上がった！");
			pl.removePotionEffect(PotionEffectType.SPEED);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
		}else if (ram < 0.3D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.GREEN + "跳躍力が上がった！");
			pl.removePotionEffect(PotionEffectType.JUMP);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 0));
		}else if (ram < 0.4D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.GOLD + "振りの速さが上がった！");
			pl.removePotionEffect(PotionEffectType.FAST_DIGGING);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 250, 0));
		}else if (ram < 0.5D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.LIGHT_PURPLE + "軽い再生能力を得た！");
			pl.removePotionEffect(PotionEffectType.REGENERATION);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 0));
		}else if (ram < 0.6D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "マゾな精神を得た?の?");
			pl.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 250, 1));
		}else if (ram < 0.7D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "サゾな精神を得た！");
			pl.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			pl.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 250, 0));
		}else if (ram < 0.8D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.AQUA + "天空の力を得た！");
			if (pl.getWorld().isThundering()){
				pl.sendMessage(THRPlugin.thrpre + ChatColor.YELLOW + "天は雷光の如き力を授けて下さった！");
				pl.removePotionEffect(PotionEffectType.SPEED);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
				pl.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0));
				pl.removePotionEffect(PotionEffectType.FAST_DIGGING);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 200, 1));
			}else if (pl.getWorld().hasStorm()){
				pl.sendMessage(THRPlugin.thrpre + ChatColor.DARK_BLUE + "天は雨の尊い恵みを授けて下さった！");
				pl.removePotionEffect(PotionEffectType.JUMP);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 1));
				pl.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0));
				pl.removePotionEffect(PotionEffectType.REGENERATION);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
			}else{
				pl.sendMessage(THRPlugin.thrpre + ChatColor.GOLD + "天は晴天の輝く強さを授けて下さった！");
				pl.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 400, 0));
				pl.removePotionEffect(PotionEffectType.HEAL);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 0, 10));
				pl.removePotionEffect(PotionEffectType.NIGHT_VISION);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 0));
			}
		}else if (ram < 0.9D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.UNDERLINE + "時の力を得た！");
			if (pl.getWorld().getTime() < 14000L){
				pl.sendMessage(THRPlugin.thrpre + ChatColor.GOLD + "太陽のエナジーは貴方にすざましき再生力を与える！");
				pl.removePotionEffect(PotionEffectType.REGENERATION);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
			}else{
				pl.sendMessage(THRPlugin.thrpre + ChatColor.YELLOW + "月の煌きはあなたの心を狂わすであろう");
				pl.removePotionEffect(PotionEffectType.CONFUSION);
				pl.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 1));
			}
		}else if (ram < 1.0D){
			pl.sendMessage(THRPlugin.thrpre + ChatColor.GRAY + "失敗！");
		}else{
			pl.sendMessage(THRPlugin.thrpre + ChatColor.GRAY + "失敗！");
		}
		Object casting = new FixedMetadataValue(plugin, Boolean.valueOf(false));
		pl.setMetadata("casting", (MetadataValue)casting);
		MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(true));
		pl.setMetadata("using-magic", usingmagic);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				Player pl = event.getPlayer();
				MetadataValue usingmagic = new FixedMetadataValue(plugin, Boolean.valueOf(false));
				pl.setMetadata("using-magic", usingmagic);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "詠唱のクールダウンが解けました");
			}
		}, 300L);
	}

	////アクブスキル系
	public static void ninngyo_swimming(Player pl, final Plugin plugin, final PlayerMoveEvent event, int boost){
		///移動スキル系
		if (pl.getLocation().getBlock().getType() == Material.WATER || pl.getLocation().getBlock().getType() == Material.STATIONARY_WATER){
			if (boost == 1){
				pl.setVelocity(pl.getLocation().getDirection().multiply(2.0D));
			}else{
				pl.setVelocity(pl.getLocation().getDirection().multiply(0.4D));
			}
		}
	}
}
