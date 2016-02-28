package net.yeahsaba.tanikyan.thr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.yeahsaba.tanikyan.thr.Listener.EventListener;
import net.yeahsaba.tanikyan.thr.Listener.SkillListener;
import net.yeahsaba.tanikyan.thr.race.schedule.THSchedule;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class THRPlugin extends JavaPlugin implements Listener {
	public static Logger logger = Logger.getLogger("Minecraft");
	public static THRPlugin plugin;
	public static Plugin plugin0;
	public static String thrpre = ChatColor.WHITE + "[" + ChatColor.RED + "THR" + ChatColor.WHITE + "]";
	public static PluginDescriptionFile pdfFile;
	private static File pluginDir = new File("plugins", "THRPlugin");
	public static File configfile = new File(pluginDir, "config.yml");
	public static FileConfiguration conf = YamlConfiguration.loadConfiguration(configfile);
	public static boolean crackshot_hook = false;
	public static boolean nametagedit_hook = false;
	public static boolean scoreboardapi_hook = false;
	public static boolean barapi_hook;

	public void onDisable(){
		logger.info("[THR] Plugin Successfully Disabled!");
		SaveThConfig();
	}

	public void onEnable(){
		//基本設定
		pdfFile = this.getDescription();
		logger.info("[THR]" + pdfFile.getVersion() + "は正しく起動しました");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		saveDefaultConfig();
		registerEventListener();
		plugin0 = this;
		THSchedule schedule = new THSchedule();
		String thpre0 = THRPlugin.thrpre;
		schedule.run1(plugin0,thpre0);
		schedule.run2(plugin0,thpre0);
		schedule.run3(plugin0,thpre0);
		//フック設定
		if (this.getConfig().getBoolean("enable-CrackShot-shooter-hook")){
			if (Bukkit.getPluginManager().getPlugin("CrackShot") != null){
				crackshot_hook = true;
				logger.info(thrpre + pdfFile.getVersion() + "は正しくCrackShotと連携しました");
			}
		}
		if (this.getConfig().getBoolean("enable-NametagEdit-tab-hook")){
			if (Bukkit.getPluginManager().getPlugin("NametagEdit") != null){
				nametagedit_hook = true;
				logger.info(thrpre + pdfFile.getVersion() + "は正しくNametagEditと連携しました");
			}
		}
		if (this.getConfig().getBoolean("enable-ScoreboardAPI-listboard-hook")){
			if (Bukkit.getPluginManager().getPlugin("ScoreboardAPI") != null){
				scoreboardapi_hook = true;
				logger.info(thrpre + pdfFile.getVersion() + "は正しくScoreboardAPIと連携しました");
			}
		}
		if (this.getConfig().getBoolean("enable-BarAPI-manabar-hook")){
			if (Bukkit.getPluginManager().getPlugin("BarAPI") != null){
				barapi_hook = true;
				logger.info(thrpre + pdfFile.getVersion() + "は正しくBarAPIと連携しました");
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args){
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("touhouraces")){
				if(args.length == 0){
					p.sendMessage(thrpre + "Version " + pdfFile.getVersion() + ". Made by:" + pdfFile.getAuthors().toString());
					return true;
				}else {
					if(args[0].equalsIgnoreCase("help")){
						if(p.hasPermission("thr.help") || p.hasPermission("thr.user")){
							sender.sendMessage(thrpre + "§6可能なプラグイン一覧");
							sender.sendMessage(thrpre + "§btouhouraces/thr : バージョン説明");
							sender.sendMessage(thrpre + "thr info : playernameの種族の情報を表示する");
							sender.sendMessage(thrpre + "thr evolinfo [内部種族名] : 種族の情報を表示する");
							sender.sendMessage(thrpre + "thr evollist : playernameの進化できる種族のリストを表示する");
							sender.sendMessage(thrpre + "thr evolchange [内部種族名] : 種族の進化を試みる");
							sender.sendMessage(thrpre + "thr mana : 現在マナ確認");
							sender.sendMessage(thrpre + "thr racelist : オンラインでいるプレイヤーの種族の統計をとる");
							sender.sendMessage(thrpre + "thr toggleskill : 行動系のスキルの発動をトグルする");
							if(p.hasPermission("thr.help")){
								sender.sendMessage(thrpre + "thr heal-mana [num] : マナをnum分回復する");
								sender.sendMessage(thrpre + "thr setpoint [num] : 自分のポイント（使い方は任意）を設定する");
								sender.sendMessage(thrpre + "thr addpoint [num] : 自分のポイント（使い方は任意）を追加する");
								sender.sendMessage(thrpre + "thr setpoint [playername] [num] : playernameのポイント（使い方は任意）を設定する");
								sender.sendMessage(thrpre + "thr addpoint [playername] [num] : playernameのポイント（使い方は任意）を追加する");
								sender.sendMessage(thrpre + "thr steppoint [max] : ポイント（使い方は任意）をmaxを上限として1上昇する");
								sender.sendMessage(thrpre + "thr steppoint [playername] [max] : playernameのポイント（使い方は任意）をmaxを上限として1上昇する");
								sender.sendMessage(thrpre + "thr setrace  [内部種族名] : 自分の種族を種族名（内部名）に変更する");
								sender.sendMessage(thrpre + "thr setrace [playername] [内部種族名] : playernameの種族を種族名（内部名）に変更する");
								sender.sendMessage(thrpre + "thr reload : リロード");
							}
							return true;
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("mana")){
						if(p.hasPermission("thr.user") || p.hasPermission("thr.checkmana")){
							  p.sendMessage(thrpre + "§a霊力：§f" + conf.getDouble(new StringBuilder("user.").append(p.getUniqueId()).append(".spilit").toString()));
							  return true;
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("heal-mana")){
						if(p.hasPermission("thr.healmana")){
							  conf.set("user." + p.getUniqueId() + ".spilit", Double.valueOf(conf.getDouble("user." + p.getUniqueId() + ".spilit") + Integer.parseInt(args[1])));
							  p.sendMessage(thrpre + ChatColor.GREEN + "霊力：" + ChatColor.LIGHT_PURPLE + conf.getDouble(new StringBuilder("user.").append(p.getUniqueId()).append(".spilit").toString()));
							  return true;
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("racelist")){
						if(p.hasPermission("thr.racelist") || p.hasPermission("thr.user")){
							OfflinePlayer[] ppl = Bukkit.getOfflinePlayers();
							p.sendMessage(thrpre + "§a オンライン中の種族リスト.");
							int pl = 0;
							while (pl < ppl.length){
								if (ppl[pl].isOnline()) {
									p.sendMessage(ChatColor.GREEN + "+" + conf.getString(new StringBuilder("user.").append(ppl[pl].getUniqueId()).append(".name").toString()) + ":" + conf.getString(new StringBuilder("user.").append(ppl[pl].getUniqueId()).append(".race").toString()) + "(" + conf.getString(new StringBuilder("user.").append(ppl[pl].getUniqueId()).append(".point").toString()) + ")");
								}
								pl++;
							}
							return true;
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("toggleskill")){
						if(p.hasPermission("thr.toggleskill") || p.hasPermission("thr.user")){
							if (p.hasMetadata("ignoreskill")){
								p.removeMetadata("ignoreskill", plugin0);
								p.sendMessage(thrpre + ChatColor.DARK_AQUA + "行動スキルは再び発動します");
								return true;
							}else{
								MetadataValue ignoreskill = new FixedMetadataValue(plugin0, Boolean.valueOf(true));
								p.setMetadata("ignoreskill", ignoreskill);
								p.sendMessage(thrpre + ChatColor.RED + "行動スキルを封印しました");
								return true;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("setrace")){
						if(p.hasPermission("thr.setrace")){
							if(args.length == 2){
								conf.set("user." + p.getUniqueId() + ".race", args[1].toString());
								SaveThConfig();
								p.sendMessage(thrpre + ChatColor.AQUA + "あなたは種族が" + conf.getString(new StringBuilder("user.").append(p.getUniqueId()).append(".race").toString()) + "になりました。");
								return true;
							}else if(args.length == 3){
								if (Bukkit.getPlayer(args[1]) != null){
									Player pl = Bukkit.getPlayer(args[1]);
									conf.set("user." + pl.getUniqueId() + ".race", args[2].toString());
									SaveThConfig();
									p.sendMessage(thrpre + ChatColor.AQUA + pl.getName() + "の種族を" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".race").toString()) + "にしました。");
									pl.sendMessage(thrpre + ChatColor.AQUA + "あなたは種族が" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".race").toString()) + "になりました。");
									return true;
								}
							}else {
								p.sendMessage(thrpre + "§c/thr setrace <PlayerName> (race)");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("setpoint")){
						if(p.hasPermission("thr.setpoint")){
							if(args.length == 2){
								int point = Integer.parseInt(args[1]);
								conf.set("user." + p.getUniqueId() + ".point", Integer.valueOf(point));
								SaveThConfig();
								p.sendMessage(thrpre + ChatColor.AQUA + "あなたはポイントが" + conf.getString(new StringBuilder("user.").append(p.getUniqueId()).append(".point").toString()) + "になりました。");
								return true;
							}else if(args.length == 3){
								if (Bukkit.getPlayer(args[1]) != null){
									Player pl = Bukkit.getPlayer(args[1]);
									int point = Integer.parseInt(args[2]);
									conf.set("user." + pl.getUniqueId() + ".point", Integer.valueOf(point));
									SaveThConfig();
									p.sendMessage(thrpre + ChatColor.AQUA + pl.getName() + "のポイントを" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "にしました。");
									pl.sendMessage(thrpre + ChatColor.AQUA + "あなたはポイントが" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "になりました。");
									return true;
								}else {
									p.sendMessage(thrpre + "§cプレイヤー名が不正です！");
									return false;
								}
							}else {
								p.sendMessage(thrpre + "§c/thr setpoint <PlayerName> (Points)");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("addpoint")){
						if(p.hasPermission("thr.setpoint")){
							if(args.length == 2){
								int point = Integer.parseInt(args[1]);
								conf.set("user." + p.getUniqueId() + ".point", Integer.valueOf(conf.getInt("user." + p.getUniqueId() + ".point") + point));
								SaveThConfig();
								p.sendMessage(thrpre + ChatColor.AQUA + "あなたはポイントが" + conf.getString(new StringBuilder("user.").append(p.getUniqueId()).append(".point").toString()) + "になりました。");
								return true;
							}else if(args.length == 3){
								if (Bukkit.getPlayer(args[1]) != null){
									Player pl = Bukkit.getPlayer(args[1]);
									int point = Integer.parseInt(args[2]);
									conf.set("user." + pl.getUniqueId() + ".point", Integer.valueOf(conf.getInt("user." + pl.getUniqueId() + ".point") + point));
									SaveThConfig();
									p.sendMessage(thrpre + ChatColor.AQUA + pl.getName() + "のポイントを" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "にしました。");
									pl.sendMessage(thrpre + ChatColor.AQUA + "あなたはポイントが" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "になりました。");
									return false;
								}else {
									p.sendMessage(thrpre + "§cプレイヤー名が不正です！");
									return false;
								}
							}else {
								p.sendMessage(thrpre + "§c/thr addpoint <PlayerName> (Points)");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("steppoint")){
						if(p.hasPermission("thr.steppoint")){
							if(args.length == 2){
								if (conf.getInt("user." + p.getUniqueId() + ".point") < Integer.parseInt(args[1])){
									conf.set("user." + p.getUniqueId() + ".point", Integer.valueOf(conf.getInt("user." + p.getUniqueId() + ".point") + 1));
									SaveThConfig();
									p.sendMessage(thrpre + ChatColor.AQUA + "あなたはポイントが" + conf.getString(new StringBuilder("user.").append(p.getUniqueId()).append(".point").toString()) + "になりました。");
									return true;
								}else {
									p.sendMessage(thrpre + "§c例外処理が発生したためコマンドを実行できませんでした");
									return false;
								}
							}else if(args.length == 3){
								if (Bukkit.getPlayer(args[1]) != null){
									Player pl = Bukkit.getPlayer(args[1]);
									if (conf.getInt("user." + p.getUniqueId() + ".point") < Integer.parseInt(args[2])){
										conf.set("user." + pl.getUniqueId() + ".point", Integer.valueOf(conf.getInt("user." + pl.getUniqueId() + ".point") + 1));
										SaveThConfig();
										p.sendMessage(thrpre + ChatColor.AQUA + pl.getName() + "のポイントを" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "にしました。");
										pl.sendMessage(thrpre + ChatColor.AQUA + "あなたはポイントが" + conf.getString(new StringBuilder("user.").append(pl.getUniqueId()).append(".point").toString()) + "になりました。");
										return true;
									}else {
										p.sendMessage(thrpre + "§cこれ以上ポイントは取得できません");
									}
								}else {
									p.sendMessage(thrpre + "§cプレイヤー名が不正です！");
									return false;
								}
							}else {
								p.sendMessage(thrpre + "§c無効な引数です！");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("evollist")){
						if(p.hasPermission("thr.evol.user.list") || p.hasPermission("thr.user")){
							p.sendMessage(thrpre + ChatColor.AQUA + p.getName() + "の進化できる先リスト");
							List<String> evolraces = new ArrayList<String>();
							for (String race : conf.getConfigurationSection("race").getKeys(false)) {
								if (conf.getString("race." + race + ".racetype.root").contains(conf.getString("user." + p.getUniqueId() + ".race"))) {
									evolraces.add(race);
								}
							}
							for (String evolrace : evolraces) {
								p.sendMessage(conf.getString(new StringBuilder("race.").append(evolrace).append(".display.real").toString()) + "：内部name＞" + evolrace);
							}
							return true;
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("evolinfo")){
						if(p.hasPermission("thr.evol.user.info") || p.hasPermission("thr.user")){
							boolean existrace = false;
							String inforace = "";
							for (String race : conf.getConfigurationSection("race").getKeys(false)) {
								if (race.toLowerCase().contains(args[1].toLowerCase())){
									existrace = true;
									inforace = race;
									break;
								}
							}
							if (existrace){
								p.sendMessage(conf.getString(new StringBuilder("race.").append(inforace).append(".display.real").toString()) + "：内部name＞" + inforace + "（" + conf.getString(new StringBuilder("race.").append(inforace).append(".display.tag").toString()) + "）の情報");
								p.sendMessage("元種族：" + conf.getString(new StringBuilder("race.").append(inforace).append(".racetype.root").toString()));
								p.sendMessage("ランク：" + conf.getString(new StringBuilder("race.").append(inforace).append(".racetype.rank").toString()));
								p.sendMessage("進化に必要な進化の欠片：" + conf.getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.shard").toString()));
								p.sendMessage("進化に必要な進化の宝石：" + conf.getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.crystal").toString()));
								p.sendMessage("進化に必要な種族素材：" + conf.getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.amount").toString()) + "個の" + Material.getMaterial(conf.getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.typeid").toString())) + "(メタ" + conf.getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.meta").toString()) + "）");
								p.sendMessage(conf.getString("race." + inforace + ".intro.story"));
								p.sendMessage(conf.getString("race." + inforace + ".intro.skills"));
								return true;
							}else	{
								p.sendMessage(thrpre + ChatColor.RED + "その種族内部nameは存在しません。");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("info")){
						if(p.hasPermission("thr.info") || p.hasPermission("thr.user")){
							boolean existrace = false;
							String inforace = "";
							for (String race : conf.getConfigurationSection("race").getKeys(false)) {
								if (race.toLowerCase().contains(conf.getString("user." + p.getUniqueId() + ".race").toLowerCase())){
									existrace = true;
									inforace = race;
									break;
								}
							}
							if (existrace){
								p.sendMessage(conf.getString(new StringBuilder("race.").append(inforace).append(".display.real").toString()) + "：内部name＞" + inforace + "（" + conf.getString(new StringBuilder("race.").append(inforace).append(".display.tag").toString()) + "）の情報");
								p.sendMessage("元種族：" + conf.getString(new StringBuilder("race.").append(inforace).append(".racetype.root").toString()));
								p.sendMessage("ランク：" + conf.getString(new StringBuilder("race.").append(inforace).append(".racetype.rank").toString()));
								p.sendMessage("進化に必要な進化の欠片：" + conf.getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.shard").toString()));
								p.sendMessage("進化に必要な進化の宝石：" + conf.getString(new StringBuilder("race.").append(inforace).append(".evol.evolpoint.crystal").toString()));
								p.sendMessage("進化に必要な種族素材：" + conf.getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.amount").toString()) + "個の" + Material.getMaterial(conf.getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.typeid").toString())) + "(メタ" + conf.getInt(new StringBuilder("race.").append(inforace).append(".evol.raceitem.meta").toString()) + "）");
								p.sendMessage(conf.getString("race." + inforace + ".intro.story"));
								p.sendMessage(conf.getString("race." + inforace + ".intro.skills"));
								return true;
							}else {
								p.sendMessage(thrpre + ChatColor.RED + "その種族内部nameは存在しません。");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else if(args[0].equalsIgnoreCase("evolchange")){
						if (p.hasPermission("thr.evol.user.change") || p.hasPermission("thr.user")){
							boolean existrace = false;
							String inforace = "";
							for (String race : conf.getConfigurationSection("race").getKeys(false)) {
								if (race.toLowerCase().contains(args[1].toLowerCase())){
									existrace = true;
									inforace = race;
									break;
								}
							}
							if (existrace){
								if (conf.getString("race." + inforace + ".racetype.root").contains(conf.getString("user." + p.getUniqueId() + ".race"))){
									PlayerInventory inventory = p.getInventory();
									int ok_shard = 0;
									int ok_crystal = 0;
									int ok_raceitem = 0;
									ItemStack shard = null;
									ItemStack crystal = null;
									ItemStack raceitem = null;
									if (conf.getInt("race." + inforace + ".evol.evolpoint.shard") != 0){
										shard = new ItemStack(Material.PRISMARINE_SHARD, conf.getInt("race." + inforace + ".evol.evolpoint.shard"));
										if (inventory.contains(shard)) {
											ok_shard = 1;
										} else {
											ok_shard = 2;
										}
									}
									if (conf.getInt("race." + inforace + ".evol.evolpoint.crystal") != 0){
										crystal = new ItemStack(Material.PRISMARINE_CRYSTALS, conf.getInt("race." + inforace + ".evol.evolpoint.crystal"));
										if (inventory.contains(crystal)) {
											ok_crystal = 1;
										} else {
											ok_shard = 2;
										}
									}
									if (conf.getInt("race." + inforace + ".evol.raceitem.amount") != 0){
										raceitem = new ItemStack(Material.getMaterial(conf.getInt("race." + inforace + ".evol.raceitem.typeid")), conf.getInt("race." + inforace + ".evol.raceitem.amount"));
										int raceitemmeta = conf.getInt("race." + inforace + ".evol.raceitem.meta");
										raceitem.setDurability((short)raceitemmeta);
										if (inventory.contains(raceitem)) {
											ok_raceitem = 1;
										} else {
											ok_raceitem = 2;
										}
									}
									if ((ok_shard == 2) || (ok_crystal == 2) || (ok_raceitem == 2)){
										p.sendMessage(thrpre + ChatColor.RED + "その種族に進化する為のアイテムがありません！");
										return false;
									}else {
										p.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 1.0F, 1.0F);
										if (ok_shard == 1) {
											p.getInventory().remove(shard);
										}
										if (ok_crystal == 1) {
											p.getInventory().remove(crystal);
										}
										if (ok_raceitem == 1) {
											p.getInventory().remove(raceitem);
										}
										conf.set("user." + p.getUniqueId() + ".race", inforace);
										SaveThConfig();
										Bukkit.broadcastMessage(thrpre + ChatColor.BLUE + p.getName() + "は" + conf.getString(new StringBuilder("race.").append(inforace).append(".racetype.root").toString()) + "から" + conf.getString(new StringBuilder("race.").append(inforace).append(".display.real").toString()) + "に進化した！！");
										ItemStack rewarditem = null;
										if (conf.getInt("race." + inforace + ".evol.rewarditem.amount") != 0){
											rewarditem = new ItemStack(Material.getMaterial(conf.getInt("race." + inforace + ".evol.rewarditem.typeid")), conf.getInt("race." + inforace + ".evol.rewarditem.amount"));
											int rewarditemmeta = conf.getInt("race." + inforace + ".evol.rewarditem.meta");
											rewarditem.setDurability((short)rewarditemmeta);
											p.getInventory().addItem(new ItemStack[] { rewarditem });
										}
										return true;
									}
								}else {
									p.sendMessage(thrpre + ChatColor.RED + "進化できる種族ではありません！");
									return false;
								}
							}else {
								p.sendMessage(thrpre + ChatColor.RED + "その種族内部nameは存在しません。");
								return false;
							}
						}else {
							p.sendMessage(thrpre + "§c権限がありません！");
							return false;
						}
					}else {
						p.sendMessage(thrpre + "§cコマンドが存在しません！");
					}
				}
			}
		}
		return false;
	}

	public void registerEventListener(){
		new EventListener(this);
		new SkillListener(this);
	}

	public static void SaveThConfig(){
		try {
			conf.save(configfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void reloadThConfig(){
		configfile = new File(pluginDir, "config.yml");
		conf = YamlConfiguration.loadConfiguration(configfile);
	}
}
