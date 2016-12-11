package com.example.DB.download;

import java.util.List;

import com.example.kugou.download_entities.*;
/**
 * ������Ϣ���ݿ�ӿ�
 * @author Administrator
 *
 */
public interface ThreadDAO_download {
	/**
	 * �����߳���Ϣ
	 */
	public void insertThread(ThreadInfo  threadInfo);
	/**
	 * ɾ���߳���Ϣ
	 */
	public void deleteThread(String url);
	/**
	 *�����߳����ؽ���
	 */
	public void updateThread(String url,int thread_id,int finished);
	/**
	 * ��ѯ�߳���Ϣ
	 */
	public List<ThreadInfo> getThread(String url);
	/**
	 * ��ѯ�߳��Ƿ����
	 */
	public boolean isExists(String url,int thread_id);
}
