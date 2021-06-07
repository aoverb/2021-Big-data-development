package main;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class TableData implements Observer{
	static DefaultTableModel tModel;
	static Vector<Vector<String>> data;
	static HashMap<String, String> state;
	static HashMap<String, String> opreation;
	static Vector names;
	static long lastmill;
	static boolean lock;
	public TableData(){
		lastmill = 0;
		lock = false;
		tModel = new DefaultTableModel();
		Vector row = new Vector(); // ������������ʹ������add()���Ԫ�أ�����������String��Object�ȣ��м��о�new����������
		state = new HashMap<String, String>();
		opreation = new HashMap<String, String>();
		row.add("����");
		row.add("�ļ�");
		row.add("����");
		data = new Vector<Vector<String>>(); // ����������������Ϊ�б�ֹһ�У������������������������ӷ���add(row)
		names = new Vector();// ����������ʹ������add()�����������
		names.addAll(row);
		tModel.setDataVector(data, names); // ����ģ���е�Ԫ�أ������Զ���ʾ���б���
	}
	public DefaultTableModel getModel(){
		return tModel;
	}
	public void clear(){
		data.clear();
		state.clear();
		opreation.clear();
		tModel.setRowCount(0);
	}
	public void add(String opr, String fp, String pgs){
		Vector row = new Vector(); // ������������ʹ������add()���Ԫ�أ�����������String��Object�ȣ��м��о�new����������
		row.add(opr);
		row.add(fp);
		row.add(pgs);
		state.put(fp, pgs);
		opreation.put(fp, opr);
		data.add(row);
		//tModel.setRowCount(tModel.getRowCount() + 1);
		
	}
	public void sortData(){
		Collections.sort(data, new Comparator<Vector<String>>(){

			@Override
			public int compare(Vector<String> arg0, Vector<String> arg1) {
				// TODO Auto-generated method stub
				if (arg0.get(0).compareTo(arg1.get(0)) != 0) return arg0.get(0).compareTo(arg1.get(0));
				return arg0.get(1).compareTo(arg1.get(1));
			}
		});
	}
	public void refresh(){
		sortData();
		tModel.setDataVector(data, names); // ����ģ���е�Ԫ�أ������Զ���ʾ���б���
	}
	@Override
	public void update(String unit, String state) {
		long nowmill = System.currentTimeMillis();
		if (nowmill - lastmill > 1000 || state == "���"){
			lastmill = System.currentTimeMillis();
		}else{
			return;
		}
		while (lock){
			//...
		}
		lock = true;
		TableData.state.put(unit, state);
		/*regenerate*/
		data.clear();
		for (String k: TableData.state.keySet()){
			Vector row = new Vector(); // ������������ʹ������add()���Ԫ�أ�����������String��Object�ȣ��м��о�new����������
			row.add(TableData.state.get(k));
			row.add(k);
			row.add(TableData.opreation.get(k));
			data.add(row);			
		}
		
		refresh();
		lock = false;
	}
}


