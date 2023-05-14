package com.example.EthanApiPlugin;

import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemQuery
{
	private List<Widget> items;
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static ItemManager itemManager = RuneLite.getInjector().getInstance(ItemManager.class);

	ItemQuery(List<Widget> items)
	{
		this.items = new ArrayList(items);
	}

	public ItemQuery filter(Predicate<? super Widget> predicate)
	{
		items = items.stream().filter(predicate).collect(Collectors.toList());
		return this;
	}

	public ItemQuery withAction(String action)
	{
		items = items.stream().filter(item -> Arrays.asList(item.getActions()).contains(action)).collect(Collectors.toList());
		return this;
	}

	public ItemQuery withId(int id)
	{
		items = items.stream().filter(item -> item.getItemId() == id).collect(Collectors.toList());
		return this;
	}

	public ItemQuery withName(String name)
	{
		items = items.stream().filter(item -> item.getName().equals(name)).collect(Collectors.toList());
		return this;
	}

	public ItemQuery quantityGreaterThan(int quanity)
	{
		items = items.stream().filter(item -> item.getItemQuantity() > quanity).collect(Collectors.toList());
		return this;
	}

	public ItemQuery nameContains(String name)
	{
		items = items.stream().filter(item -> item.getName().contains(name)).collect(Collectors.toList());
		return this;
	}

	public ItemQuery nonPlaceHolder()
	{
		return quantityGreaterThan(0);
	}

	public ItemQuery idInList(List<Integer> ids)
	{
		items = items.stream().filter(item -> ids.contains(item.getItemId())).collect(Collectors.toList());
		return this;
	}

	public ItemQuery indexIs(int index)
	{
		items = items.stream().filter(item -> item.getIndex() == index).collect(Collectors.toList());
		return this;
	}

	public ItemQuery matchesWildCardNoCase(String input)
	{
		items =
				items.stream().
						filter(item -> WildcardMatcher.matches(input.toLowerCase(), Text.removeTags(item.getName().toLowerCase()))).
						collect(Collectors.toList());
		return this;
	}

	public ItemQuery onlyNoted()
	{
		items = items.stream().filter(this::isNoted).collect(Collectors.toList());
		return this;
	}

	public ItemQuery onlyUnnoted()
	{
		items = items.stream().filter(item -> !isNoted(item)).collect(Collectors.toList());
		return this;
	}

	public boolean empty()
	{
		return items.size() == 0;
	}

	public ItemQuery filterUnique()
	{
		items = items.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Widget::getItemId))), ArrayList::new));
		return this;
	}

	public List<Widget> result()
	{
		return items;
	}

	public Optional<Widget> first()
	{
		Widget returnWidget = null;
		if (items.size() == 0)
		{
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(items.get(0));
	}

	@SneakyThrows
	public boolean isNoted(Widget item)
	{
		ItemComposition itemComposition = EthanApiPlugin.itemDefs.get(item.getItemId());
		return itemComposition.getNote() != -1;
	}
}
