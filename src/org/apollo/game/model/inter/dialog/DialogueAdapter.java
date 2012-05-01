package org.apollo.game.model.inter.dialog;

/**
 * An adapter for the {@link DialogueListener}.
 * @author Chris Fletcher
 */
public abstract class DialogueAdapter implements DialogueListener {

	/**
	 * @return {@code false} by default, unless overridden.
	 */
	@Override
	public boolean buttonClicked(int button) {
		/* empty */
		return false;
	}

	@Override
	public void continued() {
		/* empty */
	}

	@Override
	public void interfaceClosed() {
		/* empty */
	}
}