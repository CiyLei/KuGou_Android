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
 * 下载服务
 * 根据intent的Action()值来判断用户要进行怎么的操作(如下载开始,下载停止),还有传递过来歌曲信息类
 * 再把歌曲信息类传给InitTheard子类,进行本地创建和设置长度的操作,通过Handler回传,在Handler中分析成歌曲下载类
 * 
 * InitTheard		指定一个歌曲信息类,进行本地创建和设置长度的操作,然后通过handler将歌曲信息类传递出去
 * handler			将歌曲信息类分析成歌曲下载类,进行下载操作
 * receiver			广播,接受歌曲下载进度和下载完毕的消息
 * 
 */
public class DownLoadService extends Service {
	//下载路径
	public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mKuGouDown/" ;
	//开始下载	命令
	public static final String ACTION_START = "ACTION_START";
	//停止下载	命令
	public static final String ACTION_STOP = "ACTION_STOP";
	//下载进度更新	命令
	public static final String ACTION_UPDATE = "ACTION_UPDATE";
	//下载进完成	命令
	public static final String ACTION_FINISH = "ACTION_FINISH";
	//InitTheard子类分析出下载信息类的时候进行回传的命令
	public static final int MSG_INIT = 0;
	//已栈的方式保存下载的任务信息
	public static List<Search_information> downSearch_informations = new ArrayList<Search_information>();
	//线程池
	public static ExecutorService executorService = Executors.newCachedThreadPool();  
	//根据一个ID,可取出所有的下载类
	public static Map<String, DownLoadTask> Tasks = new LinkedHashMap<String, DownLoadTask>();
	//本地歌曲ID对标准实类(下载完毕和下载中进行操作)
	private ThreadOADImpl_songlist impl = new ThreadOADImpl_songlist(DownLoadService.this);
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//排除空传的情况
		if(intent != null){
			if(!intent.getAction().isEmpty()){
				if(intent.getAction().equals(ACTION_START)){
					//获取传来的歌曲信息类
					Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");   //获取从activity传过来的参数
					//将歌曲信息类交给InitTheard类来进行本地创建和设置长度
					InitTheard initTheard = new InitTheard(search_information);
					//加入线程池
					DownLoadService.executorService.execute(initTheard);
				}
				else if(intent.getAction().equals(ACTION_STOP)){
					//获取传来的歌曲信息类
					Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");
					//根据歌曲ID来停止歌曲下载类
					DownLoadTask downLoadTask = Tasks.get(search_information.songId);
					if(downLoadTask != null)
						downLoadTask.isPause = true;
				}
			}
		}
		//注册广播
		IntentFilter filter = new IntentFilter();
        filter.addAction(DownLoadService.ACTION_FINISH);
        filter.addAction(DownLoadService.ACTION_UPDATE);
        registerReceiver(receiver, filter);
        
		return super.onStartCommand(intent, flags, startId);
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver(){
    	public void onReceive(Context context, Intent intent) {
    		//如果接受到歌曲进度更新的广播
    		if(DownLoadService.ACTION_UPDATE.equals(intent.getAction())){
    			//获取歌曲的ID和进度
    			String id = intent.getStringExtra("id");
				int finished = intent.getIntExtra("finished", 0);
				//更新下载任务的操作
				for (Search_information search_information : downSearch_informations) {
					//如果找到了下载任务
					if(search_information.songId.equals(id)){
						//进行进度更新
						search_information.finished = finished;
						//如果数据库里面没有此信息数据,就加入,否则更新
						if(!impl.isExists(id)){
							impl.insertSongInfo(search_information);
						}else {
							impl.updateSongInfo(search_information.songId, search_information.finished);
						}
					}
				}//如果接受到歌曲下载完成的广播
    		}else if (DownLoadService.ACTION_FINISH.equals(intent.getAction())) {
    			//获取下载完成的歌曲信息
				Search_information search_information = (Search_information) intent.getSerializableExtra("search_information");
				//以防万一,手动进行进度到100的操作
				search_information.finished = 100;
				//更新进度到本地歌曲ID对照表数据库
				impl.updateSongInfo(search_information.songId, search_information.finished);
				//更新数据库中的url变成本地路径
				impl.updateSongInfo_URL(search_information.songId, DOWNLOAD_PATH + search_information.songId + "." + search_information.format);
				//如果界面的一个textview控件存在,更新数目(此textview显示的是本地歌曲数量)
				if(Fragment_ting.tv_songlist_num != null){
					List<Search_information> search_informations = impl.getAllFinishSongInfo();
					Fragment_ting.tv_songlist_num.setText(search_informations.size() + "首");
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
				//取出传来的歌曲信息类
				Search_information search_information = (Search_information) msg.obj;
				//将歌曲信息类交给DownLoadTask下载类,进行下载分析操作		(上下文,要下载的歌曲信息类,要已几个线程进行下载)
				DownLoadTask downLoadTask = new DownLoadTask(DownLoadService.this, search_information,3);
				//进行下载
				downLoadTask.downLoad();
				Tasks.put(search_information.songId, downLoadTask);
				//加入到下载任务表(如果有的话就不用添加)
				for (Search_information search_information2 : downSearch_informations) {
					if(search_information2.songId.equals(search_information.songId))
						return;
				}
				downSearch_informations.add(search_information);
				//因为插入到list是在尾部,所以调到上面
				for (int i = downSearch_informations.size() -1 ; i > 0; i--) {
					Search_information s = downSearch_informations.get(i);
					downSearch_informations.set(i, downSearch_informations.get(i - 1));
					downSearch_informations.set(i - 1, s);
				}
				break;
			}
		};
	};
	//传来歌曲信息类,分析长度,设置长度,并在本地创建好,再通过handler传出去
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
				//获取下载文件的长度
				URL url = new URL(search_information.songLink);
				conn = (HttpURLConnection) url.openConnection();
				//设置超时
				conn.setConnectTimeout(3000);   
				//设置请求方式   下载用GET
				conn.setRequestMethod("GET");   
				//文件总长度
				int length = -1;
				if(conn.getResponseCode() == HttpStatus.SC_OK){   //如果连接成功
					length = conn.getContentLength();
				}
				if(length <= 0)
					return;
				File dir = new File(DOWNLOAD_PATH);
				//如果文件不存在的话就创建一个
				if(!dir.exists()){   
					dir.mkdir();
				}
				//创建本地文件
				File file =new File(dir, search_information.songId + "." + search_information.format);
				//随机访问文件  第二个参数是设置权限  r-read 读 | w-write 写 | d-delete 删除
				raf = new RandomAccessFile(file, "rwd"); 
				//设置长度
				raf.setLength(length);
				//设置歌曲信息类的长度
				search_information.songLength = length;
				//发送出去
				handler.obtainMessage(MSG_INIT, search_information).sendToTarget();  
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				//最后关闭所有连接
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
