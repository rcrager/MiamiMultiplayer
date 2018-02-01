package levelEditor.main.window;

import java.awt.Point;

public class LevelEditorPoint {
	/*
	 * This is used for the creation of rectangles mostly.
	 * get a start point and an end point.
	 */
	
	Point startPoint, endPoint;
	LevelEditorID id;
	
	public LevelEditorPoint(Point start, Point end, LevelEditorID id){
		this.startPoint = start;
		this.endPoint = end;
		this.id = id;
	}
	
	public Point getStartPoint(){
		return startPoint;
	}
	public Point getEndPoint(){
		return endPoint;
	}
	public LevelEditorID getId(){
		return id;
	}
	
	enum LevelEditorID{
		RECT,
		LINE,
		SPAWN,
		PISTOL,
		RIFLE,
		SHOTGUN,
		PAINTBRUSH;
	}
}
