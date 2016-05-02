package com.hcjcch.pm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.pm.event.AppDetailEvent;
import com.hcjcch.pm.event.UnInstallAppEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 21:11
 */

public class AllApplicationAdapter extends RecyclerView.Adapter {
    private List<AppInfo> appInfoList = new ArrayList<>();

    public AllApplicationAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info_application_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AppInfo appInfo = appInfoList.get(position);
        ((AppViewHolder) holder).appIcon.setImageDrawable(appInfo.getAppIcon());
        ((AppViewHolder) holder).appNameTxt.setText(appInfo.getName());
        ((AppViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AppDetailEvent(appInfo));
            }
        });
        ((AppViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus.getDefault().post(new UnInstallAppEvent(appInfo));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.app_icon)
        ImageView appIcon;

        @Bind(R.id.app_name_txt)
        TextView appNameTxt;

        View itemView;

        public AppViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }
}
