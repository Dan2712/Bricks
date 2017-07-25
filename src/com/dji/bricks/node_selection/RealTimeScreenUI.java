package com.dji.bricks.node_selection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.android.ddmlib.IDevice;
import com.dji.bricks.MainEntry;
import com.dji.bricks.GlobalObserver;
import com.dji.bricks.mini_decode.MiniCapUtil;
import com.dji.bricks.node_selection.UiAutomatorHelper.UiAutomatorException;
import com.dji.bricks.node_selection.UiAutomatorHelper.UiAutomatorResult;
import com.dji.bricks.node_selection.tree.BasicTreeNode;
import com.dji.bricks.node_selection.tree.UiNode;

/**
 * @author Dan
 * @Description canvas show real-time screen, and response to mouse action
 */

public class RealTimeScreenUI extends JPanel implements GlobalObserver, MouseListener, MouseMotionListener {
	private static final Logger LOG = Logger.getLogger("ReakTimeScreenUI.class");
	private ExecutorService cachedThreadPool = null;
	
	private IDevice device;
    private MiniCapUtil minicap = null;

    private int width;
	private int height;
	
	private float mScale = 1.0f;
    private int mDx, mDy;
    private int panel_bounds = 550;
    
    private BufferedImage mScreenshot;
    private Cursor mOrginialCursor;
    private Cursor mCrossCursor;
    private BasicStroke s;
    
    private UiAutomatorModel mModel;
    private UiAutomatorResult result;
    private volatile Boolean isSelected = false;
    private Map<String, String> node_info = new HashMap<String, String>();
    private VariableChangeObserve obs = null;
    private JPanel parentPanel = null;
    private String screenPath = "";
    public static Boolean isRuncase = false;
    private Rectangle rect;
	private volatile boolean isPainting = true;
    
	public RealTimeScreenUI(IDevice device, VariableChangeObserve obs, JPanel parentPanel) {
    	this.device = device;
    	this.obs = obs;
    	this.parentPanel = parentPanel;
    	minicap = new MiniCapUtil(device);
		minicap.registerObserver(this);
//		minicap.takeScreenShotOnce();
		minicap.startScreenListener();
		
        mOrginialCursor = getCursor();
        mCrossCursor = new Cursor(Cursor.HAND_CURSOR);
        cachedThreadPool = MainEntry.cachedThreadPool;
    }
        
	@Override
	public void frameImageChange(BufferedImage image) {
		if (isPainting) {
			this.mScreenshot = image;
			if (!isRuncase) {
				cachedThreadPool.submit((new Runnable() {
					
					@Override
					public void run() {
						try {
							mModel = null;
							result = UiAutomatorHelper.takeSnapshot(device, null, false, mScreenshot);
							mModel = result.model;
						} catch (UiAutomatorException e) {
							LOG.debug("Loading. Current page doesn't contain UI Hierarchy xml.");
						}
					}
				}));
			}
			this.updateScreenshotTransformation();
			this.setSize(panel_bounds, panel_bounds);
		
			parentPanel.repaint();
			this.paintImmediately(new Rectangle(mDx, mDy, width, height));
		}
	}
	
