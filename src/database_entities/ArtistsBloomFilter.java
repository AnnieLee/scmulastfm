package database_entities;

import ormdroid.Entity;

public class ArtistsBloomFilter extends Entity {
	
	public int id;
	public String bitset;
	public int expectedSize;
	public int size;

}
