package org.apollo.game.event.impl;

import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.sync.seg.SynchronizationSegment;

/**
 * An event which is sent to synchronize the players.
 * @author Graham
 */
public final class PlayerSynchronizationEvent extends Event {

	/**
	 * The player receiving the event.
	 */
	private final Player player;

	/**
	 * The last known region.
	 */
	private final Position lastKnownRegion;

	/**
	 * The player's position.
	 */
	private final Position position;

	/**
	 * A flag indicating if the region has changed.
	 */
	private final boolean regionChanged;

	/**
	 * The current player's synchronization segment.
	 */
	private final SynchronizationSegment segment;

	/**
	 * The number of local players.
	 */
	private final int localPlayers;

	/**
	 * A list of segments.
	 */
	private final List<SynchronizationSegment> segments;

	/**
	 * Creates the player synchronization event.
	 * @param player The player recieving the event.
	 * @param lastKnownRegion The last known region.
	 * @param position The player's current position.
	 * @param regionChanged A flag indicating if the region has changed.
	 * @param segment The current player's synchronization segment.
	 * @param localPlayers The number of local players.
	 * @param segments A list of segments.
	 */
	public PlayerSynchronizationEvent(Player player, Position lastKnownRegion, Position position,
			boolean regionChanged, SynchronizationSegment segment, int localPlayers,
			List<SynchronizationSegment> segments) {
		this.player = player;
		this.lastKnownRegion = lastKnownRegion;
		this.position = position;
		this.regionChanged = regionChanged;
		this.segment = segment;
		this.localPlayers = localPlayers;
		this.segments = segments;
	}

	/**
	 * Gets the last known region.
	 * @return The last known region.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * Gets the number of local players.
	 * @return The number of local players.
	 */
	public int getLocalPlayers() {
		return localPlayers;
	}

	/**
	 * Gets the player.
	 * @return {@link Player} The player reciving the event.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the player's position.
	 * @return The player's position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the current player's segment.
	 * @return The current player's segment.
	 */
	public SynchronizationSegment getSegment() {
		return segment;
	}

	/**
	 * Gets the synchronization segments.
	 * @return The segments.
	 */
	public List<SynchronizationSegment> getSegments() {
		return segments;
	}

	/**
	 * Checks if the region has changed.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean hasRegionChanged() {
		return regionChanged;
	}
}
