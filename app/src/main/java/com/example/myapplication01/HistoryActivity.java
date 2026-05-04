package com.example.myapplication01;

import android.app.DatePickerDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication01.model.ClipData;
import com.example.myapplication01.utils.NetworkManager;
import com.example.myapplication01.utils.StorageManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes3.dex */
public class HistoryActivity extends AppCompatActivity {
    private HistoryAdapter adapter;
    private Button btnApplyFilter;
    private Button btnClearFilter;
    private TextInputEditText etSearch;
    private LinearLayout layoutFilter;
    private NetworkManager networkManager;
    private RecyclerView rvHistory;
    private Spinner spinnerStatus;
    private Spinner spinnerTimeRange;
    private StorageManager storageManager;
    private TextView tvDateRange;
    private TextView tvEmpty;
    private TextView tvRecordCount;
    private List<ClipData> historyList = new ArrayList();
    private List<ClipData> filteredList = new ArrayList();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private long customStartTime = 0;
    private long customEndTime = Long.MAX_VALUE;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.storageManager = new StorageManager(this);
        this.networkManager = NetworkManager.getInstance();
        initViews();
        setupFilter();
        loadHistory();
    }

    private void initViews() {
        this.rvHistory = (RecyclerView) findViewById(R.id.rv_history);
        this.tvEmpty = (TextView) findViewById(R.id.tv_empty);
        this.tvRecordCount = (TextView) findViewById(R.id.tv_record_count);
        this.layoutFilter = (LinearLayout) findViewById(R.id.layout_filter);
        this.etSearch = (TextInputEditText) findViewById(R.id.et_search);
        this.spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
        this.spinnerTimeRange = (Spinner) findViewById(R.id.spinner_time_range);
        this.btnApplyFilter = (Button) findViewById(R.id.btn_apply_filter);
        this.btnClearFilter = (Button) findViewById(R.id.btn_clear_filter);
        this.tvDateRange = (TextView) findViewById(R.id.tv_date_range);
        this.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new HistoryAdapter(this.filteredList);
        this.rvHistory.setAdapter(this.adapter);
        this.btnApplyFilter.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m50lambda$initViews$0$comexamplemyapplication01HistoryActivity(view);
            }
        });
        this.btnClearFilter.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m51lambda$initViews$1$comexamplemyapplication01HistoryActivity(view);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$initViews$0$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m50lambda$initViews$0$comexamplemyapplication01HistoryActivity(View v) {
        applyFilter();
    }

    /* JADX INFO: renamed from: lambda$initViews$1$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m51lambda$initViews$1$comexamplemyapplication01HistoryActivity(View v) {
        clearFilter();
    }

    private void setupFilter() {
        String[] statuses = {"全部", "待发送", "已发送", "发送失败"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerStatus.setAdapter((SpinnerAdapter) statusAdapter);
        String[] timeRanges = {"全部时间", "今日", "昨日", "近7天", "近30天", "自定义"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeRanges);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTimeRange.setAdapter((SpinnerAdapter) timeAdapter);
        this.spinnerTimeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.example.myapplication01.HistoryActivity.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HistoryActivity historyActivity = HistoryActivity.this;
                if (position == 5) {
                    historyActivity.showCustomDateRangePicker();
                } else {
                    historyActivity.tvDateRange.setVisibility(8);
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCustomDateRangePicker() {
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog startPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda6
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                this.f$0.m53x1cc31089(cal, datePicker, i, i2, i3);
            }
        }, cal.get(1), cal.get(2), cal.get(5));
        startPicker.setTitle("选择开始日期");
        startPicker.show();
    }

    /* JADX INFO: renamed from: lambda$showCustomDateRangePicker$3$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m53x1cc31089(Calendar cal, DatePicker view, int year, int month, int dayOfMonth) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, month, dayOfMonth, 0, 0, 0);
        this.customStartTime = startCal.getTimeInMillis();
        DatePickerDialog endPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda3
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public final void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                this.f$0.m52x82224e08(datePicker, i, i2, i3);
            }
        }, cal.get(1), cal.get(2), cal.get(5));
        endPicker.setTitle("选择结束日期");
        endPicker.show();
    }

    /* JADX INFO: renamed from: lambda$showCustomDateRangePicker$2$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m52x82224e08(DatePicker view2, int year2, int month2, int dayOfMonth2) {
        Calendar endCal = Calendar.getInstance();
        endCal.set(year2, month2, dayOfMonth2, 23, 59, 59);
        this.customEndTime = endCal.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.tvDateRange.setText(sdf.format(new Date(this.customStartTime)) + " 至 " + sdf.format(new Date(this.customEndTime)));
        this.tvDateRange.setVisibility(0);
    }

    private void applyFilter() {
        String status;
        String keyword = this.etSearch.getText() != null ? this.etSearch.getText().toString() : HttpUrl.FRAGMENT_ENCODE_SET;
        int statusPos = this.spinnerStatus.getSelectedItemPosition();
        switch (statusPos) {
            case 1:
                status = "PENDING";
                break;
            case 2:
                status = "SENT";
                break;
            case 3:
                status = "FAILED";
                break;
            default:
                status = HttpUrl.FRAGMENT_ENCODE_SET;
                break;
        }
        long[] timeRange = getTimeRange(this.spinnerTimeRange.getSelectedItemPosition());
        long startTime = timeRange[0];
        long endTime = timeRange[1];
        this.filteredList.clear();
        this.filteredList.addAll(this.storageManager.filter(keyword, status, startTime, endTime));
        this.adapter.notifyDataSetChanged();
        updateEmptyView();
    }

    private void clearFilter() {
        this.etSearch.setText(HttpUrl.FRAGMENT_ENCODE_SET);
        this.spinnerStatus.setSelection(0);
        this.spinnerTimeRange.setSelection(0);
        this.customStartTime = 0L;
        this.customEndTime = Long.MAX_VALUE;
        this.tvDateRange.setVisibility(8);
        loadHistory();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private long[] getTimeRange(int position) {
        long[] range = new long[2];
        Calendar cal = Calendar.getInstance();
        range[1] = cal.getTimeInMillis();
        switch (position) {
            case 0:
                range[0] = 0;
                range[1] = Long.MAX_VALUE;
                return range;
            case 1:
                cal.set(11, 0);
                cal.set(12, 0);
                cal.set(13, 0);
                range[0] = cal.getTimeInMillis();
                return range;
            case 2:
                cal.add(6, -1);
                cal.set(11, 0);
                cal.set(12, 0);
                cal.set(13, 0);
                range[0] = cal.getTimeInMillis();
                cal.set(11, 23);
                cal.set(12, 59);
                cal.set(13, 59);
                range[1] = cal.getTimeInMillis();
                return range;
            case 3:
                cal.add(6, -7);
                range[0] = cal.getTimeInMillis();
                return range;
            case 4:
                cal.add(6, -30);
                range[0] = cal.getTimeInMillis();
                return range;
            case 5:
                range[0] = this.customStartTime;
                range[1] = this.customEndTime;
                return range;
            default:
                return range;
        }
    }

    private void loadHistory() {
        this.historyList.clear();
        this.historyList.addAll(this.storageManager.getHistory());
        this.filteredList.clear();
        this.filteredList.addAll(this.historyList);
        this.adapter.notifyDataSetChanged();
        updateEmptyView();
    }

    private void updateEmptyView() {
        this.tvRecordCount.setText("共 " + this.filteredList.size() + " 条记录");
        boolean zIsEmpty = this.filteredList.isEmpty();
        TextView textView = this.tvEmpty;
        if (zIsEmpty) {
            textView.setVisibility(0);
            this.rvHistory.setVisibility(8);
        } else {
            textView.setVisibility(8);
            this.rvHistory.setVisibility(0);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_export_json) {
            exportToJson();
            return true;
        }
        if (id == R.id.action_export_csv) {
            exportToCsv();
            return true;
        }
        if (id == R.id.action_toggle_filter) {
            this.layoutFilter.setVisibility(this.layoutFilter.getVisibility() == 0 ? 8 : 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportToJson() {
        new Thread(new Runnable() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m49x8b09d04d();
            }
        }).start();
    }

    /* JADX INFO: renamed from: lambda$exportToJson$5$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m49x8b09d04d() {
        String json = this.storageManager.exportToJson();
        final File file = this.storageManager.saveExportFile(json, "clipboard_history_" + System.currentTimeMillis() + ".json");
        this.mainHandler.post(new Runnable() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m48xf0690dcc(file);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$exportToJson$4$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m48xf0690dcc(File file) {
        if (file != null) {
            Toast.makeText(this, "已导出到: " + file.getAbsolutePath(), 1).show();
        } else {
            Toast.makeText(this, "导出失败", 0).show();
        }
    }

    private void exportToCsv() {
        new Thread(new Runnable() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m47lambda$exportToCsv$7$comexamplemyapplication01HistoryActivity();
            }
        }).start();
    }

    /* JADX INFO: renamed from: lambda$exportToCsv$7$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m47lambda$exportToCsv$7$comexamplemyapplication01HistoryActivity() {
        String csv = this.storageManager.exportToCsv();
        final File file = this.storageManager.saveExportFile(csv, "clipboard_history_" + System.currentTimeMillis() + ".csv");
        this.mainHandler.post(new Runnable() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m46lambda$exportToCsv$6$comexamplemyapplication01HistoryActivity(file);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$exportToCsv$6$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m46lambda$exportToCsv$6$comexamplemyapplication01HistoryActivity(File file) {
        if (file != null) {
            Toast.makeText(this, "已导出到: " + file.getAbsolutePath(), 1).show();
        } else {
            Toast.makeText(this, "导出失败", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resendClip(ClipData data) {
        data.setStatus("PENDING");
        data.incrementRetryCount();
        this.storageManager.updateClip(data);
        String url = this.storageManager.getUrl();
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "请先配置服务器地址", 0).show();
        } else {
            this.networkManager.sendReport(url, this.storageManager.getMethod(), data, new AnonymousClass2(data));
        }
    }

    /* JADX INFO: renamed from: com.example.myapplication01.HistoryActivity$2, reason: invalid class name */
    class AnonymousClass2 implements NetworkManager.NetworkCallback {
        final /* synthetic */ ClipData val$data;

        AnonymousClass2(ClipData clipData) {
            this.val$data = clipData;
        }

        @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
        public void onSuccess() {
            this.val$data.setStatus("SENT");
            HistoryActivity.this.storageManager.updateClip(this.val$data);
            HistoryActivity.this.mainHandler.post(new Runnable() { // from class: com.example.myapplication01.HistoryActivity$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m56lambda$onSuccess$0$comexamplemyapplication01HistoryActivity$2();
                }
            });
        }

        /* JADX INFO: renamed from: lambda$onSuccess$0$com-example-myapplication01-HistoryActivity$2, reason: not valid java name */
        /* synthetic */ void m56lambda$onSuccess$0$comexamplemyapplication01HistoryActivity$2() {
            Toast.makeText(HistoryActivity.this, "发送成功", 0).show();
            HistoryActivity.this.adapter.notifyDataSetChanged();
        }

        @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
        public void onFailure(final String error) {
            this.val$data.setStatus("FAILED");
            HistoryActivity.this.storageManager.updateClip(this.val$data);
            HistoryActivity.this.mainHandler.post(new Runnable() { // from class: com.example.myapplication01.HistoryActivity$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m55lambda$onFailure$1$comexamplemyapplication01HistoryActivity$2(error);
                }
            });
        }

        /* JADX INFO: renamed from: lambda$onFailure$1$com-example-myapplication01-HistoryActivity$2, reason: not valid java name */
        /* synthetic */ void m55lambda$onFailure$1$comexamplemyapplication01HistoryActivity$2(String error) {
            Toast.makeText(HistoryActivity.this, "发送失败: " + error, 0).show();
            HistoryActivity.this.adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void copyToClipboard(String content) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService("clipboard");
        android.content.ClipData clip = android.content.ClipData.newPlainText("clipboard", content);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "已复制到剪切板", 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeleteConfirm(final ClipData data, final int position) {
        new AlertDialog.Builder(this).setTitle("删除记录").setMessage("确定要删除这条记录吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m54xd249f078(data, position, dialogInterface, i);
            }
        }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: renamed from: lambda$showDeleteConfirm$9$com-example-myapplication01-HistoryActivity, reason: not valid java name */
    /* synthetic */ void m54xd249f078(final ClipData data, int position, DialogInterface dialog, int which) {
        this.historyList.remove(data);
        this.filteredList.remove(data);
        List<ClipData> allHistory = this.storageManager.getHistory();
        allHistory.removeIf(new Predicate() { // from class: com.example.myapplication01.HistoryActivity$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((ClipData) obj).getId().equals(data.getId());
            }
        });
        this.storageManager.saveHistory(allHistory);
        this.adapter.notifyItemRemoved(position);
        updateEmptyView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class HistoryAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<ClipData> dataList;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        public HistoryAdapter(List<ClipData> dataList) {
            this.dataList = dataList;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new ViewHolder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(ViewHolder holder, final int position) {
            String statusText;
            int statusColor;
            final ClipData data = this.dataList.get(position);
            holder.tvContent.setText(data.getClipContent());
            holder.tvTimestamp.setText(this.sdf.format(new Date(data.getTimestamp())));
            if ("SENT".equals(data.getStatus())) {
                statusText = "已发送";
                statusColor = -16744448;
            } else {
                String statusText2 = data.getStatus();
                if ("FAILED".equals(statusText2)) {
                    statusText = "发送失败";
                    statusColor = SupportMenu.CATEGORY_MASK;
                } else {
                    statusText = "待发送";
                    statusColor = -23296;
                }
            }
            holder.tvStatus.setText(statusText);
            holder.tvStatus.setTextColor(statusColor);
            if ("FAILED".equals(data.getStatus())) {
                holder.btnResend.setVisibility(0);
                holder.btnResend.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication01.HistoryActivity$HistoryAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        this.f$0.m57xb1344921(data, view);
                    }
                });
            } else {
                holder.btnResend.setVisibility(8);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication01.HistoryActivity$HistoryAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.f$0.m58xb7381480(data, view);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.example.myapplication01.HistoryActivity$HistoryAdapter$$ExternalSyntheticLambda2
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return this.f$0.m59xbd3bdfdf(data, position, view);
                }
            });
        }

        /* JADX INFO: renamed from: lambda$onBindViewHolder$0$com-example-myapplication01-HistoryActivity$HistoryAdapter, reason: not valid java name */
        /* synthetic */ void m57xb1344921(ClipData data, View v) {
            HistoryActivity.this.resendClip(data);
        }

        /* JADX INFO: renamed from: lambda$onBindViewHolder$1$com-example-myapplication01-HistoryActivity$HistoryAdapter, reason: not valid java name */
        /* synthetic */ void m58xb7381480(ClipData data, View v) {
            HistoryActivity.this.copyToClipboard(data.getClipContent());
        }

        /* JADX INFO: renamed from: lambda$onBindViewHolder$2$com-example-myapplication01-HistoryActivity$HistoryAdapter, reason: not valid java name */
        /* synthetic */ boolean m59xbd3bdfdf(ClipData data, int position, View v) {
            HistoryActivity.this.showDeleteConfirm(data, position);
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.dataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            MaterialButton btnResend;
            TextView tvContent;
            TextView tvStatus;
            TextView tvTimestamp;

            public ViewHolder(View itemView) {
                super(itemView);
                this.tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                this.tvTimestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
                this.tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
                this.btnResend = (MaterialButton) itemView.findViewById(R.id.btn_resend);
            }
        }
    }
}
