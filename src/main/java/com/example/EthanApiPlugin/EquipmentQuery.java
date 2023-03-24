package com.example.EthanApiPlugin;

import net.runelite.api.Client;
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

public class EquipmentQuery
{
	List<EquipmentPiece> equipmentPieces;

	public EquipmentQuery(ArrayList<EquipmentPiece> equipmentPieces)
	{
		this.equipmentPieces = new ArrayList<>(equipmentPieces);
	}

	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static ItemManager itemManager = RuneLite.getInjector().getInstance(ItemManager.class);

	public EquipmentQuery filter(Predicate<? super EquipmentPiece> predicate)
	{
		equipmentPieces = equipmentPieces.stream().filter(predicate).collect(Collectors.toList());
		return this;
	}

	public EquipmentQuery withAction(String action)
	{
		equipmentPieces =
				equipmentPieces.stream().filter(item -> Arrays.asList(item.realWidget.getActions()).contains(action)).collect(Collectors.toList());
		return this;
	}

	public EquipmentQuery withId(int id)
	{
		equipmentPieces =
				equipmentPieces.stream().filter(item -> item.referenceIdWidget.getItemId() == id).collect(Collectors.toList());
		return this;
	}

	public EquipmentQuery withName(String name)
	{
		equipmentPieces =
				equipmentPieces.stream().filter(item -> item.realWidget.getName().equals(name)).collect(Collectors.toList());
		return this;
	}

	public EquipmentQuery nameContains(String name)
	{
		equipmentPieces = equipmentPieces.stream().filter(item -> item.realWidget.getName().contains(name)).collect(Collectors.toList());
		return this;
	}

	public EquipmentQuery idInList(List<Integer> ids)
	{
		equipmentPieces =
				equipmentPieces.stream().filter(item -> ids.contains(item.referenceIdWidget.getItemId())).collect(Collectors.toList());
		return this;
	}

	public EquipmentQuery matchesWildCardNoCase(String input)
	{
		equipmentPieces =
				equipmentPieces.stream().
						filter(item -> WildcardMatcher.matches(input.toLowerCase(),
								Text.removeTags(item.realWidget.getName().toLowerCase()))).
						collect(Collectors.toList());
		return this;
	}

	public boolean empty()
	{
		return equipmentPieces.size() == 0;
	}

	public EquipmentQuery filterUnique()
	{
		equipmentPieces =
				equipmentPieces.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(x -> x.referenceIdWidget.getItemId()))), ArrayList::new));
		return this;
	}

	public List<Widget> result()
	{
		return equipmentPieces.stream().map(x -> x.realWidget).collect(Collectors.toList());
	}

	public Optional<Widget> first()
	{
		Widget returnWidget = null;
		if (equipmentPieces.size() == 0)
		{
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(equipmentPieces.get(0).realWidget);
	}
}
