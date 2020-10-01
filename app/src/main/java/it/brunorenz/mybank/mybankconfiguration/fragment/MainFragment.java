package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfo;
import it.brunorenz.mybank.mybankconfiguration.utility.MessageStatisticManager;

public class MainFragment extends Fragment {

    private Typeface tf;
    private boolean day;
    private static final String TAG =
            MainFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        day = true;
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        refreshBar(day);
        return v;
    }

    private ChartViewPagerAdapter createCardAdapter() {
        ChartViewPagerAdapter adapter = new ChartViewPagerAdapter(this.getActivity());
        return adapter;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(createCardAdapter());

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        day = position == 0;
                        tab.setText(position == 0 ? "OGGI" : "TOTALI");
                        //refreshBar(day);
                    }
                }).attach();


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
        MessageStatisticManager stat = new MessageStatisticManager();
        MessageStatisticInfo info = stat.readData(getContext());
        Log.d(TAG,"Statistics for day : "+day);
    }
}