package com.cluedelayedgratification;

class TierState
{
	int bankCount;
	int inventoryCount;
	boolean openAllowed;
	int maxSeen;
	int maxAtUnlock;

	void reset()
	{
		bankCount = 0;
		inventoryCount = 0;
		openAllowed = false;
		maxSeen = 0;
		maxAtUnlock = 0;
	}
}
