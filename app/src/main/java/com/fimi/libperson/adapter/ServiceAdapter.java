package com.fimi.libperson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fimi.android.app.R;
import com.fimi.kernel.region.ServiceItem;
import com.fimi.kernel.utils.FontUtil;

import java.util.List;


public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ServiceItem> list;

    public ServiceAdapter(List<ServiceItem> list, Context context) {
        this.list = null;
        this.context = null;
        this.inflater = null;
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public ServiceItem getItem(int position) {
        return this.list.get(position);
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
        ServiceItem itemBean = this.list.get(position);
        holder.mTvItemTitle.setText(this.context.getString(itemBean.getInfo()));
        if (itemBean.isSelect()) {
            holder.mIvArrow.setVisibility(View.VISIBLE);
            holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.login_forget_password_frequently));
        } else {
            holder.mIvArrow.setVisibility(View.INVISIBLE);
            holder.mTvItemTitle.setTextColor(this.context.getResources().getColor(R.color.login_font_select));
        }
        return convertView;
    }


    private class ViewHolder {
        ImageView mIvArrow;
        TextView mTvItemTitle;

        private ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(ServiceAdapter.this.context);
            View view = inflater.inflate(R.layout.adapt_language_setting, parent, false);
            this.mTvItemTitle = view.findViewById(R.id.tv_item_title);
            this.mIvArrow = view.findViewById(R.id.iv_arrow);
            FontUtil.changeFontLanTing(ServiceAdapter.this.context.getAssets(), this.mTvItemTitle);
            return view;
        }
    }
}
