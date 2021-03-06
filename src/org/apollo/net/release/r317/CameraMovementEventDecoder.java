package org.apollo.net.release.r317;

import org.apollo.game.event.impl.CameraMovementEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link CameraMovementEventDecoder}.
 * @author Steve
 */
public final class CameraMovementEventDecoder extends EventDecoder<CameraMovementEvent> {

	@Override
	public CameraMovementEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int y = (int) reader.getSigned(DataType.SHORT);
		int x = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
		return new CameraMovementEvent(x, y);
	}
}
