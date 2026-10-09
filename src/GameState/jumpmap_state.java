package GameState;

import main.GamePanel;
import TileMap.*;
import Entity.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class jumpmap_state extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private AudioPlayer bgMusic;
	
	public jumpmap_state(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.png");
		tileMap.loadMap("/Maps/jumpmap.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/background.jpg", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(50, 800);
		
		
		bgMusic = new AudioPlayer("/Music/bgm.mp3");
		bgMusic.play();
		
	}
	
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());

	}
	
	public void draw(final Graphics2D g) {

        // draw bg
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        class MyKeyListener extends KeyAdapter {
            public void keyPressed(KeyEvent e) {
                Container contentPane = (Container)e.getSource();

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    g.drawString("Clear", 110, 900);
                }

            }
        }
    }
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_SPACE) player.setJumping(true);
		if(k == KeyEvent.VK_UP) player.setClear(true);
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
	}
	
}