
public class ShipEntity extends Entity {
	private Game game;
	
	public ShipEntity(Game game, String ref, int x, int y){
		super(ref, x, y);
		this.game =game;
	}
	public void move(long delta){
		if((dx<0) && (x<10)){
			return;
		}
		if((dx>0) && (x>750)){
			return;
		}
		
		super.move(delta);
	}
	
	public void collidedWith(Entity other){
		if (AlienEntity.class.isInstance(other)){
			System.exit(0);
		}
	}
	@Override
	public int getType() {
		return 0;
	}
	
}
