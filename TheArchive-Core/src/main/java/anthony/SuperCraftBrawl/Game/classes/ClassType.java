package anthony.SuperCraftBrawl.Game.classes;

import org.bukkit.entity.Player;

import anthony.SuperCraftBrawl.Game.GameInstance;
import anthony.SuperCraftBrawl.Game.classes.all.BabyCowClass;
import anthony.SuperCraftBrawl.Game.classes.all.BatClass;
import anthony.SuperCraftBrawl.Game.classes.all.BlazeClass;
import anthony.SuperCraftBrawl.Game.classes.all.BunnyClass;
import anthony.SuperCraftBrawl.Game.classes.all.ButterBroClass;
import anthony.SuperCraftBrawl.Game.classes.all.ButterGolemClass;
import anthony.SuperCraftBrawl.Game.classes.all.Cactus;
import anthony.SuperCraftBrawl.Game.classes.all.ChickenClass;
import anthony.SuperCraftBrawl.Game.classes.all.CreeperClass;
import anthony.SuperCraftBrawl.Game.classes.all.DarkSethBling;
import anthony.SuperCraftBrawl.Game.classes.all.EnderdragonClass;
import anthony.SuperCraftBrawl.Game.classes.all.EndermanClass;
import anthony.SuperCraftBrawl.Game.classes.all.FluxtyClass;
import anthony.SuperCraftBrawl.Game.classes.all.GhastClass;
import anthony.SuperCraftBrawl.Game.classes.all.HerobrineClass;
import anthony.SuperCraftBrawl.Game.classes.all.HorseClass;
import anthony.SuperCraftBrawl.Game.classes.all.IrongolemClass;
import anthony.SuperCraftBrawl.Game.classes.all.NinjaClass;
import anthony.SuperCraftBrawl.Game.classes.all.NotchClass;
import anthony.SuperCraftBrawl.Game.classes.all.OcelotClass;
import anthony.SuperCraftBrawl.Game.classes.all.PigClass;
import anthony.SuperCraftBrawl.Game.classes.all.PotatoClass;
import anthony.SuperCraftBrawl.Game.classes.all.SatermelonClass;
import anthony.SuperCraftBrawl.Game.classes.all.SethBlingClass;
import anthony.SuperCraftBrawl.Game.classes.all.SheepClass;
import anthony.SuperCraftBrawl.Game.classes.all.SkeletonClass;
import anthony.SuperCraftBrawl.Game.classes.all.SlimeClass;
import anthony.SuperCraftBrawl.Game.classes.all.SnowGolemClass;
import anthony.SuperCraftBrawl.Game.classes.all.SnowmanClass;
import anthony.SuperCraftBrawl.Game.classes.all.SpiderClass;
import anthony.SuperCraftBrawl.Game.classes.all.SquidClass;
import anthony.SuperCraftBrawl.Game.classes.all.SteveClass;
import anthony.SuperCraftBrawl.Game.classes.all.TNTClass;
import anthony.SuperCraftBrawl.Game.classes.all.WitchClass;
import anthony.SuperCraftBrawl.Game.classes.all.WitherClass;
import anthony.SuperCraftBrawl.ranks.Rank;
import net.md_5.bungee.api.ChatColor;

public enum ClassType {

