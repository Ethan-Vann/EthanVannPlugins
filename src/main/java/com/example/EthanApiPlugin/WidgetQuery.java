package com.example.EthanApiPlugin;

import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetQuery
{
	List<Widget> widgets;

	WidgetQuery(HashSet<Widget> widgets)
	{
		this.widgets = new ArrayList<>(widgets);
	}

	public List<Widget> result()
	{
		return widgets;
	}

	public WidgetQuery withAction(String action)
	{
		widgets = widgets.stream().filter(widget -> widget.getActions() != null && Arrays.asList(widget.getActions()).contains(action)).collect(java.util.stream.Collectors.toList());
		return this;
	}

	public boolean empty()
	{
		return widgets.isEmpty();
	}

	public WidgetQuery hiddenState(boolean hidden)
	{
		widgets = widgets.stream().filter(widget -> widget.isHidden() == hidden).collect(java.util.stream.Collectors.toList());
		return this;
	}

	public WidgetQuery withItemId(int itemId)
	{
		widgets = widgets.stream().filter(widget -> widget.getItemId() == itemId).collect(java.util.stream.Collectors.toList());
		return this;
	}

	public WidgetQuery nonPlaceHolder()
	{
		return quantityGreaterThan(0);
	}

	public WidgetQuery itemIdInList(List<Integer> ids)
	{
		widgets = widgets.stream().filter(item -> ids.contains(item.getItemId())).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery quantityGreaterThan(int quanity)
	{
		widgets = widgets.stream().filter(item -> item.getItemQuantity() > quanity).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery nameContains(String name)
	{
		widgets =
				widgets.stream().filter(item -> item.getName() != null && item.getName().contains(name)).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery withName(String name)
	{
		widgets = widgets.stream().filter(item -> item.getName() != null && item.getName().equals(name)).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery withText(String text)
	{
		widgets = widgets.stream().filter(item -> item.getText() != null && item.getText().equals(text)).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery withTextContains(String text)
	{
		widgets = widgets.stream().filter(item -> item.getText() != null && item.getText().contains(text)).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery withParentId(int parentId)
	{
		widgets = widgets.stream().filter(item -> item.getParentId() == parentId).collect(Collectors.toList());
		return this;
	}

	public WidgetQuery nameMatchesWildCardNoCase(String input)
	{
		widgets =
				widgets.stream().
						filter(item -> item.getName() != null && WildcardMatcher.matches(input.toLowerCase(),
								Text.removeTags(item.getName().toLowerCase()))).
						collect(Collectors.toList());
		return this;
	}
}
