package com.kosn.entity;

import java.util.List;
import java.util.Random;

import com.kosn.util.World;

public final class NonPlayerDefaults {
	public String name, description;
	public int level, money, exp, hitPoints, maxHitPoints, attack, defense;

	private World world = World.getInstance();
	
	private List<NonPlayer> creaturePool = world.getCreaturePool();
	
	int randomIndex = new Random().nextInt(creaturePool.size());
	NonPlayer randomCreature = creaturePool.get(randomIndex);
	
	private int playerLevel = world.getPlayer().getLevel();
		
	public NonPlayerDefaults() {
		this.name = randomCreature.getName();
		this.description = randomCreature.getDescription();
		this.level = this.playerLevel;
		this.money = 3;
		this.exp = world.getPlayer().getExpToNextLevel()/10;
		this.hitPoints = this.playerLevel * 2;
		this.maxHitPoints = this.playerLevel * 2;
		this.attack = this.playerLevel;
		this.defense = this.playerLevel;
	}
}
