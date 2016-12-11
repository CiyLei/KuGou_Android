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
 * 文件下载类
 * 指定要下载的文件信息类和要分多少线程进行下载
 * 
 * DownLoadTask()				初始化,得到上下文,待下载的歌曲信息类,下载线程数
 * downLoad()					根据要下载的歌曲信息类和下载线程数,把一个文件分析成一段一段的下载信息类,加入到线程池,进行下载
 * checkAllThreadsFinshed()		检查是否每个下载信息类完毕(就是下载好一个歌曲了)
 * DownLoadThread 				指定一个下载信息类  进行下载操作
 * 
 * @author Administrator
 *
 */
public class DownLoadTask {
	
	private Context context;
	private Search_information search_information;
	//下载信息数据库实类
	private ThreadOADImpl_download oad;
	//默认启动一个线程进行下载
	private int ThreadCount = 1;
	//存放下载信息类
	private List<DownLoadThread> downLoadThreads;
	//存放下载总进度
	private int finished = 0;
	//是否停止
	public boolean isPause =false;
	
	public DownLoadTask(Context context, Search_information search_information, int ThreadCount) {
		super();
		this.context = context;
		this.search_information = search_information;
		this.ThreadCount = ThreadCount;
		oad = new ThreadOADImpl_download(context);
	}
	
	public void downLoad(){
		//读取数据库的线程信息
		List<ThreadInfo> threadInfos = oad.getThread(search_information.songLink);
		//如果数据库没有此文件的下载信息,有的话就直接使用
		if(threadInfos.size() == 0){	
			//获得那个线程的下载长度
			int length = search_information.songLength / ThreadCount;
			for (int i = 0; i < ThreadCount; i++) {
				//创建线程信息
				ThreadInfo threadInfo = new ThreadInfo(i, search_information.songLink, i * length, (i + 1) * length -1, 0);
				if(i == ThreadCount -1){
					//如果是最后一个,长度就全包了
					threadInfo.setEnd(search_information.songLength);
				}
				//添加到线程信息集合中
				threadInfos.add(threadInfo);
				//添加到数据库
				oad.insertThread(threadInfo);
			}
		}
		downLoadThreads = new ArrayList<DownLoadTask.DownLoadThread>();
		//启动多个线程进行下载
		for (ThreadInfo threadInfo : threadInfos) {
			DownLoadThread thread = new DownLoadThread(threadInfo);
			DownLoadService.executorService.execute(thread);
			downLoadThreads.add(thread);
		}
	}
	
	private synchronized void checkAllThreadsFinshed(){
		//判断一个文件的每段是否全部下载完毕
		boolean allFinshed = true;
		for (DownLoadThread downLoadThread : downLoadThreads) {
			if(!downLoadThread.isFinished){
				allFinshed = false;
				break;
			}
		}
		//如果下载完毕,就发送广播
		if(allFinshed){
			Intent intent = new Intent(DownLoadService.ACTION_FINISH);
			intent.putExtra("search_information", search_information);
			context.sendBroadcast(intent);
			//删除线程信息
			oad.deleteThread(search_information.songLink);
		}
	}
	
	class DownLoadThread extends Thread{
		private ThreadInfo threadInfo;
		public boolean isFinished = false; //线程是否执行完毕

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
				//设置线程的下载位置
				int start = threadInfo.getStart() + threadInfo.getFinished();
				conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
				//设置文件的写入位置
				File file = new File(DownLoadService.DOWNLOAD_PATH, search_information.songId + "." + search_information.format);
				raf= new RandomAccessFile(file, "rwd");
				raf.seek(start);						//设置文件写入位置
				Intent intent = new Intent("ACTION_UPDATE");
				finished += threadInfo.getFinished();
				//开始下载
				if(conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT){  //加载部分内容成功
					//读取数据
					is = conn.getInputStream();
					byte[] buffer = new byte[1024*4];
					int len=-1;
					long time = System.currentTimeMillis();
					while((len = is.read(buffer)) != -1){   //is读取buffer长度的字节数 如果返回-1 说明后面没有了 不是的话说明后面还有
						//写入文件
						raf.write(buffer,0,len);
						//累加整个文件的进度
						finished += len;
						//累加每个线程完成的进度 保存到数据库
						threadInfo.setFinished(threadInfo.getFinished() + len);
						//进度通过广播发送给UI
						if(System.currentTimeMillis() - time > 1000){	//每一秒一次广播
							intent.putExtra("id", search_information.songId);
							//已百分比的数字来进行广播进度  int*100会超出范围 所以强转一下
							intent.putExtra("finished", (int)(finished / (float)search_information.songLength * 100));	
							context.sendBroadcast(intent);
							time = System.currentTimeMillis();
						}
						if(isPause){
							oad.updateThread(threadInfo.getUrl(), threadInfo.getId(), threadInfo.getFinished());
							return;
						}
					}
					//表示线程执行完毕
					isFinished = true;
					//一个线程执行完毕时查询一下是否全部线程执行完毕
					checkAllThreadsFinshed();
				}
				//暂停时,进度写入数据库
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
