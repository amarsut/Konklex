package org.apollo.backend.impl;

import org.apollo.backend.codec.FrontendPacket;
import org.apollo.backend.codec.FrontendPacketReader;
import org.apollo.backend.method.MethodDecoder;
import org.apollo.backend.method.impl.KickMethod;

/**
 * An {@link MethodDecoder} for the {@link KickMethod}
 * @author Steve
 */
public final class KickMethodDecoder extends MethodDecoder<KickMethod> {

	@Override
	public KickMethod decode(FrontendPacket packet) {
		FrontendPacketReader reader = new FrontendPacketReader(packet);
		String player = reader.getString("player");
		String key = reader.getString("key");
		return new KickMethod(player, key);
	}

}