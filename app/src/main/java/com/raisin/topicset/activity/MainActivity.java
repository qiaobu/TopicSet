package com.raisin.topicset.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.raisin.topicset.R;
import com.raisin.topicset.adapter.ViewPagerAdapter;
import com.raisin.topicset.fragment.AddFragment;
import com.raisin.topicset.fragment.HomeFragment;
import com.raisin.topicset.permission.PermissionHelper;
import com.raisin.topicset.permission.PermissionInterface;
import com.raisin.topicset.sqldb.SQLiteDbHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionInterface {

    private PermissionHelper mPermissionHelper;

    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private MenuItem menuItem;

    private boolean mIsExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DB
        setContentView(R.layout.activity_main);
        SQLiteDbHelper helper = new SQLiteDbHelper(getApplicationContext());
        helper.getWritableDatabase();

        initView();

        // 初始化并发起权限申请
        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();
    }

    private void initView() {
        // viewPager
        viewPager = findViewById(R.id.viewpager);

        // ViewPagerAdapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new AddFragment());
//        adapter.addFragment(new ExportFragment());
//        adapter.addFragment(new UserFragment());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        // BottomNavigationView
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            // 首页
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                return true;
                            // 添加
                            case R.id.navigation_add:
                                viewPager.setCurrentItem(1);
                                return true;
                            // 生成
                            case R.id.navigation_export:
                                viewPager.setCurrentItem(2);
                                return true;
//                            // 我的
//                            case R.id.navigation_person:
//                                viewPager.setCurrentItem(3);
//                                return true;
                        }
                        return false;
                    }
                });

        // ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * 双击退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        // TODO
        switch (view.getId()) {
        }
    }

    // Permission methods start
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getPermissionsRequestCode() {
        //设置权限请求requestCode，只有不跟onRequestPermissionsResult方法中的其他请求码冲突即可。
        return 10000;
    }

    @Override
    public String[] getPermissions() {
        //设置该界面所需的全部权限
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
    }

    @Override
    public void requestPermissionsSuccess() {
        //权限请求用户已经全部允许
        initView();
    }

    @Override
    public void requestPermissionsFail() {
        //权限请求不被用户允许。可以提示并退出或者提示权限的用途并重新发起权限申请。
    }
    // Permission methods end

}
