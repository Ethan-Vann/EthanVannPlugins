package com.example.EthanApiPlugin;

import net.runelite.api.widgets.Widget;

import javax.management.Query;
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
	Query query = new Query();
	private List<Widget> items;
	ItemQuery(List<Widget> items){
		this.items = new ArrayList(items);
	}
	public ItemQuery filter(Predicate<? super Widget> predicate){
		items = items.stream().filter(predicate).collect(Collectors.toList());
		return this;
	}
	public ItemQuery hasAction(String action){
		items = items.stream().filter(item -> Arrays.asList(item.getActions()).contains(action)).collect(Collectors.toList());
		return this;
	}
	public ItemQuery hasId(int id){
		items = items.stream().filter(item -> item.getItemId() == id).collect(Collectors.toList());
		return this;
	}
	public ItemQuery withName(String name){
		items = items.stream().filter(item -> item.getName().equals(name)).collect(Collectors.toList());
		return this;
	}
	public ItemQuery nameContains(String name){
		items = items.stream().filter(item -> item.getName().contains(name)).collect(Collectors.toList());
		return this;
	}
	public ItemQuery idInList(List<Integer> ids){
		items = items.stream().filter(item -> ids.contains(item.getItemId())).collect(Collectors.toList());
		return this;
	}
	public ItemQuery indexIs(int index){
		items = items.stream().filter(item -> item.getIndex() == index).collect(Collectors.toList());
		return this;
	}
	public boolean empty(){
		return items.size() == 0;
	}
	public ItemQuery filterUnique(){
		items = items.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Widget::getItemId))), ArrayList::new));
		return this;
	}
	public List<Widget> result(){
		return items;
	}
	public Optional<Widget> first(){
		Widget returnWidget = null;
		if(items.size()== 0){
			return Optional.ofNullable(null);
		}
		return Optional.ofNullable(items.get(0));
	}
}
