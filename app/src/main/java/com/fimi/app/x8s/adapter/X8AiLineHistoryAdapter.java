package com.fimi.app.x8s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fimi.android.app.R;
import com.fimi.kernel.store.sqlite.entity.X8AiLinePointInfo;
import com.fimi.kernel.utils.DateUtil;

import java.util.List;


public class X8AiLineHistoryAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final Context context;
    private final int defaultColor;
    private final List<X8AiLinePointInfo> mList;
    private final int selectColor;
    private int select = -1;

    public X8AiLineHistoryAdapter(@NonNull Context context, List<X8AiLinePointInfo> mList) {
        this.context = context;
        this.mList = mList;
        this.defaultColor = context.getResources().getColor(R.color.white_90);
        this.selectColor = context.getResources().getColor(R.color.x8_fc_all_setting_blue);
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = holder.initView(parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        X8AiLinePointInfo info = this.mList.get(position);
        String p = this.context.getString(R.string.x8_ai_fly_line_history_time_pattern);
        String[] str = DateUtil.getStringByFormat2(info.getTime(), p);
        holder.mTvItemTitle1.setText(str[0]);
        holder.mTvItemTitle2.setText(str[1]);
        String ds = this.context.getString(R.string.x8_ai_fly_line_history_distance);
        String distance = String.format(ds, "" + info.getDistance());
        holder.mTvItemTitle3.setText("" + distance);
        if (this.select == position) {
            holder.mTvItemTitle1.setTextColor(this.selectColor);
            holder.mTvItemTitle2.setTextColor(this.selectColor);
            holder.mTvItemTitle3.setTextColor(this.selectColor);
        } else {
            holder.mTvItemTitle1.setTextColor(this.defaultColor);
            holder.mTvItemTitle2.setTextColor(this.defaultColor);
            holder.mTvItemTitle3.setTextColor(this.defaultColor);
        }
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.select = position;
        notifyDataSetChanged();
    }

    public X8AiLinePointInfo getItemSelect() {
        if (this.select == -1) {
            return null;
        }
        return this.mList.get(this.select);
    }


    public class ViewHolder {
        public TextView mTvItemTitle1;
        public TextView mTvItemTitle2;
        public TextView mTvItemTitle3;

        public ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(X8AiLineHistoryAdapter.this.context);
            View view = inflater.inflate(R.layout.x8_ai_line_history_item_layout, parent, false);
            this.mTvItemTitle1 = view.findViewById(R.id.tvItme1);
            this.mTvItemTitle2 = view.findViewById(R.id.tvItme2);
            this.mTvItemTitle3 = view.findViewById(R.id.tvItme3);
            return view;
        }
    }
}
