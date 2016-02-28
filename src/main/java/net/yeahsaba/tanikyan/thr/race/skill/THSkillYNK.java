package net.yeahsaba.tanikyan.thr.race.skill;

import net.yeahsaba.tanikyan.thr.THRPlugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class THSkillYNK extends JavaPlugin {
	//攻撃スキル系
	///パッシブ系
	//ダメージ系
	public static void kami_faith_attack(Player pl, Plugin plugin, EntityDamageByEntityEvent event,int boost, FileConfiguration conf){
		if (boost > 0 && boost < 15){
			if (event.getDamage() > 0.0D && event.getDamage() <= 4.0D){
				event.setDamage(event.getDamage() + 1.0D);
			}else if (event.getDamage() > 4.0D && event.getDamage() <= 8.0D){
				event.setDamage(event.getDamage() + 2.0D);
			}else if (event.getDamage() > 8.0D && event.getDamage() <= 12.0D){
				event.setDamage(event.getDamage() + 3.0D);
			}else if (event.getDamage() > 12.0D){
				event.setDamage(event.getDamage() + 4.0D);
			}
		}else if (boost >= 15){
			event.setDamage(event.getDamage() * 1.5D);
			conf.set("user." + pl.getUniqueId() + ".spilit", conf.getDouble("user." + pl.getUniqueId() + ".spilit") - 4);
		}
	}

	//防御系
	public static void kami_faith_defence(Player pl, Plugin plugin, EntityDamageByEntityEvent event, int boost, FileConfiguration conf){
		if (boost > 0 && boost < 15){
			if (event.getDamage() > 2.0D && event.getDamage() <= 6.0D){
				event.setDamage(event.getDamage() - 1.0D);
			}else if (event.getDamage() > 6.0D && event.getDamage() <= 10.0D){
				event.setDamage(event.getDamage() - 2.0D);
			}else if (event.getDamage() > 10.0D && event.getDamage() <= 14.0D){
				event.setDamage(event.getDamage() - 3.0D);
			}else if (event.getDamage() > 14.0D){
				event.setDamage(event.getDamage() - 4.0D);
			}
		}else if (boost >= 15){
			event.setDamage(event.getDamage() / 1.5D);
			conf.set("user." + pl.getUniqueId() + ".spilit", conf.getDouble("user." + pl.getUniqueId() + ".spilit") - 2);
		}
	}

	public static void houzyousin_feed(Player pl, Plugin plugin, EntityDamageEvent event){
		if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION) event.setCancelled(true);
	}

	public static void houzyousin_potato(Player pl, Plugin plugin, EntityDamageByEntityEvent event,int boost){
		if ((Math.random() >= 0.8D) && ((event.getEntity() instanceof Player)) && boost > 0.0D){
			((Player)event.getEntity()).setFoodLevel(((Player)event.getEntity()).getFoodLevel() - 1);
			event.getEntity().sendMessage(THRPlugin.thrpre + ChatColor.GOLD + pl.getName() + "はおいしい芋を見せつけてきた！！");
		}
	}

	public static void yakusin_darkside(Player pl, Plugin plugin, EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Player && event.getDamage() >= pl.getHealth()){
			Player killplayer = (Player) event.getDamager();
			if (!killplayer.isDead()){
				killplayer.sendMessage(THRPlugin.thrpre + ChatColor.DARK_RED + "あなた厄神の祟りを受けた！！！");
				killplayer.damage(50.0D);
			}
		}
	}
}
