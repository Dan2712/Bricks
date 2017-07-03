package node_selection;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.android.ddmlib.IDevice;

import mini_decode.AndroidScreenObserver;
import mini_decode.MiniCapUtil;
import node_selection.UiAutomatorHelper.UiAutomatorException;
import node_selection.UiAutomatorHelper.UiAutomatorResult;
import node_selection.tree.BasicTreeNode;
import node_selection.tree.UiNode;

/**
 * @author Dan
 * @Description canvas show real-time screen, and response to mouse action
 */

public class RealTimeScreenUI extends JPanel implements AndroidScreenObserver, MouseListener, MouseMotionListener {
	private static final Logger LOG = Logger.getLogger("ReakTimeScreenUI.class");
	
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
    private Boolean isSelected = false;
    private Map<String, String> node_info = new HashMap();
    private VariableChangeObserve obs = new VariableChangeObserve();
    
	public RealTimeScreenUI(IDevice device) {
    	this.device = device;
    	minicap = new MiniCapUtil(device);
		minicap.registerObserver(this);
//		minicap.takeScreenShotOnce();
		minicap.startScreenListener();
		
        mOrginialCursor = getCursor();
        mCrossCursor = new Cursor(Cursor.HAND_CURSOR);
    }
        
	@Override
	public void frameImageChange(BufferedImage image) {
		this.mScreenshot = image;
		UiAutomatorResult result = null;
		try {
			result = UiAutomatorHelper.takeSnapshot(device, null, true, mScreenshot);
			this.mModel = result.model;
		} catch (UiAutomatorException e) {
			LOG.debug("Loading. Current page doesn't contain UI Hierarchy xml.");
		}
		this.updateScreenshotTransformation();
		
		if (mModel.isExploreMode())
			this.repaint();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		try {
			if (mScreenshot == null)
				return;
			
			//setsize this view so that it won't be overlapping 
//			this.setSize(mScreenshot.getWidth(), mScreenshot.getHeight());
			g2.drawImage(mScreenshot, mDx, mDy, width, height, this);
//			this.setSize(panel_bounds, panel_bounds);
			mScreenshot.flush();
			//repaint this so that it could be more smooth
//			this.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mModel != null) {
			Rectangle rect = mModel.getCurrentDrawingRect();
			if (rect != null) {
				g2.setColor(Color.RED);
				if (mModel.isExploreMode()) {
					s = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{15,10,}, 0.0f);
					g2.setColor(Color.RED);
					g2.setStroke(s);
				} else {
					s = new BasicStroke(1.5f);
					g2.setColor(Color.RED);
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
    
    public MiniCapUtil getMinicap() {
		return minicap;
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
            repaint();
        }
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(mCrossCursor);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		setCursor(mOrginialCursor);
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
	            	node_info.put("xpath", node_sel.getXpath());
	            	node_info.put("clickable", node_sel.getAttribute("clickable"));
	            	node_info.put("scrollable", node_sel.getAttribute("scrollable"));
	            	obs.setInfo(node_info);
	            	repaint();
            	}
            }
		}
	}
	
}
