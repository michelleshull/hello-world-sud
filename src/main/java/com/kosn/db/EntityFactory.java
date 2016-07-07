package com.kosn.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.kosn.entity.Ability;
import com.kosn.entity.Item;
import com.kosn.entity.NonPlayer;
import com.kosn.entity.Room;

public class EntityFactory {

	private File file;
	
	//singleton
	private static EntityFactory instance = null;
	protected EntityFactory() {
	}
	public static EntityFactory getInstance() {
		if(instance == null) {
			instance = new EntityFactory();
		}
		return instance;
	}
	
	public void loadFile(String fileName) {
		File currDir = new File(".");
		String path = "";
		path = currDir.getAbsolutePath();
		path = path.substring(0, path.length()-1);
		this.file = new File(path + "src/main/resources/entityRepository/" + fileName + ".json");
	}
		
	public List<Room> createRooms() {
		loadFile("Room");
		List<Room> rooms = new ArrayList<Room>();
		try {
			rooms = parseRoomJson();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms;
	}
	
	public List<Item> createItems() {
		loadFile("Item");
		List<Item> items = new ArrayList<Item>();
		try {
			items = parseItemJson();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	
	public List<Ability> createAbilities() {
		loadFile("Ability");
		List<Ability> abilities = new ArrayList<Ability>();
		try {
			abilities = parseAbilityJson();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return abilities;
	}
	
	public List<NonPlayer> createNonPlayers() {
		loadFile("NonPlayer");
		List<NonPlayer> creatures = new ArrayList<NonPlayer>();
		try {
			creatures = parseNonPlayerJson();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return creatures;
	}

	public List<Room> parseRoomJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	List<Room> rooms = mapper.readValue(this.file,
    			TypeFactory.defaultInstance().constructCollectionType(List.class,
    					Room.class));
    	return rooms;
    }

	public List<Item> parseItemJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	List<Item> items = mapper.readValue(this.file,
    			TypeFactory.defaultInstance().constructCollectionType(List.class,
    					Item.class));
    	return items;
    }
	
	public List<NonPlayer> parseNonPlayerJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	List<NonPlayer> creatures = mapper.readValue(this.file,
    			TypeFactory.defaultInstance().constructCollectionType(List.class,
    					NonPlayer.class));
    	return creatures;
    }
	
	private List<Ability> parseAbilityJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
    	List<Ability> abilities = mapper.readValue(this.file,
    			TypeFactory.defaultInstance().constructCollectionType(List.class,
    					Ability.class));
    	return abilities;
	}
	
	//polymorphism or generics? Idk wtf to do here
//	public <E> List<E> parseJson(String fileType, Class<E> objectType) throws JsonParseException, JsonMappingException, IOException {
//		File currDir = new File(".");
//		this.path = currDir.getAbsolutePath();
//		this.path = this.path.substring(0, this.path.length()-1);
//		File file = new File(this.path + "src/main/resources/entityRepository/" + fileType + ".json");
//		List<E> stuff = new ArrayList<E>();
//		
//		ObjectMapper mapper = new ObjectMapper();
//		
//		stuff = mapper.readValue(file,
//    			TypeFactory.defaultInstance().constructCollectionType(List.class,
//    					Object.class));
//		
//		for (Object o : stuff) {
//			System.out.println(o);
//			System.out.println(o.getClass());
//		}
//		
//		return stuff;
//	}
}