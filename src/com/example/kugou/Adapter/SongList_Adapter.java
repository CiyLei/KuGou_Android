package com.example.kugou.Adapter;

import java.util.List;

import com.example.kugou.Home;
import com.example.kugou.MPService;
import com.example.kugou.R;
import com.example.kugou.SongPlayer;
import com.example.kugou.Controls.RoundImageView;
import com.example.kugou.DBdata.Search_information;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 播放歌曲列表的Adapter
 * 
 * @author Administrator
 *
 */
public class SongList_Adapter extends BaseAdapter implements OnClickListener {

	Context context;
	List<Search_information> search_informations;
	
	OnSongListDeleteClickListener mOnSongListDeleteClickListener;
	
	public interface OnSongListDeleteClickListener{
		void onSongListDeleteClick(View v);
	}
	
	public void SetOnSongListDeleteClickListener(OnSongListDeleteClickListener mOnSongListDeleteClickListener){
		this.mOnSongListDeleteClickListener = mOnSongListDeleteClickListener;
	}
	
	public SongList_Adapter(Context context,List<Search_information> search_informations){
		this.context = context;
		this.search_informations = search_informations;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return search_informations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return search_informations.get(position);
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
		LayoutInflater inflater= LayoutInflater.from(context);
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.songlist_adapter, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_songlist_index = (TextView) convertView.findViewById(R.id.tv_songlist_index);
			viewHolder.tv_songlist_song = (TextView) convertView.findViewById(R.id.tv_songlist_song);
			viewHolder.tv_songlist_singer = (TextView) convertView.findViewById(R.id.tv_songlist_singer);
			viewHolder.img_songlist_ls_delete = (ImageView) convertView.findViewById(R.id.img_songlist_ls_delete);
			viewHolder.img_songlist_select = (RoundImageView) convertView.findViewById(R.id.img_songlist_select);
			viewHolder.line_songlist = (LinearLayout) convertView.findViewById(R.id.line_songlist);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String index = (position + 1) + "";
		if ((position + 1) < 10)
			index = "0" + index;
		viewHolder.tv_songlist_index.setText(index);
		viewHolder.tv_songlist_song.setText(search_informations.get(position).songName);
		
		String artistName = search_informations.get(position).artistName;
		if(artistName.trim().isEmpty())
			artistName = viewHolder.tv_songlist_song.getText().toString();
		viewHolder.tv_songlist_singer.setText(artistName);
		
		if(position == MPService.SongPosition){
			viewHolder.tv_songlist_index.setVisibility(View.INVISIBLE);
			viewHolder.img_songlist_select.setVisibility(View.VISIBLE);
			if(!MPService.isLoadImg){
				viewHolder.img_songlist_select.setImageDrawable(Home.img_home_bottom.getDrawable());
			}
			if(context instanceof SongPlayer){
				viewHolder.line_songlist.setBackgroundResource(R.color.blur_line_select);
			}
			else{
				viewHolder.tv_songlist_song.setTextColor(context.getResources().getColor(R.color.login_top_color));
				viewHolder.tv_songlist_singer.setTextColor(context.getResources().getColor(R.color.login_top_color));
			}
		}
		else {
			viewHolder.tv_songlist_index.setVisibility(View.VISIBLE);
			viewHolder.img_songlist_select.setVisibility(View.INVISIBLE);
			if(context instanceof SongPlayer){
				viewHolder.line_songlist.setBackgroundResource(R.color.blur_line);
			}
			else{
				viewHolder.tv_songlist_song.setTextColor(Color.BLACK);
				viewHolder.tv_songlist_singer.setTextColor(context.getResources().getColor(R.color.wxh_pop_font_color));
			}
		}
		
		viewHolder.img_songlist_ls_delete.setTag(position);
		
		viewHolder.img_songlist_ls_delete.setOnClickListener(this);
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_songlist_index,tv_songlist_song,tv_songlist_singer;
		ImageView img_songlist_ls_delete;
		RoundImageView img_songlist_select;
		LinearLayout line_songlist;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mOnSongListDeleteClickListener.onSongListDeleteClick(v);
	}

}
