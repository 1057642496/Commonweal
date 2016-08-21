package com.goldenratio.commonweal.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.goldenratio.commonweal.MyApplication;
import com.goldenratio.commonweal.R;
import com.goldenratio.commonweal.bean.Bid;
import com.goldenratio.commonweal.bean.Deposit;
import com.goldenratio.commonweal.bean.Good;
import com.goldenratio.commonweal.widget.PopEnterPassword;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.iwgang.countdownview.CountdownView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class GoodDetailActivity extends Activity implements View.OnClickListener
        , BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private CountdownView mCountdownView, mCountdownFive;
    private Long endTime;
    private Good mGood;
    private TextView mTvGoodName, mTvGoodDescription, mTvUserName,
            mTvBid, mTvDeposit, mTvNowCoin, mTvStartCoin, mTvStartTime, mTvLastTime, mTvLoading;
    private LinearLayout mLlCv;
    private int picSize;
    private GoodDetailActivity mContext;
    private String mUserId;
    private Double deposit;
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        initView();
        initData();
        initSliderLayout();
        mUserId = ((MyApplication) getApplication()).getObjectID();
    }

    private void initSliderLayout() {
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, String> urlMaps = new HashMap<>();
        Log.d(TAG, "initView: " + mGood.getGood_Photos().size());
        List picList = mGood.getGood_Photos();
        for (int i = 0; i < picList.size(); i++) {
            urlMaps.put(i + "", picList.get(i).toString());
        }

        for (String name : urlMaps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(urlMaps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isDeposit();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mContext = GoodDetailActivity.this;
        mTvGoodName = (TextView) findViewById(R.id.tv_good_name);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mCountdownView = (CountdownView) findViewById(R.id.cv_endtime);
        mCountdownFive = (CountdownView) findViewById(R.id.cv_five);
        mTvGoodDescription = (TextView) findViewById(R.id.tv_good_description);
        mTvBid = (TextView) findViewById(R.id.tv_bid);
        mLlCv = (LinearLayout) findViewById(R.id.ll_good_detail_cv);
        mTvStartCoin = (TextView) findViewById(R.id.tv_start_coin);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);
        mTvLastTime = (TextView) findViewById(R.id.tv_last_time);
        mTvLoading = (TextView) findViewById(R.id.tv_loading);
        mTvNowCoin = (TextView) findViewById(R.id.tv_now_coin);
        mTvDeposit = (TextView) findViewById(R.id.tv_deposit);
        mTvBid.setOnClickListener(this);
        mTvDeposit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bid:
                mTvBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View root = View.inflate(mContext, R.layout.dialog_good_bid, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setView(root);
                        final Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        TextView mDialogTvBid = (TextView) root.findViewById(R.id.tv_bid);
                        mDialogTvBid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText mEtCoin = (EditText) root.findViewById(R.id.et_coin);
                                String mStrCoin = mEtCoin.getText().toString();
                                if (mStrCoin.equals("")) {
                                    Toast.makeText(mContext, "请输入出价公益币", Toast.LENGTH_SHORT).show();
                                } else if (Double.valueOf(mStrCoin) <= Double.valueOf(mGood.getGood_NowCoin())) {
                                    Toast.makeText(mContext, "请输入大于当前公益币", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    queryCoinAndSixPwd(1, mEtCoin.getText().toString());
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.tv_deposit:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setMessage("保证金为物品起步价的30%。\n" + "本物品起步价：" + mGood.getGood_StartCoin() + "公益币\n保证金：" +
                        deposit + "公益币");
                builder.setPositiveButton("交保证金", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //从Mysql查询用户当前公益币和密码
                        queryCoinAndSixPwd(0, "");
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
        }
    }

    /**
     * 初始化数据
     * 从intent获取传过来的数据
     * 设置相应控件的数据显示
     */

    private void initData() {
        Intent intent = getIntent();
        endTime = intent.getLongExtra("EndTime", 0);
        mGood = (Good) intent.getSerializableExtra("Bmob_Good");
        Log.d("lxc", "initData: ----> " + mGood.getObjectId() + "endtime-->" + endTime);
        mCountdownView.start(endTime);

        mTvGoodName.setText(mGood.getGood_Name());
        mTvGoodDescription.setText(mGood.getGood_Description());
        mTvStartCoin.setText(mGood.getGood_StartCoin());
        mTvNowCoin.setText(mGood.getGood_NowCoin());
        picSize = mGood.getGood_Photos().size();
//        if (picSize == 1) {
//            Glide.with(this).load(mGood.getGood_Photos().get(0)).into(mIvOnePic);
//        } else {
//            mGvPic.setAdapter(new mAdapter());
//        }
        //保证金为公益币的30%
        deposit = (Double.valueOf(mGood.getGood_StartCoin()) * 0.3);

        mTvStartTime.setText(mGood.getCreatedAt());
        mTvLastTime.setText(mGood.getUpdatedAt());
    }

    private void initIsGoodStatus() {
        //根据最后一位出价者字段判断是否有人出价
        if (endTime > 0) {
            getLastBidUpdatedAt();
        } else {
            changeTextViewVisibitity(2);
            mTvBid.setText("出价结束");
        }
    }

    private void getLastBidUpdatedAt() {
        BmobQuery<Bid> bidBmobQuery = new BmobQuery<>();
        bidBmobQuery.addQueryKeys("createdAt");
        bidBmobQuery.order("-createdAt");
        bidBmobQuery.addWhereEqualTo("Bid_Good", mGood.getObjectId());
        bidBmobQuery.findObjects(new FindListener<Bid>() {
            @Override
            public void done(List<Bid> list, BmobException e) {
                if (e == null) {
                    getBmobServerTime(list.get(0).getCreatedAt());
                } else {
                    changeTextViewVisibitity(2);
                }
            }
        });
    }

    private void getBmobServerTime(final String createdAt) {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    long createdTime = StringToLongAll(createdAt);
                    long nowTime = aLong * 1000L;
                    //30000： 30秒内所有不能再次出价。降低服务器并发及最后一位出价者的不确定性。
                    long leftTime = 30000 - (nowTime - createdTime);
                    if (leftTime < 0) {
                        changeTextViewVisibitity(2);
                        Log.d(TAG, "onSuccess: " + createdTime + "-->" + nowTime);
                        Log.d(TAG, "onSuccess: " + leftTime);
                        //30秒内无用户出价
                    } else {
                        changeTextViewVisibitity(0);
                        //30秒内有用户出价
                        mCountdownFive.start(leftTime);
                        mCountdownFive.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                //30秒倒计时结束，显示“出价”控件
                                changeTextViewVisibitity(2);
                            }
                        });
                    }
                } else {
                    Log.d("Kiuber_LOG", "fail: " + e.getMessage());
                    changeTextViewVisibitity(3);
                }
            }
        });
    }

    public long StringToLongAll(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long updateTime = 0;
        try {
            updateTime = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(updateTime);
        return updateTime;
    }

    public long StringToLongPart(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long updateTime = 0;
        try {
            updateTime = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(updateTime);
        return updateTime;
    }

    private void isDeposit() {
        BmobQuery<Deposit> depositBmobQuery = new BmobQuery<>();
        depositBmobQuery.addWhereEqualTo("D_User", mUserId);
        depositBmobQuery.include("D_User");
        depositBmobQuery.addWhereEqualTo("D_GoodId", mGood.getObjectId());
        depositBmobQuery.findObjects(new FindListener<Deposit>() {
            @Override
            public void done(List<Deposit> list, BmobException e) {
                if (e == null) {
                    int listSize = list.size();
                    if (listSize == 1) {
                        //支付保证金了
                        initIsGoodStatus();
                    } else if (listSize == 0) {
                        //未支付保证金
                        changeTextViewVisibitity(1);
//                        Toast.makeText(mContext, "未支付保证金", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "未知状态", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Kiuber_LOG", "done: " + mUserId + "~" + mGood.getObjectId());
                }
            }
        });
    }

    private void pay(final boolean alipayOrWechatPay, final double price) {

        BP.pay("公益币充值", "描述", price, alipayOrWechatPay, new PListener() {


            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(mContext, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                updateUserCoin(deposit + price);
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderI) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            mContext,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(mContext, "支付中断!" + reason, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void updateUserCoin(double sumCoin) {
        String url = "http://123.206.89.67/WebService1.asmx/UpdateUserCoinByObjectId";
        OkHttpClient okHttpClient = new OkHttpClient();
        final String mStrObjectId = ((MyApplication) getApplication()).getObjectID();
        if (mStrObjectId != null) {
            RequestBody body = new FormBody.Builder()
                    .add("ObjectId", mStrObjectId)
                    .add("UserCoin", sumCoin + "")
                    .build();
            Log.d("Kiuber_LOG", "updateUserCoin: " + mStrObjectId + "-->" + sumCoin);

            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    final String e1 = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GoodDetailActivity.this, e1, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showPayKeyBoard1(deposit.toString());
                        }
                    });
                }
            });
        }
    }


    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPayKeyBoard1(String userCoin, String userPwd, String coin) {
        PopEnterPassword popEnterPassword = new PopEnterPassword(this, userCoin, userPwd, "交保证金", coin, "保证金", mGood.getObjectId());
        // 显示窗口
        popEnterPassword.showAtLocation(this.findViewById(R.id.layoutContent),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void showPayKeyBoard2(String userPwd, String coin) {
        PopEnterPassword popEnterPassword = new PopEnterPassword(this, userPwd, "出价验证", coin, "出价", mGood.getObjectId(), 0.0);

        // 显示窗口
        popEnterPassword.showAtLocation(this.findViewById(R.id.layoutContent),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void showPayKeyBoard() {
        PopEnterPassword popEnterPassword = new PopEnterPassword(this, "设置六位数密码", "", "新密码");

        // 显示窗口
        popEnterPassword.showAtLocation(this.findViewById(R.id.layoutContent),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    private void queryCoinAndSixPwd(final int flag, final String bidCoin) {
        String root = "http://123.206.89.67/WebService1.asmx/";
        String method = "QueryUserCoinAndSixPwdByObjectId";
        String URL = root + method;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("ObjectId", mUserId)
                .build();
        Request req = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        Call call = client.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        try {
                            String coin, pwd;
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                coin = jsonArray.getJSONObject(i).getString("User_Coin");
                                pwd = jsonArray.getJSONObject(i).getString("User_SixPwd");
                                //用户公益币与保证金公益币的差值
                                /**
                                 * 0：支付保证金
                                 * 1：出价验证
                                 */
                                if (flag == 0) {
                                    final double poor = deposit - Double.valueOf(coin);
                                    final double sxf = poor * 0.05;
                                    if (poor < 0 || poor == 0) {
                                        //用户公益币够交保证金的
                                        if (Objects.equals(pwd, "0")) {
                                            //用户的六位数密码为空，第一次设置六位数密码
                                            Toast.makeText(GoodDetailActivity.this, "请先设置六位数密码", Toast.LENGTH_SHORT).show();
                                            showPayKeyBoard();
                                            //TODO 继续
                                        } else {
                                            showPayKeyBoard1(-poor + "", pwd, deposit.toString());
                                            Toast.makeText(GoodDetailActivity.this, "请输入支付密码" + pwd, Toast.LENGTH_SHORT).show();
                                            //用户已经设置6位数密码
                                        }
                                    } else {
                                        //用户当前公益币不足交保证金，提示充值
                                        AlertDialog.Builder builder = new AlertDialog.Builder(GoodDetailActivity.this);
                                        builder.setMessage("账号公益币：" + coin + "，还缺" + poor + "公益币，您将充值"
                                                + poor + "公益币（由于个人开发团队限制，" +
                                                "平台收取5%的手续费）");
                                        builder.setPositiveButton("去充值", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                pay(false, sxf + poor / 10);
                                            }
                                        });
                                        builder.setNegativeButton("取消", null);
                                        builder.show();
                                    }

                                } else if (flag == 1) {
                                    double poor = Double.valueOf(bidCoin) - Double.valueOf(coin);
                                    showPayKeyBoard2(pwd, bidCoin);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private void changeTextViewVisibitity(int flag) {
        switch (flag) {
            case 0:
                mTvLoading.setVisibility(View.GONE);
                mLlCv.setVisibility(View.VISIBLE);
                mTvDeposit.setVisibility(View.GONE);
                mTvBid.setVisibility(View.GONE);
                break;
            case 1:
                mTvLoading.setVisibility(View.GONE);
                mLlCv.setVisibility(View.GONE);
                mTvDeposit.setVisibility(View.VISIBLE);
                mTvBid.setVisibility(View.GONE);
                break;
            case 2:
                mTvLoading.setVisibility(View.GONE);
                mLlCv.setVisibility(View.GONE);
                mTvDeposit.setVisibility(View.GONE);
                mTvBid.setVisibility(View.VISIBLE);
                break;
            case 3:
                mTvLoading.setVisibility(View.GONE);
                mLlCv.setVisibility(View.GONE);
                mTvDeposit.setVisibility(View.GONE);
                mTvBid.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class mAdapter extends BaseAdapter {
        private DisplayMetrics displayMetrics;
        private Context mContext;
        int width, height;

        private mAdapter() {
            mContext = GoodDetailActivity.this;
            displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        }

        @Override
        public int getCount() {
            return picSize;
        }

        @Override
        public List getItem(int position) {
            return mGood.getGood_Photos();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(width / 3, width / 3));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            Glide.with(mContext).load(getItem(position).get(position)).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicPhotoShow.class);
                    intent.putExtra("index", position);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) mGood.getGood_Photos());
                    mContext.startActivity(intent);
                    //设置切换动画
                    ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            return imageView;
        }
    }
}
