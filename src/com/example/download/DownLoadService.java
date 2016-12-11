package com.example.download;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpConnection;
import org.apache.http.HttpStatus;

import com.example.DB.songlist.ThreadOADImpl_songlist;
import com.example.kugou.Fragment_ting;
import com.example.kugou.DBdata.Search_information;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

/**
 * ���ط���
 * ����intent��Action()ֵ���ж��û�Ҫ������ô�Ĳ���(�����ؿ�ʼ,����ֹͣ),���д��ݹ���������Ϣ��
 * �ٰѸ�����Ϣ�ഫ��InitTheard����,���б��ش��������ó��ȵĲ���,ͨ��Handler�ش�,��Handler�з����ɸ���������
 * 
 * InitTheard		ָ��һ��������Ϣ��,���б��ش��������ó��ȵĲ���,Ȼ��ͨ��handler��������Ϣ�ഫ�ݳ�ȥ
 * handler			��������Ϣ������ɸ���������,�������ز���
 * receiver			�㲥,���ܸ������ؽ��Ⱥ�������ϵ���Ϣ
 * 
 */
public class DownLoadService extends Service {
	//����·��
	public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mKuGouDown/" ;
	//��ʼ����	����
	public static final String ACTION_START = "ACTION_START";
	//ֹͣ����	����
	public static final String ACTION_STOP = "ACTION_STOP";
	//���ؽ��ȸ���	����
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	//���ؽ����	����
	public static final String ACTION_FINISH = "ACTION_FINISH";
	//InitTheard���������������Ϣ���ʱ����лش�������
	public static final int MSG_INIT = 0;
	//��ջ�ķ�ʽ�������ص�������Ϣ
	public static List<Search_information> downSearch_informations = new ArrayList<Search_information>();
	//�̳߳�
	public static ExecutorService executorService = Executors.newCachedThreadPool();  
	//����һ��ID,��ȡ�����е�������
	public static Map<String, DownLoadTask> Tasks = new LinkedHashMap<String, DownLoadTask>();
	//���ظ���ID�Ա�׼ʵ��(������Ϻ������н��в���)
	private ThreadOADImpl_songlist impl = new ThreadOADImpl_songlist(DownLoadService.this);
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//�ų��մ������
		if(intent != null){
			if(!intent.getAction().isEmpty()){
				if(intent.getAction().equals(ACTION_START)){
					//��ȡ�����ĸ�����Ϣ��
					Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");   //��ȡ��activity�������Ĳ���
					//��������Ϣ�ཻ��InitTheard�������б��ش��������ó���
					InitTheard initTheard = new InitTheard(search_information);
					//�����̳߳�
					DownLoadService.executorService.execute(initTheard);
				}
				else if(intent.getAction().equals(ACTION_STOP)){
					//��ȡ�����ĸ�����Ϣ��
					Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");
					//���ݸ���ID��ֹͣ����������
					DownLoadTask downLoadTask = Tasks.get(search_information.songId);
					if(downLoadTask != null)
						downLoadTask.isPause = true;
				}
			}
		}
		//ע��㲥
		IntentFilter filter = new IntentFilter();
        filter.addAction(DownLoadService.ACTION_FINISH);
        filter.addAction(DownLoadService.ACTION_UPDATE);
        registerReceiver(receiver, filter);
        