	Cactus(1, 0, 0), TNT(2, 350, 0), Enderdragon(3, 0, Rank.Vip), Skeleton(4, 0, 0), Ninja(5, 2500, 0),
	IronGolem(6, 0, Rank.Vip), Enderman(7, 0, 0), Ghast(8, 0, Rank.Vip), Chicken(9, 500, 0), Slime(10, 0, Rank.Vip),
	ButterGolem(11, 0, Rank.Vip), DarkSethBling(12, 150, 0), Witch(13, 1000, 0), SnowGolem(14, 800, 0),
	Bat(15, 0, Rank.Vip), SethBling(16, 0, Rank.Vip), Sheep(17, 550, 0), Horse(18, 0, 0), Melon(19, 0, Rank.Vip),
	/* Rabbit, */ Squid(20, 0, 0), Spider(21, 0, 0), BabyCow(22, 0, Rank.Vip), Herobrine(23, 0, Rank.Vip),
	Bunny(24, 450, 0), ButterBro(25, 1200, 0), Snowman(26, 0, 0), Fluxty(27, 0, 0), Steve(28, 1000, 0),
	Notch(29, 2000, 0), Pig(30, 0, 0), Blaze(31, 0, 0), Potato(32, 750, 0), Wither(33, 0, 0), Ocelot(34, 250, 0), Creeper(35, 0, 0); // FIX NOTCH'S TOKEN COST AFTER

	private int id;
	private int tokenCost = 0;
	private int level = 0;
	private Rank donor;

	private ClassType(int id, int tokenCost, int level) {
		this.id = id;
		this.tokenCost = tokenCost;
		this.level = level;
	}

	private ClassType(int id, int tokenCost, Rank donor) {
		this.id = id;
		this.tokenCost = tokenCost;
		this.donor = donor;
	}

	public Rank getMinRank() {
		return donor;
	}

	public int getID() {
		return id;
	}

	public int getTokenCost() {
		return tokenCost;
	}

	public int getLevel() {
		return level;
	}

	public static ClassType fromID(int id) {
		for (ClassType ct : ClassType.values()) {
			if (ct.getID() == id) {
				return ct;
			}
		}
		return null;
	}

	public BaseClass GetClassInstance(GameInstance instance, Player player) {
		switch (this) {
		case Cactus:
			return new Cactus(instance, player);
		case Notch:
			return new NotchClass(instance, player);
		case Ocelot:
			return new OcelotClass(instance, player);
		case Creeper:
			return new CreeperClass(instance, player);
		case Wither:
			return new WitherClass(instance, player);
		case Blaze:
			return new BlazeClass(instance, player);
		case Potato:
			return new PotatoClass(instance, player);
		case Steve:
			return new SteveClass(instance, player);
		case Fluxty:
			return new FluxtyClass(instance, player);
		case Snowman:
			return new SnowmanClass(instance, player);
		case Enderdragon:
			return new EnderdragonClass(instance, player);
		case Skeleton:
			return new SkeletonClass(instance, player);
		case TNT:
			return new TNTClass(instance, player);
		case Ninja:
			return new NinjaClass(instance, player);
		case IronGolem:
			return new IrongolemClass(instance, player);
		case Enderman:
			return new EndermanClass(instance, player);
		case Ghast:
			return new GhastClass(instance, player);
		case Herobrine:
			return new HerobrineClass(instance, player);
		case Chicken:
			return new ChickenClass(instance, player);
		case Slime:
			return new SlimeClass(instance, player);
		case ButterGolem:
			return new ButterGolemClass(instance, player);
		case DarkSethBling:
			return new DarkSethBling(instance, player);
		case Witch:
			return new WitchClass(instance, player);
		case SnowGolem:
			return new SnowGolemClass(instance, player);
		case Bat:
			return new BatClass(instance, player);
		case SethBling:
			return new SethBlingClass(instance, player);
		case Sheep:
			return new SheepClass(instance, player);
		case Horse:
			return new HorseClass(instance, player);
		case Melon:
			return new SatermelonClass(instance, player);
		case ButterBro:
			return new ButterBroClass(instance, player);
		/*
		 * case Rabbit: return new RabbitClass(instance, player);
		 */
		case Squid:
			return new SquidClass(instance, player);
		case Spider:
			return new SpiderClass(instance, player);
		case BabyCow:
			return new BabyCowClass(instance, player);
		case Bunny:
			return new BunnyClass(instance, player);
		case Pig:
			return new PigClass(instance, player);
		}
		return null;
	}

