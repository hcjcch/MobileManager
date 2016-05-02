package com.hcjcch.pm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcjcch.mobilemanager.R;
import com.hcjcch.pm.ApkFile;
import com.hcjcch.pm.event.NoInstallApkLongClickEvent;

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
 * @time 16/5/1 14:24
 */

public class UnInstalledApkAdapter extends RecyclerView.Adapter {

    private List<ApkFile> apkFiles = new ArrayList<>();

    public UnInstalledApkAdapter(List<ApkFile> apkFiles) {
        this.apkFiles = apkFiles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UnInstalledApkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info_application_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((UnInstalledApkViewHolder) holder).appNameTxt.setText(apkFiles.get(position).getName());
        ((UnInstalledApkViewHolder) holder).appIcon.setImageDrawable(apkFiles.get(position).getIcon());
        ((UnInstalledApkViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NoInstallApkLongClickEvent(apkFiles.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return apkFiles.size();
    }

    static class UnInstalledApkViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.app_icon)
        ImageView appIcon;

        @Bind(R.id.app_name_txt)
        TextView appNameTxt;

        View itemView;

        public UnInstalledApkViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }
}
