package com.example.kugou.download_entities;
/**
 * 下载文件类
 * 
 * id		文件下载唯一ID	多线程下载保存数据库是来区别彼此
 * url		文件url
 * start	文件开始下载位置
 * end		文件结束下载位置
 * finished	文件下载完成进度
 * @author Administrator
 *
 */
public class ThreadInfo {

	public ThreadInfo() {
		super();
	}
	public ThreadInfo(int id, String url, int start, int end, int finished) {
		super();
		this.id = id;
		this.url = url;
		this.start = start;
		this.end = end;
		this.finished = finished;
	}
	private int id;
	private String url;
	private int start;
	private int end;
	private int finished;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getFinished() {
		return finished;
	}
	public void setFinished(int finished) {
		this.finished = finished;
	}
	@Override
	public String toString() {
		return "ThreadInfo [id=" + id + ", url=" + url + ", start=" + start
				+ ", end=" + end + ", finished=" + finished + "]";
	}
	
}
