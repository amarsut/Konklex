package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FourthItemActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FourthItemActionEvent}.
 * @author Graham
 */
public final class FourthItemActionEventDecoder extends EventDecoder<FourthItemActionEvent> {

    /*
     * (non-Javadoc)
     * @see
     * org.apollo.net.release.EventDecoder#decode(org.apollo.net.codec.game.
     * GamePacket)
     */
    @Override
    public FourthItemActionEvent decode(GamePacket packet) {
	final GamePacketReader reader = new GamePacketReader(packet);
	final int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
	final int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
	final int id = (int) reader.getUnsigned(DataType.SHORT);
	return new FourthItemActionEvent(interfaceId, id, slot);
    }
}
