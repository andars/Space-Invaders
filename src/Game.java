import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
//TODO
/*
 -Explosion animation
 -sounds
 -shooting back
 -different arrangements for levels
*/
public class Game extends Canvas {
	
	private Sprite menu;
	private int highscore = 0;
	private int score = 0;
	private int level = 1;
	private int gamemode = 1;
	private BufferStrategy strategy;
	private boolean gamerunning = true;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> removeList = new ArrayList<Entity>();
	private Entity ship;
	private int alienCount;
	private double moveSpeed = 300;
	private long fireInterval = 500;
	private long lastFire=0;
	private boolean logicRequired = false;
	private Font font;
	private boolean ifWin = false;
	private boolean ifLose = false;
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean firePressed = false;
	
	public static void main(String[] args){
		Game game = new Game();
		game.GameLoop();
	}
	
	public Game(){
		JFrame container = new JFrame("Game");
		JPanel panel = (JPanel)container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		//panel.setLayout(null);
		
		panel.setBounds(0,0,800,600);
		panel.add(this);
		
		setIgnoreRepaint(true);
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		panel.setVisible(true);
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyInputHandler());
		requestFocus();
		 
		try {
			font = loadFont("ca.ttf");
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Random rand = new Random();
		//writeHighScore(0);
		loadHighScore();
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		initEntities(level);
	}

	
	public void GameLoop(){
		long lastLoopTime = System.currentTimeMillis();
		
		while(gamerunning){
			
			
				
			//get time to make physics independent
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			
			//get graphics context and blank screen
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			//g.drawRect(1, 1, 50, 50);
			g.fillRect(0, 0, 800, 600);
			
			if(gamemode == 1){
				menu = SpriteStore.get().getSprite("sprites/menu.gif");
				menu.draw(g,0,0);
				strategy.show();
				try{Thread.sleep(5);}catch (Exception e){}
			}else{
				
			for(int i=0; i < entities.size();i++){
				Entity entity = (Entity)entities.get(i);
				entity.move(delta);
			}
			for(int i=0; i<entities.size();i++){
				Entity entity = (Entity)entities.get(i);
				entity.draw(g);
			}
			
			for(int j = 0; j<entities.size(); j++){
				for(int k=j+1;k<entities.size();k++){
					Entity me = (Entity) entities.get(j);
					Entity him = (Entity) entities.get(k);
					if(me.collidesWith(him)){
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}
		
			
			entities.removeAll(removeList);
			removeList.clear();
			
			if(ifWin){
				level++;
				initAliens(level);
				ifWin = false;
				//g.setColor(Color.green);
				//g.fillRect(0,0,800,600);
				/*
				try {
					font =loadFont("ca.ttf");
				} catch (FontFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				g.setFont(font);
				g.setColor(Color.black);
				g.drawString("You Win!", 350, 300);
				try{Thread.sleep(100);}catch (Exception e){}
				 */
			}if(ifLose){
				g.setColor(Color.red);
				g.fillRect(0, 0, 800, 600);
				try{Thread.sleep(100);}catch (Exception e){}
				
			}
			showScore(g);
			showHighScore(g);
			//if(LogicRequired)
			//System.out.println(alienCount);
			g.dispose();
			strategy.show();
			
			ship.setHMovement(0);
			if(leftPressed && !rightPressed){
				ship.setHMovement(-moveSpeed);
			}if(!leftPressed && rightPressed){
				ship.setHMovement(moveSpeed);
			}
			if (firePressed){
				tryToFire();
			}
			//System.out.println(g);
			try{Thread.sleep(5);}catch (Exception e){}
			}
		}
	}
	private void tryToFire() {
		if(System.currentTimeMillis()-lastFire < fireInterval){
			return;
		}
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", ship.getX()+10, ship.getY()-30);
		entities.add(shot);
	}
	
	public Font loadFont(String ref) throws FontFormatException, IOException{
		InputStream fin = this.getClass().getResourceAsStream(ref);
		System.out.println("font loaded");
		 return Font.createFont (Font.PLAIN,fin).deriveFont(20f);
	}

	public void removeEntity(Entity e){
		removeList.add(e);
	}
	public void initEntities(int level){
		Random rand = new Random();
		
		ship = new ShipEntity(this, "sprites/home.gif",370,550);
		entities.add(ship);
		alienCount = 0;
		for(int row = 0; row < 6; row++){
			for(int x=0; x<13; x++){
				Entity alien;
				if ((x % 3 ==0 && row % 2 ==0)||((x+1)%3==0 && row%2==1)){
					alien = new AlienEntity(this, 70 +(x*50)+10, 30+(row*40), 2);
				}else{
					alien = new AlienEntity(this, 70 +(x*50)+10,30+(row*40), 1);
				//alien.setHMovement(((level*0.02)+1)*70);
				}
				if (row==0||row==1||row==4||row==5){
					alien.reverse();
				}
				entities.add(alien);
				alienCount++;
				
			}
		}
	}
	public void initAliens(int level){
		
		//alienCount = 0;
		for(int row = 0; row < 4; row++){
			for(int x=0; x<13; x++){
				AlienEntity alien;
				
				 alien = new AlienEntity(this, 70 +(x*50)+10, 60+(row*40), 1, (int) ((level*0.2)+1)*70);
				//alien.setHMovement(((level*0.04)+1)*70);
				 if (row%2 == 0){
					//alien.reverse();
				 }
				alien.setMidBlock(true);
				entities.add(alien);
				alienCount++;
			}
		}
				
		
	}
	public void showScore(Graphics2D g){
		
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString("SCORE:" + score, 15, 25);
	}
	public void showHighScore(Graphics2D g){
		if(highscore >999){
			g.drawString("HI SCORE:" + highscore, 590, 25);
		}
		else if (highscore > 9999){
		g.drawString("HI SCORE:" + highscore, 590, 25);
		}
		else{
			g.drawString("HI SCORE:" + highscore, 620, 25);
		}
		if(score>highscore){
			highscore = score;
			writeHighScore(score);
			System.out.println(highscore);
		}
	}

	public void updateLogic() {
		logicRequired = true;
	}
	public void alienKilled(int type) {
		--alienCount;
		
		if (type == 1){
			score += 10;
		}else if (type == 2){
			score +=20;
		}
		if (alienCount == 0){
			ifWin = true;
		
		
		}
	}
	public void lose(){
		ifLose = true;
	}

	public void advanceAliens(){
		for(int i=0; i < entities.size();i++){
			Entity entity = (Entity)entities.get(i);
			if(AlienEntity.class.isInstance(entity)){
				entity.setY(entity.getY()+20);
			}
		}
	}	
	public void reverseAliens(){
		for(int i=0; i < entities.size();i++){
			Entity entity = (Entity)entities.get(i);
			if(AlienEntity.class.isInstance(entity)){
				entity.reverse();
			}
		}
	}	
	public void loadHighScore(){
		
		File f = new File("highscore.txt");
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("highscore.txt");
		} catch (FileNotFoundException e) {System.out.println("read failed");}
        DataInputStream dis = new DataInputStream(fis);
        try {
			highscore=dis.readInt();
		} catch (IOException e) {
			System.out.println("read failed");
			e.printStackTrace();
		}
        System.out.println(highscore);
		
	}
	public void writeHighScore(int score){
		try{
		FileOutputStream fos = new FileOutputStream("highscore.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeInt(score);
        dos.close();
		}catch(IOException e){
			System.out.println("write failed");
		}
        
	}


private class KeyInputHandler extends KeyAdapter {
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			firePressed = true;
		}
	} 
		
		
	public void keyReleased(KeyEvent e) {			
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			firePressed = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			gamemode = 2;
		}
	}

	public void keyTyped(KeyEvent e) {
		// if we hit escape, then quit the game
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		//if (e)
	}
}





}
