package com.example.myviewgroupwaterfallsflow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myviewgroupwaterfallsflow.R;
import com.example.myviewgroupwaterfallsflow.view.MyViewGroupWaterfallsFlow;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.my_view_group_waterfalls_flow)
    MyViewGroupWaterfallsFlow myViewGroupWaterfallsFlow;
    @BindView(R.id.btn_add)
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        addView(myViewGroupWaterfallsFlow);
    }

    private static int IMG_COUNT = 5;

    private void addView(MyViewGroupWaterfallsFlow myViewGroupWaterfallsFlow) {
        Random random = new Random();
        Integer num = Math.abs(random.nextInt());
        MyViewGroupWaterfallsFlow.LayoutParams layoutParams = new MyViewGroupWaterfallsFlow.LayoutParams(MyViewGroupWaterfallsFlow.LayoutParams.MATCH_PARENT, MyViewGroupWaterfallsFlow.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(this);
        if (num % IMG_COUNT == 0) {
            imageView.setImageResource(R.mipmap.pic_1);
        } else if (num % IMG_COUNT == 1) {
            imageView.setImageResource(R.mipmap.pic_2);
        } else if (num % IMG_COUNT == 2) {
            imageView.setImageResource(R.mipmap.pic_3);
        } else if (num % IMG_COUNT == 3) {
            imageView.setImageResource(R.mipmap.pic_4);
        } else if (num % IMG_COUNT == 4) {
            imageView.setImageResource(R.mipmap.pic_5);
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        myViewGroupWaterfallsFlow.addView(imageView, layoutParams);
        myViewGroupWaterfallsFlow.setOnItemClickListener(new MyViewGroupWaterfallsFlow.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                Toast.makeText(MainActivity.this, "item=" + index, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
