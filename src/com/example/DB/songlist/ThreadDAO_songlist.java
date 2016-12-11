package com.example.DB.songlist;

import java.util.List;

import com.example.kugou.DBdata.Search_information;
import com.example.kugou.download_entities.*;
/**
 * ���ݿ�ӿ�
 * @author Administrator
 */
public interface ThreadDAO_songlist {
	/**
	 * �������ID���ձ���Ϣ
	 */
	public void insertSongInfo(Search_information  search_information);
	/**
	 * ɾ������ID���ձ���Ϣ
	 */
	public void deleteSongInfo(String songid);
	/**
	 * ���¸���ID���ձ�Ľ�����Ϣ
	 */
	public void updateSongInfo(String songid,int finished);
	/**
	 * ���¸���ID���ձ��URL��Ϣ(�������ʱ�����,��url��ɱ���·��)
	 */
	public void updateSongInfo_URL(String songid,String URL);
	/**
	 * ��ѯ����ID���ձ���Ϣ
	 */
	public Search_information getSongInfo(String songid);
	/**
	 * ��ѯȫ������ID���ձ��������Ϣ
	 */
	public List<Search_information> getAllSongInfo();
	/**
	 * ��ѯ����ID���ձ���Ϣ�Ƿ����
	 */
	public boolean isExists(String songid);
}
