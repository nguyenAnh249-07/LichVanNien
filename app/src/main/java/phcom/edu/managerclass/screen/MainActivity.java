package phcom.edu.managerclass.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.appota.lunarcore.LunarCoreHelper;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;

import phcom.edu.managerclass.R;
import phcom.edu.managerclass.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private int lunarDay, lunarMonth, lunarYear, day, month, year, dayOfWeek;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // lấy ngày hôm nay
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        binding.tvThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CalenderMonthYear.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        binding.tvMr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        CalenderCan(String.valueOf(year));
        binding.tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Chọn ngày");
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis((Long) selection);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    month = calendar.get(Calendar.MONTH) + 1;
                    year = calendar.get(Calendar.YEAR);
                    dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    CalenderCan(String.valueOf(year));
                });
            }
        });

    }

    private void CalenderCan(String year1) {
        final String[] CAN = { "Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ" };
        final String[] CHI = { "Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi" };

        int canIndex = Integer.parseInt(year1) % 10;
        int chiIndex = Integer.parseInt(year1) % 12;
        binding.tvCanchi.setText(CAN[canIndex] + " " + CHI[chiIndex]);// hiển thị can chi năm
        int mlunarDay[] = LunarCoreHelper.convertSolar2Lunar(day, month, year, 7);// chuyển đổi ngày tháng năm dương
                                                                                  // lịch sang âm lịch
        int lunarDay = mlunarDay[0];
        int lunarMonth = mlunarDay[1];
        int lunarYear = mlunarDay[2];
        binding.tvA.setText("Ngày " + lunarDay);
        binding.tvM.setText("Tháng " + lunarMonth);
        binding.tvMonth.setText("Tháng " + month + "- 2024");
        if (dayOfWeek == 1) {
            binding.tvThu.setText("Chủ nhật");
        } else {
            binding.tvThu.setText("Thứ " + dayOfWeek);
        }
        binding.tvD.setText(day + "");

        // lấy giờ realtime và hiển thị
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        int minute = calendar1.get(Calendar.MINUTE);
        // am pm
        if (hour > 12) {
            hour = hour - 12;
            binding.tvHour.setText(hour + ":" + minute + " PM");
        } else {
            binding.tvHour.setText(hour + ":" + minute + " AM");
        }
    }
}