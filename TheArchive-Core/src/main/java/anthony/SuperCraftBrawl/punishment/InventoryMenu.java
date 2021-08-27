package anthony.SuperCraftBrawl.punishment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import anthony.SuperCraftBrawl.ItemHelper;
import anthony.SuperCraftBrawl.Main;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;

public abstract class InventoryMenu implements InventoryProvider {
	private static Random invRandom = new Random();

	public SmartInventory inventory;
	private final Main core;
	private Player player;
	private InventoryContents contents;
	private Runnable backRunnable = null, lastCloseRunnable = null;
	private boolean backClose = false;
	private List<SlotPos> slots = new ArrayList<>();

	private String initTitle;
	private int initRow;

	private boolean initialOpened = false, closeable, changed = false;

	private InventoryMenu previousMenu = null;

	private List<Consumer<InventoryCloseEvent>> closeEvents = new ArrayList<>();
	private List<Consumer<InventoryClickEvent>> clickEvents = new ArrayList<>();
	private List<Consumer<InventoryDragEvent>> dragEvents = new ArrayList<>();

	private boolean openingNewPage = false;

	private int listIndex = 0;

	public InventoryMenu(Main core, String title, boolean closeable, int rows, Player player) {
		this.closeable = closeable;
		this.initTitle = title;
		this.initRow = rows;
		this.core = core;
		this.player = player;
		createInventory(title, closeable, rows);
	}

	public InventoryMenu(Main core, String title, boolean closeable, int rows) {
		this(core, title, closeable, rows, null);
	}

	public InventoryMenu(Main core, String title) {
		this(core, title, true, 4);
	}

