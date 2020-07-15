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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfo;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfoEntry;
import it.brunorenz.mybank.mybankconfiguration.utility.MessageStatisticManager;

public class MainChartFragment extends Fragment {

    private PieChart chartSMS;
    private PieChart chartPUSH;
    private BarChart chart1PUSH;
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
        chart1PUSH = v.findViewById(R.id.pieChart1PUSH);

        displayBarChart(chart1PUSH,"PUSH");
        displayChart(chartSMS, "SMS");
        displayChart(chartPUSH, "PUSH");
        chartSMS.setData(generatePieData("SMS"));
        chartPUSH.setData(generatePieData("PUSH"));
        chart1PUSH.setData(generateBarData("PUSH",false));
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

    private void displayBarChart(BarChart chart, String type) {
        chart.setDragEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);


        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);


        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(3);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.RED);
        //xAxis.setSpaceTop(80f);

        // data has AxisDependency.LEFT
        YAxis left = chart.getAxisLeft();
        left.setTextSize(10f);
        left.setTextColor(Color.RED);
        left.setGranularity(1f); //
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        chart.getAxisRight().setEnabled(false); // no right axis

        // Legend
        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        // space between the legend entries on the y-axis
        // set custom labels and colors
        l.setXEntrySpace(5f); // space between the legend entries on the x-axis
        l.setYEntrySpace(5f);
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

    private BarData generateBarData(String type, boolean today)
    {
        boolean sms = type.equals("SMS");
        MessageStatisticManager stat = new MessageStatisticManager();
        MessageStatisticInfo info = stat.readData(getContext());
        MessageStatisticInfoEntry entry = sms ? (today ? info.getSms() : info.getTotSms()): (today ? info.getPush() : info.getTotPush());
        BarData d = null;
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) (entry.getTot()-entry.getSent())));
        entries.add(new BarEntry(1f,  (float) entry.getSent()));
        entries.add(new BarEntry(2f, (float) entry.getAccepted()));
        BarDataSet set = new BarDataSet(entries, today ? "Messaggi odierni" : "Messaggi totali");
        d =  new BarData(set);
        return d;
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

            PieDataSet ds1 = new PieDataSet(entries1, "Messaggi odierni");
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