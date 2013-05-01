import java.awt.Graphics;
import java.awt.Rectangle;


public abstract class Entity {
	protected double x;
	protected double y;
	protected Sprite sprite;

	protected double dx;
	protected double dy;
	
	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();
	
	public Entity(String ref, int x, int y){
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}
	public Entity (int x, int y){
		//this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}
	
	public void move(long delta){
		x += (dx*delta)/1000;
		y += (dy*delta)/1000;
	}
	
	public void draw(Graphics g){
		sprite.draw(g, (int) x, (int) y) ;
		//System.out.println("y");
	}
	public void setHMovement(double dx){
		this.dx = dx;
	}
	public void setVMovement(double dy){
		this.dy = dy;
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int) y;
	}
	public void setY(int y){
		this.y = y;
	}
	public boolean collidesWith(Entity other){
		me.setBounds((int)x, (int)y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int)other.x, (int)other.y, other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(him);
	}
	public abstract void collidedWith(Entity other);
	public abstract int getType();
	public void doLogic(){}
	public void reverse(){
		dx= -dx;
	}
	public void setSprite(Sprite sprite){
		this.sprite = sprite;
	}
}
