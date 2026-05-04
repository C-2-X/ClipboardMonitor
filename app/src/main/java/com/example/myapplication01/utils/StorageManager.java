package com.example.myapplication01.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.myapplication01.model.ClipData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes5.dex */
public class StorageManager {
    private static final String HISTORY_FILE = "clipboard_history.json";
    private static final String KEY_ENABLED = "monitoring_enabled";
    private static final String KEY_FLOAT_WINDOW_ENABLED = "float_window_enabled";
    private static final String KEY_FLOAT_X = "float_x";
    private static final String KEY_FLOAT_Y = "float_y";
    private static final String KEY_METHOD = "request_method";
    private static final String KEY_URL = "target_url";
    private static final int MAX_HISTORY_SIZE = 1000;
    private static final String PREF_NAME = "monitor_config";
    private final Context context;
    private final Gson gson = new Gson();
    private final SharedPreferences prefs;

    public StorageManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void saveConfig(String url, String method, boolean enabled) {
        this.prefs.edit().putString(KEY_URL, url).putString(KEY_METHOD, method).putBoolean(KEY_ENABLED, enabled).apply();
    }

    public void setFloatingWindowEnabled(boolean enabled) {
        this.prefs.edit().putBoolean(KEY_FLOAT_WINDOW_ENABLED, enabled).apply();
    }

    public boolean isFloatingWindowEnabled() {
        return this.prefs.getBoolean(KEY_FLOAT_WINDOW_ENABLED, false);
    }

    public void saveFloatingWindowPosition(int x, int y) {
        this.prefs.edit().putInt(KEY_FLOAT_X, x).putInt(KEY_FLOAT_Y, y).apply();
    }

    public int getFloatingWindowX() {
        return this.prefs.getInt(KEY_FLOAT_X, 0);
    }

