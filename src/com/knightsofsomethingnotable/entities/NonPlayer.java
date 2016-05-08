package com.knightsofsomethingnotable.entities;
import java.util.ArrayList;
import java.util.HashMap;

import com.knightsofsomethingnotable.main.Main;
import com.knightsofsomethingnotable.management.Room;
import com.knightsofsomethingnotable.management.World;

public class NonPlayer extends Character {

    public NonPlayer(String name, String description, int level, int money, int exp, int hitPoints, int maxHitPoints,
                     int attack, int defense, ArrayList<Item> inventory, HashMap<String, Item> equipment) {
        super(name, description, level, money, exp, hitPoints, maxHitPoints, attack, defense, inventory, equipment);
    }

    public static String printNonPlayerInfo(String target, Room room) {

        NonPlayer nonPlayer = null;
        String result = "";

        for (NonPlayer creature : room.getCreatures() ) {
            if (creature.getName().equals(target)) {
                nonPlayer = creature;
                break;
            }
        }

        if (nonPlayer != null) {
            System.out.println("Name: " + nonPlayer.getName() + "(Lv." + nonPlayer.getLevel() + ")");
            System.out.println(nonPlayer.getDescription());
            System.out.println("HP: " + nonPlayer.getHitPoints() + "/" + nonPlayer.getMaxHitPoints());
            System.out.println("Attack: " + nonPlayer.getAttack());
            System.out.println("Defense: " + nonPlayer.getDefense());
            System.out.println();
            result = "creature";
        }

        return result;
    }


    public static void attackNonPlayer(String target, Room room, Player player) {

        NonPlayer nonPlayer = null;

        int playerAttack;
        int playerDefense;
        int playerLevel;
        int playerHitPoints;
        int playerMaxHitPoints;
        int playerExp;
        int playerExpToNextLevel;
        int playerMoney;
        int playerCurrentHitPoints;
        int playerNewExp;

        int targetAttack;
        int targetHitPoints;
        int targetMaxHitPoints;
        int targetExp;
        int targetMoney;
        int targetCurrentHitPoints;

        // find target
        for (NonPlayer creature : room.getCreatures() ) {
            if (creature.getName().equals(target)) {
                nonPlayer = creature;
                break;
            }
        }

        // print message and exit method if not found
        if (nonPlayer == null) {
            System.out.println("There is no " + target + " here.");
            return;
        }

        if (!Main.getCombat()) {
    		System.out.println("Combat started...");
    		System.out.println();
    		Main.setCombat(true);
        }
        
        // get player attack, defense, hp, exp, exptonextlevel
        playerAttack = player.getAttack();
        playerDefense = player.getDefense();
        playerLevel = player.getLevel();
        playerHitPoints = player.getHitPoints();
        playerMaxHitPoints = player.getMaxHitPoints();
        playerExp = player.getExp();
        playerExpToNextLevel = player.getExpToNextLevel();
        playerMoney = player.getMoney();

        // get target attack, defense, hp, exp
        targetAttack = nonPlayer.getAttack();
        targetHitPoints = nonPlayer.getHitPoints();
        targetMaxHitPoints = nonPlayer.getMaxHitPoints();
        targetExp = nonPlayer.getExp();
        targetMoney = nonPlayer.getMoney();

        // math player hp
        playerCurrentHitPoints = playerHitPoints - targetAttack;


        //if player dead
        // set hp to max hp (player and target), load cell, playermoney = playermoney * 0.9
        if (playerCurrentHitPoints <= 0) {
            player.setHitPoints(playerMaxHitPoints);
            player.setMoney((int) Math.round(playerMoney * 0.9));
            System.out.println(player.getName() + " has been knocked unconscious! " +
                    (playerMoney - player.getMoney()) + " money has been lost!");
            Main.setCombat(false);
            Main.setCurrentRoom(Main.getDefaultRoom());
            World.print(Main.getCurrentRoom());
            // load cell
            return;
        } else {
            player.setHitPoints(playerCurrentHitPoints);
            System.out.println("The " + nonPlayer.getName() + " hits " + player.getName() + " for " + targetAttack + " point(s) of damage!");
            System.out.println(player.getName() + " HP: " + playerCurrentHitPoints + "/" + playerMaxHitPoints + "\n");
        }

        // math target hp
        targetCurrentHitPoints = targetHitPoints - playerAttack;

        // if target dead
        // give xp to player, set player hp to max
        // if player exp >= exptonextlevel
        // level + 1, exptonextlevel = exptonextlevel * 1.05
        if (targetCurrentHitPoints <= 0) {
            playerNewExp = playerExp + targetExp;
            player.setExp(playerNewExp);
            player.setMoney(playerMoney + targetMoney);
            player.setHitPoints(playerMaxHitPoints);
            nonPlayer.setHitPoints(targetMaxHitPoints);
            System.out.println(player.getName() + " has defeated the " + nonPlayer.getName() + "!");
            System.out.println(player.getName() + "'s health has fully recovered!\n");
            // remove creature from room list? Right now just resets health.
            
            Main.setCombat(false);
            System.out.println();
            System.out.println("Combat ended");
            System.out.println();
            
            if (playerNewExp >= playerExpToNextLevel) {
                player.setLevel(playerLevel + 1);
                player.setExpToNextLevel((int) Math.round(playerExpToNextLevel * 1.5));
                player.setMaxHitPoints((int) Math.round(playerMaxHitPoints + 1.05));
                player.setHitPoints((int) Math.round(playerMaxHitPoints + 1.05));
                player.setAttack(playerAttack + 1);
                player.setDefense(playerDefense + 1);
                System.out.println(player.getName() + " has attained level " + player.getLevel() + "!");
            }

            System.out.println("Another " + nonPlayer.getName() + " has appeared!");

        } else {
            nonPlayer.setHitPoints(targetCurrentHitPoints);
            System.out.println(player.getName() + " hits " + nonPlayer.getName() + " for " + playerAttack + " point(s) of damage!");
            System.out.println(nonPlayer.getName() + " HP: " + targetCurrentHitPoints + "/" + targetMaxHitPoints);
        }
    }
    
    public String toString() {
		String toString = getName();
		return toString;
	}
    
    
}