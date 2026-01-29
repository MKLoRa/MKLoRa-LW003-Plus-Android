package com.moko.lw003plus.activity.device;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elvishew.xlog.XLog;
import com.moko.lw003plus.activity.BaseActivity;
import com.moko.lw003plus.databinding.Lw003PlusActivityDecoderBinding;
import com.moko.lw003plus.utils.DecoderModule;
import com.moko.lw003plus.utils.ToastUtils;

import java.io.File;

/**
 * @author: jun.liu
 * @date: 2023/4/13 15:42
 * @des:
 */
public class DecoderActivity extends BaseActivity {
    private Lw003PlusActivityDecoderBinding mBind;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = Lw003PlusActivityDecoderBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        initView();
    }

    private void initView() {
        mBind.ivBack.setOnClickListener(v -> finish());
        mBind.btnDecoder.setOnClickListener(v -> decode());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void decode() {
        if (isWindowLocked()) return;
        if (TextUtils.isEmpty(mBind.etRawData.getText())) {
            ToastUtils.showToast(this, "please input raw data first");
            mBind.tvResult.setText("");
            return;
        }
        if (TextUtils.isEmpty(mBind.etPort.getText())) {
            ToastUtils.showToast(this, "please input port first");
            mBind.tvResult.setText("");
            return;
        }
        //导出数据内容
        String path = DecoderModule.getInstance(getApplicationContext()).getNewHtmlFilePath();
        if (TextUtils.isEmpty(path) || !(new File(path)).exists()) {
            ToastUtils.showToast(this, "file not exit,please export data first");
            mBind.tvResult.setText("");
            return;
        }
        showLoadingProgressDialog();
        String str = mBind.etRawData.getText().toString().replaceAll(" ", "");
        String rawStr = "\'" + str + "\'";
        int port = Integer.parseInt(mBind.etPort.getText().toString().trim());
        if (null == mWebView) {
            mWebView = new WebView(this);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            //要加载本地sd卡上的文件必须添加以下属性
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setAllowContentAccess(true);
        }
        mWebView.loadUrl("file://" + path);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //现在很少还有4.4的系统了吧
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript("javascript:Decoder(" + rawStr + "," + port + ")", value -> {
                        String json = value.substring(1, value.length() - 1);
                        String result = json.replaceAll("\\\\", "");
                        XLog.i("333333执行了调用方法" + result);
                        mBind.tvResult.postDelayed(() -> {
                            mBind.tvResult.setText(stringToJSON(result));
                            dismissLoadingProgressDialog();
                        }, 300);
                    });
                }
            }
        });
    }

    /**
     * 格式化一下json数据显示格式 缩进 换行啥的这些
     *
     * @param strJson json
     * @return 格式化后的json字符串
     */
    private String stringToJSON(String strJson) {
        int tabNum = 0;
        StringBuilder jsonFormat = new StringBuilder();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c).append("\n").append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n").append(getSpaceOrTab(tabNum)).append(c);
            } else if (c == ',') {
                jsonFormat.append(c).append("\n").append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c).append(" ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c).append("\n").append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n").append(getSpaceOrTab(tabNum)).append(c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    private String getSpaceOrTab(int tabNum) {
        StringBuilder sbTab = new StringBuilder();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }
}
