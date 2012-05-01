package org.apollo.game.sync;

import org.apollo.game.GameService;
import org.apollo.game.model.NPC;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.sync.task.NPCSynchronizationTask;
import org.apollo.game.sync.task.PlayerSynchronizationTask;
import org.apollo.game.sync.task.PostNPCSynchronizationTask;
import org.apollo.game.sync.task.PostPlayerSynchronizationTask;
import org.apollo.game.sync.task.PreNPCSynchronizationTask;
import org.apollo.game.sync.task.PrePlayerSynchronizationTask;
import org.apollo.game.sync.task.SynchronizationTask;
import org.apollo.util.CharacterRepository;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a single thread (the {@link GameService} thread from which this is called). Each client is processed sequentially.
 * Therefore this class will work well on machines with a single core/processor. The {@link ParallelClientSynchronizer} will work better on machines with multiple cores/processors,
 * however, both classes will work.
 * @author Graham
 */
public final class SequentialClientSynchronizer extends ClientSynchronizer {

	/*
	 * (non-Javadoc)
	 * @see org.apollo.game.sync.ClientSynchronizer#synchronize()
	 */
	@Override
	public void synchronize() {
		CharacterRepository<Player> players = World.getWorld().getPlayerRepository();
		CharacterRepository<NPC> npcs = World.getWorld().getNpcRepository();
		for (Player player : players) {
			SynchronizationTask task = new PrePlayerSynchronizationTask(player);
			task.run();
		}
		for (Player player : players) {
			SynchronizationTask task = new PlayerSynchronizationTask(player);
			task.run();
		}
		for (Player player : players) {
			SynchronizationTask task = new PostPlayerSynchronizationTask(player);
			task.run();
		}
		for (NPC npc : npcs) {
			SynchronizationTask task = new PreNPCSynchronizationTask(npc);
			task.run();
		}
		for (Player player : players) {
			SynchronizationTask task = new NPCSynchronizationTask(player);
			task.run();
		}
		for (NPC npc : npcs) {
			SynchronizationTask task = new PostNPCSynchronizationTask(npc);
			task.run();
		}
	}
}
