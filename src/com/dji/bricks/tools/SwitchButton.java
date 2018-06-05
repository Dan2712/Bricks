package com.dji.bricks.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JToggleButton;

/**
 * @author DraLastat
 *
 */
public class SwitchButton extends JToggleButton {

    private static final int SPACE_WIDTH = 2; 
    private int borderWidth = 0; 
    private int buttonPanelWidth = 50; 
    private int buttonWidth = 25; 
    private int buttonHeight = 25; 
    private int buttonArc = 25; 
    protected float buttonShapeX = 0, buttonShapeY = 0, buttonShapeStart, buttonShapeEnd; 
    private RoundRectangle2D.Float borderShape; 
    private RoundRectangle2D.Float backgroundShape; 
    private BufferedImage insideShadow; 
    private RoundRectangle2D.Float buttonShape; 
    private BufferedImage buttonShadow; 
    private int buttonShadowRadius = 5; 
    private Color borderColor = Color.LIGHT_GRAY; 
    private Color backgroundColor = Color.GRAY; 
    private Color backgroundEnabledColor = Color.decode("#4682b4"); 
    private Color backgroundDisabledColor = Color.GRAY; 
    private Color buttonColor = Color.WHITE; 
    private Thread thread = Thread.currentThread(); 
    private static final int BTN_PRESSED_ANIM_DELAY = 15; 
    private static final int BTN_PRESSED_ANIM_SUM_COUNT = 75; 
    private int btnPressedAnimCount = 0;
    private static final int BTN_POSITION_ANIM_SUM_COUNT = 75; 
    private int btnPositionAnimCount = 0; 
    private static final int BACKGROUND_ANIM_SUM_COUNT = 50; 
    private int backgroundAnimCount = 0; 

