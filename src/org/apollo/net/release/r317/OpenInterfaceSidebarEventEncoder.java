package org.apollo.net.release.r317;

import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link OpenInterfaceSidebarEvent}.
 * @author Graham
 */
public final class OpenInterfaceSidebarEventEncoder extends EventEncoder<OpenInterfaceSidebarEvent> {

	/*
	 * (non-Javadoc)
	 * @see org.apollo.net.release.EventEncoder#encode(org.apollo.game.event.Event)
	 */
	@Override
	public GamePacket encode(OpenInterfaceSidebarEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(248);
		builder.put(DataType.SHORT, DataTransformation.ADD, event.getInterfaceId());
		builder.put(DataType.SHORT, event.getSidebarId());
		return builder.toGamePacket();
	}
}
