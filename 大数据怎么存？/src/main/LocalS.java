package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import org.apache.commons.codec.digest.DigestUtils;

import com.amazonaws.util.Md5Utils;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

public class LocalS {
	static Config config = Config.getInstance();
	static String syncPath;
	public LocalS(String syncPath){
		LocalS.syncPath = syncPath;
		config.load();
	}
	public boolean exist(String fileName){
		if ((new File(syncPath + fileName).exists())){// If it exists, then we'll check its md5.
			return true;
		}
		return false;
	}
	public static long size(String fileName){
		return (new File(syncPath + fileName)).length();
	}
	
	public static Date lastModified(String fileName){
		return new Date((new File(syncPath + fileName)).lastModified());
	}	
	/*
	public static String md5(String fileName) throws FileNotFoundException, IOException{
		FileInputStream f = new FileInputStream(syncPath + fileName);
		String md5 = DigestUtils.md5Hex(f);
		f.close();
	    return md5;
	}*/
	public static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
	public static String md5(String inputFile) {
		int bufferSize = Integer.valueOf(config.get(cfgItem.sliceSize));
		inputFile = syncPath + inputFile;
		if ((new File(inputFile)).isDirectory()) return "folder";
        long contentLength = new File(inputFile).length();
        FileInputStream fis = null;
        List<String> md5s = new ArrayList<>();
        
        try {
            fis = new FileInputStream(inputFile);
            byte[] buffer = new byte[bufferSize];
            int fileNum = 0;
            bufferSize = Math.toIntExact(Math.min(bufferSize, contentLength - (long) fileNum * bufferSize));
            while ((fis.read(buffer, 0, bufferSize)) != -1 && bufferSize != 0) {
            	System.out.println(bufferSize);
                md5s.add(bytesToHex(Md5Utils.computeMD5Hash(subBytes(buffer, 0, bufferSize))));
                fileNum++;
                bufferSize = Math.toIntExact(Math.min(bufferSize, contentLength - (long) fileNum * bufferSize));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String md5 : md5s) {
            stringBuilder.append(md5);
        }
        String hex = stringBuilder.toString();
        byte[] raw = BaseEncoding.base16().decode(hex.toUpperCase());
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putBytes(raw);
        String digest = hasher.hash().toString();

        return digest + "-" + md5s.size();
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }
	
	public void delete(String fileName){
		File delF = new File(syncPath + fileName);
		delF.delete();
	}
	
	public String getSyncPath(){
		return syncPath;
	}
	
	public void setSyncPath(String syncPath){
		LocalS.syncPath = syncPath;
	}
	
	public static List<List<String>> fileList() {
		List<List<String>> rtn = new ArrayList<List<String>>();
        File file = new File(syncPath);
        LinkedList<File> list = new LinkedList<>();
        
        if (file.exists()) {
        	
            if (null == file.listFiles()) {
                return null;
            }
            
            for (File f : file.listFiles()) {
            	List<String> sub2lst = new ArrayList<String>();
                if (f.isDirectory()) {
                	String nf = f.getAbsolutePath().substring(syncPath.length());
					sub2lst.add("folder");
                	list.add(f);
                    sub2lst.add(nf + "/");
                    sub2lst.add(String.valueOf(size(nf)));
                    sub2lst.add(lastModified(nf).toString());
                } else {
                	String nf = f.getAbsolutePath().substring(syncPath.length());
                	sub2lst.add(md5(nf));
                    sub2lst.add(nf);
                    sub2lst.add(String.valueOf(size(nf)));
                    sub2lst.add(lastModified(nf).toString());
                }
                rtn.add(sub2lst);
            }
            while (!list.isEmpty()) {
            	
                File[] files = list.removeFirst().listFiles();
                if (null == files) {
                    continue;
                }
                
                for (File f : files) {
                	List<String> sublst = new ArrayList<String>();
                    if (f.isDirectory()) {
                    	String nf = f.getAbsolutePath().substring(syncPath.length());
                    	sublst.add("0");
                    	list.add(f);
                        sublst.add(nf + "/");
                        System.out.println(nf);
                        sublst.add(String.valueOf(size(nf)));
                        sublst.add(lastModified(nf).toString());
                    } else {
                    	String nf = f.getAbsolutePath().substring(syncPath.length());
                    	sublst.add(md5(nf));
                        sublst.add(nf);
                        sublst.add(String.valueOf(size(nf)));
                        sublst.add(lastModified(nf).toString());
                    }
                    rtn.add(sublst);
                }
                
            }
            
        } else {
        	//file not found
        }
        return rtn;
    }
	
}
