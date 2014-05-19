package at.meinedomain.CheckIt;

public class Point {
	private int x;
	private int y;
	
	public Point(int i, int j){
		x = i;
		y = j;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int i){
		x = i;
	}
	public void setY(int j){
		y = j;
	}
	public void setPoint(int i, int j){
		x = i;
		y = j;
	}
}
