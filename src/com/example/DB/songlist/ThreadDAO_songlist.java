package com.example.DB.songlist;

import java.util.List;

import com.example.kugou.DBdata.Search_information;
import com.example.kugou.download_entities.*;
/**
 * 数据库接口
 * @author Administrator
 */
public interface ThreadDAO_songlist {
	/**
	 * 插入歌曲ID对照表信息
	 */
	public void insertSongInfo(Search_information  search_information);
	/**
	 * 删除歌曲ID对照表信息
	 */
	public void deleteSongInfo(String songid);
	/**
	 * 更新歌曲ID对照表的进度信息
	 */
	public void updateSongInfo(String songid,int finished);
	/**
	 * 更新歌曲ID对照表的URL信息(完成下载时会调用,将url变成本地路径)
	 */
	public void updateSongInfo_URL(String songid,String URL);
	/**
	 * 查询歌曲ID对照表信息
	 */
	public Search_information getSongInfo(String songid);
	/**
	 * 查询全部歌曲ID对照表的所有信息
	 */
	public List<Search_information> getAllSongInfo();
	/**
	 * 查询歌曲ID对照表信息是否存在
	 */
	public boolean isExists(String songid);
}
