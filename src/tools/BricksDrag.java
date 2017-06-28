package tools;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 * 砖块拖动
 *
 * @author DraLastat
 */
public class BricksDrag extends MouseAdapter{
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseMoved(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//如果进入并且在边框内，则显示调整大小形态
		//如果进入但不在边框内
		//计算原点。
		JComponent comp=(JComponent)e.getSource();
		JComponent panel=(JComponent)comp.getParent();
		Point p=comp.getLocationOnScreen();
		SwingUtilities.convertPointFromScreen(p, panel);//获取组件在Panel中的坐标
		Point np=new Point(p.x+10, p.y+10);//新矩形的原点
		int nwidth=comp.getWidth()-20;
		int nheight=comp.getHeight()-20;
		super.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseExited(e);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		JComponent comp = (JComponent) e.getSource();
		JPanel panel=(JPanel)comp.getParent();
		Point p=e.getLocationOnScreen();
		SwingUtilities.convertPointFromScreen(p, panel);
		p.x-=comp.getWidth()/2;
		p.y-=comp.getHeight()/2;
		//comp.setLocation(p.x,p.y);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.print("Released:");
		//System.out.println("("+e.getX()+","+e.getY()+")");
		//super.mouseReleased(e);
	}


	@Override
	public void mouseDragged(MouseEvent e) {

		//移动
		JComponent comp = (JComponent) e.getSource();
		JPanel panel=(JPanel)comp.getParent();
		Point p=e.getLocationOnScreen();
		System.out.print("JButton桌面坐标："+"("+p.x+","+p.y+")\t");
		SwingUtilities.convertPointFromScreen(p, panel);
		System.out.println("JButton在JPanel坐标："+"("+p.x+","+p.y+")\t");
		p.x-=comp.getWidth()/2;
		p.y-=comp.getHeight()/2;
		comp.setLocation(p.x,p.y);
		//调整大小


		//super.mouseDragged(e);
	}
}
