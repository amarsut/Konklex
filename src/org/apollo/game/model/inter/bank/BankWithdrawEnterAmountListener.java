package org.apollo.game.model.inter.bank;

import org.apollo.game.model.Player;
import org.apollo.game.model.inter.EnterAmountListener;

/**
 * An {@link EnterAmountListener} for withdrawing items.
 * @author Graham
 */
public final class BankWithdrawEnterAmountListener implements EnterAmountListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The item slot.
	 */
	private final int slot;

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * Creates the bank withdraw amount listener.
	 * @param player The player.
	 * @param slot The slot.
	 * @param id The id.
	 */
	public BankWithdrawEnterAmountListener(Player player, int slot, int id) {
		this.player = player;
		this.slot = slot;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apollo.game.model.inter.EnterAmountListener#amountEntered(int)
	 */
	@Override
	public void amountEntered(int amount) {
		if (player.getInterfaceSet().contains(BankConstants.BANK_WINDOW_ID)) {
			BankUtils.withdraw(player, slot, id, amount);
		}
	}
}