	public String getClassDesc() {
		switch (this) {
		case Cactus:
			return "A pricklyyy living thing, made up of thornws & blood..";
		case Ocelot:
			return "Chase down your opponents with your high speed or Purr Attack!";
		case Creeper:
			return "Defeat your opponents with your explosive arsenal";
		case Potato:
			return "Who doesn't like potatoes?!";
		case Wither:
			return "Utilize your explosive skulls to defeat your enemies!";
		case Notch:
			return "The owner of Minecraft..";
		case Blaze:
			return "ITS A BLAZE LOL!";
		case Steve:
			return "OMG OMG GET HYPED!!!";
		case Fluxty:
			return "We cannot have HATERS in the community.. So use your Wood Axe to kick em all out!";
		case Snowman:
			return "This is a Snowman, not a SnowGolem. Get it right pleb!";
		case Skeleton:
			return "A long range shooter effective at taking down their targets";
		case Enderdragon:
			return "Jump higher than your opponents and teleport around!";
		case Enderman:
			return "Stare into the souls of your enemies whilst teleporting around them";
		case Horse:
			return "Nayyy!! Different effects = different powers!";
		case Squid:
			return "UNDA DA SEA! UNDA DA SEA!";
		case Spider:
			return "Bite and poison your enemies while fighting them!";
		case Ninja:
			return "Ninja 2.0 (idk xD)";
		case TNT:
			return "Blow up your enemies with TNT!";
		case Chicken:
			return "Bock bock backaaack! One of the best classes hehe tip";
		case DarkSethBling:
			return "The evil counterpart of the redstone King";
		case Witch:
			return "She lives in daydreams with me! (She)";
		case Sheep:
			return "Different colors of wool gives you different powers!";
		case SnowGolem:
			return "This is a SnowGolem, not a Snowman. Get it right pleb!";
		case Bunny:
			return "Easter Bunny is coming to town!";
		case ButterBro:
			return "Yo, you there Sky??";
		case IronGolem:
			return "Smack your enemies into the air while defending your village!";
		case Ghast:
			return "Burn down your enemies with your sorrows";
		case Slime:
			return "Throw sticky grenades and attack enemies!";
		case ButterGolem:
			return "Once a proud member of the Sky Army, the ButterGolem now stands as a relic of a bygone era..";
		case Bat:
			return "Dance around your opponents with SUPER high jumps!";
		case SethBling:
			return "The creator of SCB, wanna fight?!?!";
		case Melon:
			return "The Owner of the server in the game?!";
		case BabyCow:
			return "moo.. MOO!!";
		case Herobrine:
			return "Use your Diamond of Despair to play tricks on your opponents!";
		case Pig:
			return "Hit and run. In your panic, you gain speed when hit";
		}

		return null;
	}

