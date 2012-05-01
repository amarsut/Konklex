package org.apollo.game.model.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apollo.game.event.impl.FriendEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.io.player.PlayerLoader;

/**
 * Holds the friends.
 * @author Steve
 */
public final class PlayerMessaging {

	/**
	 * Event of input.
	 */
	public enum Event {
		/**
		 * Adding a user.
		 */
		FRIEND,
		/**
		 * Ignoring a user.
		 */
		IGNORE
	}

	/**
	 * Hold the friends.
	 */
	private final Map<String, Event> friends = new HashMap<String, Event>();

	/**
	 * Hold the friend capacity.
	 */
	private final int capacity;

	/**
	 * Hold the chat id.
	 */
	private int lastchat = 0;

	/**
	 * The friend size.
	 */
	private int size = 0;

	/**
	 * Holds the player.
	 */
	private final Player player;

	/**
	 * Create a new friends list.
	 * @param player The player.
	 */
	public PlayerMessaging(Player player) {
		this.player = player;
		this.capacity = player.isMembers() ? 200 : 100;
	}

	/**
	 * Add a user.
	 * @param who The user
	 * @param what Friend or ignore
	 * @param loader Are we loading from the {@link PlayerLoader}
	 * @throws Exception the exception
	 */
	public void add(String who, Event what, boolean loader) throws Exception {
		who = who.toLowerCase();
		if (loader) {
			friends.put(who, what);
		} else {
			if (size > capacity) {
				throw new Exception("Friends list full.");
			} else {
				if (what == Event.FRIEND) {
					if (friends.get(who) != null) {
						if (friends.get(who).equals(Event.IGNORE)) {
							throw new Exception("Friend is already ignored.");
						} else
							if (friends.get(who).equals(Event.FRIEND)) {
								throw new Exception("Friend is already added.");
							} else {
								friends.put(who, what);
							}
					} else {
						friends.put(who, what);
					}
				} else
					if (what == Event.IGNORE) {
						if (friends.get(who) != null) {
							if (friends.get(who).equals(Event.FRIEND)) {
								throw new Exception("Friend is already added.");
							} else
								if (friends.get(who).equals(Event.IGNORE)) {
									throw new Exception("Friend is already ignored.");
								} else {
									friends.put(who, what);
								}
						} else {
							friends.put(who, what);
						}
					}
			}
		}
		this.size = friends.size();
	}

	/**
	 * Get the friend capacity.
	 * @return {@link Capacity}
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Delete a user.
	 * @param who The user.
	 * @param what Friend or ignore.
	 * @throws Exception the exception
	 */
	public void delete(String who, Event what) throws Exception {
		who = who.toLowerCase();
		if (what == Event.FRIEND) {
			if (friends.get(who) != null) {
				if (friends.get(who).equals(Event.IGNORE)) {
					throw new Exception("Friend is ignored.");
				} else
					if (!friends.get(who).equals(Event.FRIEND)) {
						throw new Exception("Friend is not added.");
					} else {
						friends.remove(who);
					}
			} else {
				friends.remove(who);
			}
		} else
			if (what == Event.IGNORE) {
				if (friends.get(who) != null) {
					if (friends.get(who).equals(Event.FRIEND)) {
						throw new Exception("Friend is added.");
					} else
						if (!friends.get(who).equals(Event.IGNORE)) {
							throw new Exception("Friend is not ignored.");
						} else {
							friends.remove(who);
						}
				} else {
					friends.remove(who);
				}
			}
		this.size = friends.size();
	}

	/**
	 * Gets the friends list.
	 * @return {@link Map}
	 */
	public Map<String, Event> getFriends() {
		return this.friends;
	}

	/**
	 * Get the chat id.
	 * @return {@link Integer}
	 */
	public int getLastId() {
		return lastchat++;
	}

	/**
	 * Get the value for the event.
	 * @param what The event.
	 * @return {@link Integer}
	 * @throws Exception the exception
	 */
	public int getValue(Event what) throws Exception {
		if (what == Event.FRIEND) {
			return 1;
		} else
			if (what == Event.IGNORE) {
				return 2;
			} else {
				throw new Exception("Invalid event.");
			}
	}

	/**
	 * Get the event for the value.
	 * @param what The event.
	 * @return {@link Event}
	 * @throws Exception the exception
	 */
	public Event getValue(int what) throws Exception {
		if (what == 1) {
			return Event.FRIEND;
		} else
			if (what == 2) {
				return Event.IGNORE;
			} else {
				throw new Exception("Invalid event.");
			}
	}

	/**
	 * Refresh the friend list.
	 * @throws Exception the exception
	 */
	public void refresh() throws Exception {
		for (Entry<String, Event> entry : friends.entrySet()) {
			FriendEvent make = new FriendEvent(entry.getKey(), getValue(entry.getValue()));
			make.setWorld(World.getWorld().isPlayerOnline(entry.getKey()) ? 1 : World.getWorld().getMessaging().isPlayerOnline(entry.getKey()) ? 1 : 0);
			player.send(make);
		}
	}

	/**
	 * Manually refresh the specified player.
	 * @param player the player
	 * @throws Exception the exception
	 */
	public void refresh(String player) throws Exception {
		player = player.toLowerCase();
		if (friends.containsKey(player)) {
			Event event = friends.get(player);
			FriendEvent make = new FriendEvent(player, getValue(event));
			make.setWorld(World.getWorld().isPlayerOnline(player) ? 1 : World.getWorld().getMessaging().isPlayerOnline(player) ? 1 : 0);
			this.player.send(make);
		}
	}

	/**
	 * Get the friends size.
	 * @return {@link Integer}
	 */
	public int size() {
		return size;
	}
}
