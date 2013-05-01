
public class AlienEntity extends Entity {
	private Game game;
	private double moveSpeed = 70;
	private int type;
	private long lastFrameChange;
	private Sprite[] frames = new Sprite[2];
	private long frameLength = 500;
	private int frameNumber = 0;
	private boolean midBlock;
	
	public AlienEntity(Game game, int x, int y, int type){
		super(x,y);
		if(type == 2){
			
			
			frames[0] = SpriteStore.get().getSprite("sprites/green.gif");
			frames[1] = SpriteStore.get().getSprite("sprites/green2.gif");
			sprite = frames[0];
			super.setSprite(sprite);
			this.game = game;
			this.type = type;
			dx = -moveSpeed;
		}else{
		
		
			//frames[0] = sprite;
			frames[0] = SpriteStore.get().getSprite("sprites/alien.gif");
			frames[1] = SpriteStore.get().getSprite("sprites/alien2.gif");
			sprite = frames[0];
			super.setSprite(sprite);
			this.game = game;
			this.type = type;
			dx = -moveSpeed;
		}
	}
	public AlienEntity(Game game, int x, int y, int type, int moveSpeed){
		super("sprites/alien.gif",x,y);
		
		frames[0] = sprite;
		frames[1] = SpriteStore.get().getSprite("sprites/alien2.gif");
		
		this.game = game;
		this.type = type;
		this.moveSpeed = moveSpeed;
		dx = -moveSpeed;
	}
	public void move(long delta){
		lastFrameChange+=delta;
		
		if(lastFrameChange>frameLength){
			lastFrameChange = 0;
			frameNumber++;
			if (frameNumber >= frames.length){
				frameNumber=0;
			}
			sprite=frames[frameNumber];
			
		}
		
		if((dx<0) && (x<10)){
			doLogic();
		}if((dx>0)&&(x>760)){
			doLogic();
		}
		super.move(delta);
	}
	
	public void collidedWith(Entity other){
		//if(AlienEntity.class.isInstance(other)){
		//	reverse();
		//}
	}
	
	public void doLogic(){
		game.reverseAliens();
		game.advanceAliens();
		if (y>570){
			game.lose();
		}
	}
	
	public void reverse(){
		dx= -dx;
	}
	public int getType(){
		return type;
	}
	public void setMidBlock(boolean midBlock){
		this.midBlock = midBlock;
	}
	
}
