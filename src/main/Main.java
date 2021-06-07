package main;

import java.nio.channels.FileChannel;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;
import javax.swing.JOptionPane;

import java.nio.file.Paths;
import java.util.*;


public class Main{//Syncer
	static Config config = Config.getInstance(); 
	static Aws aws;
	static LocalS ls;
	static GUI gui;
	static TableData tData;

	private static void loadCfg(){
		config.load();
	}
	
	private static void local2aws(){
		//We want to upload all files but:
		//1. Bucket has files that local does not have;(you may delete them)
		//2. Bucket has files that local has, some of which content might be changed.(you may ask user for his/her will)
		//Once we handled the problems above, we could simply upload all files.
		
		
		//Have a look at file list in aws storage
		List<String> deleteList = new ArrayList<String>();
		List<String> uploadList = new ArrayList<String>();
		HashSet<String> noUploadList = new HashSet<String>();
		List<List<String>> afl = aws.fileList();
		String fp = "", eTag = "", lm = "", size = "";
		for (List<String> li : afl){
			for (int i = 0; i < li.size(); i++){
				switch(i){
				case 0:
					eTag = li.get(i);
				case 1:
					fp = li.get(i).replace("\\", "/");
				case 2:
					lm = li.get(i);
				case 3:
					size = li.get(i);
				default:
				}
			}
			if (ls.exist(fp)){// If it exists, then we'll check its md5.
				if (!LocalS.md5(fp).equals("folder") && !LocalS.md5(fp).equals(eTag)){
					int choice = JOptionPane.showConfirmDialog(null, "��Ͱ" + config.get(cfgItem.bucketName) + "�н���ͬ��ʱ��⵽�����³�ͻ��\n" +
							"�ļ�" + fp + "�Ѵ��ڡ�\n\nͰ�е��ļ���Ϣ\n��С��" + size + "\n�޸����ڣ�" + lm + "\n\n���ص��ļ���Ϣ\n��С��"+
							LocalS.size(fp) + "\n�޸����ڣ�"+ LocalS.lastModified(fp).toString() + "\n\n������ǡ��滻�ļ���������񡱱����ļ���"
							);
					if (choice == JOptionPane.NO_OPTION){
						noUploadList.add(fp);
					}
					//replace or keep it intact?
					
				}else{
					noUploadList.add(fp);
				}
			}else{ // If it does'nt exist, then delete it.
				deleteList.add(fp);
			}
		}
		
		//When we step here, we only need to upload all files in local folder.
		//But note that we shouldn't replace the files that user had decided to keep 'em intact.
		List<List<String>> lfl = LocalS.fileList();
		for (List<String> li : lfl){
			for (int i = 0; i < li.size(); i++){
				switch(i){
				case 0:
					eTag = li.get(i);
				case 1:
					fp = li.get(i).replace("\\", "/");
				case 2:
					lm = li.get(i);
				case 3:
					size = li.get(i);
				default:
				}
			}
			if (noUploadList.add(fp) == true){
				uploadList.add(fp);
			}
		}
		
		//We can finally do things we want :)
		tData.clear();
		for (String d : uploadList){
			tData.add("�ϴ�", d, "�Ŷ���");
		}
		tData.refresh();
		gui.repaint();
		aws.resetTask();
		for (String d : uploadList){

			aws.uploadfile(config.get(cfgItem.syncPath) + d, d, Long.valueOf(config.get(cfgItem.sliceSize)));
			System.out.println(d);

		}
		Thread t = new Thread(aws);
		t.start();
		
		aws.deleteFile(deleteList);

		
	}
	