    public SwitchButton() {
        init();
        setBorder(null);
        setFocusable(false);
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        createAnimThread().start();
    }
    private Thread createAnimThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int delay = 0;
                float start, end;
                float tempButtonShapeWidth, tempButtonShapeX;
                boolean isNeedRepaint;
                while (thread.isAlive()) {
                    isNeedRepaint = false;
                    start = System.nanoTime();
                    if (getModel().isPressed()) { 
                        if (delay <= BTN_PRESSED_ANIM_DELAY) delay++;
                    } else {
                        delay = 0;
                    }
                    if (getModel().isPressed() && delay > BTN_PRESSED_ANIM_DELAY && btnPressedAnimCount <= BTN_PRESSED_ANIM_SUM_COUNT) {
                        isNeedRepaint = true;
                        if (!getModel().isSelected()) {
                            buttonShape.width = quartEaseInOut(btnPressedAnimCount++, buttonWidth, buttonWidth / 2, BTN_PRESSED_ANIM_SUM_COUNT);
                        } else {
                            tempButtonShapeWidth = quartEaseInOut(btnPressedAnimCount++, buttonWidth, buttonWidth / 2, BTN_PRESSED_ANIM_SUM_COUNT);
                            buttonShape.width = tempButtonShapeWidth;
                            buttonShape.x = buttonShapeEnd - (tempButtonShapeWidth - buttonWidth);
                        }
                    }
                    if (!getModel().isPressed() && btnPressedAnimCount >= 0) {
                        isNeedRepaint = true;
                        buttonShape.width = quartEaseInOut(btnPressedAnimCount--, buttonWidth, buttonWidth / 2, BTN_PRESSED_ANIM_SUM_COUNT);
                        if (getModel().isSelected()) {
                            buttonShape.x = buttonShapeEnd - (buttonShape.width - buttonWidth);
                        }
                    }
                    if (getModel().isSelected() && btnPositionAnimCount <= BTN_POSITION_ANIM_SUM_COUNT) {
                        isNeedRepaint = true;
                        buttonShape.x = quartEaseInOut(btnPositionAnimCount++, buttonShapeStart, buttonShapeEnd - buttonShapeStart, BTN_POSITION_ANIM_SUM_COUNT);
                    }
                    if (getModel().isSelected() && backgroundAnimCount <= BACKGROUND_ANIM_SUM_COUNT) {
                        try {
                            backgroundColor = gradient(backgroundDisabledColor, backgroundEnabledColor, backgroundAnimCount++, BACKGROUND_ANIM_SUM_COUNT);
                            isNeedRepaint = true;
                        } catch (Exception ignored) {
                        }
                    }
                    if (!getModel().isSelected() && btnPositionAnimCount >= 0) {
                        tempButtonShapeX = quartEaseInOut(btnPositionAnimCount--, buttonShapeStart, buttonShapeEnd - buttonShapeStart, BTN_POSITION_ANIM_SUM_COUNT);
                        buttonShape.x = tempButtonShapeX - (buttonShape.width - buttonWidth);
                        isNeedRepaint = true;
                    }
                    if (!getModel().isSelected() && backgroundAnimCount >= 0) {
                        try {
                            backgroundColor = gradient(backgroundDisabledColor, backgroundEnabledColor, backgroundAnimCount--, BACKGROUND_ANIM_SUM_COUNT);
                            isNeedRepaint = true;
                        } catch (Exception ignored) {
                        }
                    }
                    if (isNeedRepaint) {
                        buttonShadow = createButtonShadow();
                        repaint();
                    }
                    end = (System.nanoTime() - start) / 1000000;
                    try {
                        Thread.sleep(end > 5 ? 0 : (int) (5 - end)); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private float quartEaseInOut(float currentTime, float beginning, float changeIn, float duration) {
        if ((currentTime /= duration / 2) < 1)
            return changeIn / 2 * currentTime * currentTime * currentTime * currentTime + beginning;
        return -changeIn / 2 * ((currentTime -= 2) * currentTime * currentTime * currentTime - 2) + beginning;
    }

    private Color gradient(Color color1, Color color2, int count, int sum) throws IllegalArgumentException {

        if (count < 0 || count > sum) throw new IllegalArgumentException("错误的参数");

        int r = color1.getRed() + (color2.getRed() - color1.getRed()) * count / sum;
        int g = color1.getGreen() + (color2.getGreen() - color1.getGreen()) * count / sum;
        int b = color1.getBlue() + (color2.getBlue() - color1.getBlue()) * count / sum;

        r = r > 255 ? 255 : (r < 0 ? 0 : r);
        g = g > 255 ? 255 : (g < 0 ? 0 : g);
        b = b > 255 ? 255 : (b < 0 ? 0 : b);

        return new Color(r, g, b);
    }
    
    public void init(int buttonPanelWidth, int buttonWidth, int buttonHeight, int buttonArc) {
        this.buttonPanelWidth = buttonPanelWidth;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.buttonArc = buttonArc;
        init();
    }
    
    public void init() {
        float spaceSize = SPACE_WIDTH * 2; 
        float borderSize = borderWidth * 2; 

        if (borderWidth > 0) {
            float borderShapeWidth = buttonPanelWidth + borderSize + spaceSize;
            float borderShapeHeight = buttonHeight + borderSize + spaceSize;
            float borderShapeArc = buttonArc * borderShapeHeight / buttonHeight; 

            if (borderShape == null) borderShape = new RoundRectangle2D.Float();
            borderShape.setRoundRect(0, 0, borderShapeWidth, borderShapeHeight, borderShapeArc, borderShapeArc);
        }

        float backgroundShapeWidth = buttonPanelWidth + spaceSize;
        float backgroundShapeHeight = buttonHeight + spaceSize;
        float backgroundShapeArc = buttonArc * backgroundShapeHeight / buttonHeight; 

        if (backgroundShape == null) backgroundShape = new RoundRectangle2D.Float();
        backgroundShape.setRoundRect(borderWidth, borderWidth, backgroundShapeWidth, backgroundShapeHeight, backgroundShapeArc, backgroundShapeArc);
        insideShadow = createInsideShadow(backgroundShape, 5); 

        buttonShapeX = buttonShapeY = buttonShapeStart = borderWidth + SPACE_WIDTH;
        buttonShapeEnd = (borderShape == null ? backgroundShape.width : borderShape.width) - borderWidth - SPACE_WIDTH - buttonWidth;

        if (buttonShape == null) buttonShape = new RoundRectangle2D.Float();
        buttonShape.setRoundRect(buttonShapeX, buttonShapeY, buttonWidth, buttonHeight, buttonArc, buttonArc);

        buttonShadow = createButtonShadow();

        Dimension dimension = getPreferredSize();
        if (dimension == null) dimension = new Dimension();
        if (borderShape != null) { 
            dimension.setSize(borderShape.width, borderShape.height);
        } else {
            dimension.setSize(backgroundShape.width, backgroundShape.height);
        }
        setPreferredSize(dimension);
    }
    
    private BufferedImage createInsideShadow(RoundRectangle2D.Float shape, int radius) {

        RoundRectangle2D.Float _shape = (RoundRectangle2D.Float) shape.clone();

        _shape.x = _shape.y = radius; 
        int width = _shape.getBounds().width + radius * 2;
        int height = _shape.getBounds().height + radius * 2;

        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadow.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Area area = new Area(_shape);
        Area fill = new Area(new Rectangle2D.Float(0, 0, width, height));
        fill.exclusiveOr(area); 
        g2d.fill(fill);

        g2d.dispose();

        shadow = getGaussianBlurFilter(radius, true).filter(shadow, null);
        shadow = getGaussianBlurFilter(radius, false).filter(shadow, null);

        _shape.x = _shape.y = 0; 

        shadow = shadow.getSubimage(radius, radius, _shape.getBounds().width, _shape.getBounds().height);
        g2d = shadow.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        area = new Area(_shape);
        fill = new Area(new Rectangle2D.Float(0, 0, _shape.width, _shape.height));
        fill.exclusiveOr(area);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fill(fill);
        g2d.setComposite(AlphaComposite.SrcIn.derive(0.75F));
        g2d.fill(_shape);
        g2d.dispose();

        return shadow;
    }

    private BufferedImage createButtonShadow() {
        return createButtonShadow(buttonShape, buttonShadowRadius);
    }

    private BufferedImage createButtonShadow(RoundRectangle2D.Float shape, int radius) {

        RoundRectangle2D.Float buttonShadowShape = (RoundRectangle2D.Float) shape.clone();

        int width = (int) (borderShape == null ? backgroundShape.width : borderShape.width);
        int height = buttonShadowShape.getBounds().height + radius * 2;

        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadow.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.75F));
        buttonShadowShape.y += 2;
        g2d.fill(buttonShadowShape);
        g2d.dispose();

        shadow = getGaussianBlurFilter(radius, true).filter(shadow, null);
        shadow = getGaussianBlurFilter(radius, false).filter(shadow, null);

        return shadow;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (borderShape != null) {
            g2d.setColor(borderColor);
            g2d.fill(borderShape);
        }
        if (backgroundShape != null) {
            g2d.setColor(backgroundColor);
            g2d.fill(backgroundShape);
        }
        if (insideShadow != null) {
            g2d.drawImage(insideShadow, borderWidth, borderWidth, null);
        }
        if (buttonShadow != null) {
            g2d.drawImage(buttonShadow, 0, 0, null);
        }
        if (buttonShape != null) {
            g2d.setColor(buttonColor);
            g2d.fill(buttonShape);
        }
        g2d.dispose();
        super.paintComponent(g);
    }

    private ConvolveOp getGaussianBlurFilter(int radius, boolean horizontal) {

        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public int getButtonShadowRadius() {
        return buttonShadowRadius;
    }

    public void setButtonShadowRadius(int buttonShadowRadius) {
        this.buttonShadowRadius = buttonShadowRadius;
    }

    public void setBackgroundEnabledColor(Color color) {
        backgroundEnabledColor = color;
    }

    public void setBackgroundDisabledColor(Color color) {
        backgroundDisabledColor = color;
    }
}