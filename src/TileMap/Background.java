package TileMap;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(double x, double y) {
		if (image == null) return;
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	// 맵 크기에 비례하여 플레이어가 아래에서 위로 올라갈 때 자연스럽게 배경이 스크롤되도록 하는 메소드
	public void setPosition(double mapX, double mapY, int mapWidth, int mapHeight) {
		if (image == null) return;
		
		int imgW = image.getWidth();
		int imgH = image.getHeight();
		
		// 세로 스크롤 연동: 맵 진행도에 맞추어 배경의 아래(지상)부터 위(하늘)까지 부드럽게 이동
		int scrollableMapH = mapHeight - GamePanel.HEIGHT;
		int scrollableImgH = imgH - GamePanel.HEIGHT;
		
		if (scrollableMapH > 0 && scrollableImgH > 0) {
			// mapY 범위: -(scrollableMapH) (맨 바닥) ~ 0 (맨 꼭대기)
			double progressY = -mapY / (double)scrollableMapH; // 1.0 (바닥) -> 0.0 (정상)
			if (progressY < 0.0) progressY = 0.0;
			if (progressY > 1.0) progressY = 1.0;
			
			this.y = -progressY * scrollableImgH;
		} else {
			this.y = (mapY * moveScale) % GamePanel.HEIGHT;
		}
		
		// 가로 스크롤 연동: 맵 좌우 이동에 맞추어 배경도 부드럽게 패럴랙스 이동
		int scrollableMapW = mapWidth - GamePanel.WIDTH;
		int scrollableImgW = imgW - GamePanel.WIDTH;
		
		if (scrollableMapW > 0 && scrollableImgW > 0) {
			double progressX = -mapX / (double)scrollableMapW; // 0.0 (왼쪽 끝) -> 1.0 (오른쪽 끝)
			if (progressX < 0.0) progressX = 0.0;
			if (progressX > 1.0) progressX = 1.0;
			
			this.x = -progressX * scrollableImgW;
		} else {
			this.x = (mapX * moveScale) % GamePanel.WIDTH;
		}
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g) {
		if (image == null) return;
		
		int imgW = image.getWidth();
		int imgH = image.getHeight();
		
		g.drawImage(image, (int)x, (int)y, null);
		
		// X축 여백 방지 타일링
		if (x > 0) {
			g.drawImage(image, (int)x - imgW, (int)y, null);
		}
		if (x + imgW < GamePanel.WIDTH) {
			g.drawImage(image, (int)x + imgW, (int)y, null);
		}
		
		// Y축 여백 방지 타일링
		if (y > 0) {
			g.drawImage(image, (int)x, (int)y - imgH, null);
		}
		if (y + imgH < GamePanel.HEIGHT) {
			g.drawImage(image, (int)x, (int)y + imgH, null);
		}
	}
	
	public void drawtbg(Graphics2D g) {
		if (image == null) return;
		g.drawImage(image, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
	}
}
