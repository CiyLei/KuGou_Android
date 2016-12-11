package com.example.kugou.DBdata;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
/**
 * ��ҳ��Ϣ���ط�����(����Լ�����ҳ�ӿ�)
 * 
 * getIsSuccess()			ָ����ҳ����,�����Ƿ�ɹ�(���¼�Ƿ�ɹ�,����Ƿ�ɹ�...)
 * getIsSuccessTxt()		���ɹ��Ƿ�ת��Ϊ����
 * getMsg()					��ȡʧ����Ϣ,������
 * getSearch_information()	����ҳ��Ϣת���ɹ�������Ϣ��
 * Patterns()				ָ�����������,���ط��ϵ���Ϣ
 * RateToTxt()				��Ϊ�ҵ������ǴӰٶ�͵��,�ٶȱ�ʶ�����������������ֱ�ʾ��,����ת����ͨ���׶��ĺ���
 * SizeToTxt()				ͬ���ٶ����ֵĴ�С��λ��B,����ת����MB
 * TimeToTxt()				ͬ���ٶ����ֵ�����ʱ����λ����,����ת����3:10��ʽ��
 * 
 * @author Administrator
 *
 */
public class post_return_analysis {

	public static boolean getIsSuccess(String post_return_information){
		String s =post_return_information.substring(0,1);
		if(s.equals("F")){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static String getIsSuccessTxt(String post_return_information){
		String s =post_return_information.substring(0,1);
		if(s.equals("F")){
			return "ʧ��";
		}
		else if(s.equals("T")){
			return "�ɹ�";
		}
		else {
			return "δ֪";
		}
	}
	
	public static String getMsg(String post_return_information){
		if(post_return_information.indexOf("msg:")!=-1)
			return post_return_information.substring(post_return_information.indexOf("msg:") + 4);
		return "";
	}
	
	public static Search_information getSearch_information(String result){
		Search_information search_information = new Search_information();
		search_information.songId = Patterns("(?<=songId\":).+?(?=,)",result);
		search_information.songName = Search_information.unicodeToString(Patterns("(?<=songName\":\").+?(?=\")", result));
		search_information.artistName = Search_information.unicodeToString(Patterns("(?<=artistName\":\").+?(?=\")", result));
		search_information.albumName = Search_information.unicodeToString(Patterns("(?<=albumName\":\").+?(?=\")", result));
		search_information.songPicSmall = Patterns("(?<=songPicSmall\":\").+?(?=\")", result).replace("\\", "");
		search_information.songPicBig = Patterns("(?<=songPicBig\":\").+?(?=\")", result).replace("\\", "");
		search_information.songPicRadio = Patterns("(?<=songPicRadio\":\").+?(?=\")", result).replace("\\", "");
		search_information.time = TimeToTxt(Patterns("(?<=time\":).+?(?=,)", result));
		search_information.lrcLink = Patterns("(?<=lrcLink\":\").+?(?=\")", result).replace("\\", "");
		search_information.songLink = Patterns("(?<=songLink\":\").+?(?=\")", result).replace("\\", "");
		search_information.format = Patterns("(?<=format\":\").+?(?=\",)", result);
		search_information.rate = RateToTxt(Patterns("(?<=rate\":).+?(?=,)", result));
		search_information.size = SizeToTxt(Patterns("(?<=size\":).+?(?=,)", result));
		return search_information;
	}
	
	public static String Patterns(String patternString,String result){
		String valueString="";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(result);
		if(matcher.find())
			valueString=matcher.group();
		return valueString;
	}
	
	public static String RateToTxt(String rate){
		String ratetxt = "";
		switch (rate.trim()) {
		case "128":
			ratetxt = "��׼����";
			break;
		case "192":
			ratetxt = "��������";
			break;
		case "256":
			ratetxt = "��������";
			break;
		default:
			ratetxt = "��������";
			break;
		}
		return ratetxt;
	}
	
	public static String SizeToTxt(String size){
		float sizetxt= Float.parseFloat(size.trim());
		BigDecimal bd = new BigDecimal(sizetxt / 1024 / 1024);
		sizetxt = bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return (sizetxt + "MB");
	}
	
	public static String TimeToTxt(String time){
		float timetxt = Float.parseFloat(time.trim());
		int h = (int)timetxt / 60;
		int s = (int)timetxt % 60;
		return ( (h<10 ? "0" + h : h) + ":" + (s<10 ? "0" + s : s));
	}
}
