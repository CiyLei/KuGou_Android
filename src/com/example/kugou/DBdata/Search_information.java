package com.example.kugou.DBdata;

import java.io.Serializable;
/**
 * ������Ϣ��
 * 
 * unicodeToString()		unicode����ת����
 * 
 * @author Administrator
 *
 */
public class Search_information implements Serializable {
	public String songId;				//����id
	public String artistName;			//����
	public String songName;				//����
	public String albumName;			//ר��
	public String songPicSmall;			//����СͼƬ
	public String songPicBig;			//������ͼƬ
	public String songPicRadio;			//��������ͼƬ
	public String time;					//����ʱ��
	public String lrcLink;				//���·��
	public String songLink;				//����·��
	public String format;				//��������	��mp3
	public String rate;					//����������
	public String size;					//������С
	public String songList;				//���������ղظ赥������
	public int songLength;				//�����ĳ���
	public int finished = 0;			//������������ɶ�
	
	public static String unicodeToString(String unicode) {  
		
		StringBuffer string = new StringBuffer();
		  
	     String[] hex = unicode.split("\\\\u");
	     for (int i = 1; i < hex.length; i++) {
	    	 
	         // ת����ÿһ�������
	        int data = Integer.parseInt(hex[i].substring(0, 4), 16);
	  
	         // ׷�ӳ�string
	         string.append((char) data + hex[i].substring(4));
	         
	     }
	  
	     return string.toString();
	}
}