	private static void aws2local(){
		//We could simply download all files but:
		//1. Local may have files that bucket does not have;(you could just delete 'em)
		//2. Local has files that bucket has, some of which content might be changed.(you may ask user for his/her will)
		//Once we handled the problems above, we could simply download all files.
		
		//Have a look at file list in aws storage
		List<String> deleteList = new ArrayList<String>();
		List<String> downloadList = new ArrayList<String>();
		HashSet<String> noDownloadList = new HashSet<String>();
		HashMap<String, String> bucketFileHash = new HashMap<String, String>();
		HashMap<String, String> bucketFileSize = new HashMap<String, String>();
		HashMap<String, String> bucketFileLastModified = new HashMap<String, String>();
		List<List<String>> afl = aws.fileList();
		String fp = "", eTag = "", lm = "", size = "";
		for (List<String> li : afl){
			for (int i = 0; i < li.size(); i++){
				switch(i){
				case 0:
					eTag = li.get(i);
				case 1:
					fp = li.get(i).replace("\\", "/");
				case 2:
					lm = li.get(i);
				case 3:
					size = li.get(i);
				default:
				}
			}
			
			bucketFileHash.put(fp, eTag);
			bucketFileLastModified.put(fp, lm);
			bucketFileSize.put(fp, size);
		}

		List<List<String>> lfl = LocalS.fileList();
		for (List<String> li : lfl){
			for (int i = 0; i < li.size(); i++){
				switch(i){
				case 0:
					eTag = li.get(i);
				case 1:
					fp = li.get(i).replace("\\", "/");
				case 2:
					lm = li.get(i);
				case 3:
					size = li.get(i);
				default:
				}
			}
			System.out.println("!!!!!!" + fp);
			if (bucketFileHash.get(fp) == null){
				System.out.println("delete: " + fp);
				deleteList.add(fp);
			}else if (!LocalS.md5(fp).equals("folder") && !LocalS.md5(fp).equals(bucketFileHash.get(fp))){
				int choice = JOptionPane.showConfirmDialog(null, "�ڽ�������Ͱ" + config.get(cfgItem.bucketName) + "ͬ��ʱ��⵽�����³�ͻ��\n" +
						"�ļ�" + fp + "�Ѵ��ڡ�\n\nͰ�е��ļ���Ϣ\n��С��" + bucketFileSize.get(fp) + "\n�޸����ڣ�" + bucketFileLastModified.get(fp) + "\n\n���ص��ļ���Ϣ\n��С��"+
						LocalS.size(fp) + "\n�޸����ڣ�"+ LocalS.lastModified(fp).toString() + "\n\n������ǡ��滻�ļ���������񡱱����ļ���"
						);
				if (choice == JOptionPane.NO_OPTION){
					noDownloadList.add(fp);
				}
				//replace or keep it intact?
				
			}else{
				System.out.println("don;t download " + fp);
				noDownloadList.add(fp);
			}
		}
		for (List<String> li : afl){
			for (int i = 0; i < li.size(); i++){
				switch(i){
				case 0:
					eTag = li.get(i);
				case 1:
					fp = li.get(i);
				case 2:
					lm = li.get(i);
				case 3:
					size = li.get(i);
				default:
				}
			}
			if (noDownloadList.add(fp) == true){
				downloadList.add(fp);
			}
			
		}
		//We can finally do things we want :)
		tData.clear();
		for (String d : downloadList){
			tData.add("����", d, "�Ŷ���");
		}
		tData.refresh();
		gui.repaint();
		aws.resetTask();
		
		
		
		for (String d : downloadList){
			//Make sure we have corresponding folders in local path
			int findex = 0;
			System.out.println(d.indexOf("/", findex));
			while(d.indexOf("/", findex) != -1){
				
				findex = d.indexOf("/", findex) + 1;
				File nf = new File(config.get(cfgItem.syncPath) + d.substring(0, findex));
				if(!nf.exists() && !nf.isDirectory()) nf.mkdir();
				System.out.println("make dir: " + nf.getAbsolutePath());
			}

			aws.downloadfile(config.get(cfgItem.syncPath) + d, d, Long.valueOf(config.get(cfgItem.sliceSize)));
			System.out.println(d);

		}
		List<String> delList = new ArrayList<String>();
		for (String fn: deleteList){
			System.out.println(fn);
			delList.add(config.get(cfgItem.syncPath) + fn);
		}
		aws.setDeleteList(delList);
		Thread t = new Thread(aws);
		t.start();

		

		
	}	
	
	public static void main(String[] args){
		loadCfg(); 
		aws = new Aws(config.get(cfgItem.accessKey), config.get(cfgItem.secretKey), config.get(cfgItem.serviceEndpoint), config.get(cfgItem.signingRegion), config.get(cfgItem.bucketName));
		
		ls = new LocalS(config.get(cfgItem.syncPath));
		tData = new TableData();
		aws.setObserver(tData);
		gui = new GUI("s3 syncer", 50, 50, 980, 480);
		gui.add(gui.makeBtn("����Ͱ�ļ�", 300, 10, 200, 30, new boundEvent(){
			@Override
			public void trigger(){
				local2aws();
			}
		}));
		gui.add(gui.makeBtn("���±����ļ�", 30, 10, 200, 30, new boundEvent(){
			@Override
			public void trigger(){
				aws2local();
			}
		}));
		/*
		gui.add(gui.makeBtn("����", 260, 10, 200, 30, new boundEvent(){
			@Override
			public void trigger(){
				tData.add("1", "2", "3");
				tData.sortData();
			}
		}));
		gui.add(gui.makeBtn("��ʾ", 260, 50, 200, 30, new boundEvent(){
			@Override
			public void trigger(){
				tData.refresh();
			}
		}));*/
		gui.add(gui.makeScrollPane(gui.makeTable(tData.getModel()), 10, 50, 950, 380));

        // �������������
        gui.displayGUI();

		loadCfg();

	}


}
