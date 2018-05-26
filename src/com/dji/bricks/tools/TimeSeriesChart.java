package com.dji.bricks.tools;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TimeSeriesChart extends JFrame {

	private String title = "";
	private List<Integer> CPU_values;
	private List<Integer> Mem_values;
	private static final int MAX_VALUE = 180;
	private static final int MAX_COUNT_OF_VALUES = 50;
	private MyCanvas trendChartCanvas = new MyCanvas();

	private final int FREAME_X = 50;
	private final int FREAME_Y = 50;
	private final int FREAME_WIDTH = 750;
	private final int FREAME_HEIGHT = 250;

	private final int Origin_X = FREAME_X + 50;
	private final int Origin_Y = FREAME_Y + FREAME_HEIGHT - 30;

	private final int XAxis_X = FREAME_X + FREAME_WIDTH - 30;
	private final int XAxis_Y = Origin_Y;
	private final int YAxis_X = Origin_X;
	private final int YAxis_Y = FREAME_Y + 30;

	private final int TIME_INTERVAL = 10;
	private final int PRESS_INTERVAL = 30;

	public TimeSeriesChart() {
		super("CPU/Memory info");
		CPU_values = Collections.synchronizedList(new ArrayList<Integer>());
		Mem_values = Collections.synchronizedList(new ArrayList<Integer>());

//		new Thread(new Runnable() {
//			public void run() {
//				Random rand = new Random();
//				try {
//					while (true) {
//						addCPUValue(rand.nextInt(MAX_VALUE) + 90);
//						addMemValue(rand.nextInt(MAX_VALUE) + 90);
//						repaint();
//						Thread.sleep(100);
//					}
//				} catch (InterruptedException b) {
//					b.printStackTrace();
//				}
//			}
//
//		}).start();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(300, 200, 900, 600);
		this.add(trendChartCanvas, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public void addCPUValue(int value) {
		if (CPU_values.size() > MAX_COUNT_OF_VALUES) {
			CPU_values.remove(0);
		}
		CPU_values.add(value);
	}
	
	public void addMemValue(int value) {
		if (Mem_values.size() > MAX_COUNT_OF_VALUES) {
			Mem_values.remove(0);
		}
		Mem_values.add(value);
	}
	
	class MyCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
			paintCPU(g);
			paintMemory(g);
		}
		
		private void paintCPU(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;

			Color c = new Color(200, 70, 0);
			g.setColor(c);
			super.paintComponent(g);

			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int w = XAxis_X;
			int xDelta = w / MAX_COUNT_OF_VALUES;
			int length = CPU_values.size() - 10;

			for (int i = 0; i < length - 1; ++i) {
				System.out.println(CPU_values.get(i));
				g2D.drawLine(xDelta * (MAX_COUNT_OF_VALUES - length + i), CPU_values.get(i),
						xDelta * (MAX_COUNT_OF_VALUES - length + i + 1), CPU_values.get(i + 1));
			}

			g2D.setStroke(new BasicStroke(Float.parseFloat("2.0F")));

			g.drawLine(Origin_X, Origin_Y, XAxis_X, XAxis_Y);
			g.drawLine(XAxis_X, XAxis_Y, XAxis_X - 5, XAxis_Y - 5);
			g.drawLine(XAxis_X, XAxis_Y, XAxis_X - 5, XAxis_Y + 5);

			g.drawLine(Origin_X, Origin_Y, YAxis_X, YAxis_Y);
			g.drawLine(YAxis_X, YAxis_Y, YAxis_X - 5, YAxis_Y + 5);
			g.drawLine(YAxis_X, YAxis_Y, YAxis_X + 5, YAxis_Y + 5);

			g.setColor(Color.BLUE);
			g2D.setStroke(new BasicStroke(Float.parseFloat("1.0f")));

			for (int i = Origin_X, j = 0; i < XAxis_X; i += TIME_INTERVAL * 5, j += TIME_INTERVAL) {
				g.drawString(" " + j, i - 10, Origin_Y + 20);
			}
			g.drawString("time(s)", XAxis_X + 5, XAxis_Y + 5);

			for (int i = Origin_Y, j = 0; i > YAxis_Y; i -= PRESS_INTERVAL, j += TIME_INTERVAL * 3) {
				g.drawString(j + " ", Origin_X - 30, i + 3);
			}
			g.drawString("CPU", YAxis_X - 5, YAxis_Y - 5);
			g.setColor(Color.BLACK);
			
			for (int i = Origin_Y; i > YAxis_Y; i -= PRESS_INTERVAL) {
				g.drawLine(Origin_X, i, Origin_X + 65 * TIME_INTERVAL, i);
			}
			
			for (int i = Origin_X; i < XAxis_X; i += TIME_INTERVAL * 5) {
				g.drawLine(i, Origin_Y, i, Origin_Y - 6 * PRESS_INTERVAL);
			}
		}
		
		private void paintMemory(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;

			Color c = new Color(200, 70, 0);
			g.setColor(c);

			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int w = XAxis_X;
			int xDelta = w / MAX_COUNT_OF_VALUES;
			int length = Mem_values.size() - 10;

			for (int i = 0; i < length - 1; ++i) {
				System.out.println(Mem_values.get(i));
				g2D.drawLine(xDelta * (MAX_COUNT_OF_VALUES - length + i), Mem_values.get(i) + 240,
						xDelta * (MAX_COUNT_OF_VALUES - length + i + 1), Mem_values.get(i + 1) + 240);
			}

			g2D.setStroke(new BasicStroke(Float.parseFloat("2.0F")));

			g.drawLine(Origin_X, Origin_Y + 240, XAxis_X, XAxis_Y + 240);
			g.drawLine(XAxis_X, XAxis_Y + 240, XAxis_X - 5, XAxis_Y + 235);
			g.drawLine(XAxis_X, XAxis_Y + 240, XAxis_X - 5, XAxis_Y + 245);

			g.drawLine(Origin_X, Origin_Y + 240, YAxis_X, YAxis_Y + 240);
			g.drawLine(YAxis_X, YAxis_Y + 240, YAxis_X - 5, YAxis_Y + 245);
			g.drawLine(YAxis_X, YAxis_Y + 240, YAxis_X + 5, YAxis_Y + 245);

			g.setColor(Color.BLUE);
			g2D.setStroke(new BasicStroke(Float.parseFloat("1.0f")));

			for (int i = Origin_X, j = 0; i < XAxis_X; i += TIME_INTERVAL * 5, j += TIME_INTERVAL) {
				g.drawString(" " + j, i - 10, Origin_Y + 260);
			}
			g.drawString("time(s)", XAxis_X + 5, XAxis_Y + 245);

			for (int i = Origin_Y, j = 0; i > YAxis_Y; i -= PRESS_INTERVAL, j += TIME_INTERVAL * 3) {
				g.drawString(j + " ", Origin_X - 30, i + 243);
			}
			g.drawString("Memory", YAxis_X - 5, YAxis_Y + 235);
			g.setColor(Color.BLACK);
			
			for (int i = Origin_Y + 240; i > YAxis_Y + 240; i -= PRESS_INTERVAL) {
				g.drawLine(Origin_X, i, Origin_X + 65 * TIME_INTERVAL, i);
			}
			
			for (int i = Origin_X; i < XAxis_X; i += TIME_INTERVAL * 5) {
				g.drawLine(i, Origin_Y + 240, i, Origin_Y + 240 - 6 * PRESS_INTERVAL);
			}
		}
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new TimeSeriesChart();
//	}
}
