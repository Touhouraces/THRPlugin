package net.yeahsaba.tanikyan.thr.race;

import net.yeahsaba.tanikyan.thr.THRPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TouhouRaces extends JavaPlugin implements Listener {

	////冗長防止
	public static boolean magic_iscastable(Player pl, int mana,String string){
		if (((MetadataValue) pl.getMetadata("casting").get(0)).asBoolean()) {
			pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "他の魔法を詠唱中です");
			return false;
		} else if (((MetadataValue) pl.getMetadata("using-magic").get(0)).asBoolean()) {
			pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "他の魔法を使用中です");
			return false;
		} else {
			if (THRPlugin.conf.getDouble("user." + pl.getUniqueId() + ".spilit") > mana){
				pl.sendMessage(THRPlugin.thrpre + ChatColor.LIGHT_PURPLE + string);
				return true;
			}else{
				pl.sendMessage(THRPlugin.thrpre + ChatColor.RED + "霊力が不足しています");
				return false;
			}
		}
    }

	//リスポーン体力調整グローバル (THSkillGlobalより移動)
	public static void global_respawnhealth(Player pl, Plugin plugin, PlayerRespawnEvent event){
		pl.setMaxHealth(100D);
	}

	//霊力調整グローバル (THSkillGlobalより移動)
	public static void global_charge_mana(Player pl, Plugin plugin, String pluginpre, PlayerInteractEvent event){
		Material dust_is_ok = pl.getItemInHand().getType() ;
		if (pl.getMetadata("spilituse").get(0).asDouble() != 0){
			 MetadataValue spilituse = new FixedMetadataValue(THRPlugin.plugin0, 0) ;
			 pl.setMetadata("spilituse", spilituse);
			 pl.sendMessage(THRPlugin.thrpre + ChatColor.WHITE + "霊力ノーマル");
		}else{
			if (dust_is_ok == Material.SUGAR){
				MetadataValue spilituse = new FixedMetadataValue(THRPlugin.plugin0, 5) ;
				pl.setMetadata("spilituse", spilituse);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.AQUA + "霊力消費小");
			}else if (dust_is_ok == Material.SULPHUR){
				MetadataValue spilituse = new FixedMetadataValue(THRPlugin.plugin0, 15) ;
				pl.setMetadata("spilituse", spilituse);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.DARK_GRAY + "霊力消費大");
			}else if (dust_is_ok == Material.GLOWSTONE_DUST){
				MetadataValue spilituse = new FixedMetadataValue(THRPlugin.plugin0, -10) ;
				pl.setMetadata("spilituse", spilituse);
				pl.sendMessage(THRPlugin.thrpre + ChatColor.YELLOW + "霊力回復中");
			}
		}
	}

	public static void NoDamageTick(Plugin plugin, Player p, int wait, int tick){
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				p.setNoDamageTicks(tick);
			}
		}, wait);
	}
}
