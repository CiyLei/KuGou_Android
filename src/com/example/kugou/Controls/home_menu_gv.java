package com.example.kugou.Controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kugou.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.SimpleAdapter;
/**
 * 用户侧滑菜单的子菜单控件
 * @author Administrator
 *
 */
public class home_menu_gv extends GridView {

    List<Map<String, Object>> dataList;
    SimpleAdapter simdapter;
    String[] icon_name= {"会员中心","流量包月","搜索","听歌识曲"
    		,"定时","3D丽音","传歌","设置"};
    int[] icon={R.drawable.home_menu_hyzx,R.drawable.home_menu_llby,R.drawable.home_menu_ss,
    		R.drawable.home_menu_tgsq,R.drawable.home_menu_ds,R.drawable.home_menu_3dly,
    		R.drawable.home_menu_cg,R.drawable.home_menu_sz};
	
	public home_menu_gv(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public home_menu_gv(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public home_menu_gv(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		dataList=new ArrayList<Map<String, Object>>();
        simdapter=new SimpleAdapter(context, getdata()
        		, R.layout.home_menu_gv_item, new String[] {"key","value"}
        		, new int[] {R.id.home_menu_item_img,R.id.home_menu_item_txt});
        this.setAdapter(simdapter);
		
	}

	private List<Map<String, Object>> getdata()
    {
    	Map<String, Object> map;
    	for(int i=0;i<8;i++){
    		map=new HashMap<String, Object>();
    		map.put("key", icon[i]);
    		map.put("value", icon_name[i]);
    		dataList.add(map);
    	}
    	return dataList;
    }
	
}