	public String getTag() {
		switch (this) {
		case Bat:
			return "" + ChatColor.DARK_GRAY + ChatColor.BOLD + ChatColor.ITALIC + "Bat" + ChatColor.RESET;
		case Wither:
			return "" + ChatColor.DARK_GRAY + ChatColor.ITALIC + "Wither" + ChatColor.RESET;
		case Ocelot:
			return "" + ChatColor.YELLOW + ChatColor.BOLD + "Ocelot" + ChatColor.RESET;
		case Creeper:
			return "" + ChatColor.YELLOW + "Creeper" + ChatColor.RESET;
		case Notch:
			return "" + ChatColor.BLACK + ChatColor.BOLD + "Notch" + ChatColor.RESET;
		case Blaze:
			return "" + ChatColor.DARK_RED + "Blaze" + ChatColor.RESET;
		case Potato:
			return "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Potato" + ChatColor.RESET;
		case Steve:
			return "" + ChatColor.AQUA + "Steve" + ChatColor.RESET;
		case Snowman:
			return "" + ChatColor.RESET + "Snow" + ChatColor.DARK_GREEN + "Man" + ChatColor.RESET;
		case Fluxty:
			return "" + ChatColor.GREEN + ChatColor.BOLD + "Fluxty" + ChatColor.RESET;
		case ButterGolem:
			return "" + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.ITALIC + "ButterGolem" + ChatColor.RESET;
		case Herobrine:
			return "" + ChatColor.GRAY + ChatColor.BOLD + "Herobrine" + ChatColor.RESET;
		case Cactus:
			return "" + ChatColor.DARK_GREEN + "Cactus" + ChatColor.RESET;
		case Chicken:
			return "" + ChatColor.YELLOW + ChatColor.BOLD + "Chicken" + ChatColor.RESET;
		case DarkSethBling:
			return "" + ChatColor.DARK_GRAY + ChatColor.BOLD + ChatColor.ITALIC + "DarkSethBling" + ChatColor.RESET;
		case Enderdragon:
			return "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Ender" + ChatColor.RESET + ChatColor.BLACK
					+ ChatColor.BOLD + "Dragon" + ChatColor.RESET;
		case Enderman:
			return "" + ChatColor.BLACK + "Enderman" + ChatColor.RESET;
		case Ghast:
			return "" + ChatColor.RESET + "Ghast" + ChatColor.RESET;
		case IronGolem:
			return "" + ChatColor.GRAY + ChatColor.BOLD + ChatColor.ITALIC + "IronGolem" + ChatColor.RESET;
		case Ninja:
			return "" + ChatColor.BLUE + ChatColor.BOLD + "Ninja" + ChatColor.RESET;
		case SethBling:
			return "" + ChatColor.RED + ChatColor.BOLD + ChatColor.ITALIC + "SethBling" + ChatColor.RESET;
		case Sheep:
			return "" + ChatColor.RESET + ChatColor.BOLD + "Sheep" + ChatColor.RESET;
		case Skeleton:
			return "" + ChatColor.GRAY + ChatColor.ITALIC + "Skeleton" + ChatColor.RESET;
		case Slime:
			return "" + ChatColor.GREEN + ChatColor.BOLD + "Slime" + ChatColor.RESET;
		case SnowGolem:
			return "" + ChatColor.RESET + ChatColor.BOLD + "SnowGolem" + ChatColor.RESET;
		case TNT:
			return "" + ChatColor.RED + ChatColor.BOLD + "T" + ChatColor.RESET + ChatColor.BOLD + "N" + ChatColor.RESET
					+ ChatColor.RED + ChatColor.BOLD + "T" + ChatColor.RESET;
		case Witch:
			return "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Witch" + ChatColor.RESET;
		case Horse:
			return "" + ChatColor.DARK_GREEN + ChatColor.ITALIC + "Horse" + ChatColor.RESET;
		case Melon:
			return "" + ChatColor.YELLOW + "Melon" + ChatColor.RESET;
		/*
		 * case Rabbit: return "" + ChatColor.GREEN + ChatColor.ITALIC + "Rabbit";
		 */
		case Squid:
			return "" + ChatColor.DARK_BLUE + ChatColor.ITALIC + "Squid" + ChatColor.RESET;
		case Spider:
			return "" + ChatColor.RED + ChatColor.ITALIC + "Spider" + ChatColor.RESET;
		case BabyCow:
			return "" + ChatColor.RED + ChatColor.ITALIC + ChatColor.BOLD + "BabyCow" + ChatColor.RESET;
		case Bunny:
			return "" + ChatColor.YELLOW + ChatColor.ITALIC + ChatColor.BOLD + "Bunny" + ChatColor.RESET;
		case ButterBro:
			return "" + ChatColor.YELLOW + ChatColor.BOLD + "ButterBro" + ChatColor.RESET;
		case Pig:
			return "" + ChatColor.BLUE + ChatColor.ITALIC + "Pig" + ChatColor.RESET;
		default:
			break;

		}
		return this.toString();
	}
}