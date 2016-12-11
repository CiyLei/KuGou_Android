package com.example.kugou.Adapter;

import com.example.download.DownLoadService;
import com.example.kugou.R;
import com.example.kugou.DBdata.Search_information;
import com.example.kugou.DBdata.select_analysis;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 下载界面的 Adapter
 * 
 * DownMusic()					继续下载或者停止下载
 * updateProgress				更新进度
 * remove_Search_information	移除下载记录
 * 
 * @author Administrator
 *
 */
public class Down_Adapter extends BaseAdapter implements OnClickListener {

	Context context;
	List<Search_information> Search_informations;
	ListView listView;
	
	public Down_Adapter(Context context,List<Search_information> Search_informations,ListView listView){
		this.context = context;
		this.Search_informations = Search_informations;
		this.listView = listView;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Search_informations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Search_informations.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		Search_information search_information = Search_informations.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.home_main_songlist_item, null);
			viewHolder.img_down = (ImageView) convertView.findViewById(R.id.img_down);
			viewHolder.img_down_delete = (ImageView) convertView.findViewById(R.id.img_down_delete);
			viewHolder.tv_down_name = (TextView) convertView.findViewById(R.id.tv_down_name);
			viewHolder.tv_down_remind = (TextView) convertView.findViewById(R.id.tv_down_remind);
			viewHolder.tv_finished = (TextView) convertView.findViewById(R.id.tv_finished_1);
			viewHolder.pb_finished = (ProgressBar) convertView.findViewById(R.id.pb_finished);
			
			viewHolder.img_down.setTag(position);
			viewHolder.img_down_delete.setTag(position);
			
			viewHolder.img_down.setOnClickListener(this);
			viewHolder.img_down_delete.setOnClickListener(this);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_finished.setText(search_information.finished + "%");
		viewHolder.pb_finished.setProgress(search_information.finished);
		
		viewHolder.tv_down_name.setText(search_information.songName);
		if(!search_information.artistName.trim().isEmpty())
			viewHolder.tv_down_name.setText(viewHolder.tv_down_name.getText().toString() + "-" +
					search_information.artistName);
		
		if(DownLoadService.Tasks.get(search_information.songId) != null){
			if(DownLoadService.Tasks.get(search_information.songId).isPause){
				viewHolder.tv_down_remind.setVisibility(View.VISIBLE);
				viewHolder.pb_finished.setVisibility(View.INVISIBLE);
				viewHolder.img_down.setImageResource(R.drawable.down_start);
			}
		}else {
			if(search_information.finished != 100){
				viewHolder.tv_down_remind.setVisibility(View.VISIBLE);
				viewHolder.pb_finished.setVisibility(View.INVISIBLE);
				viewHolder.img_down.setImageResource(R.drawable.down_start);
			}
		}
		
		return convertView;
	}

	class ViewHolder{
		ImageView img_down,img_down_delete;
		TextView tv_down_name,tv_down_remind,tv_finished;
		ProgressBar pb_finished;
	}
	
	public void DownMusic(String Action,Search_information search_information){
		
		Intent intent = new Intent(context, DownLoadService.class);
		intent.setAction(Action);
		intent.putExtra("search_information", search_information);
		context.startService(intent);
		
	}
	
	public void updateProgress(String songid,int finished){
		for (Search_information search_information : Search_informations) {
			if(search_information.songId.equals(songid)){
				search_information.finished = finished;
				notifyDataSetChanged();
			}
		}
	}
	
	public void remove_Search_information(Search_information search_information){
		Search_informations.remove(search_information);
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (int) v.getTag();
		switch (v.getId()) {
		case R.id.img_down:
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.img_down = (ImageView) listView.getChildAt(position).findViewById(R.id.img_down);
			viewHolder.tv_down_remind = (TextView) listView.getChildAt(position).findViewById(R.id.tv_down_remind);
			viewHolder.pb_finished = (ProgressBar) listView.getChildAt(position).findViewById(R.id.pb_finished);
			
			if(DownLoadService.Tasks.get(Search_informations.get(position).songId) != null){
				if(DownLoadService.Tasks.get(Search_informations.get(position).songId).isPause){
					viewHolder.img_down.setImageResource(R.drawable.down_stop);
					DownMusic(DownLoadService.ACTION_START, Search_informations.get(position));
					viewHolder.tv_down_remind.setVisibility(View.INVISIBLE);
					viewHolder.pb_finished.setVisibility(View.VISIBLE);
				}else {
					viewHolder.img_down.setImageResource(R.drawable.down_start);
					DownMusic(DownLoadService.ACTION_STOP, Search_informations.get(position));
					viewHolder.tv_down_remind.setVisibility(View.VISIBLE);
					viewHolder.pb_finished.setVisibility(View.INVISIBLE);
				}
			}else{
				DownMusic(DownLoadService.ACTION_START,Search_informations.get(position));
				viewHolder.tv_down_remind.setVisibility(View.INVISIBLE);
				viewHolder.pb_finished.setVisibility(View.VISIBLE);
				viewHolder.img_down.setImageResource(R.drawable.down_stop);
			}
			
			break;
		case R.id.img_down_delete:
			
			DownMusic(DownLoadService.ACTION_STOP, Search_informations.get(position));
			DownLoadService.Tasks.remove(Search_informations.get(position).songId);
			DownLoadService.downSearch_informations.remove(Search_informations.get(position));
			notifyDataSetChanged();
			
			break;
		}
	}
}
