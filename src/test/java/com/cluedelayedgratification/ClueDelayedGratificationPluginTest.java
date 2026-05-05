package com.cluedelayedgratification;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ClueDelayedGratificationPluginTest
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ClueDelayedGratificationPlugin.class);
		RuneLite.main(args);
	}
}
