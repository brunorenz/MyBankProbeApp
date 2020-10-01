package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Fill;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfo;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfoEntry;
import it.brunorenz.mybank.mybankconfiguration.utility.MessageStatisticManager;

public class MainChartFragment extends Fragment {

    //private PieChart chartSMS;
    //private PieChart chartPUSH;
    private BarChart chart1SMS;
    private BarChart chart1PUSH;
    private Typeface tf;
    private boolean day;
    private static final String TAG =
            MainChartFragment.class.getSimpleName();

    public void setDay(boolean day) {
        this.day = day;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //day = true;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        //tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        chart1SMS = v.findViewById(R.id.pieChart1SMS);
        //chartPUSH = v.findViewById(R.id.pieChartPUSH);
        chart1PUSH = v.findViewById(R.id.pieChart1PUSH);

        displayBarChart(chart1SMS,"SMS");
        displayBarChart(chart1PUSH,"PUSH");
        refreshBar(day);
        Log.d(TAG,"Creato grafico tipo day = "+day);
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
        xAxis.setTextSize(12f);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.BLUE);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String l = "";
                int i = (int) value;
                switch (i)
                {
                    case 0:
                        l = "Filtrati";
                        break;
                    case 1:
                        l = "Inviati";
                        break;
                    case 2:
                        l = "Accettati";
                        break;
                }
                return l;
            }
        });

        //xAxis.setSpaceTop(80f);

        // data has AxisDependency.LEFT
        YAxis left = chart.getAxisLeft();
        left.setTextSize(12f);
        left.setTextColor(Color.RED);
        left.setGranularity(1f); //
        left.setDrawLabels(true); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        left.setDrawZeroLine(false); // draw a zero line
        //left.setValueFormatter(new DefaultValueFormatter(0));
        chart.getAxisRight().setEnabled(false); // no right axis

        // Legend
        Legend l = chart.getLegend();
        l.setFormSize(12f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        // space between the legend entries on the y-axis
        // set custom labels and colors
        l.setXEntrySpace(05f); // space between the legend entries on the x-axis
        l.setYEntrySpace(05f);
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

    private BarData generateBarData(MessageStatisticInfo info, String type, boolean today)
    {
        boolean sms = type.equals("SMS");
        MessageStatisticInfoEntry entry = sms ? (today ? info.getSms() : info.getTotSms()): (today ? info.getPush() : info.getTotPush());

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) (entry.getTot()-entry.getSent())));
        entries.add(new BarEntry(1f,  (float) entry.getSent()));
        entries.add(new BarEntry(2f, (float) entry.getAccepted()));
        int startColor0 = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
        int startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
        int startColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);

        int endColor0 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
        int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
        int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);

        List<Fill> gradientFills = new ArrayList<>();
        gradientFills.add(new Fill(startColor0, endColor0));
        gradientFills.add(new Fill(startColor1, endColor1));
        gradientFills.add(new Fill(startColor2, endColor2));

        BarDataSet set = new BarDataSet(entries, today ? "Messaggi "+type+" odierni" : "Messaggi "+type+" totali");
        set.setFills(gradientFills);
        ArrayList<String> xVals = new ArrayList<String>();

        BarData d =  new BarData(set);
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

    public void onViewCreatedX(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                day = pos == 0;
                refreshBar(day);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout)  view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBar(day);
                swipeLayout.setRefreshing(false);
            }
        });


//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(MainChartFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    private void refreshBar(boolean day)
    {
        Log.d(TAG,"Refresh statistic for day = "+day);
        MessageStatisticManager stat = new MessageStatisticManager();
        MessageStatisticInfo info = stat.readData(getContext());
        chart1PUSH.setData(generateBarData(info,"PUSH",day));
        chart1SMS.setData(generateBarData(info,"SMS",day));
        chart1SMS.invalidate();
        chart1PUSH.invalidate();

    }
}