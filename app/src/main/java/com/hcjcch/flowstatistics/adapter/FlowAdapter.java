package com.hcjcch.flowstatistics.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcjcch.flowstatistics.model.AppInfo;
import com.hcjcch.mobilemanager.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/3 00:37
 */

public class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.FlowViewHolder> {

    private List<AppInfo> appInfoList;

    public FlowAdapter(List<AppInfo> appInfoList) {
        this.appInfoList = appInfoList;
    }

    @Override
    public FlowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_info, parent, false));
    }

    @Override
    public void onBindViewHolder(FlowViewHolder holder, int position) {
        AppInfo appInfo = appInfoList.get(position);
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.appName.setText(appInfo.getAppName());
        holder.flowNumber.setText(appInfo.getAppFlow() + "M");
        holder.status.setText(appInfo.isFlowConnect() || appInfo.isWifiConnect() ? "允许" : "拒绝");
        holder.status.setTextColor(appInfo.isFlowConnect() || appInfo.isWifiConnect()
                ? holder.status.getResources().getColor(R.color.green)
                : holder.status.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    static class FlowViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.app_icon)
        ImageView appIcon;
        @Bind(R.id.app_name_txt)
        TextView appName;
        @Bind(R.id.flow_number_txt)
        TextView flowNumber;
        @Bind(R.id.status_txt)
        TextView status;

        public FlowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
