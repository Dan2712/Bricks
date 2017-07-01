package tools;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 * ש���϶�
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
		//������벢���ڱ߿��ڣ�����ʾ������С��̬
		//������뵫���ڱ߿���
		//����ԭ�㡣
		JComponent comp=(JComponent)e.getSource();
		JComponent panel=(JComponent)comp.getParent();
		Point p=comp.getLocationOnScreen();
		SwingUtilities.convertPointFromScreen(p, panel);//��ȡ�����Panel�е�����
		Point np=new Point(p.x+10, p.y+10);//�¾��ε�ԭ��
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

		//�ƶ�
		JComponent comp = (JComponent) e.getSource();
		JPanel panel=(JPanel)comp.getParent();
		Point p=e.getLocationOnScreen();
		System.out.print("JButton"+"("+p.x+","+p.y+")\t");
		SwingUtilities.convertPointFromScreen(p, panel);
		System.out.println("JButton JPanel"+"("+p.x+","+p.y+")\t");
		p.x-=comp.getWidth()/2;
		p.y-=comp.getHeight()/2;
		comp.setLocation(p.x,p.y);
		//������С


		//super.mouseDragged(e);
	}
}
