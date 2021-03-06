package com.kosn.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.kosn.application.Application;
import com.kosn.util.TextHandler;
import com.kosn.util.World;

public class Player extends Character {

    private int expToNextLevel;

	public Player(PlayerDefaults pd) {
		this(pd.name, 
				pd.description, 
				pd.level, 
				pd.money, 
				pd.exp, 
				pd.expToNextLevel, 
				pd.hitPoints, 
				pd.maxHitPoints, 
				pd.attack, 
				pd.defense
				);
	}
    
    public Player(String name, 
    		String description, 
    		int level, 
    		int money, 
    		int exp, 
    		int expToNextLevel, 
    		int hitPoints, 
    		int maxHitPoints,
    		int attack, 
    		int defense
    		) {
        super(name, description, level, money, exp, hitPoints, maxHitPoints, attack, defense);
        this.expToNextLevel = expToNextLevel;
    }
    
    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public void setExpToNextLevel(int expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }
    
	public void levelUpPlayer(Player _player) {
		_player.setLevel(_player.getLevel() + 1);
		_player.setExpToNextLevel((int) Math.round(_player.getExpToNextLevel() * 1.5));
		_player.setMaxHitPoints((int) Math.round(_player.getMaxHitPoints() + 1.05));
		_player.setHitPoints(Math.round(_player.getMaxHitPoints()));
		_player.setAttack(_player.getAttack() + 1);
		_player.setDefense(_player.getDefense() + 1);
        System.out.format("%s has attained level %d!\n", _player.getName(), _player.getLevel());
	}
	
	public String killPlayer() {
		World world = World.getInstance();
		Room defaultRoom = world.getDefaultRoom();
		
		hitPoints = maxHitPoints;
		int moneyLost = ((int) Math.round(money * 0.9));
		money = money - moneyLost;
		
		if (money > 0) {
			System.out.format("You have been knocked unconscious! %d money has been lost!\n", moneyLost);
		} else {
			System.out.println("You have been knocked unconscious!");
		}

        Application.toggleCombatOff();
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        world.setCurrentRoom(defaultRoom);
        defaultRoom.toString();
        return "respawn";
	}

	public void addToExp(int _exp) {
		this.exp += _exp;
	}

	public void adjustMoney(int _money) {
		this.money += _money;
	}

	public int calculateMoneyLost(double _moneyLost) {
		return (int)Math.round(this.money * _moneyLost);
	}
	
	@Override
    public String toString() {
        return 
        		"Name: " + this.getName() + 
        		"(Lv." + this.getLevel() + ")" + 
        		"\n" +
        		"HP: " + this.getHitPoints() + "/" + this.getMaxHitPoints() +
        		"\n" +
        		"Attack: " + this.getAttack() + 
        		"\n" + 
        		"Defense: " + this.getDefense() + 
        		"\n" +
        		"Exp: " + this.getExp() + "/" + this.expToNextLevel + 
        		"\n" +
        		"Money: " + this.getMoney() + 
        		"\n";
    }

	public void putItemInInventory(String targetItemName, Room thisRoom) {
		List<Item> roomItems = new ArrayList<Item>();
		roomItems = thisRoom.getItems();
        boolean inRoom = false;
        Item item = null;
        
        // Check if item is a valid room item
        for (Item roomItem : roomItems) {
            if (roomItem.getName().equals(targetItemName) | roomItem.getName().startsWith(targetItemName)) {
                inRoom = true;
                item = roomItem;
                break;
            }
        }

        if (!inRoom) {
            System.out.println("You don't see that here.");
            return;
        }
        System.out.format("You pick up the %s.\n", item.getName());
        inventory.add(item);
        Collections.sort(inventory);
        Room.removeItem(thisRoom, item);
	}
	
	public void removeItemFromInventory(String targetItemName, Room thisRoom) {
		Item item = checkInventoryForItem(targetItemName);
        if (item == null) {
            System.out.println("You don't have that.");
            return;
        } 
        System.out.format("You put down the %s.", item.getName());
        inventory.remove(item);
        Room.addItem(thisRoom, item);
	}

	public void printStatus() {
		printInfo();
		printInventory();
		printEquipment();
	}
	
	public void printInfo() {
		TextHandler.printAsTwoColumnsLeftAligned("Name", this.getName());
		TextHandler.printAsTwoColumnsLeftAligned("HP", String.format("%d/%d", this.getHitPoints(), this.getMaxHitPoints()));
		TextHandler.printAsTwoColumnsLeftAligned("Attack", Integer.toString(this.getAttack()));
		TextHandler.printAsTwoColumnsLeftAligned("Defense", Integer.toString(this.getDefense()));
		TextHandler.printAsTwoColumnsLeftAligned("Exp", String.format("%d/%d", this.getExp(), this.getExpToNextLevel()));
		TextHandler.printAsTwoColumnsLeftAligned("Money", Integer.toString(this.getMoney()));
	}

	public void printInventory() {
        System.out.println("\nInventory:");
        for (Item item : inventory) {
            System.out.println(item.getName());
        }
	}
	
	public void printEquipment() {
		System.out.println("\nEquipment:");
        for (Entry<EquipSlot, Item> entry: equipment.entrySet()) {
            System.out.format("%s: %s\n", entry.getKey(), entry.getValue().getName());
        }
	}

	public Item checkInventoryForItem(String target) {
		for (Item checkItem: inventory) {
            if (checkItem.getName().equals(target)) {
                return checkItem;
            }
            if (checkItem.getName().startsWith(target)) {
                return checkItem;
            }
        }
		return null;
	}
	
	public Item checkEquipmentForItem(String target) {
		List<Item> equippedItems = new ArrayList<Item>(equipment.values());
		for (Item checkItem: equippedItems) {
            if (checkItem.getName().equals(target)) {
                return checkItem;
            }
        }
		for (Item checkItem: equippedItems) {
            if (checkItem.getName().startsWith(target)) {
                return checkItem;
            }
        }

		return null;
	}
}
