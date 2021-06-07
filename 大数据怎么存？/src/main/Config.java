package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Config {
	static String bucketName;
	static String filePath;
	static String accessKey;
	static String secretKey;
	static String serviceEndpoint;
	static String signingRegion;
	static String syncPath;
	static String sliceThreshold;
	static String sliceSize;
	static String lastSync;
	private static Config instance;
    private Config(){

    }
    public static Config getInstance(){
    	bucketName = "wuhongming";
    	filePath   = "C:\\Users\\Sayori\\workspace\\01\\hello.txt";
    	accessKey = "294D4C03E0EB6B66040F";
    	secretKey = "W0REODlBRjgzOTU1NEVENDRBMTE1MjY0QTg3NzYy";
    	serviceEndpoint = "http://10.16.0.1:81";
    	signingRegion = "";
    	syncPath = "D:\\1\\sync\\";
    	sliceThreshold = "20";
    	sliceSize = "5242880";
    	lastSync = "2020-2-20 15:15:15";
        if(instance==null){
            instance=new Config();
        }
        return instance;
    }
    public static void modify(cfgItem item, String val){
    	switch(item){
	    	case bucketName:
	    		break;
	    	case filePath:
	    		break;
	    	case accessKey:
	    		break;
	    	case secretKey:
	    		break;
	    	case serviceEndpoint:
	    		break;
	    	case signingRegion:
	    		break;
	    	case syncPath:
	    		break;
	    	case sliceThreshold:
	    		break;
	    	case sliceSize:
	    		break;
	    	case lastSync:
	    		break;
    	}    	
    }
    
    public void load(){
        String jsonStr = "";
        try {
            File jsonFile = new File("config.json");
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            JSONObject conf = JSON.parseObject(jsonStr);
        	bucketName = (String)conf.get("bucketName");
        	filePath   = (String)conf.get("filePath");
        	accessKey = (String)conf.get("accessKey");
        	secretKey = (String)conf.get("secretKey");
        	serviceEndpoint = (String)conf.get("serviceEndpoint");
        	signingRegion = (String)conf.get("signingRegion");
        	syncPath = (String)conf.get("syncPath");
        	sliceThreshold = (String)conf.get("sliceThreshold");
        	sliceSize = (String)conf.get("sliceSize");
        	lastSync = (String)conf.get("lastSync");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String get(cfgItem item){
    	switch(item){
	    	case bucketName:
	    		return bucketName;
	    	case filePath:
	    		return filePath;
	    	case accessKey:
	    		return accessKey;
	    	case secretKey:
	    		return secretKey;
	    	case serviceEndpoint:
	    		return serviceEndpoint;
	    	case signingRegion:
	    		return signingRegion;
	    	case syncPath:
	    		return syncPath;
	    	case sliceThreshold:
	    		return sliceThreshold;
			case sliceSize:
		    	return sliceSize;
			case lastSync:
		    	return lastSync;
			default:
				return bucketName;
    	}
    }
}
