package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfo;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfoEntry;
import it.brunorenz.mybank.mybankconfiguration.utility.MessageStatisticManager;

public class MainChartFragment extends Fragment {

    private PieChart chartSMS;
    private PieChart chartPUSH;
    private Typeface tf;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        //tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        chartSMS = v.findViewById(R.id.pieChartSMS);
        chartPUSH = v.findViewById(R.id.pieChartPUSH);

        displayChart(chartSMS, "SMS");
        displayChart(chartPUSH, "PUSH");
        chartSMS.setData(generatePieData("SMS"));
        chartPUSH.setData(generatePieData("PUSH"));
        return v;
    }

    private void moveOffScreen(PieChart chart) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        //getContext().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        int offset = (int)(height * 0.65); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams) chart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        chart.setLayoutParams(rlParams);
    }

    private void displayChart(PieChart chart, String type) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        //chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);
    }


    protected PieData generatePieData(String type) {

        boolean sms = type.equals("SMS");
        MessageStatisticManager stat = new MessageStatisticManager();
        MessageStatisticInfo info = stat.readData(getContext());
        PieData d = null;

        int count = 3;
        MessageStatisticInfoEntry entry = sms ? info.getSms() : info.getPush();
        if (entry.getTot() > 0) {
            ArrayList<PieEntry> entries1 = new ArrayList<>();
            float tot = entry.getAccepted() + entry.getTot();
            float f = (float) (entry.getTot() - entry.getSent()) / tot * 100;
            float a = (float) entry.getAccepted() / tot * 100;
            float s = (float) (entry.getSent() - entry.getAccepted()) / tot * 100;
            entries1.add(new PieEntry(f, "Filtrati"));
            entries1.add(new PieEntry(a, "Accettati"));
            entries1.add(new PieEntry(s, "Scartati"));

            PieDataSet ds1 = new PieDataSet(entries1, "Messaggi odierrni");
            ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            ds1.setSliceSpace(2f);
            ds1.setValueTextColor(Color.WHITE);
            ds1.setValueTextSize(12f);

            d = new PieData(ds1);
        }
        return d;
    }


    protected PieData generatePieData() {

        int count = 4;

        ArrayList<PieEntry> entries1 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            entries1.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quarter " + (i + 1)));
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        //d.setValueTypeface(tf);

        return d;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(MainChartFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }
}