	public InventoryMenu(Main core, String title, Player player) {
		this(core, title, true, 4, player);
	}
	
	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}

	public void createInventory(String title, boolean closeable, int rows) {
		inventory = SmartInventory.builder().id(title.replace(" ", "").toLowerCase() + invRandom.nextInt(10000))
				.provider(this).size(rows, 9).title(ChatColor.BLACK + title).closeable(closeable)
				.listener(new InventoryListener<>(InventoryCloseEvent.class, e -> {
					if (!openingNewPage) {
						for (Consumer<InventoryCloseEvent> c : closeEvents)
							c.accept(e);
					}
				})).listener(new InventoryListener<>(InventoryClickEvent.class, e -> {
					for (Consumer<InventoryClickEvent> c : clickEvents)
						c.accept(e);
				})).listener(new InventoryListener<>(InventoryDragEvent.class, e -> {
					for (Consumer<InventoryDragEvent> c : dragEvents)
						c.accept(e);
				})).

				build();
	}

	/**
	 * This will simply copy over info.
	 */
	public InventoryMenu(InventoryMenu menu) {
		this.inventory = menu.inventory;
		this.core = menu.core;
		this.player = menu.player;
		this.contents = menu.contents;
		this.backRunnable = menu.backRunnable;
		this.slots = menu.slots;
		this.closeable = menu.closeable;

		this.previousMenu = menu;

		this.initTitle = menu.initTitle;
		this.initRow = menu.initRow;
		this.initialOpened = true;
		new BukkitRunnable() {
			@Override
			public void run() {
				loadMenu();
			}
		}.runTask(core); // So other variables are initialised

	}

	/**
	 * Adds a listener when the inventory closes
	 */
	public void addCloseListener(Consumer<InventoryCloseEvent> consumer) {
		closeEvents.add(consumer);
	}

	public void addClickListener(Consumer<InventoryClickEvent> consumer) {
		clickEvents.add(consumer);
	}

	public void addDragListener(Consumer<InventoryDragEvent> consumer) {
		dragEvents.add(consumer);
	}

	private void changedInventory(SmartInventory inv, InventoryContents contents) {
		this.inventory = inv;
		this.contents = contents;
		if (previousMenu != null)
			previousMenu.changedInventory(inv, contents);
	}

	public void setPreviousMenu(InventoryMenu menu) {
		this.previousMenu = menu;
		this.previousMenu.inventory = this.inventory;
		this.previousMenu.contents = this.contents;
	}

	private void setBackIcon() {
		if (backRunnable != null)
			contents.set(0, 0, ClickableItem
					.of(ItemHelper.setDetails(new ItemStack(Material.ARROW), ChatColor.GRAY + "Go back"), e -> {
						boolean wasBackClose = backClose;
						if (!wasBackClose)
							openingNewPage = true;
						clearAll();
						if (backRunnable != null) {
							Runnable runnable = backRunnable;
							backRunnable = null;
							runnable.run();
						}
						if (!wasBackClose)
							openingNewPage = false;
					}));
	}

	public void restateInitDetails() {
		changeMenuDetails(initTitle, initRow);
	}

	public void changeMenuDetails(int newRows) {
		changeMenuDetails(ChatColor.stripColor(inventory.getTitle()), newRows);
	}

	public void changeMenuDetails(String title, int newRows) {
		if (ChatColor.stripColor(inventory.getTitle()).equals(title) && inventory.getRows() == newRows)
			return;
		inventory.close(player);
		this.inventory = (SmartInventory.builder().id(title.replace(" ", "").toLowerCase() + invRandom.nextInt(10000))
				.provider(this).size(newRows, 9).title(ChatColor.BLACK + title).closeable(closeable)
				.listener(new InventoryListener<>(InventoryCloseEvent.class, e -> {
					if (!openingNewPage) {
						for (Consumer<InventoryCloseEvent> c : closeEvents)
							c.accept(e);
					}
				})).listener(new InventoryListener<>(InventoryClickEvent.class, e -> {
					for (Consumer<InventoryClickEvent> c : clickEvents)
						c.accept(e);
				})).listener(new InventoryListener<>(InventoryDragEvent.class, e -> {
					for (Consumer<InventoryDragEvent> c : dragEvents)
						c.accept(e);
				})).build());
		changed = true;
		inventory.open(player);
	}

	public InventoryContents getContents() {
		return contents;
	}

	public Player getPlayer() {
		return player;
	}

	public Main getCore() {
		return core;
	}

	/**
	 * Plays a click sound
	 */
	public void click() {
		player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
	}

	public void setHeader(ItemStack item, String tabName) {
		contents.fillRow(0, ClickableItem.empty(ItemHelper
				.setDetails(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), ChatColor.GRAY + tabName)));
		contents.set(0, 4, ClickableItem.empty(item));
		setBackIcon();
	}
	public void setBack(Runnable runnable) {
		setBack(runnable, false);
	}

	public void setLastCloseBack() {
		if (lastCloseRunnable != null)
			setBack(lastCloseRunnable, true);
	}

	public void setBack(Runnable runnable, boolean backClose) {
		this.backClose = backClose;
		if (backClose)
			lastCloseRunnable = runnable;
		this.backRunnable = runnable;
		setBackIcon();
	}

	/**
	 * Sets the contents and keeps track
	 */
	public void setContents(int row, int column, ClickableItem clickableItem) {
		contents.set(row, column, clickableItem);
		slots.removeIf(slot -> {
			return slot.getRow() == row && slot.getColumn() == column;
		});
		slots.add(new SlotPos(row, column));
	}

	public void addToList(ItemStack item) {
		this.setContents(listIndex / 9 + 1, listIndex % 9, ClickableItem.empty(item));
		listIndex++;
	}

	/**
	 * Sets update contents. This allows the item to easily be updated.
	 * 
	 * @param item a supplier which will refresh the item
	 * @param r    a click event. Calling the first runnable will refresh.
	 */

	/**
	 * Sets update contents. This allows the item to easily be updated.
	 * 
	 * @param item a supplier which will refresh the item
	 * @param r    a click event. Calling the first runnable will refresh.
	 */

	public void clearAll() {
		this.listIndex = 0;
		for (SlotPos pos : slots)
			contents.set(pos, null);
		slots.clear();
	}

	public void loadNewPage(Runnable run) {
		openingNewPage = true;
		click();
		clearAll();
		this.backRunnable = null;
		run.run();
		openingNewPage = false;
	}

	/**
	 * Opens the inventory for a player
	 */
	public void open(Player player) {
		this.player = player;
		this.inventory.open(player);
	}

	public void close() {
		inventory.close(player);
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		if (changed) {
			changedInventory(inventory, contents);
			changed = false;
		}

		this.contents = contents;
		if (!initialOpened) { // Incase row number was changed
			loadMenu();
			initialOpened = true;
		}
	}

	public abstract void loadMenu();
}
