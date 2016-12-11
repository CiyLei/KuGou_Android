package com.example.kugou.download_entities;

import java.io.Serializable;
import java.util.List;

import com.example.kugou.DBdata.Search_information;

public class Singer_SongsInfo implements Serializable {

	public String Singer_Name;
	public int Singer_Pic;
	public List<Search_information> Songs;
	
}
