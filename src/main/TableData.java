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
		Vector row = new Vector(); // 数据行向量，使用它的add()添加元素，比如整数、String、Object等，有几行就new几个行向量
		state = new HashMap<String, String>();
		opreation = new HashMap<String, String>();
		row.add("操作");
		row.add("文件");
		row.add("进度");
		data = new Vector<Vector<String>>(); // 数据行向量集，因为列表不止一行，往里面添加数据行向量，添加方法add(row)
		names = new Vector();// 列名向量，使用它的add()方法添加列名
		names.addAll(row);
		tModel.setDataVector(data, names); // 设置模型中的元素，它会自动显示在列表中
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
		Vector row = new Vector(); // 数据行向量，使用它的add()添加元素，比如整数、String、Object等，有几行就new几个行向量
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
		tModel.setDataVector(data, names); // 设置模型中的元素，它会自动显示在列表中
	}
	@Override
	public void update(String unit, String state) {
		long nowmill = System.currentTimeMillis();
		if (nowmill - lastmill > 1000 || state == "完成"){
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
			Vector row = new Vector(); // 数据行向量，使用它的add()添加元素，比如整数、String、Object等，有几行就new几个行向量
			row.add(TableData.state.get(k));
			row.add(k);
			row.add(TableData.opreation.get(k));
			data.add(row);			
		}
		
		refresh();
		lock = false;
	}
}


