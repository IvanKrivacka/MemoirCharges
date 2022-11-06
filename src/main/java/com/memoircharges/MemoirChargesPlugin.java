package com.memoircharges;

import com.google.inject.Provides;

import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.*;
import net.runelite.client.util.Text;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.Notifier;

@Slf4j
@PluginDescriptor(
	name = "Memoir Charges"
)
public class MemoirChargesPlugin extends Plugin
{
	private static final Pattern BOOK_OF_THE_DEAD_USE_PATTERN = Pattern.compile(
		"The Book of the Dead now has (\\d+) memories remaining\\."
	);
	private static final Pattern MEMOIRS_USE_PATTERN = Pattern.compile(
		"Kharedst\'s Memoirs now has (\\d+) memories remaining\\."
	);
	private Counter botdInfobox;
	private Counter memoirsInfobox;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClientThread clientThread;
	@Inject
	private ItemManager itemManager;
	@Inject
	private InfoBoxManager infoBoxManager;
	@Inject
	private Notifier notifier;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Memoir Charges started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Memoir Charges stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() == ChatMessageType.GAMEMESSAGE || event.getType() == ChatMessageType.SPAM)
		{
			String message = Text.removeTags(event.getMessage());
			Matcher bookOfTheDeadMatcher = BOOK_OF_THE_DEAD_USE_PATTERN.matcher(message);
			Matcher memoirsMatcher = MEMOIRS_USE_PATTERN.matcher(message);
			if (bookOfTheDeadMatcher.find())
			{
				if (botdInfobox != null)
				{
					botdInfobox.setCount(Integer.parseInt(bookOfTheDeadMatcher.group(1)));
				}
				else
				{
					final BufferedImage image = itemManager.getImage(25818, 1, false);
					botdInfobox = new Counter(image, this, Integer.parseInt(bookOfTheDeadMatcher.group(1)));
					infoBoxManager.addInfoBox(botdInfobox);
				}
				if (Integer.parseInt(bookOfTheDeadMatcher.group(1)) < 1)
				{
					notifier.notify("Your Book of the Dead has run out of charges");
				}
			}
			else if (memoirsMatcher.find())
			{

				if (memoirsInfobox != null)
				{
					memoirsInfobox.setCount(Integer.parseInt(memoirsMatcher.group(1)));
				}
				else
				{
					final BufferedImage image = itemManager.getImage(21760, 1, false);
					memoirsInfobox = new Counter(image, this, Integer.parseInt(memoirsMatcher.group(1)));
					infoBoxManager.addInfoBox(memoirsInfobox);
				}

				if (Integer.parseInt(memoirsMatcher.group(1)) < 1)
				{
					notifier.notify("Your Kharedst's memoirs has run out of charges");
				}
			}
		}
	}


}