		return super.onStartCommand(intent, flags, startId);
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver(){
    	public void onReceive(Context context, Intent intent) {
    		//������ܵ��������ȸ��µĹ㲥
    		if(DownLoadService.ACTION_UPDATE.equals(intent.getAction())){
    			//��ȡ������ID�ͽ���
    			String id = intent.getStringExtra("id");
				int finished = intent.getIntExtra("finished", 0);
				//������������Ĳ���
				for (Search_information search_information : downSearch_informations) {
					//����ҵ�����������
					if(search_information.songId.equals(id)){
						//���н��ȸ���
						search_information.finished = finished;
						//������ݿ�����û�д���Ϣ����,�ͼ���,�������
						if(!impl.isExists(id)){
							impl.insertSongInfo(search_information);
						}else {
							impl.updateSongInfo(search_information.songId, search_information.finished);
						}
					}
				}//������ܵ�����������ɵĹ㲥
    		}else if (DownLoadService.ACTION_FINISH.equals(intent.getAction())) {
    			//��ȡ������ɵĸ�����Ϣ
				Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");
				//�Է���һ,�ֶ����н��ȵ�100�Ĳ���
				search_information.finished = 100;
				//���½��ȵ����ظ���ID���ձ����ݿ�
				impl.updateSongInfo(search_information.songId, search_information.finished);
				//�������ݿ��е�url��ɱ���·��
				impl.updateSongInfo_URL(search_information.songId, DOWNLOAD_PATH + search_information.songId + "." + search_information.format);
				//��������һ��textview�ؼ�����,������Ŀ(��textview��ʾ���Ǳ��ظ�������)
				if(Fragment_ting.tv_songlist_num != null){
					List<Search_information> search_informations = impl.getAllFinishSongInfo();
					Fragment_ting.tv_songlist_num.setText(search_informations.size() + "��");
				}
			}
    	};
    };
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INIT:
				//ȡ�������ĸ�����Ϣ��
				Search_information search_information = (Search_information) msg.obj;
				//��������Ϣ�ཻ��DownLoadTask������,�������ط�������		(������,Ҫ���صĸ�����Ϣ��,Ҫ�Ѽ����߳̽�������)
				DownLoadTask downLoadTask = new DownLoadTask(DownLoadService.this, search_information,3);
				//��������
				downLoadTask.downLoad();
				Tasks.put(search_information.songId, downLoadTask);
				//���뵽���������(����еĻ��Ͳ������)
				for (Search_information search_information2 : downSearch_informations) {
					if(search_information2.songId.equals(search_information.songId))
						return;
				}
				downSearch_informations.add(search_information);
				//��Ϊ���뵽list����β��,���Ե�������
				for (int i = downSearch_informations.size() -1 ; i > 0; i--) {
					Search_information s = downSearch_informations.get(i);
					downSearch_informations.set(i, downSearch_informations.get(i - 1));
					downSearch_informations.set(i - 1, s);
				}
				break;
			}
		};
	};
	//����������Ϣ��,��������,���ó���,���ڱ��ش�����,��ͨ��handler����ȥ
	class InitTheard extends Thread{
		
		Search_information search_information = null;
		RandomAccessFile raf = null;
		
		public InitTheard(Search_information search_information) {
			this.search_information = search_information;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpURLConnection conn = null;
			try {
				//��ȡ�����ļ��ĳ���
				URL url = new URL(search_information.songLink);
				conn = (HttpURLConnection) url.openConnection();
				//���ó�ʱ
				conn.setConnectTimeout(3000);   
				//��������ʽ   ������GET
				conn.setRequestMethod("GET");   
				//�ļ��ܳ���
				int length = -1;
				if(conn.getResponseCode() == HttpStatus.SC_OK){   //������ӳɹ�
					length = conn.getContentLength();
				}
				if(length <= 0)
					return;
				File dir = new File(DOWNLOAD_PATH);
				//����ļ������ڵĻ��ʹ���һ��
				if(!dir.exists()){   
					dir.mkdir();
				}
				//���������ļ�
				File file =new File(dir, search_information.songId + "." + search_information.format);
				//��������ļ�  �ڶ�������������Ȩ��  r-read �� | w-write д | d-delete ɾ��
				raf = new RandomAccessFile(file, "rwd"); 
				//���ó���
				raf.setLength(length);
				//���ø�����Ϣ��ĳ���
				search_information.songLength = length;
				//���ͳ�ȥ
				handler.obtainMessage(MSG_INIT, search_information).sendToTarget();  
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				//���ر���������
				try {
					raf.close();
					conn.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.run();
		}
	}
}
