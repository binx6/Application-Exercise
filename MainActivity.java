package com.binx6;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);

        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo packageInfo = packageInfoList.get(i);

            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(packageManager);
            final String packageName = packageInfo.packageName;
            final String className = packageInfo.applicationInfo.className != null ? packageInfo.applicationInfo.className : "";
            String entryClassName = "";

            Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                ComponentName componentName = launchIntent.getComponent();
                if (componentName != null) {
                    entryClassName = componentName.getClassName();
                }
            }

            LinearLayout appLayout = new LinearLayout(this);
            appLayout.setOrientation(LinearLayout.HORIZONTAL);
            appLayout.setPadding(0, 8, 0, 8);

            ImageView iconView = new ImageView(this);
            iconView.setImageDrawable(appIcon);

            TextView nameView = new TextView(this);
            nameView.setText(appName);
            nameView.setTextSize(18);

            appLayout.addView(iconView);
            appLayout.addView(nameView);

            appLayout.setTag(entryClassName);

            appLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String entryClassName = (String) view.getTag();
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

						String message = "包名：" + packageName + "\n\n处理类名：" + className + "\n\n入口类名：" + entryClassName;
						builder.setTitle("应用信息")
                            .setMessage(message)
                            .setPositiveButton("确定", null);

						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});

            layout.addView(appLayout);
        }

        scrollView.addView(layout);
        setContentView(scrollView);
    }
}
