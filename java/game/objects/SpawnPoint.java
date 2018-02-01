package game.objects;

import java.awt.Graphics;
import java.util.List;

import game.id.ObjectID;
import game.util.classes.GameObject;

public class SpawnPoint extends GameObject {
	/*
	 * When a player gets shot by a paintball
	 * it picks a random spawn point which then
	 * the x&y of player are set to the x&y of
	 * the spawn point.
	 */

	public SpawnPoint(int x, int y, ObjectID id) {
		super(x, y, id, 3);
	}

	@Override
	public void update(List<GameObject> objects) {
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub

	}

}
