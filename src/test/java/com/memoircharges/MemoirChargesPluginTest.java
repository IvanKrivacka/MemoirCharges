package com.memoircharges;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MemoirChargesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MemoirChargesPlugin.class);
		RuneLite.main(args);
	}
}