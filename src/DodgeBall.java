import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class DodgeBall extends GraphicsProgram implements ActionListener {
	private ArrayList<GOval> balls;
	private ArrayList<GRect> enemies;
	private GLabel text;
	private Timer movement;
	private RandomGenerator rgen;
	private int numTimes = 0;
	private int numEnemiesDestroyed = 0;
	GLabel enemiesDestroyed;
	
	public static final int SIZE = 25;
	public static final int SPEED = 2;
	public static final int MS = 50;
	public static final int MAX_ENEMIES = 10;
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH = 300;
	
	
	public void run() {
		rgen = RandomGenerator.getInstance();
		balls = new ArrayList<GOval>();
		enemies = new ArrayList<GRect>();
		enemiesDestroyed = new GLabel("Enemies destroyed: "+numEnemiesDestroyed,0,WINDOW_HEIGHT-1);
		text = new GLabel("Enemies left: "+enemies.size(), 0, WINDOW_HEIGHT-enemiesDestroyed.getHeight());
		add(text);
		add(enemiesDestroyed);
		
		movement = new Timer(MS, this);
		movement.start();
		addMouseListeners();
	}
	
	public void actionPerformed(ActionEvent e) {
		moveAllBallsOnce();
		enemiesDestroyed.setLabel("Enemies destroyed: "+numEnemiesDestroyed);
		if(numTimes % 40 == 0) addAnEnemy();
		numTimes++;
		moveAllEnemiesOnce();
		if (enemies.size()==MAX_ENEMIES) {
			movement.stop();
			removeAll();
			GLabel lose = new GLabel("You lose!");
			GLabel score = new GLabel("You survived for: "+numTimes);
			lose.setLocation(WINDOW_WIDTH/2-(lose.getWidth()/2), WINDOW_HEIGHT/2);
			score.setLocation(WINDOW_WIDTH/2-(score.getWidth()/2), WINDOW_HEIGHT/2+lose.getHeight());
			add(lose);
			add(score);
		}
	}
	
	public void moveAllEnemiesOnce() {
		for (GRect enemy:enemies) {
			enemy.move(0, rgen.nextInt(-2, 2));
		}
	}
	
	public void mousePressed(MouseEvent e) {
		for(GOval b:balls) {
			if(b.getX() < SIZE * 2.5) {
				return;
			}
		}
		addABall(e.getY());     
	}
	
	private void addABall(double y) {
		GOval ball = makeBall(SIZE/2, y);
		add(ball);
		balls.add(ball);
	}
	
	public GOval makeBall(double x, double y) {
		GOval temp = new GOval(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
		temp.setColor(Color.RED);
		temp.setFilled(true);
		return temp;
	}
	
	private void addAnEnemy() {
		GRect e = makeEnemy(rgen.nextInt(0, WINDOW_HEIGHT-SIZE/2));
		enemies.add(e);
		text.setLabel("Enemies left: " + enemies.size());
		add(e);
	}
	
	public GRect makeEnemy(double y) {
		GRect temp = new GRect(WINDOW_WIDTH-SIZE, y-SIZE/2, SIZE, SIZE);
		temp.setColor(Color.GREEN);
		temp.setFilled(true);
		return temp;
	}

	private void moveAllBallsOnce() {
		for(GOval ball:balls) {
			ball.move(SPEED, 0);
			if (getElementAt(ball.getX()+ball.getWidth()+1,ball.getY()+ball.getHeight()/2) instanceof GRect) {
				GObject target =getElementAt(ball.getX()+ball.getWidth()+1,ball.getY()+ball.getHeight()/2);
				remove(target);
				enemies.remove(target);
				numEnemiesDestroyed++;
			}
			
		}
	}
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	public static void main(String args[]) {
		new DodgeBall().start();
	}
}
