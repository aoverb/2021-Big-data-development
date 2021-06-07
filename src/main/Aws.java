package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.nio.ByteBuffer;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;


public class Aws implements Subject, Runnable{
	static AmazonS3 s3;
	static String bucketName;
	static ArrayList<String> mulFP, mulKN;
	static ArrayList<Long> mulPS;
	static List<String> delList;
	static Integer taskcnt;
	static boolean lock;
	final static int STATE_UPLOAD = 1;
	final static int STATE_DOWNLOAD = 2;
	final static int STATE_DELETE = 3;
	final static int STATE_CREATE = 4;
	static ArrayList<Integer> state;
	Observer observer;
	public boolean deleteFile(String path) {
		return false;
		
	}
	public Aws(String accessKey, String secretKey, String serviceEndpoint, String signingRegion, String bucketName) {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(true);
		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
		s3 = AmazonS3ClientBuilder.standard()
				                .withCredentials(new AWSStaticCredentialsProvider(credentials))
				                .withClientConfiguration(ccfg)
				                .withEndpointConfiguration(endpoint)
				                .withPathStyleAccessEnabled(true)
				                .build();
		try{
			s3.listBuckets();

		}
		catch (Exception ex){
			JOptionPane.showMessageDialog(null, "无法连接aws s3服务，请检查你的网络连接、配置文件是否正确。\n程序将退出。");
			System.exit(0);
		}
		Aws.bucketName = bucketName;
		lock = false;
		taskcnt = 0;
		mulFP = new ArrayList<String>();
		mulKN = new ArrayList<String>();
		mulPS = new ArrayList<Long>();
		state = new ArrayList<Integer>(); 
	}
	
	public List<List<String>> fileList(){
		List<List<String>> rtn = new ArrayList<List<String>>();
		System.out.println(bucketName);
		ListObjectsV2Result result = s3.listObjectsV2(bucketName);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		for (S3ObjectSummary os : objects) {
		    List<String> sublst= new ArrayList<String>();
		    sublst.add(os.getETag());
		    sublst.add(os.getKey());
		    sublst.add(os.getLastModified().toString());
		    sublst.add(String.valueOf(os.getSize()));
		    rtn.add(sublst);
		}
		return rtn;
	}
	public void fileConcat(String dest, String fp1, String fp2) throws IOException{
		FileInputStream filea = new FileInputStream(fp1);
	     FileInputStream fileb = new FileInputStream(fp2);
	     File outfile=new File(dest);
	     int filesizea=filea.available();//计算文件的大小
	     int filesizeb=fileb.available();
	     FileOutputStream fos=new FileOutputStream(outfile);
	 
	     int hasReada = 0;
	     int hasReadb=0; 
	      int count = 0;
	      int countb = 0;
	     byte[] bufa=new byte[1024];
	     byte[] bufc=new byte[1024];
	     byte[] buf_yua=new byte[filesizea%1024];
	     byte[] buf_yub=new byte[filesizeb%1024];
	 
	     while( (hasReada=filea.read(bufa) )>0 )
	      {

		if(count<filesizea-filesizea%1024)
	      {  
	        for(int i=0;i<bufa.length && count<filesizea-filesizea%1024;i++)
	         {
	 
	          bufc[i]=(byte)(bufa[i] & 0xFF);
	          count++;
	 
	         }
	        fos.write(bufc);
	      }
	      else if(count>=filesizea-filesizea%1024 && count<filesizea)
	      {
	 
	        for(int j=0; count>=filesizea-filesizea%1024 && count<filesizea ;j++)
	        {
	          buf_yua[j]=(byte)(bufa[j] & 0xFF);
	          count++;
	 
	        }
	        fos.write(buf_yua);
	      }
	 
	      }
	 
	     while( (hasReadb=fileb.read(bufa) )>0 )
	      {
	      
		if(countb<filesizeb-filesizeb%1024)
	      {  
	        for(int i=0;i<bufa.length && countb<filesizeb-filesizeb%1024;i++)
	         {
	 
	          bufc[i]=(byte)(bufa[i] & 0xFF);
	          countb++;
	 
	         }
	        fos.write(bufc);
	      }
	      else if(countb>=filesizeb-filesizeb%1024 && countb<filesizeb)
	      {
	 
	        for(int j=0; countb>=filesizeb-filesizeb%1024 && countb<filesizeb ;j++)
	        {
	          buf_yub[j]=(byte)(bufa[j] & 0xFF);
	          countb++;
	 
	        }
	        fos.write(buf_yub);
	      }
	 
	 
	 
	      } 
	     filea.close();
	     fileb.close();
	     fos.close();
	}
	
