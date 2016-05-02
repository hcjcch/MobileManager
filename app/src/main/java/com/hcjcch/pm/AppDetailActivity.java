package com.hcjcch.pm;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcjcch.mobilemanager.BaseActivity;
import com.hcjcch.mobilemanager.R;
import com.hcjcch.pm.contarct.AppDetailContract;
import com.hcjcch.pm.contarct.AppDetailPresenterImpl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/1 21:37
 */

public class AppDetailActivity extends BaseActivity implements AppDetailContract.View {

    public final static String INTENT_KEY_PACKAGE_NAME = "intent_key_package_name";
    private String packageName;
    private AppDetailContract.Presenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.app_icon)
    ImageView appIcon;

    @Bind(R.id.app_name)
    TextView TextAppName;

    @Bind(R.id.app_package_name)
    TextView appPackageName;

    @Bind(R.id.layout_app)
    RelativeLayout layoutApp;

    @Bind(R.id.text_traffic)
    TextView textTraffic;

    @Bind(R.id.text_upload_traffic)
    TextView textUploadTraffic;

    @Bind(R.id.layout_traffic)
    RelativeLayout layoutTraffic;

    @Bind(R.id.btn_open_detail)
    Button btnOpenDetail;

    @Bind(R.id.btn_un_install)
    Button btnUnInstall;

    @Override
    protected void subOnCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_app_detail);
        ButterKnife.bind(this);
        packageName = getIntent().getExtras().getString(INTENT_KEY_PACKAGE_NAME);
        if (packageName == null) {
            finish();
        }
        presenter = new AppDetailPresenterImpl(this);
        presenter.init(packageName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void toolbarNavigationClick(View v) {
        finish();
    }

    @Override
    public void setImageView(Drawable drawable) {
        appIcon.setImageDrawable(drawable);
    }

    @Override
    public void setAppName(String appName) {
        TextAppName.setText(appName);
    }

    @Override
    public void setPackageName(String packageName) {
        appPackageName.setText(packageName);
    }

    @Override
    public void setUploadTraffic(int traffic) {
        textUploadTraffic.setText("传输：" + traffic + "M");
    }

    @OnClick(R.id.btn_open_detail)
    void openDetail() {
        presenter.intentToAppInformation(this);
    }

    @OnClick(R.id.btn_un_install)
    void unInstall() {
        presenter.unInstall();
    }

    @Override
    public void setTitle() {
        setTitle("应用详情");
    }
}
