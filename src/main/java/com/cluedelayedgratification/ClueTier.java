package com.cluedelayedgratification;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.gameval.ItemID;

enum ClueTier
{
	BEGINNER(ItemID.TRAIL_REWARD_CASKET_BEGINNER),
	EASY(ItemID.TRAIL_REWARD_CASKET_EASY),
	MEDIUM(ItemID.TRAIL_REWARD_CASKET_MEDIUM),
	HARD(ItemID.TRAIL_REWARD_CASKET_HARD),
	ELITE(ItemID.TRAIL_REWARD_CASKET_ELITE),
	MASTER(ItemID.TRAIL_REWARD_CASKET_MASTER);

	final int itemId;

	ClueTier(int itemId)
	{
		this.itemId = itemId;
	}

	private static final Map<Integer, ClueTier> BY_ITEM_ID;

	static
	{
		Map<Integer, ClueTier> map = new HashMap<>();
		for (ClueTier tier : values())
		{
			map.put(tier.itemId, tier);
		}
		BY_ITEM_ID = Collections.unmodifiableMap(map);
	}

	static ClueTier forItemId(int itemId)
	{
		return BY_ITEM_ID.get(itemId);
	}
}
