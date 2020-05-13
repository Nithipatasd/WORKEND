package snake.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
public class Board extends JPanel implements ActionListener{

	private final int B_Width = 300;
	private final int B_Height = 300;
	private final int Dot_Size = 10;
	private final int All_Dot = 900;
	private final int Rand_pos = 29;
	private final int Delay = 140;
	
	private final int x[] = new int[All_Dot];
	private final int y[] = new int[All_Dot];
	
	private int Dots;
	private int Apples_x;
	private int Apples_y;
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	
	private Timer timer;
	private Image ball;
	private Image apple;
	private Image head;
	private Image ac;

	
	public Board() {
		addKeyListener(new TAdapter());
		setBackground(Color.BLACK);
		setFocusable(true);
		setPreferredSize(new Dimension(B_Width,B_Height));
		

		JButton b = new JButton("Start");
		add(b);
		b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		setBackground(Color.BLACK);
		b.setVisible(false);
		startGame();
						
					}
					
				});
	}
	
	private void startGame() {
	loadImages();
	initGame();
	}
		
	private void loadImages() {
		ImageIcon iid = new ImageIcon(getClass().getResource("/res/dot.png"));
		ball = iid.getImage();
		
		ImageIcon iia = new ImageIcon(getClass().getResource("/res/apple.png"));
		apple = iia.getImage();
		
		ImageIcon iih = new ImageIcon(getClass().getResource("/res/head.png"));
		head = iih.getImage();
	}
	
	private void initGame() {
		Dots = 3;
		
		for(int z = 0;z < Dots;z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}
		locateApple();
		
		timer = new Timer(Delay,this);
		timer.start();
	} 
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(ac,0,0,null);
		super.paintComponent(g); 
		doDrawing(g);
	}
	
	private void doDrawing(Graphics g) {
		if(inGame) {
			g.drawImage(apple,Apples_x,Apples_y,this);
			
			for(int i = 0 ; i < Dots;i++) {
				if(i == 0) {
					g.drawImage(head,x[i],y[i],this);
				}else {
					g.drawImage(ball,x[i], y[i], this);
				}
			}
			
			Toolkit.getDefaultToolkit().sync();
			
		}else {
			gameOver(g);
		}
	}
	
	private void gameOver(Graphics g) {
		String message = "Game Over Oh You so noob";
		Font small = new Font("Angsana new",Font.BOLD,30);
		FontMetrics metr = getFontMetrics(small);
		
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message,(B_Width - metr.stringWidth(message)) /2,B_Height / 2);
		
		JButton play = new JButton("Play Again");
		play.setBounds(100,170,100,30);
		add(play);
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play.setVisible(false);
				inGame = true;
				initGame();
				
			}
			
		});
	}
	
	private void checkApple() {
		if((x[0] == Apples_x) && (y[0] == Apples_y)) {
			Dots++;
			locateApple();
		}
	}
	
	private void move() {
		for(int i = Dots; i > 0 ; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		if(leftDirection) {
			x[0] -= Dot_Size;
		}
		if(rightDirection) {
			x[0] += Dot_Size;
		}
		if(upDirection) {
			y[0] -= Dot_Size;
		}
		if(downDirection) {
			y[0] += Dot_Size;
		}
	}
	
	private void checkCollision() {
		for(int i = Dots ;i > 0 ; i-- ) {
			if((i > 4)&&(x[0] == x[i]&&(y[0] == y[i]))) {
				inGame = false;
			}
		}
		if(y[0] >= B_Height) {
			inGame = false;
		}
		if(y[0] < 0 ) {
			inGame = false;
		}
		if(x[0] >= B_Width) {
			inGame = false;
		}
		if(x[0] < 0) {
			inGame = false;
		}
		if(!inGame) {
			timer.stop();
		}
	}
	
	private void locateApple() {
		int r = (int)(Math.random() * Rand_pos);
		Apples_x = r * Dot_Size;
		
		r = (int) (Math.random() * Rand_pos);
		Apples_y = r * Dot_Size;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(inGame) {
			checkApple();
			checkCollision();
			move();
		} 
		repaint();
	}
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT && !rightDirection) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}
			if(key == KeyEvent.VK_RIGHT && !leftDirection) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
			}
			if(key == KeyEvent.VK_UP && !downDirection) {
				upDirection = true;
				rightDirection = false;
				leftDirection = false;
			}
			if(key == KeyEvent.VK_DOWN && !upDirection) {
				downDirection = true;
				rightDirection = false;
				leftDirection = false;
			}
		}
	}
}
