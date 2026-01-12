package com.moko.lw003plus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.moko.lw003plus.BuildConfig;
import com.moko.lw003plus.R;
import com.moko.lw003plus.activity.device.DecoderActivity;
import com.moko.lw003plus.databinding.Lw003ProActivityAboutBinding;
import com.moko.lw003plus.utils.Utils;


public class AboutActivity extends BaseActivity {
    private Lw003ProActivityAboutBinding mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003ProActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        if (!BuildConfig.IS_LIBRARY) {
            mBind.appVersion.setText(String.format("APP Version:V%s", Utils.getVersionInfo(this)));
        }
        mBind.tvDecode.setOnClickListener(v-> startActivity(new Intent(this, DecoderActivity.class)));
    }

    public void onBack(View view) {
        finish();
    }

    public void onCompanyWebsite(View view) {
        if (isWindowLocked())
            return;
        Uri uri = Uri.parse("https://" + getString(R.string.company_website));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
