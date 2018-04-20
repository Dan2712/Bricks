package com.dji.bricks.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BricksTable extends JTable{
    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Color[] rowColors = new Color[] {
                randomColor(), randomColor(), randomColor()
        };
        final JTable table = new JTable(3, 3);
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JPanel pane = new JPanel();
                pane.setBackground(rowColors[row]);
                return pane;
            }
        });
//        frame.setLayout(new BorderLayout());
//
//        JButton btn = new JButton("Change row2's color");
//        btn.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                rowColors[1] = randomColor();
//                table.repaint();
//            }
//        });

//        frame.add(table, BorderLayout.NORTH);
//        frame.add(btn, BorderLayout.SOUTH);
//        frame.pack();
//        frame.setVisible(true);
    }

    private static Color randomColor() {
        Random rnd = new Random();
        return new Color(rnd.nextInt(256),
                rnd.nextInt(256), rnd.nextInt(256));
    }
}