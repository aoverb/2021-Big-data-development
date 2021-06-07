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
		

        // ���ñ��������ɫ
        table.setForeground(Color.BLACK);                   // ������ɫ
        table.setFont(new Font(null, Font.PLAIN, 14));      // ������ʽ
        table.setSelectionForeground(Color.DARK_GRAY);      // ѡ�к�������ɫ
        table.setSelectionBackground(Color.LIGHT_GRAY);     // ѡ�к����屳��
        table.setGridColor(Color.GRAY);                     // ������ɫ

        // ���ñ�ͷ
        table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // ���ñ�ͷ����������ʽ
        //table.getTableHeader().setForeground(Color.RED);                // ���ñ�ͷ����������ɫ
        table.getTableHeader().setResizingAllowed(false);               // ���ò������ֶ��ı��п�
        table.getTableHeader().setReorderingAllowed(false);             // ���ò������϶������������

        // �����и�
        table.setRowHeight(30);

        // ��һ���п�����Ϊ40

        // ���ù�������ӿڴ�С�������ô�С�������ݣ���Ҫ�϶����������ܿ�����
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
