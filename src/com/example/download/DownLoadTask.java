package com.example.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpStatus;

import com.example.DB.download.*;
import com.example.DB.download.ThreadOADImpl_download;
import com.example.kugou.download_entities.*;
import com.example.kugou.DBdata.Search_information;

import android.content.Context;
import android.content.Intent;
import android.text.style.URLSpan;
import android.util.Log;
/**
 * �ļ�������
 * ָ��Ҫ���ص��ļ���Ϣ���Ҫ�ֶ����߳̽�������
 * 
 * DownLoadTask()				��ʼ��,�õ�������,�����صĸ�����Ϣ��,�����߳���
 * downLoad()					����Ҫ���صĸ�����Ϣ��������߳���,��һ���ļ�������һ��һ�ε�������Ϣ��,���뵽�̳߳�,��������
 * checkAllThreadsFinshed()		����Ƿ�ÿ��������Ϣ�����(�������غ�һ��������)
 * DownLoadThread 				ָ��һ��������Ϣ��  �������ز���
 * 
 * @author Administrator
 *
 */
public class DownLoadTask {
	
	private Context context;
	private Search_information search_information;
	//������Ϣ���ݿ�ʵ��
	private ThreadOADImpl_download oad;
	//Ĭ������һ���߳̽�������
	private int ThreadCount = 1;
	//���������Ϣ��
	private List<DownLoadThread> downLoadThreads;
	//��������ܽ���
	private int finished = 0;
	//�Ƿ�ֹͣ
	public boolean isPause =false;
	
	public DownLoadTask(Context context, Search_information search_information, int ThreadCount) {
		super();
		this.context = context;
		this.search_information = search_information;
		this.ThreadCount = ThreadCount;
		oad = new ThreadOADImpl_download(context);
	}
	
	public void downLoad(){
		//��ȡ���ݿ���߳���Ϣ
		List<ThreadInfo> threadInfos = oad.getThread(search_information.songLink);
		//������ݿ�û�д��ļ���������Ϣ,�еĻ���ֱ��ʹ��
		if(threadInfos.size() == 0){	
			//����Ǹ��̵߳����س���
			int length = search_information.songLength / ThreadCount;
			for (int i = 0; i < ThreadCount; i++) {
				//�����߳���Ϣ
				ThreadInfo threadInfo = new ThreadInfo(i, search_information.songLink, i * length, (i + 1) * length -1, 0);
				if(i == ThreadCount -1){
					//��������һ��,���Ⱦ�ȫ����
					threadInfo.setEnd(search_information.songLength);
				}
				//��ӵ��߳���Ϣ������
				threadInfos.add(threadInfo);
				//��ӵ����ݿ�
				oad.insertThread(threadInfo);
			}
		}
		downLoadThreads = new ArrayList<DownLoadTask.DownLoadThread>();
		//��������߳̽�������
		for (ThreadInfo threadInfo : threadInfos) {
			DownLoadThread thread = new DownLoadThread(threadInfo);
			DownLoadService.executorService.execute(thread);
			downLoadThreads.add(thread);
		}
	}
	
	private synchronized void checkAllThreadsFinshed(){
		//�ж�һ���ļ���ÿ���Ƿ�ȫ���������
		boolean allFinshed = true;
		for (DownLoadThread downLoadThread : downLoadThreads) {
			if(!downLoadThread.isFinished){
				allFinshed = false;
				break;
			}
		}
		//����������,�ͷ��͹㲥
		if(allFinshed){
			Intent intent = new Intent(DownLoadService.ACTION_FINISH);
			intent.putExtra("search_information", search_information);
			context.sendBroadcast(intent);
			//ɾ���߳���Ϣ
			oad.deleteThread(search_information.songLink);
		}
	}
	
	class DownLoadThread extends Thread{
		private ThreadInfo threadInfo;
		public boolean isFinished = false; //�߳��Ƿ�ִ�����

		public DownLoadThread(ThreadInfo threadInfo) {
			this.threadInfo = threadInfo;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpURLConnection conn = null;
			RandomAccessFile raf = null;
			InputStream is =null;
			try {
				URL url = new URL(threadInfo.getUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				//�����̵߳�����λ��
				int start = threadInfo.getStart() + threadInfo.getFinished();
				conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
				//�����ļ���д��λ��
				File file = new File(DownLoadService.DOWNLOAD_PATH, search_information.songId + "." + search_information.format);
				raf= new RandomAccessFile(file, "rwd");
				raf.seek(start);						//�����ļ�д��λ��
				Intent intent = new Intent("ACTION_UPDATE");
				finished += threadInfo.getFinished();
				//��ʼ����
				if(conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT){  //���ز������ݳɹ�
					//��ȡ����
					is = conn.getInputStream();
					byte[] buffer = new byte[1024*4];
					int len=-1;
					long time = System.currentTimeMillis();
					while((len = is.read(buffer)) != -1){   //is��ȡbuffer���ȵ��ֽ��� �������-1 ˵������û���� ���ǵĻ�˵�����滹��
						//д���ļ�
						raf.write(buffer,0,len);
						//�ۼ������ļ��Ľ���
						finished += len;
						//�ۼ�ÿ���߳���ɵĽ��� ���浽���ݿ�
						threadInfo.setFinished(threadInfo.getFinished() + len);
						//����ͨ���㲥���͸�UI
						if(System.currentTimeMillis() - time > 1000){	//ÿһ��һ�ι㲥
							intent.putExtra("id", search_information.songId);
							//�Ѱٷֱȵ����������й㲥����  int*100�ᳬ����Χ ����ǿתһ��
							intent.putExtra("finished", (int)(finished / (float)search_information.songLength * 100));	
							context.sendBroadcast(intent);
							time = System.currentTimeMillis();
						}
						if(isPause){
							oad.updateThread(threadInfo.getUrl(), threadInfo.getId(), threadInfo.getFinished());
							return;
						}
					}
					//��ʾ�߳�ִ�����
					isFinished = true;
					//һ���߳�ִ�����ʱ��ѯһ���Ƿ�ȫ���߳�ִ�����
					checkAllThreadsFinshed();
				}
				//��ͣʱ,����д�����ݿ�
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					is.close();
					raf.close();
					conn.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
