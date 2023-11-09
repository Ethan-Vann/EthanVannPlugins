Plugins released by Ethan Vann and Nequil

# EthanAPI Documentation

Welcome to the EthanAPI documentation! EthanAPI is a Runelite Plugin API designed to enhance the automation and interaction within the game environment. This document covers the functionalities provided by the `Collections` and `InteractionAPI` modules of the EthanAPI.

## Table of Contents

- [Overview](#overview)
- [Modules](#modules)
  - [Collections](#collections)
  - [InteractionAPI](#interactionapi)

## Overview

EthanAPI provides developers with powerful tools to interact with and query the game's environment. The API is split into two main modules:

- **Collections**: Allows querying of various game entities such as NPCs, items on the ground, and items within the player's inventory and bank.
- **InteractionAPI**: Enables the sending of action packets to interact with the game entities automatically, combined with the necessary click packets.


## Modules

### Collections

#### Overview

The Collections module of EthanAPI provides functionality to query the visible game screen and retrieve data about various game entities. This includes NPCs, items on the ground, items in your inventory, and items in your bank.

[Insert More Detailed Description or Subsections about Collections Here]

#### API Endpoints (Collections)

### Collections Module - Bank Class

The `Bank` class is part of the Collections module within EthanAPI. It provides methods to interact with and retrieve information about the items present in the player's bank within RuneLite. Below are the key functionalities provided by the `Bank` class:

#### `public static ItemQuery search()`

Returns an `ItemQuery` object that can be used to perform queries on the items in the player's bank.

- **Returns**: `ItemQuery` — A queryable object containing the list of items in the bank.

#### `public static boolean isOpen()`

Checks if the bank interface is currently open.

- **Returns**: `boolean` — `true` if the bank is open, `false` otherwise.


#### Internal Classes

- `BankItemWidget`

  An internal representation of an item within the bank, including its name, ID, quantity, and index.

#### Example Usage

```java

// Check if the bank is open
if (Bank.isOpen()) {
    // Search for items in the bank
    ItemQuery query = Bank.search();
    
    // Example usage of the query to get items
    List<Item> items = query.findItems("itemName");
    
    // Process the items as needed
    for (Item item : items) {
        // Do something with the item
    }
}
```
### Collections Module - BankInventory Class

The `BankInventory` class is a component of the Collections module in EthanAPI that manages and retrieves information about the items present in a player's bank inventory. It offers a streamlined approach for querying bank inventory items within the RuneLite client.

#### `public static ItemQuery search()`

Provides an `ItemQuery` object populated with the items in the player's bank inventory, allowing for complex queries.

- **Returns**: `ItemQuery` - An object that enables querying over the collected bank inventory items.

#### Event Listeners

- `@Subscribe public void onWidgetLoaded(WidgetLoaded e)`

  Called when a Widget is loaded within the client, specifically listening for the bank inventory items container. Updates the internal list of bank inventory items accordingly.

  - **Parameters**:
    - `e` — `WidgetLoaded` event data.

- `@Subscribe public void onItemContainerChanged(ItemContainerChanged e)`

  Invoked when there is a change in an item container, watching for changes in bank inventory. It refreshes the internal items list in response to these changes.

  - **Parameters**:
    - `e` — `ItemContainerChanged` event data, particularly containing the container ID.

- `public static void RetryCollection()`

  This method is used to re-collect the items from the bank inventory widgets, refreshing the internal list of items.

- `@Subscribe public void onGameStateChanged(GameStateChanged gameStateChanged)`

  Monitors changes in the game state such as world hopping, logging in, or losing connection, and clears the bank inventory items list in response to ensure data consistency.

  - **Parameters**:
    - `gameStateChanged` — `GameStateChanged` event data.

#### Usage

```java
// Using the BankInventory class to query items in the bank inventory
BankInventory bankInventory = new BankInventory();

// Perform a search to retrieve an ItemQuery object
ItemQuery query = BankInventory.search();

// Use the query to find specific items or perform operations
// Example: Finding an item with item ID 12345
List<Widget> items = query.findItemsById(12345);

// Now you can iterate through the items list and perform your desired operations
for (Widget itemWidget : items) {
    // Access item properties and methods, for example:
    // int itemId = itemWidget.getItemId();
    // int quantity = itemWidget.getItemQuantity();
    // Perform further actions with the item widget data...
}
```
# BankItemWidget Documentation

The `BankItemWidget` class is part of the `com.example.EthanApiPlugin.Collections` package, specifically designed for the `EthanApiPlugin`. This class implements the `Widget` interface and primarily manages individual items within a bank-like interface, including their attributes such as name, ID, and quantity.

## Class Overview

- **Package**: `com.example.EthanApiPlugin.Collections`
- **Implemented Interface**: `Widget`

## Constructor

- `BankItemWidget(String name, int itemid, int quantity, int index)`
  - Initializes a new instance of `BankItemWidget` with specified parameters.
  - Parameters:
    - `name`: The name of the item.
    - `itemid`: The ID of the item.
    - `quantity`: The quantity of the item.
    - `index`: The index of the item in the widget.

## Methods

### Public Methods

- `int getId()`
  - Returns the ID of the bank item container widget.
- `String getName()`
  - Returns the name of the item.
- `int getItemId()`
  - Returns the item ID.
- `int getItemQuantity()`
  - Returns the quantity of the item.
- `int getIndex()`
  - Returns the index of the item in the widget.
- `String[] getActions()`
  - Retrieves the set of actions that can be performed on the item, such as "Withdraw-X" or "Examine".
- `boolean contains(Point point)`
  - Checks if the widget contains the specified point.
- ... and other inherited or implemented methods from the `Widget` interface, primarily getters and setters for widget properties.

### Protected/Private Methods

- No specific private or protected methods are detailed in the provided code.

## Implementation Details

- Most setter methods in this implementation return `null` or perform no operation, indicating a potentially read-only or limited functionality design.
- The `getActions` method is particularly notable, as it defines interactive options for the item based on game state or user settings (e.g., `Withdraw-All`, `Placeholder`).
- `contains(Point point)` might be intended for hit-testing within the widget area.

## Usage Example

```java
// Assuming existence of a method to retrieve a specific BankItemWidget
BankItemWidget itemWidget = getBankItemWidget("itemName");

// Retrieve item details
int itemId = itemWidget.getItemId();
int itemQuantity = itemWidget.getItemQuantity();
String itemName = itemWidget.getName();

// Perform operations based on the retrieved data
// (e.g., displaying item details, interfacing with other components)
```
# DepositBox Class Documentation

The `DepositBox` class is part of the `com.example.EthanApiPlugin.Collections` package for the `EthanApiPlugin`, which interacts with the deposit box feature in a RuneLite client environment. This class is responsible for managing and updating the items present in the deposit box.

## Class Overview

- **Package**: `com.example.EthanApiPlugin.Collections`
- **Dependencies**:
  - `com.example.EthanApiPlugin.Collections.query.ItemQuery`
  - `net.runelite.api.*`
  - `net.runelite.client.*`
  - `java.util.*`
- **Functionality**: Interacts with and manages items within the deposit box of a RuneLite client.

## Constructor

- `DepositBox()`
  - Default constructor.

## Class Members

### Static Members

- `static Client client`
  - Instance of the RuneLite client.
- `static List<Widget> depositBoxItems`
  - List of widgets representing the items in the deposit box.

## Methods

### Public Methods

- `static ItemQuery search()`
  - Returns a new `ItemQuery` instance for querying deposit box items.
  - **Return Type**: `ItemQuery`

### Event Handling Methods

- `@Subscribe public void onWidgetLoaded(WidgetLoaded e)`
  - Handles the `WidgetLoaded` event. Updates the `depositBoxItems` list when the deposit box widget is loaded.
- `@Subscribe public void onItemContainerChanged(ItemContainerChanged e)`
  - Handles the `ItemContainerChanged` event. Updates the `depositBoxItems` list when the items in the deposit box change.
- `@Subscribe public void onGameStateChanged(GameStateChanged gameStateChanged)`
  - Handles the `GameStateChanged` event. Clears the `depositBoxItems` list on certain game states like hopping, login screen, or connection lost.

## Implementation Details

- The class subscribes to several events to keep the deposit box item list updated:
  - `onWidgetLoaded` is triggered when any widget is loaded, but it specifically looks for the deposit box widget (group ID 192).
  - `onItemContainerChanged` is triggered when the item container changes (container ID 93).
  - `onGameStateChanged` clears the items when the game state changes to hopping, login screen, or connection lost.
- It uses Java's Stream API to filter and collect items into the `depositBoxItems` list, ignoring certain item IDs (e.g., 6512, -1).
- Exception handling is included to clear the `depositBoxItems` list in case of a `NullPointerException`.

## Usage Example

```java
// Creating an instance of DepositBox (assuming within a RuneLite plugin environment)
DepositBox depositBox = new DepositBox();

// Searching for items in the deposit box
ItemQuery query = DepositBox.search();
// Further operations can be performed using the query object

// The class mainly functions through event handling, reacting to changes in the game state.
```
# DuelItem Class Documentation

The `DuelItem` class is a part of the `com.example.EthanApiPlugin.Collections` package, specifically designed for the `EthanApiPlugin`. This class encapsulates a simple data structure for items used in a duel setting, presumably for a RuneLite client plugin.

## Class Overview

- **Package**: `com.example.EthanApiPlugin.Collections`
- **Dependencies**:
  - `net.runelite.api.widgets.Widget`

## Class Description

The `DuelItem` class represents an item in a duel context. Each instance of `DuelItem` contains information about the item, including its widget (which is presumably clickable in the RuneLite client UI) and its name.

## Constructor

- `DuelItem(Widget widg, String Name)`
  - Constructs a new `DuelItem` with the specified widget and name.
  - **Parameters**:
    - `Widget widg`: The widget associated with the duel item.
    - `String Name`: The name of the duel item.

## Methods

### Public Methods

- `public Widget getClickable()`
  - Returns the widget associated with this duel item.
  - **Return Type**: `Widget`

- `public String getName()`
  - Returns the name of the duel item.
  - **Return Type**: `String`

## Usage Example

```java
// Assuming you have a widget and a name for the duel item
Widget itemWidget = ...; // Widget instance for the duel item
String itemName = "Sword of Valor";

// Creating a DuelItem instance
DuelItem duelItem = new DuelItem(itemWidget, itemName);

// Accessing properties of the duel item
Widget clickable = duelItem.getClickable(); // gets the associated widget
String name = duelItem.getName(); // gets the name of the item
```
# Equipment Class Documentation

The `Equipment` class, part of the `com.example.EthanApiPlugin.Collections` package, is designed for managing equipment items in a RuneLite client plugin. This class is crucial for tracking and manipulating the state of equipment items within the game.

## Class Overview

- **Package**: `com.example.EthanApiPlugin.Collections`
- **Dependencies**:
  - `com.example.EthanApiPlugin.Collections.query.EquipmentItemQuery`
  - `java.util.ArrayList`
  - `java.util.HashMap`
  - `java.util.List`
  - `net.runelite.api.*`
  - `net.runelite.api.widgets.Widget`
  - `net.runelite.api.widgets.WidgetInfo`
  - `net.runelite.client.RuneLite`
  - `net.runelite.client.eventbus.Subscribe`

## Class Description

The `Equipment` class is responsible for monitoring and handling changes in the player's equipment. It subscribes to events related to equipment changes and updates its internal state accordingly. The class provides functionalities to search and retrieve equipment item data.

## Constructor

- `public Equipment()`
  - Basic constructor, initializes the `Equipment` object.

## Methods

### Public Static Methods

- `public static EquipmentItemQuery search()`
  - Initiates a search among the collected equipment items.
  - **Returns**: `EquipmentItemQuery` object for querying equipment items.

- `public static void RetryCollection()`
  - Placeholder method. Currently, it has no implementation.

### Event Handler Methods

- `@Subscribe public void onItemContainerChanged(ItemContainerChanged e)`
  - Handles changes in the item container, particularly the equipment section.
  - **Parameters**: `ItemContainerChanged e` - the event representing a change in the item container.

## Static Initialization

- Initializes two HashMaps: `equipmentSlotWidgetMapping` and `mappingToIterableIntegers`, which are used to map equipment slots to their respective widget IDs and to provide an iterable mapping for these slots, respectively.

## Usage Example

```java
// Assuming the class is part of a RuneLite plugin
Equipment equipmentManager = new Equipment();

// Subscribing to the item container change event is managed by the RuneLite client's event handling mechanism.

// To search for equipment items
EquipmentItemQuery query = Equipment.search();
// Further query operations can be performed on the 'query' object as per 'EquipmentItemQuery' class functionalities
```
# ETileItem Class 

## Overview

The `ETileItem` class represents a tile item in a specific location in the game world. This class is part of a plugin for an API, indicated by its package structure `com.example.EthanApiPlugin.Collections`. It interacts with game world items through mouse actions and manages their location.

## Class Declaration

```java
public class ETileItem
```

## Properties

- `WorldPoint location`: A `WorldPoint` object representing the geographical location of the tile item in the game world.
- `TileItem tileItem`: The `TileItem` object associated with this instance. It represents the actual item on the tile.

## Constructor

```java
public ETileItem(WorldPoint worldLocation, TileItem tileItem)
```

Initializes a new instance of the `ETileItem` class.

### Parameters

- `WorldPoint worldLocation`: The world location of the tile item.
- `TileItem tileItem`: The tile item to be associated with this instance.

## Methods

### `getLocation()`

```java
public WorldPoint getLocation()
```

Returns the world location of the tile item.

#### Returns

- `WorldPoint`: The geographical location of the tile item.

### `getTileItem()`

```java
public TileItem getTileItem()
```

Returns the tile item associated with this instance.

#### Returns

- `TileItem`: The tile item.

### `interact(boolean ctrlDown)`

```java
public void interact(boolean ctrlDown)
```

Sends an interaction request for the tile item. This typically involves a mouse click action.

#### Parameters

- `boolean ctrlDown`: A flag indicating whether the control key is held down during interaction.

## Usage Example

```java
WorldPoint location = new WorldPoint(x, y, z);
TileItem myTileItem = new TileItem(); // Assuming TileItem is properly instantiated
ETileItem eTileItem = new ETileItem(location, myTileItem);

WorldPoint itemLocation = eTileItem.getLocation();
TileItem tileItem = eTileItem.getTileItem();
eTileItem.interact(false);
```

# Inventory Class 

## Overview

The `Inventory` class is a component of an API plugin, designed to manage and interact with the inventory in a game, presumably a RuneLite client-based game like Old School RuneScape. This class provides functionalities such as querying inventory items, checking the number of empty slots, and handling inventory changes.

## Class Declaration

```java
public class Inventory
```

## Properties

- `static Client client`: An instance of the game client, acquired from the RuneLite injector.
- `static List<Widget> inventoryItems`: A dynamic list of widgets representing items in the inventory.

## Constructor

### `Inventory()`

The default constructor. It's empty and doesn't perform any initialization.

## Methods

### `search()`

```java
public static ItemQuery search()
```

Creates a new `ItemQuery` instance to query inventory items.

#### Returns

- `ItemQuery`: An instance to facilitate inventory item querying.

### `getEmptySlots()`

```java
public static int getEmptySlots()
```

Calculates the number of empty slots in the inventory.

#### Returns

- `int`: The number of empty inventory slots.

### `full()`

```java
public static boolean full()
```

Checks if the inventory is full.

#### Returns

- `boolean`: `true` if the inventory is full, `false` otherwise.

### `getItemAmount(int itemId)`

```java
public static int getItemAmount(int itemId)
```

Gets the quantity of a specific item in the inventory based on its ID.

#### Parameters

- `int itemId`: The ID of the item.

#### Returns

- `int`: The quantity of the specified item in the inventory.

### `getItemAmount(String itemName)`

```java
public static int getItemAmount(String itemName)
```

Gets the quantity of a specific item in the inventory based on its name.

#### Parameters

- `String itemName`: The name of the item.

#### Returns

- `int`: The quantity of the specified item in the inventory.

### `reloadInventory()`

```java
public static void reloadInventory()
```

Reloads the inventory items from the client's inventory widget.

### Event Handlers

#### `onItemContainerChanged(ItemContainerChanged e)`

Handles the `ItemContainerChanged` event, which is triggered when an item container (like the inventory) changes.

#### Parameters

- `ItemContainerChanged e`: The event data.

#### `onGameStateChanged(GameStateChanged gameStateChanged)`

Handles the `GameStateChanged` event, triggered when the game's state changes (e.g., logging in, hopping worlds).

#### Parameters

- `GameStateChanged gameStateChanged`: The event data.

## Usage Example

```java
// Assuming this code is part of a RuneLite plugin
Inventory inventory = new Inventory();

// Querying the inventory
ItemQuery query = Inventory.search();
int emptySlots = Inventory.getEmptySlots();
boolean isFull = Inventory.full();
int itemAmountById = Inventory.getItemAmount(1234); // Example item ID
int itemAmountByName = Inventory.getItemAmount("itemName"); // Example item name

// React to inventory changes in event handlers
public void onEvent(ItemContainerChanged event) {
    // Process event
}
```

# NPCs Class 
## Overview

The `NPCs` class is a part of an API plugin, designed to manage and interact with non-player characters (NPCs) in a game, likely within a RuneLite client-based game like Old School RuneScape. This class provides the functionality to query NPCs and handle updates to the list of NPCs on each game tick.

## Class Declaration

```java
public class NPCs
```

## Properties

- `static Client client`: An instance of the game client, acquired from the RuneLite injector.
- `private static final List<NPC> npcList`: A list that holds the NPCs currently in the game.

## Constructor

### `NPCs()`

The default constructor is empty and doesn't perform any specific operations.

## Methods

### `search()`

```java
public static NPCQuery search()
```

Creates a new `NPCQuery` instance for querying NPCs.

#### Returns

- `NPCQuery`: An instance to facilitate NPC querying.

## Event Handlers

### `onGameTick(GameTick e)`

Handles the `GameTick` event, updating the list of NPCs each game tick.

#### Parameters

- `GameTick e`: The event data associated with each game tick.

#### Description

- This method clears the `npcList` and repopulates it with current NPCs in the game, ensuring the list remains up to date. It filters out null NPCs and those with an ID of -1.

## Usage Example

```java
// Assuming this code is part of a RuneLite plugin
NPCs npcs = new NPCs();

// Querying NPCs
NPCQuery query = NPCs.search();
// Perform queries using query object

// React to game tick in event handlers
public void onEvent(GameTick event) {
    // Process event
}
```

# Players Class

## Overview

The `Players` class is part of an API plugin designed to interact with player entities in a game environment, presumably within a RuneLite client-based game like Old School RuneScape. This class allows for querying player entities and dynamically updating the player list on each game tick.

## Class Declaration

```java
public class Players
```

## Properties

- `static List<Player> players`: A list to store player entities currently present in the game.
- `static Client client`: An instance of the game client, obtained from the RuneLite injector.

## Constructor

### `Players()`

The default constructor is empty and is primarily used for initializing the class.

## Methods

### `search()`

```java
public static PlayerQuery search()
```

Creates and returns a new instance of `PlayerQuery` for querying players.

#### Returns

- `PlayerQuery`: An instance that enables querying various player attributes and states.

## Event Handlers

### `onGameTick(GameTick e)`

Responds to the `GameTick` event, updating the list of players.

#### Parameters

- `GameTick e`: The event object that is triggered on each game tick.

#### Description

- This method clears the existing `players` list and repopulates it with the current players in the game. It ensures that the player list is up to date with the game state, excluding null players.

## Usage Example

```java
// Assuming this code is part of a RuneLite plugin
Players playersInstance = new Players();

// To query players
PlayerQuery query = Players.search();
// Use the query object to filter or find specific players

// Handle game tick in an event method
public void onEvent(GameTick event) {
    // Process game tick event
}
```
# ShopInventory Class 

## Overview

The `ShopInventory` class, as part of the `com.example.EthanApiPlugin.Collections` package, is designed for managing and querying inventory items within a shop environment in a RuneLite client-based game, such as Old School RuneScape. This class provides functionality to track and search through the items available in a game shop's inventory.

## Class Declaration

```java
public class ShopInventory
```

## Properties

- `static Client client`: An instance of the game client, retrieved from the RuneLite injector.
- `static List<Widget> shopInventoryItems`: A list to store the widget items representing the shop's inventory.

## Constructor

### `ShopInventory()`

The default constructor is empty and is used primarily for class initialization.

## Methods

### `search()`

```java
public static ItemQuery search()
```

Creates and returns a new instance of `ItemQuery` for querying shop inventory items.

#### Returns

- `ItemQuery`: An instance that enables querying various item attributes and states within the shop inventory.

## Event Handlers

### `onWidgetLoaded(WidgetLoaded e)`

Updates the shop inventory items when a shop widget is loaded.

#### Parameters

- `WidgetLoaded e`: The event object triggered when a widget is loaded.

### `onItemContainerChanged(ItemContainerChanged e)`

Updates the shop inventory items when the item container changes.

#### Parameters

- `ItemContainerChanged e`: The event object triggered when an item container within the game changes.

### `onGameStateChanged(GameStateChanged gameStateChanged)`

Clears the shop inventory items list on certain game state changes.

#### Parameters

- `GameStateChanged gameStateChanged`: The event object triggered when the game state changes.

## Usage Example

```java
// Assuming this code is part of a RuneLite plugin
ShopInventory shopInventory = new ShopInventory();

// To query shop inventory items
ItemQuery query = ShopInventory.search();
// Use the query object to filter or find specific items

// Handling various events to update shop inventory
public void onEvent(WidgetLoaded event) {
    // Process widget loaded event
}

public void onEvent(ItemContainerChanged event) {
    // Process item container changed event
}

public void onEvent(GameStateChanged event) {
    // Process game state changed event
}
```

# TileItems Class 

## Overview

The `TileItems` class, residing in the `com.example.EthanApiPlugin.Collections` package, is designed to manage and facilitate querying of tile items in a gaming environment, likely in the context of a RuneLite client-based game. This class provides a convenient way to handle collections of `ETileItem` objects, representing items on tiles in the game world.

## Class Declaration

```java
public class TileItems
```

## Properties

- `public static List<ETileItem> tileItems`: A static list that stores instances of `ETileItem`. It serves as a central repository of tile items accessible throughout the application.

## Constructor

### `TileItems()`

The default constructor, which is empty. Its primary purpose is to initialize the `TileItems` class.

## Methods

### `search()`

```java
public static TileItemQuery search()
```

Creates and returns an instance of `TileItemQuery`, which can be used to perform queries on the list of `ETileItem` instances.

#### Returns

- `TileItemQuery`: This object allows querying within the list of tile items based on various criteria (e.g., location, item properties).

## Usage Example

```java
// Instance creation of TileItems class
TileItems tileItems = new TileItems();

// To search for tile items based on certain criteria
TileItemQuery query = TileItems.search();
// Further actions can be performed using the query object, like filtering

// Example: Finding a specific tile item based on a custom criterion
// (assuming relevant methods are defined in TileItemQuery)
ETileItem specificItem = query.findItemBySomeCriterion(...);
```
# TileObjects Class 

## Overview

The `TileObjects` class, part of the `com.example.EthanApiPlugin.Collections` package, is designed for managing and querying tile objects within a RuneLite client environment. It handles various types of objects on tiles, such as game objects, ground objects, wall objects, and decorative objects, offering a dynamic way to interact with the game's environment.

## Class Declaration

```java
public class TileObjects
```

## Properties

- `static Client client`: A static instance of the `Client`, obtained from the RuneLite injector. This client instance is used to interact with the game client.
- `static List<TileObject> tileObjects`: A static list that holds `TileObject` instances. It serves as a central collection of tile objects present in the game.

## Constructor

### `TileObjects()`

The default constructor, which is empty. It is used for initializing an instance of the `TileObjects` class.

## Methods

### `search()`

```java
public static TileObjectQuery search()
```

- **Purpose**: Creates and returns a `TileObjectQuery` instance, allowing for queries to be performed on the `tileObjects` list.
- **Returns**: `TileObjectQuery` - An instance to facilitate querying within the list of tile objects.

### `onGameTick(GameTick e)`

```java
@Subscribe(priority = 10000.0F)
public void onGameTick(GameTick e)
```

- **Purpose**: Event handler that updates the `tileObjects` list on each game tick. It clears the current list and repopulates it based on the current state of the game.
- **Parameters**:
  - `GameTick e`: The `GameTick` event object.
- **Behavior**:
  1. Clears the `tileObjects` list and `TileItems.tileItems` list.
  2. Iterates through all tiles in the current scene and adds relevant objects to the `tileObjects` list.
  3. Handles various types of objects (game objects, ground objects, wall objects, and decorative objects) and adds them to the list if they meet certain conditions (like having a valid ID).

## Usage Example

```java
TileObjects tileObjects = new TileObjects();

// To query tile objects
TileObjectQuery query = TileObjects.search();
// Perform queries using the query object

// Example: Find a specific tile object by some criterion
// (Assuming relevant methods are defined in TileObjectQuery)
TileObject specificObject = query.findObjectByCriterion(...);
```
# Widgets Class 

## Overview

The `Widgets` class in the `com.example.EthanApiPlugin.Collections` package is designed to facilitate the retrieval and querying of UI widgets within the RuneLite client environment. It leverages the RuneLite API to access and process widget data, providing a comprehensive method to explore the widget hierarchy in the game's UI.

## Class Declaration

```java
public class Widgets
```

## Properties

- `static Client client`: A static instance of `Client`, obtained from the RuneLite injector. This instance is used to interact with the game client and access widget information.

## Constructor

### `Widgets()`

The default constructor, which is empty. It is used for creating an instance of the `Widgets` class.

## Methods

### `search()`

```java
public static WidgetQuery search()
```

- **Purpose**: To compile a comprehensive set of all widgets present in the RuneLite client UI, including dynamic, nested, and static children of each widget.
- **Returns**: `WidgetQuery` - An instance to facilitate querying within the collected set of widgets.
- **Algorithm**:
  1. Initialize a `HashSet<Widget>` to store unique widgets.
  2. Initialize a `Queue<Widget>` with root widgets.
  3. Iterate through the queue:
     - Poll a widget from the queue.
     - If it's not null, process its children (dynamic, nested, static) by adding them to the queue and the HashSet.
  4. Repeat until the queue is empty.
  5. Return a new `WidgetQuery` object with the HashSet of widgets.

## Usage Example

```java
Widgets widgets = new Widgets();

// To query widgets
WidgetQuery query = Widgets.search();
// Perform queries using the query object

// Example: Find a specific widget by some criterion
// (Assuming relevant methods are defined in WidgetQuery)
Widget specificWidget = query.findWidgetByCriterion(...);
```




### InteractionAPI

#### Overview

InteractionAPI allows for the automation of game entity interactions by sending specific packets associated with click events. This facilitates interactions with NPCs, ground items, inventory items, and bank items.

[Insert More Detailed Description or Subsections about InteractionAPI Here]

#### API Endpoints (InteractionAPI)
# BankInteraction Class Documentation

## Overview

The `BankInteraction` class, part of the `com.example.InteractionApi` package, is designed to provide an API for automating interactions with the bank within the RuneLite client, specifically for the game Old School RuneScape (OSRS). It allows for programmatically performing actions such as withdrawing items, using items from the bank, and setting custom withdrawal quantities.

## Class Declaration

```java
public class BankInteraction
```

## Properties

- `private static final int WITHDRAW_QUANTITY`: A constant representing the varbit ID for the withdrawal quantity in the bank. This is used to manage custom withdrawal amounts.

## Constructor

### `BankInteraction()`

The default constructor is empty and is used for creating instances of the `BankInteraction` class.

## Methods

### `useItem(String name, String... actions)`

- **Purpose**: To use an item from the bank with a specified name and perform actions on it.
- **Parameters**:
  - `String name`: The name of the item to be used.
  - `String... actions`: The actions to be performed on the item.
- **Returns**: `boolean` - `true` if the action is successful, `false` otherwise.
- **Implementation**:
  - Searches for the item by name in the bank.
  - Queues a click packet and performs specified widget actions on the item.
  - Returns `true` if successful.

### `useItem(int id, String... actions)`

- Similar to `useItem(String name, String... actions)` but identifies the item by its ID instead of its name.

### `useItem(Predicate<? super Widget> predicate, String... actions)`

- **Purpose**: To use an item from the bank that matches a given predicate.
- **Parameters**:
  - `Predicate<? super Widget> predicate`: The condition that the item must satisfy.
- Similar to the other `useItem` methods in terms of functionality.

### `withdrawX(Widget item, int amount)`

- **Purpose**: To withdraw a specific quantity of an item from the bank.
- **Parameters**:
  - `Widget item`: The item to be withdrawn.
  - `int amount`: The amount to withdraw.
- **Implementation**:
  - Checks if the current withdrawal amount is the same as requested; if so, it performs the withdrawal.
  - Otherwise, sets the custom withdrawal amount and executes the necessary client script to update this value.

### `useItemIndex(int index, String... actions)`

- Similar to `useItem(String name, String... actions)` but identifies the item by its index in the bank.

### `useItem(Widget item, String... actions)`

- **Purpose**: To use a specific widget item and perform actions on it.
- **Parameters**:
  - `Widget item`: The item widget to be used.
- Similar to the other `useItem` methods in terms of functionality.

## Usage Example

```java
// Use an item by name
boolean success = BankInteraction.useItem("Rune Essence", "Withdraw-All");

// Withdraw a specific quantity of an item
Widget runeEssenceWidget = // Obtain widget for Rune Essence
BankInteraction.withdrawX(runeEssenceWidget, 500);
```
# BankInventoryInteraction Class 

## Overview

The `BankInventoryInteraction` class, part of the `com.example.InteractionApi` package, is designed to facilitate interactions with the player's bank inventory within the RuneLite client for Old School RuneScape (OSRS). This class provides methods to automate various actions on items within the bank inventory, such as using or applying actions to specific items.

## Class Declaration

```java
public class BankInventoryInteraction
```

## Constructor

### `BankInventoryInteraction()`

The default constructor is empty and is used for creating instances of the `BankInventoryInteraction` class.

## Methods

### `useItem(String name, String... actions)`

- **Purpose**: To use an item from the bank inventory by its name.
- **Parameters**:
  - `String name`: The name of the item.
  - `String... actions`: The actions to be performed on the item.
- **Returns**: `boolean` - `true` if the action is successful, `false` otherwise.
- **Implementation**:
  - Searches for the item by name in the bank inventory.
  - Queues a click packet and performs the specified widget actions on the item.
  - Uses `Optional` to handle the presence or absence of the item.

### `useItem(int id, String... actions)`

- Similar to `useItem(String name, String... actions)` but uses the item's ID to find it in the bank inventory.

### `useItem(Predicate<? super Widget> predicate, String... actions)`

- **Purpose**: To use an item from the bank inventory that matches a given predicate.
- **Parameters**:
  - `Predicate<? super Widget> predicate`: The condition to match the item.
- **Implementation**: Similar to `useItem(String name, String... actions)`, but uses a predicate for item selection.

### `useItemIndex(int index, String... actions)`

- **Purpose**: To use an item based on its index in the bank inventory.
- **Parameters**:
  - `int index`: The index of the item in the bank inventory.
- **Implementation**: Similar to other `useItem` methods, but selects the item based on its index.

### `useItem(Widget item, String... actions)`

- **Purpose**: To directly use a specified `Widget` item from the bank inventory.
- **Parameters**:
  - `Widget item`: The item widget to be used.
- **Returns**: `boolean` - `true` if the item is not null and the action is performed, `false` otherwise.
- **Implementation**:
  - Checks if the provided `Widget` item is not null.
  - If not null, queues a click packet and performs the specified actions on the item.

## Usage Example

```java
// Use an item by name
boolean success = BankInventoryInteraction.useItem("Lobster", "Eat");

// Use an item by its ID
boolean used = BankInventoryInteraction.useItem(377, "Cook");

// Use an item based on a custom condition (predicate)
boolean usedWithPredicate = BankInventoryInteraction.useItem(
    widget -> widget.getName().contains("Potion"),
    "Drink"
);
```
# InteractionHelper Class 

## Overview

The `InteractionHelper` class, part of the `com.example.InteractionApi` package, is designed to assist in performing interactions related to prayers in the Old School RuneScape (OSRS) game. This class provides methods to toggle individual or multiple prayers as well as the quick prayer feature.

## Class Declaration

```java
public class InteractionHelper
```

## Constructor

### `InteractionHelper()`

The default constructor is empty and is used for creating instances of the `InteractionHelper` class.

## Class Fields

### `quickPrayerWidgetID`

- **Type**: `int`
- **Description**: Stores the packed widget ID for the quick prayer widget on the minimap.
- **Initialization**: Set to the packed ID of `MINIMAP_QUICK_PRAYER_ORB` from `WidgetInfo`.

## Methods

### `toggleNormalPrayer(int packedWidgID)`

- **Purpose**: To toggle a specific prayer on or off.
- **Parameters**:
  - `int packedWidgID`: The packed widget ID of the prayer to be toggled.
- **Implementation**:
  - Queues a click packet and then sends a widget action packet to toggle the specified prayer.

### `toggleNormalPrayers(List<Integer> packedWidgIDs)`

- **Purpose**: To toggle multiple prayers on or off.
- **Parameters**:
  - `List<Integer> packedWidgIDs`: A list of packed widget IDs for the prayers to be toggled.
- **Implementation**:
  - Iterates through the list of widget IDs and performs the same action as `toggleNormalPrayer` for each ID.

### `togglePrayer()`

- **Purpose**: To toggle the quick prayer feature on or off.
- **Implementation**:
  - Queues a click packet at coordinates (0, 0), then sends a widget action packet using the `quickPrayerWidgetID`.

## Usage Example

```java
// Toggle a single normal prayer
InteractionHelper.toggleNormalPrayer(WidgetInfo.PRAYER_THICK_SKIN.getPackedId());

// Toggle multiple normal prayers
List<Integer> prayers = Arrays.asList(
    WidgetInfo.PRAYER_BURST_OF_STRENGTH.getPackedId(),
    WidgetInfo.PRAYER_CLARITY_OF_THOUGHT.getPackedId()
);
InteractionHelper.toggleNormalPrayers(prayers);

// Toggle quick prayers
InteractionHelper.togglePrayer();
```

# InventoryInteraction Class

## Overview

The `InventoryInteraction` class, part of the `com.example.InteractionApi` package, is designed to facilitate various interactions with the player's inventory in Old School RuneScape (OSRS) through the RuneLite client. This class offers methods for performing actions on items within the inventory, such as using, dropping, or equipping them.

## Class Declaration

```java
public class InventoryInteraction
```

## Constructor

### `InventoryInteraction()`

The default constructor is empty and serves the purpose of creating instances of the `InventoryInteraction` class.

## Methods

### `useItem(String name, String... actions)`

- **Purpose**: To perform specified actions on the first item in the inventory with a given name.
- **Parameters**:
  - `String name`: The name of the item.
  - `String... actions`: The actions to be performed on the item.
- **Returns**: `boolean` - `true` if the action was successful, `false` otherwise.

### `useItem(int id, String... actions)`

- **Purpose**: To perform specified actions on the first item in the inventory with a given ID.
- **Parameters**:
  - `int id`: The ID of the item.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successful, `false` otherwise.

### `useItem(Set<Integer> id, String... actions)`

- **Purpose**: To perform actions on the first item in the inventory matching any ID in a given set.
- **Parameters**:
  - `Set<Integer> id`: A set of item IDs.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successful, `false` otherwise.

### `useItem(Predicate<? super Widget> predicate, String... actions)`

- **Purpose**: To perform actions on the first item in the inventory that matches a specified condition.
- **Parameters**:
  - `Predicate<? super Widget> predicate`: The condition to match.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successful, `false` otherwise.

### `useItemIndex(int index, String... actions)`

- **Purpose**: To perform specified actions on the item at a specific inventory index.
- **Parameters**:
  - `int index`: The index of the item in the inventory.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successful, `false` otherwise.

### `useItem(Widget item, String... actions)`

- **Purpose**: To perform specified actions on a given widget item.
- **Parameters**:
  - `Widget item`: The widget item to act upon.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successful, `false` if the item is `null`.

## Usage Example

```java
// Use an item named "Health Potion" in the inventory
InventoryInteraction.useItem("Health Potion", "Drink");

// Use an item with ID 12345
InventoryInteraction.useItem(12345, "Use");

// Use an item from a set of IDs
Set<Integer> ids = new HashSet<>(Arrays.asList(123, 456, 789));
InventoryInteraction.useItem(ids, "Examine");

// Use an item at a specific index in the inventory
InventoryInteraction.useItemIndex(3, "Drop");
```
# NPCInteraction Class 

## Overview

The `NPCInteraction` class, part of the `com.example.InteractionApi` package, is designed for interacting with non-player characters (NPCs) in Old School RuneScape (OSRS), as facilitated by the RuneLite client. This class provides methods to perform actions on NPCs based on various criteria like name, ID, or custom conditions.

## Class Declaration

```java
public class NPCInteraction
```

## Constructor

### `NPCInteraction()`

- **Purpose**: Constructs an instance of the `NPCInteraction` class. The constructor is empty and primarily serves for instantiation.

## Methods

### `interact(String name, String... actions)`

- **Purpose**: To interact with the first NPC found with a given name.
- **Parameters**:
  - `String name`: The name of the NPC.
  - `String... actions`: The actions to be performed on the NPC.
- **Returns**: `boolean` - `true` if the action was successfully executed, `false` otherwise.

### `interact(int id, String... actions)`

- **Purpose**: To interact with the first NPC found with a specific ID.
- **Parameters**:
  - `int id`: The ID of the NPC.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successfully executed, `false` otherwise.

### `interact(Predicate<? super NPC> predicate, String... actions)`

- **Purpose**: To interact with the first NPC that meets a specified condition.
- **Parameters**:
  - `Predicate<? super NPC> predicate`: The condition the NPC must fulfill.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successfully executed, `false` otherwise.

### `interactIndex(int index, String... actions)`

- **Purpose**: To interact with an NPC at a specific index in the NPCs collection.
- **Parameters**:
  - `int index`: The index of the NPC.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successfully executed, `false` otherwise.

### `interact(NPC npc, String... actions)`

- **Purpose**: To perform specified actions on a given NPC object.
- **Parameters**:
  - `NPC npc`: The NPC to interact with.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - `true` if the action was successfully executed, `false` if the NPC is `null` or its composition is `null`.

## Usage Example

```java
// Interact with an NPC named "Banker" to open a bank dialog
NPCInteraction.interact("Banker", "Bank");

// Interact with an NPC with ID 1234
NPCInteraction.interact(1234, "Talk-to");

// Interact with an NPC fulfilling a custom condition
NPCInteraction.interact(npc -> npc.getName().equals("Fisherman"), "Trade");
```
# PlayerInteractionHelper Class 

## Overview

The `PlayerInteractionHelper` class is a part of the `com.example.InteractionApi` package, specifically designed for interacting with player characters in Old School RuneScape (OSRS) through the RuneLite client. This class provides various methods to facilitate actions on both specific player objects and players identified by certain characteristics such as name or a custom condition.

## Class Declaration

```java
public class PlayerInteractionHelper
```

## Constructor

### `PlayerInteractionHelper()`

- **Purpose**: Instantiates a new `PlayerInteractionHelper` object. The constructor does not contain any specific implementation, serving primarily for instantiation purposes.

## Methods

### `interact(Player player, String... actions)`

- **Purpose**: To perform specified actions on a given `Player` object.
- **Parameters**:
  - `Player player`: The player to interact with.
  - `String... actions`: The actions to be executed on the player.
- **Returns**: `boolean` - Returns `true` if the player is not `null` and actions are executed; otherwise, returns `false`.

### `interact(String name, String... actions)`

- **Purpose**: To interact with the first player found with a given name.
- **Parameters**:
  - `String name`: The name of the player to be interacted with.
  - `String... actions`: The actions to be performed.
- **Returns**: `boolean` - Returns `true` if the action was successfully executed on a player with the specified name; otherwise, returns `false`.

### `interact(Predicate<? super Player> predicate, String... actions)`

- **Purpose**: To interact with the first player who fulfills a specified condition.
- **Parameters**:
  - `Predicate<? super Player> predicate`: The condition that identifies the player.
  - `String... actions`: The actions to be performed on the player.
- **Returns**: `boolean` - Returns `true` if the action was successfully executed on a player meeting the condition; otherwise, returns `false`.

## Usage Example

```java
// Interact with a specific player object
Player targetPlayer = ... // Assume targetPlayer is a Player object
PlayerInteractionHelper.interact(targetPlayer, "Trade");

// Interact with a player named "OsrsPlayer1"
PlayerInteractionHelper.interact("OsrsPlayer1", "Challenge");

// Interact with a player fulfilling a custom condition (e.g., specific level)
PlayerInteractionHelper.interact(
    player -> player.getCombatLevel() > 100,
    "Follow"
);
```
# PrayerInteraction Class 

## Overview

The `PrayerInteraction` class in the `com.example.InteractionApi` package is designed to manage and interact with the prayer abilities in Old School RuneScape (OSRS) through the RuneLite client. It provides methods for toggling individual prayers, changing prayer states, and performing prayer flicks.

## Class Declaration

```java
public class PrayerInteraction
```

## Fields

### `public static final HashMap<Prayer, WidgetInfoExtended> prayerMap`

- **Purpose**: Stores a mapping between `Prayer` enums and their corresponding `WidgetInfoExtended` objects, which contain extended information about the widget (UI element) associated with each prayer.

## Constructor

### `PrayerInteraction()`

- **Purpose**: Instantiates a new `PrayerInteraction` object. This constructor does not contain specific implementation details and serves primarily for object instantiation.

## Methods

### `togglePrayer(Prayer a)`

- **Purpose**: Toggles the state of a specified prayer.
- **Parameters**:
  - `Prayer a`: The prayer to be toggled.
- **Implementation**: Checks the current state of the specified prayer and toggles it using the `setVarbit` method of `EthanApiPlugin.getClient()`. It then queues a click packet and sends a widget action packet for the prayer's widget.

### `setPrayerState(Prayer prayer, boolean on)`

- **Purpose**: Sets the specified prayer to a particular state (on or off).
- **Parameters**:
  - `Prayer prayer`: The prayer to be modified.
  - `boolean on`: The desired state of the prayer (true for on, false for off).
- **Implementation**: Checks if the current state of the prayer differs from the desired state and, if so, toggles the prayer.

### `flickPrayers(Prayer... prayers)`

- **Purpose**: Performs prayer flicking for a specified set of prayers.
- **Parameters**:
  - `Prayer... prayers`: A variable number of prayers to be flicked.
- **Implementation**: Turns off all prayers in the `prayerMap` and then turns on the specified prayers.

## Static Block

The static block initializes the `prayerMap` with key-value pairs mapping each `Prayer` enum to its corresponding `WidgetInfoExtended` object.

## Usage Example

```java
// Toggling a single prayer
PrayerInteraction.togglePrayer(Prayer.PIETY);

// Setting a prayer to a specific state
PrayerInteraction.setPrayerState(Prayer.PROTECT_FROM_MAGIC, true);

// Flicking multiple prayers
PrayerInteraction.flickPrayers(Prayer.PIETY, Prayer.PROTECT_FROM_MELEE);
```

# TileObjectInteraction Class 

## Overview

The `TileObjectInteraction` class in the `com.example.InteractionApi` package is designed for interacting with tile objects within Old School RuneScape (OSRS) through the RuneLite client. This class allows for actions to be performed on tile objects, such as interacting with doors, trees, or any other interactable object found in the game environment.

## Class Declaration

```java
public class TileObjectInteraction
```

## Constructor

### `TileObjectInteraction()`

- **Purpose**: Constructs a new `TileObjectInteraction` object. The constructor is empty and serves primarily for object instantiation.

## Methods

### `interact(String name, String... actions)`

- **Purpose**: Interacts with the first tile object found with the specified name.
- **Parameters**:
  - `String name`: The name of the tile object to interact with.
  - `String... actions`: A varargs array of action strings to be performed.
- **Return**: `boolean` - Returns `true` if the interaction is successful, `false` otherwise.
- **Implementation**: Utilizes `TileObjects.search().withName(name)` to find the object, queues a click packet, and sends object action packets.

### `interact(int id, String... actions)`

- **Purpose**: Interacts with the first tile object found with the specified ID.
- **Parameters**:
  - `int id`: The ID of the tile object to interact with.
  - `String... actions`: A varargs array of action strings to be performed.
- **Return**: `boolean` - Returns `true` if the interaction is successful, `false` otherwise.
- **Implementation**: Similar to `interact(String name, String... actions)` but searches for the object by its ID.

### `interact(TileObject tileObject, String... actions)`

- **Purpose**: Interacts with a specified `TileObject`.
- **Parameters**:
  - `TileObject tileObject`: The specific tile object to interact with.
  - `String... actions`: A varargs array of action strings to be performed.
- **Return**: `boolean` - Returns `true` if the interaction is successful, `false` if the `tileObject` is null or its composition cannot be retrieved.
- **Implementation**: Checks if the `tileObject` and its composition are not null, queues a click packet, and sends object action packets.

## Usage Example

```java
// Interacting with a tile object by name
TileObjectInteraction.interact("Bank Booth", "Use");

// Interacting with a tile object by ID
TileObjectInteraction.interact(12345, "Chop down");

// Interacting with a specific tile object
TileObject tile = ... // some TileObject
TileObjectInteraction.interact(tile, "Examine");
```