	public synchronized void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		try {
			if (mScreenshot == null)
				return;
			
			g2.drawImage(mScreenshot, mDx, mDy, width, height, this);
			mScreenshot.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mModel != null) {
			rect = mModel.getCurrentDrawingRect();
			if (rect != null) {
				g2.setColor(Color.RED);
				if (mModel.isExploreMode()) {
					s = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{15,10,}, 0.0f);
					g2.setColor(Color.RED);
					g2.setStroke(s);
				} else {
					s = new BasicStroke(1.5f);
					g2.setColor(Color.RED);
					g2.setStroke(s);
				}
				g2.drawRect(mDx + getScaledSize(rect.x), mDy + getScaledSize(rect.y),
	                    getScaledSize(rect.width), getScaledSize(rect.height));
			}
		}
	}

	private int getScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size * mScale))).intValue();
        }
    }

    private int getInverseScaledSize(int size) {
        if (mScale == 1.0f) {
            return size;
        } else {
            return new Double(Math.floor((size / mScale))).intValue();
        }
    }

    /**
     * according to screen size, get the scaled size
     */
    private void updateScreenshotTransformation() {
        float scaleX = this.panel_bounds / (float) mScreenshot.getWidth();
        float scaleY = this.panel_bounds / (float) mScreenshot.getHeight();

        // use the smaller scale here so that we can fit the entire screenshot
        mScale = Math.min(scaleX, scaleY);
        this.width = getScaledSize(mScreenshot.getWidth());
        this.height = getScaledSize(mScreenshot.getHeight());
        // calculate translation values to center the image on the canvas
        mDx = (panel_bounds - getScaledSize(mScreenshot.getWidth())) / 2;
        mDy = (panel_bounds - getScaledSize(mScreenshot.getHeight())) / 2;
    }
    
    public void setModel(UiAutomatorModel model, Image screenshot) {
        mModel = model;
        repaint();
        // load xml into tree
        BasicTreeNode wrapper = new BasicTreeNode();
        // putting another root node on top of existing root node
        // because Tree seems to like to hide the root node
        wrapper.addChild(mModel.getXmlRootNode());
    }
    
    public void stopGetXml() {
    	minicap.getDataQueue().clear();
    	minicap.setRunning(false);
		mScreenshot = null;
		mModel = null;
		repaint();
	}
    
	public void togglePainting() {
		this.isPainting = !isPainting;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (mModel != null) {
            mModel.toggleExploreMode();
            System.out.println(mModel.isExploreMode());
            isSelected = !isSelected;
            parentPanel.repaint();
            repaint();
            
            cachedThreadPool.submit((new Runnable() {
				
				@Override
				public void run() {
					try {
						if (isSelected) {
							BufferedImage image = new BufferedImage(panel_bounds, panel_bounds, BufferedImage.TYPE_INT_RGB);
							RealTimeScreenUI.this.paint(image.getGraphics());
							screenPath = "screenshot/" + System.currentTimeMillis() + ".jpg";
							File screenShot = new File(screenPath);
							if (!screenShot.getParentFile().exists())
								screenShot.getParentFile().mkdirs();
							
							ImageIO.write(image.getSubimage(mDx, mDy, width, height), "jpg", screenShot);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}  
				}
			}));
        }
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(mCrossCursor);
		parentPanel.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(mOrginialCursor);
		parentPanel.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (mModel != null) {
			int x = getInverseScaledSize(e.getX() - mDx);
            int y = getInverseScaledSize(e.getY() - mDy);
            if (mModel.isExploreMode()) {
            	BasicTreeNode node = mModel.updateSelectionForCoordinates(x, y);
            	if (node != null) {
	            	mModel.setSelectedNode(node);
	            	UiNode node_sel = (UiNode) node;
	            	node_info.clear();
	            	node_info.put("xpath", "/" + node_sel.getXpath());
	            	node_info.put("clickable", node_sel.getAttribute("clickable").equals("true") ? "1" : "0");
	            	node_info.put("scrollable", node_sel.getAttribute("scrollable").equals("true") ? "1" : "0");
	            	node_info.put("checkable", node_sel.getAttribute("checkable").equals("true") ? "1" : "0");
	            	node_info.put("focusable", node_sel.getAttribute("focusable").equals("true") ? "1" : "0");
	            	node_info.put("long-clickable", node_sel.getAttribute("long-clickable").equals("true") ? "1" : "0");
	            	node_info.put("package", node_sel.getAttribute("package"));
	            	node_info.put("screenPath", screenPath);
	            	obs.setInfo(node_info);
	            	parentPanel.repaint();
	            	repaint();
            	}
            }
		}
	}

	@Override
	public void ADBChange(IDevice[] devices) {
		// TODO Auto-generated method stub
		
	}
	
}
