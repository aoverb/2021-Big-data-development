package main;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import javax.swing.JFrame;

interface boundEvent{
	public void trigger();
}


public class GUI {
	static JFrame f = null;
	static JTable table = null;
	public GUI(String caption, int x, int y, int w, int h){
		f = new JFrame(caption);
		f.setSize(w, h);
		f.setLocation(x, y);

	}
	
	public JButton makeBtn(String caption, int x, int y, int w, int h, boundEvent e){
		JButton nBtn = new JButton(caption);
		nBtn.setBounds(x, y, w, h);
		nBtn.addActionListener(new ActionListener()  {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.trigger();
			}
		});
		return nBtn;
	}
	
	public JScrollPane makeScrollPane(Component child, int x, int y, int w, int h){
        JScrollPane scrollPane = new JScrollPane(child);
        scrollPane.setBounds(x, y, w, h);
        return scrollPane;
	}	
	
	public JTable makeTable(DefaultTableModel model){
		table = new JTable(model);
		

        // 设置表格内容颜色
        table.setForeground(Color.BLACK);                   // 字体颜色
        table.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        table.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
        table.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
        table.setGridColor(Color.GRAY);                     // 网格颜色

        // 设置表头
        table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        //table.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        table.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        table.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

        // 设置行高
        table.setRowHeight(30);

        // 第一列列宽设置为40

        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table.setPreferredScrollableViewportSize(new Dimension(400, 300));

		return table;
	}
	
	public void add(Component unit){
		f.add(unit);
	}
	
	public void repaintTable(){
		table.getColumnModel().getColumn(1).setPreferredWidth(400);
	}
	
	public void repaint(){
		f.repaint();
	}
	public void displayGUI(){
		
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.update(null);
		
		f.setVisible(true);
	}
}
