package phcom.edu.managerclass.screen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appota.lunarcore.LunarCoreHelper;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import phcom.edu.managerclass.R;
import phcom.edu.managerclass.databaseEvent;
import phcom.edu.managerclass.databinding.ActivityAddEventBinding;
import phcom.edu.managerclass.model.ItemDay;
import phcom.edu.managerclass.model.event;

public class AddEventActivity extends AppCompatActivity {

    ActivityAddEventBinding binding;
    private ItemDay itemDay;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    final String[] CAN = { "Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ" };
    final String[] CHI = { "Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi" };
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        itemDay = (ItemDay) getIntent().getSerializableExtra("itemDay");

        int mlunarDay[] = LunarCoreHelper.convertSolar2Lunar(itemDay.getDay(), itemDay.getMonth(), itemDay.getYear(),
                7.0);
        int lunarDay = mlunarDay[0];
        int lunarMonth = mlunarDay[1];
        int lunarYear = mlunarDay[2];

        binding.tvMonth.setText("Thứ " + itemDay.getWeekday() + "," + itemDay.getDay() + "/" + itemDay.getMonth() + "/"
                + itemDay.getYear());
        binding.tvDay.setText("Ngày\n\n" + lunarDay + "\n\n" + getCanChiDay(lunarDay, lunarMonth, lunarYear));
        binding.tvMonthLunar.setText("Tháng\n\n" + lunarMonth + "\n\n" + getCanChiMonth(lunarMonth, lunarYear));
        binding.tvYearLunar.setText("Năm\n\n" + lunarYear + "\n\n" + getCanChiYear(lunarYear));
        binding.edtTime.setOnClickListener(new View.OnClickListener() {// chọn giờ bằng time picker của material
            @Override
            public void onClick(View v) {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()// chọn giờ
                        .setTimeFormat(TimeFormat.CLOCK_24H)// định dạng 24h cho time picker
                        .setTitleText("Chọn giờ")
                        .build();
                materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER");// hiển thị time picker
                materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
                    String hour = materialTimePicker.getHour() < 10 ? "0" + materialTimePicker.getHour()
                            : materialTimePicker.getHour() + "";// nếu giờ nhỏ hơn 10 thì thêm số 0 vào trước giờ
                    String minute = materialTimePicker.getMinute() < 10 ? "0" + materialTimePicker.getMinute()
                            : materialTimePicker.getMinute() + "";// nếu phút nhỏ hơn 10 thì thêm số 0 vào trước phút
                    binding.edtTime.setText(hour + ":" + minute);
                });
            }
        });

        binding.btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.edtTitle.getText().toString();
                String content = binding.edtContent.getText().toString();
                String hour = binding.edtTime.getText().toString();

                if (title.isEmpty() || content.isEmpty() || hour.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    event mevent = new event(title, itemDay.getDay(), itemDay.getMonth(), itemDay.getYear(), hour,
                            content);
                    new databaseEvent(getApplicationContext()).insertEv(mevent, new databaseEvent.onInsertSuccess() {
                        @Override
                        public void onSuccess() {
                            startActivity(new Intent(AddEventActivity.this, CalenderMonthYear.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    });
                }
            }
        });

        binding.back.setOnClickListener(v -> {
            startActivity(new Intent(AddEventActivity.this, CalenderMonthYear.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        });
    }

    // tính can chi của ngày tháng năm
    public String getCanChiYear(int year) {
        int can = year % 10;
        int chi = year % 12;
        String canChi = CAN[can] + " " + CHI[chi];
        return canChi;
    }

    public String getCanChiMonth(int month, int year) {
        int can = (year * 12 + month + 3) % 10;
        int chi = (month + 1) % 12;
        String canChi = CAN[can] + " " + CHI[chi];
        return canChi;
    }

    // tính can chi của ngày tháng năm
    public String getCanChiDay(int day, int month, int year) {
        int can = (year * 12 + month + 3) % 10;
        int chi = (month + 1) % 12;
        int canDay = (can * 2 + chi + day) % 10;
        int chiDay = day % 12;
        String canChi = CAN[canDay] + " " + CHI[chiDay];
        return canChi;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("bg", MODE_PRIVATE);
        String value = sharedPreferences.getString("bg", "bg1");
        if (value.equals("bg1")) {
            binding.scrollView.setBackgroundResource(R.drawable.bgradian1);
        }
        if (value.equals("bg2")) {
            binding.scrollView.setBackgroundResource(R.drawable.bgradian2);
        }
        if (value.equals("bg3")) {
            binding.scrollView.setBackgroundResource(R.drawable.bgradian3);
        }
        if (value.equals("bg4")) {
            binding.scrollView.setBackgroundResource(R.drawable.bgradian4);
        }
        if (value.equals("bg5")) {
            binding.scrollView.setBackgroundResource(R.drawable.nen);
        }
        if (value.equals("bg6")) {
            binding.scrollView.setBackgroundResource(R.drawable.bg);
        }
    }


}