    public int getFloatingWindowY() {
        return this.prefs.getInt(KEY_FLOAT_Y, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
    }

    public String getUrl() {
        return this.prefs.getString(KEY_URL, HttpUrl.FRAGMENT_ENCODE_SET);
    }

    public String getMethod() {
        return this.prefs.getString(KEY_METHOD, "POST");
    }

    public boolean isEnabled() {
        return this.prefs.getBoolean(KEY_ENABLED, false);
    }

    public synchronized void saveClip(ClipData data) {
        List<ClipData> history = getHistory();
        history.add(0, data);
        if (history.size() > 1000) {
            history = history.subList(0, 1000);
        }
        writeHistory(history);
    }

    public synchronized void updateClip(ClipData updatedData) {
        List<ClipData> history = getHistory();
        int i = 0;
        while (true) {
            if (i >= history.size()) {
                break;
            }
            if (!history.get(i).getId().equals(updatedData.getId())) {
                i++;
            } else {
                history.set(i, updatedData);
                break;
            }
        }
        writeHistory(history);
    }

    public synchronized List<ClipData> getHistory() {
        File file = new File(this.context.getFilesDir(), HISTORY_FILE);
        if (!file.exists()) {
            return new ArrayList();
        }
        try {
            FileReader reader = new FileReader(file);
            try {
                Type listType = new TypeToken<ArrayList<ClipData>>() { // from class: com.example.myapplication01.utils.StorageManager.1
                }.getType();
                List<ClipData> list = (List) this.gson.fromJson(reader, listType);
                List<ClipData> arrayList = list != null ? list : new ArrayList<>();
                reader.close();
                return arrayList;
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public synchronized ClipData getClipById(String id) {
        List<ClipData> history = getHistory();
        for (ClipData data : history) {
            if (data.getId().equals(id)) {
                return data;
            }
        }
        return null;
    }

    private void writeHistory(List<ClipData> history) {
        File file = new File(this.context.getFilesDir(), HISTORY_FILE);
        try {
            FileWriter writer = new FileWriter(file);
            try {
                this.gson.toJson(history, writer);
                writer.close();
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveHistory(List<ClipData> history) {
        writeHistory(history);
    }

    public synchronized List<ClipData> searchByKeyword(String keyword) {
        if (keyword != null) {
            if (!keyword.trim().isEmpty()) {
                final String lowerKeyword = keyword.toLowerCase();
                return (List) getHistory().stream().filter(new Predicate() { // from class: com.example.myapplication01.utils.StorageManager$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return StorageManager.lambda$searchByKeyword$0(lowerKeyword, (ClipData) obj);
                    }
                }).collect(Collectors.toList());
            }
        }
        return getHistory();
    }

    static /* synthetic */ boolean lambda$searchByKeyword$0(String lowerKeyword, ClipData data) {
        return data.getClipContent() != null && data.getClipContent().toLowerCase().contains(lowerKeyword);
    }

    public synchronized List<ClipData> filterByStatus(final String status) {
        if (status != null) {
            if (!status.isEmpty()) {
                return (List) getHistory().stream().filter(new Predicate() { // from class: com.example.myapplication01.utils.StorageManager$$ExternalSyntheticLambda3
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return status.equals(((ClipData) obj).getStatus());
                    }
                }).collect(Collectors.toList());
            }
        }
        return getHistory();
    }

    public synchronized List<ClipData> filterByTimeRange(final long startTime, final long endTime) {
        return (List) getHistory().stream().filter(new Predicate() { // from class: com.example.myapplication01.utils.StorageManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return StorageManager.lambda$filterByTimeRange$2(startTime, endTime, (ClipData) obj);
            }
        }).collect(Collectors.toList());
    }

    static /* synthetic */ boolean lambda$filterByTimeRange$2(long startTime, long endTime, ClipData data) {
        return data.getTimestamp() >= startTime && data.getTimestamp() <= endTime;
    }

    public synchronized List<ClipData> filter(final String keyword, final String status, final long startTime, final long endTime) {
        return (List) getHistory().stream().filter(new Predicate() { // from class: com.example.myapplication01.utils.StorageManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return StorageManager.lambda$filter$3(keyword, status, startTime, endTime, (ClipData) obj);
            }
        }).collect(Collectors.toList());
    }

    static /* synthetic */ boolean lambda$filter$3(String keyword, String status, long startTime, long endTime, ClipData data) {
        if (keyword != null && !keyword.trim().isEmpty() && (data.getClipContent() == null || !data.getClipContent().toLowerCase().contains(keyword.toLowerCase()))) {
            return false;
        }
        if ((status != null && !status.isEmpty() && !status.equals(data.getStatus())) || data.getTimestamp() < startTime || data.getTimestamp() > endTime) {
            return false;
        }
        return true;
    }

    public synchronized List<ClipData> getFailedClips() {
        return (List) getHistory().stream().filter(new Predicate() { // from class: com.example.myapplication01.utils.StorageManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return "FAILED".equals(((ClipData) obj).getStatus());
            }
        }).collect(Collectors.toList());
    }

    public synchronized String exportToJson() {
        List<ClipData> history;
        history = getHistory();
        return this.gson.toJson(history);
    }

    public synchronized String exportToCsv() {
        StringBuilder csv;
        List<ClipData> history = getHistory();
        csv = new StringBuilder();
        csv.append("ID,Device ID,Timestamp,Content,Type,Status,Retry Count\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        for (ClipData data : history) {
            csv.append(escapeCsv(data.getId())).append(",");
            csv.append(escapeCsv(data.getDeviceId())).append(",");
            csv.append(sdf.format(new Date(data.getTimestamp()))).append(",");
            csv.append(escapeCsv(data.getClipContent())).append(",");
            csv.append(escapeCsv(data.getClipType())).append(",");
            csv.append(escapeCsv(data.getStatus())).append(",");
            csv.append(data.getRetryCount()).append("\n");
        }
        return csv.toString();
    }

    public synchronized File saveExportFile(String content, String fileName) {
        File exportFile;
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        exportFile = new File(downloadsDir, fileName);
        try {
            FileWriter writer = new FileWriter(exportFile);
            try {
                writer.write(content);
                writer.close();
            } catch (Throwable th) {
                try {
                    writer.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return exportFile;
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "\"\"";
        }
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
