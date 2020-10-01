package it.brunorenz.mybank.mybankconfiguration.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ChartViewPagerAdapter extends FragmentStateAdapter {

    public ChartViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MainChartFragment fragment = new MainChartFragment();
        fragment.setDay(position == 0);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
