
public class ShotEntity extends Entity {
	private Game game;
	private double moveSpeed = -270;
	private boolean used = false;
	private int otherType;
	public ShotEntity(Game game, String ref, int x, int y){
		super(ref,x,y);
		this.game = game;
		dy = moveSpeed;
	}
	
	public void move(long delta){
		super.move(delta);
		
		if(y< -100){
			game.removeEntity(this);
		}
	}
	public void collidedWith(Entity other){
		if (AlienEntity.class.isInstance(other)){
			game.removeEntity(this);
			otherType = other.getType();
			game.removeEntity(other);
			game.alienKilled(otherType);
		}
	}

	@Override
	public int getType() {
				return 0;
	}
	
}
