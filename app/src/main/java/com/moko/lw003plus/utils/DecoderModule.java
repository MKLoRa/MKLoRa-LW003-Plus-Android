package com.moko.lw003plus.utils;

import android.content.Context;

import com.elvishew.xlog.XLog;
import com.moko.lw003plus.activity.LoRaLW003PlusMainActivity;
import com.moko.lw003plus.entity.PayloadFlag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DecoderModule {

    private static volatile DecoderModule INSTANCE;
    private Context context;
    private static final String FOLDER_NAME = "decoder";
    private static final String FINAL_DECODER = "LW003-B_Pro_Decoder_TTN.js";
    private static final String NEW_DECODER = "LW003-B_Pro_Decoder_TTN_NEW.txt";

    private DecoderModule(Context context) {
        this.context = context;
    }


    public static DecoderModule getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DecoderModule.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DecoderModule(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public void copyAssets2SD() {
        copyAssets(context, FOLDER_NAME, LoRaLW003PlusMainActivity.PATH_LOGCAT + File.separator + FOLDER_NAME);
        copyAssets(context, "html", LoRaLW003PlusMainActivity.PATH_LOGCAT + File.separator + "html");
    }

    private void copyAssets(Context context, String assetDir, String dir) {
        String[] files;
        try {
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File sdDir = new File(dir);
        if (!sdDir.exists()) {
            sdDir.mkdirs();
        }
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                File outFile = new File(dir, fileName);
                if (outFile.exists()) {
                    outFile.delete();
                }
                outFile.createNewFile();
                InputStream in = context.getAssets().open(assetDir + File.separator + fileName);
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFinalFilePath() {
        return LoRaLW003PlusMainActivity.PATH_LOGCAT + File.separator + FOLDER_NAME + File.separator + FINAL_DECODER;
    }

    public String getNewFilePath() {
        return LoRaLW003PlusMainActivity.PATH_LOGCAT + File.separator + FOLDER_NAME + File.separator + NEW_DECODER;
    }

    public String getNewHtmlFilePath() {
        return LoRaLW003PlusMainActivity.PATH_LOGCAT + File.separator + "html" + File.separator + "LW003-B_Pro_Decoder_Final.html";
    }

    public String getFinalHtmlPath() {
        return LoRaLW003PlusMainActivity.PATH_LOGCAT + File.separator + "html" + File.separator + "decoder.html";
    }

    public File createNewDecoder(PayloadFlag flag, String outFilePath, String finalFilePath) {
        try {
//            final String outFilePath = getNewFilePath();
            File outFile = new File(outFilePath);
            if (outFile.exists()) {
                outFile.delete();
            }
            outFile.createNewFile();
//            final String finalFilePath = getFinalFilePath();
            FileInputStream in = new FileInputStream(finalFilePath);
            FileOutputStream out = new FileOutputStream(outFile);
            InputStreamReader inReader = new InputStreamReader(in);
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            BufferedReader reader = new BufferedReader(inReader);
            BufferedWriter writer = new BufferedWriter(outWriter);
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            boolean isFlagChanged = false;
            while ((line = reader.readLine()) != null) {
                if (isFlagChanged)
                    stringBuilder.append(line);
                else if (line.contains("var iBeaconFlag"))
                    stringBuilder.append(String.format("var iBeaconFlag = 0x%s;", String.format("%04X", flag.iBeaconFlag)));
                else if (line.contains("var EddystoneUIDFlag"))
                    stringBuilder.append(String.format("var EddystoneUIDFlag = 0x%s;", String.format("%02X", flag.EddystoneUIDFlag)));
                else if (line.contains("var EddystoneURLFlag"))
                    stringBuilder.append(String.format("var EddystoneURLFlag = 0x%s;", String.format("%02X", flag.EddystoneURLFlag)));
                else if (line.contains("var EddystoneTLMFlag"))
                    stringBuilder.append(String.format("var EddystoneTLMFlag = 0x%s;", String.format("%04X", flag.EddystoneTLMFlag)));
                else if (line.contains("var BXPiBeaconFlag"))
                    stringBuilder.append(String.format("var BXPiBeaconFlag = 0x%s;", String.format("%04X", flag.BXPiBeaconFlag)));
                else if (line.contains("var BXPDeviceInfoFlag"))
                    stringBuilder.append(String.format("var BXPDeviceInfoFlag = 0x%s;", String.format("%04X", flag.BXPDeviceInfoFlag)));
                else if (line.contains("var BXPACCFlag"))
                    stringBuilder.append(String.format("var BXPACCFlag = 0x%s;", String.format("%04X", flag.BXPACCFlag)));
                else if (line.contains("var BXPTHFlag"))
                    stringBuilder.append(String.format("var BXPTHFlag = 0x%s;", String.format("%04X", flag.BXPTHFlag)));
                else if (line.contains("var BXPButtonFlag"))
                    stringBuilder.append(String.format("var BXPButtonFlag = 0x%s;", String.format("%06X", flag.BXPButtonFlag)));
                else if (line.contains("var BXPTagFlag"))
                    stringBuilder.append(String.format("var BXPTagFlag = 0x%s;", String.format("%04X", flag.BXPTagFlag)));
                else if (line.contains("var OtherTypeFlag"))
                    stringBuilder.append(String.format("var OtherTypeFlag = 0x%s;", String.format("%02X", flag.OtherTypeFlag)));
                else if (line.contains("var BXPPirFlag"))
                    stringBuilder.append(String.format("var BXPPirFlag = 0x%s;", String.format("%04X", flag.BXPPIRFlag)));
                else if (line.contains("var BXPTofFlag")) {
                    stringBuilder.append(String.format("var BXPTofFlag = 0x%s;", String.format("%04X", flag.BXPTOFFlag)));
                    isFlagChanged = true;
                } else
                    stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            writer.write(stringBuilder.toString());
            writer.flush();
            outWriter.flush();
            out.flush();
            writer.close();
            outWriter.close();
            out.close();
            reader.close();
            inReader.close();
            in.close();
            return outFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void copyNewFileAndRename(String newPath, String oldPath) throws IOException {
        //需要复制到的路径，以及图片的新命名+格式
        File result = new File(newPath);
        //判断该文件夹是否存在,不存在则新增
        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }
        FileInputStream input = new FileInputStream(oldPath);
        FileOutputStream out = new FileOutputStream(result);
        //一个容量，相当于打水的桶，可以自定义大小
        byte[] buffer = new byte[1024];
        int hasRead;
        while ((hasRead = input.read(buffer)) > 0) {
            //0：表示每次从0开始
            out.write(buffer, 0, hasRead);
        }
        XLog.i("333333path=" + result.getAbsolutePath());
        input.close();
        out.close();
    }
}
