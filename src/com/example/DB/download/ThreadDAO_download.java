package com.example.DB.download;

import java.util.List;

import com.example.kugou.download_entities.*;
/**
 * 下载信息数据库接口
 * @author Administrator
 *
 */
public interface ThreadDAO_download {
	/**
	 * 插入线程信息
	 */
	public void insertThread(ThreadInfo  threadInfo);
	/**
	 * 删除线程信息
	 */
	public void deleteThread(String url);
	/**
	 *更新线程下载进度
	 */
	public void updateThread(String url,int thread_id,int finished);
	/**
	 * 查询线程信息
	 */
	public List<ThreadInfo> getThread(String url);
	/**
	 * 查询线程是否存在
	 */
	public boolean isExists(String url,int thread_id);
}
