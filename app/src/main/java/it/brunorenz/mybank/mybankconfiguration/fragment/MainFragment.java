package it.brunorenz.mybank.mybankconfiguration.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import it.brunorenz.mybank.mybankconfiguration.R;

public class MainFragment extends Fragment {

    private Typeface tf;
    //private boolean day;
    private static final String TAG =
            MainFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ImageButton amexButton;
    private Button masterCardButton;
    private Button addReceiptButton;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
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
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //day = position == 0;
                        tab.setText(position == 0 ? "OGGI" : "TOTALI");
                        //refreshBar(day);
                    }
                }).attach();

        amexButton = view.findViewById(R.id.buttonAmex);
        if (amexButton != null) amexButton.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {
                                                                      addReceiptFragment();
                                                                  }
                                                              }
        );
    }

    private void addReceiptFragment() {
        FragmentManager manager = getParentFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainPage, new AddReceiptFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
/*
    private void refreshBar(boolean day)
    {
        Intent i = new Intent("UPDATETAB"+(day ? "_DAY":"_TOT"));
        Log.d(TAG,"Send Broadcast "+i.getAction());
    }

 */
}