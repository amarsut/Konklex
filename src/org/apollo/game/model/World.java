package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.backend.util.Notification;
import org.apollo.backend.util.Notification.Type;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.fs.parser.NpcDefinitionParser;
import org.apollo.fs.parser.ObjectDefinitionParser;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.inter.store.WorldStore;
import org.apollo.game.model.messaging.WorldMessaging;
import org.apollo.game.model.obj.WorldObject;
import org.apollo.game.model.region.RegionManager;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.game.scheduling.impl.SystemUpdateTask;
import org.apollo.io.EquipmentDefinitionParser;
import org.apollo.util.CharacterRepository;
import org.apollo.util.plugin.PluginManager;

/**
 * The world class is a singleton which contains objects like the {@link CharacterRepository} for players and NPCs. It should only contain things relevant to the in-game world and
 * not classes which deal with I/O and such (these may be better off inside some custom {@link Service} or other code, however, the circumstances are rare).
 * @author Graham
 */
public final class World {

	/**
	 * Represents the different status codes for registering a player.
	 * @author Graham
	 */
	public enum RegistrationStatus {
		/**
		 * Indicates the world is full.
		 */
		WORLD_FULL,
		/**
		 * Indicates the world is offline.
		 */
		WORLD_OFFLINE,
		/**
		 * Indicates the world is being updated.
		 */
		WORLD_UPDATING,
		/**
		 * Indicates that the player is already online.
		 */
		ALREADY_ONLINE,
		/**
		 * Indicates that the player was registered successfully.
		 */
		OK;
	}

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());

	/**
	 * The world.
	 */
	private static final World world = new World();

	/**
	 * The messaging.
	 */
	private static final WorldMessaging worldMessaging = new WorldMessaging();

	/**
	 * The world shops.
	 */
	private static final WorldStore worldStore = new WorldStore();

	/**
	 * Gets the world.
	 * @return The world.
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * The objects.
	 */
	private final WorldObject globalObjects = new WorldObject();

	/**
	 * The scheduler.
	 */
	private final Scheduler scheduler = new Scheduler();

	/**
	 * The command dispatcher.
	 */
	private final CommandDispatcher dispatcher = new CommandDispatcher();

	/**
	 * The plugin manager.
	 */
	private PluginManager pluginManager;

	/**
	 * The {@link CharacterRepository} of {@link Player}s.
	 */
	private final CharacterRepository<Player> playerRepository = new CharacterRepository<Player>(WorldConstants.MAXIMUM_PLAYERS);

	/**
	 * The {@link CharacterRepository} of {@link NPC}s.
	 */
	private final CharacterRepository<NPC> npcRepository = new CharacterRepository<NPC>(WorldConstants.MAXIMUM_NPCS);

	/**
	 * The region manager.
	 */
	private final RegionManager regionManager = new RegionManager();

	/**
	 * Creates the world.
	 */
	private World() {
	}

	/**
	 * Gets the command dispatcher. TODO should this be here?
	 * @return The command dispatcher.
	 */
	public CommandDispatcher getCommandDispatcher() {
		return dispatcher;
	}

	/**
	 * Gets the world messaging.
	 * @return The world messaging.
	 */
	public WorldMessaging getMessaging() {
		return worldMessaging;
	}

	/**
	 * Gets the npc repository.
	 * @return The npc repository.
	 */
	public CharacterRepository<NPC> getNpcRepository() {
		return npcRepository;
	}

	/**
	 * Gets the world object class.
	 * @return The world object class.
	 */
	public WorldObject getObjectManager() {
		return globalObjects;
	}

	/**
	 * Gets the specified player.
	 * @param name The player's name.
	 * @return player The player.
	 */
	public Player getPlayer(String name) {
		for (Player player : getPlayerRepository()) {
			if (player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Gets the character repository. NOTE: {@link CharacterRepository#add(Character)} and {@link CharacterRepository#remove(Character)} should not be called directly! These
	 * mutation methods are not guaranteed to work in future releases! <p> Instead, use the {@link World#register(Player)} and {@link World#unregister(Player)} methods which do the
	 * same thing and will continue to work as normal in future releases.
	 * @return The character repository.
	 */
	public CharacterRepository<Player> getPlayerRepository() {
		return playerRepository;
	}

	/**
	 * Gets the plugin manager. TODO should this be here?
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * Gets the region manager.
	 * @return The region manager.
	 */
	public RegionManager getRegionManager() {
		return regionManager;
	}

	/**
	 * Gets the world stores.
	 * @return The world stores.
	 */
	public WorldStore getStores() {
		return worldStore;
	}

	/**
	 * Initialises the world by loading definitions from the specified file system.
	 * @param release The release number.
	 * @param fs The file system.
	 * @param mgr The plugin manager. TODO move this.
	 * @throws IOException if an I/O error occurs.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager mgr) throws IOException {
		logger.info("Loading item definitions...");
		ItemDefinitionParser itemParser = new ItemDefinitionParser(fs);
		ItemDefinition[] itemDefs = itemParser.parse();
		ItemDefinition.init(itemDefs);
		logger.info("Done (loaded " + itemDefs.length + " item definitions).");
		logger.info("Loading equipment definitions...");
		int nonNull = 0;
		InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"));
		try {
			EquipmentDefinitionParser equipParser = new EquipmentDefinitionParser(is);
			EquipmentDefinition[] equipDefs = equipParser.parse();
			for (EquipmentDefinition def : equipDefs) {
				if (def != null) {
					nonNull++;
				}
			}
			EquipmentDefinition.init(equipDefs);
		} finally {
			is.close();
		}
		logger.info("Done (loaded " + nonNull + " equipment definitions).");
		logger.info("Loading object definitions...");
		ObjectDefinitionParser objectParser = new ObjectDefinitionParser(fs);
		ObjectDefinition[] objectDefs = objectParser.parse();
		ObjectDefinition.init(objectDefs);
		logger.info("Done (loaded " + objectDefs.length + " object definitions).");
		logger.info("Loading NPC definitions...");
		NpcDefinitionParser npcParser = new NpcDefinitionParser(fs);
		NpcDefinition[] npcDefs = npcParser.parse();
		NpcDefinition.init(npcDefs);
		logger.info("Done (loaded " + npcDefs.length + " NPC definitions).");
		logger.info("Loading NPC spawns...");
		loadNPCSpawns();
		logger.info("Done (loaded " + NPCSpawning.NPCslength + " NPC spawns).");
		this.pluginManager = mgr;
	}

	/**
	 * Checks if the specified player is online.
	 * @param name The player's name.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isPlayerOnline(String name) {
		for (Player player : playerRepository) {
			if (player.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads NPC spawns from {@link NPCSpawning}.
	 */
	private void loadNPCSpawns() {
		try {
			NPCSpawning.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls the {@link Scheduler#pulse()} method.
	 */
	public void pulse() {
		scheduler.pulse();
	}

	/**
	 * Registers an NPC.
	 * @param npc The NPC.
	 */
	public void register(NPC npc) {
		World.getWorld().getNpcRepository().add(npc);
	}

	/**
	 * Registers the specified player.
	 * @param player The player.
	 * @return A {@link RegistrationStatus}.
	 */
	public RegistrationStatus register(final Player player) {
		if (isPlayerOnline(player.getName())) {
			return RegistrationStatus.ALREADY_ONLINE;
		}
		if (SystemUpdateTask.isUpdating()) {
			logger.warning("Failed to register player (server updating): " + player + " [online=" + playerRepository.size() + "]");
			return RegistrationStatus.WORLD_UPDATING;
		} else
			if (Config.SERVER_WHITELIST) {
				logger.warning("Failed to register player (server offline): " + player + " [online=" + playerRepository.size() + "]");
				return RegistrationStatus.WORLD_OFFLINE;
			} else {
				boolean success = playerRepository.add(player);
				if (success) {
					Notification.getInstance().add("Player registered", player.getName() + " has logged in.", Type.NOTIFICATION);
					logger.info("Registered player: " + player + " [online=" + playerRepository.size() + "]");
					return RegistrationStatus.OK;
				} else {
					logger.warning("Failed to register player (server full): " + player + " [online=" + playerRepository.size() + "]");
					return RegistrationStatus.WORLD_FULL;
				}
			}
	}

	/**
	 * Schedules a new task.
	 * @param task The {@link ScheduledTask}.
	 */
	public void schedule(ScheduledTask task) {
		scheduler.schedule(task);
	}

	/**
	 * Unregisters the specified player.
	 * @param player The player.
	 */
	public void unregister(Player player) {
		if (playerRepository.remove(player)) {
			logger.info("Unregistered player: " + player + " [online=" + playerRepository.size() + "]");
		} else {
			logger.warning("Could not find player to unregister: " + player + "!");
		}
	}
}
