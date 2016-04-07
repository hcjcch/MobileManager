package com.hcjcch.mobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/2 16:08
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    protected void openActivity(Class targetActivity, @Nullable Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subOnCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setToolBarTitleColor();
            setBackDrawable();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolbarNavigationClick(v);
                }
            });
        }
    }

    protected void toolbarNavigationClick(View v) {

    }

    protected void setToolBarTitleColor() {
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    protected void setToolBarTitleColor(int color) {
        toolbar.setTitleTextColor(color);
    }

    protected void setBackDrawable() {
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
    }

    protected void setBackDrawable(int id) {
        toolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_24dp);
    }

    protected abstract void subOnCreate(@Nullable Bundle savedInstanceState);

}