	public void setDeleteList(List<String> delList2){
		Aws.delList = delList2;
	}
	
	public void mulDownloadfile(int idx) throws IOException{
		String filePath = mulFP.get(idx);
		String keyName = mulKN.get(idx).replace("\\", "/");
		Long partSize = mulPS.get(idx);
		File file = new File(filePath);
		S3Object o = null;

		S3ObjectInputStream s3is = null;
		FileOutputStream fos = null;
		notifys(keyName, "正在开始");
		if (keyName.substring(keyName.length() - 1).equals("/")){
			notifys(keyName, "完成");
			return;
		}
		int fcnt = 1;
		try {
			// Step 1: Initialize.
			ObjectMetadata oMetaData = s3.getObjectMetadata(bucketName, keyName);
			final long contentLength = oMetaData.getContentLength();
			final GetObjectRequest downloadRequest = 
			new GetObjectRequest(bucketName, keyName);

			int prev = 1;
			while((new File(filePath + "." + prev)).exists()){
				prev = prev + 1;
				System.out.println("断点续传？");
			}
			long filePosition, tot;
			if (prev > 1){
				(new File(filePath + "." + (prev - 1))).delete();
				// Step 2: Download parts.
				filePosition = (prev - 2) * (partSize + 1);
				tot = (prev - 2) * partSize;
			}else{
				filePosition = 0;
				tot = 0;
			}

			
			for (int i = Math.max(1, prev - 1); filePosition < contentLength; i++) {
				fcnt = i;
				File sfile = new File(filePath + "." + i);
				fos = new FileOutputStream(sfile);
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, contentLength - filePosition);

				// Create request to download a part.
				downloadRequest.setRange(filePosition, filePosition + partSize);
				o = s3.getObject(downloadRequest);

				// download part and save to local file.
				System.out.format("Downloading part %d\n", i);

				filePosition += partSize+1;
				
				s3is = o.getObjectContent();
				byte[] read_buf = new byte[64 * 1024];
				int read_len = 0;
				while ((read_len = s3is.read(read_buf)) > 0) {
					tot += read_len;
					String prgs = ((tot * 10000 / contentLength) * 0.01 + "");
					notifys(keyName, prgs.substring(0, prgs.indexOf('.') + 2) + "%");
					fos.write(read_buf, 0, read_len);
				}
				fos.close();
			}

			// Step 3: Complete.
			System.out.println("Completing download");

			System.out.format("save %s to %s\n", keyName, filePath);
		} catch (Exception e) {
			notifys(keyName, "失败:" + e.getStackTrace()[1]);
			return;
		} finally {
			if (s3is != null) try { s3is.close(); } catch (IOException e) { }
			if (fos != null) try { fos.close(); } catch (IOException e) { }
		}
		notifys(keyName, "合并文件...");
		String twirl = ".a";
		String twirl2 = ".b";
		File nf = new File(filePath + twirl);
		if (!nf.exists()){
			nf.createNewFile();
		}
		nf = new File(filePath + twirl2);
		if (!nf.exists()){
			nf.createNewFile();
		}
		fileConcat(filePath + twirl2, filePath + twirl, filePath + ".1");
		for (int i = 2; i <= fcnt; i++){
			if (i % 2 == 0){
				fileConcat(filePath + twirl, filePath + twirl2, filePath + "." + i);
			}else{
				fileConcat(filePath + twirl2, filePath + twirl, filePath + "." + i);
			}
		}
		if (fcnt % 2 == 0) nf = new File(filePath + twirl);
		else nf = new File(filePath + twirl2);
		nf.renameTo(new File(filePath));
		for (int i = 1; i <= fcnt; i++){
			(new File(filePath + "." + i)).delete();
		}
		(new File(filePath + ".a")).delete();
		(new File(filePath + ".b")).delete();
		notifys(keyName, "完成");
	}	
	
	public void mulUploadFile (int idx){
		String filePath = mulFP.get(idx);
		String keyName = mulKN.get(idx);
		Long partSize = mulPS.get(idx);
		System.out.println(keyName);
		if (filePath.substring(filePath.length() - 1).equals("/")){
			s3.putObject(bucketName, keyName, "");
			notifys(keyName, "完成");
			return;
		}
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
        MultipartUploadListing multipartUploadListing = s3.listMultipartUploads(new ListMultipartUploadsRequest(bucketName));
        boolean isBP = false;
        List<MultipartUpload> multipartUploads = multipartUploadListing.getMultipartUploads();
        int uploadIndex = 0;
        for (int i = 0; i < multipartUploads.size(); i++){
        	MultipartUpload mu = multipartUploads.get(i);
            if (mu.getKey().equals(keyName)){
            	isBP = true;
            	uploadIndex = i;
            }
        }


        if (isBP){// If it has a breakpoint, then add all ETags in our list beforehand.
            List<PartSummary> parts = s3.listParts(new ListPartsRequest(bucketName, multipartUploads.get(uploadIndex).getKey(), multipartUploads.get(uploadIndex).getUploadId())).getParts();
            for (int j = 0; j < parts.size(); j++) {
                partETags.add(new PartETag(parts.get(j).getPartNumber(), parts.get(j).getETag()));
            }
        }

        File file = new File(filePath);

        long contentLength = file.length();
        String uploadId = isBP?multipartUploads.get(uploadIndex).getUploadId():null;
        try {
            // Step 1: Initialize.
        	if (!isBP){
            	InitiateMultipartUploadRequest initRequest = 
            			new InitiateMultipartUploadRequest(bucketName, keyName);
            			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
        	}

        	notifys(keyName, isBP?"继续上传":"创建上传任务");
        	
            // Step 2: Upload parts.
        	long filePosition;
        	int k;
        	if (isBP){
        		List<PartSummary> parts = s3.listParts(new ListPartsRequest(bucketName, keyName, uploadId)).getParts();
        		k = parts.size() + 1;
        		filePosition = parts.size() * partSize;
        	}else{
        		k = 1;
        		filePosition = 0;
        	}
        	

            for (; filePosition < contentLength; k++) {
            	
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, contentLength - filePosition);
 
                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                		.withBucketName(bucketName)
                		.withKey(keyName)
                        .withUploadId(uploadId)
                        .withPartNumber(k)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);
                
                String prgs = ((filePosition * 10000 / contentLength) * 0.01 + "");
				notifys(keyName, prgs.substring(0, prgs.indexOf('.') + 2) + "%");
                // Upload part and add response to our list.
                partETags.add(s3.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
                
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

            s3.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            System.err.println(e.toString());
            if (uploadId != null && !uploadId.isEmpty()) {
                // Cancel when error occurred
                s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
            }
            System.exit(1);
        }
        notifys(keyName, "完成");
        
	}
	public void deleteFile(List<String> delList){
		List<KeyVersion> kv = new ArrayList<KeyVersion>();
		for (String k: delList){
			kv.add(new KeyVersion(k));
		}
		try {
		    DeleteObjectsRequest dor = new DeleteObjectsRequest(bucketName)
		            .withKeys(kv);
		    s3.deleteObjects(dor);
		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		}
	}
	public void downloadfile(String filePath, String keyName, Long partSize){
		mulFP.add(filePath);
		mulKN.add(keyName);
		mulPS.add(partSize);
		state.add(STATE_DOWNLOAD);
		taskcnt = taskcnt + 1;
	}
	public void uploadfile(String filePath, String keyName, Long partSize){
		mulFP.add(filePath);
		mulKN.add(keyName);
		mulPS.add(partSize);
		state.add(STATE_UPLOAD);
		taskcnt = taskcnt + 1;
	}
	public void resetTask(){
		mulFP.clear();
		mulKN.clear();
		mulPS.clear();
		state.clear();
		taskcnt = 0;
	}
	@Override
	public void setObserver(Observer o) {
		observer = o;
		
	}
	@Override
	public void notifys(String unit, String state) {
		observer.update(unit, state);
		//System.out.println(unit + ": " + state);
	}
	public void run() {
		int i = 0;
		System.out.println(i);
		for (i = 0; i < taskcnt; i++){
			System.out.println(Aws.state.get(i));
			switch(Aws.state.get(i)){
				case Aws.STATE_DOWNLOAD:
				try {
					mulDownloadfile(i);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				case Aws.STATE_UPLOAD:
					System.out.print("我怎么就上传了？？");
					mulUploadFile(i);
					break;
				default:
			}

		}
		for (String delf: delList){
			(new File(delf)).delete();
		}
		JOptionPane.showMessageDialog(null, "本地文件与远程同步成功。\n同步了" + taskcnt +"个文件，移除了"+ delList.size() +"个文件。");
	}
}
