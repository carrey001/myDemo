package com.carrey.mydemo.ruler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carrey.common.view.RulerView;
import com.carrey.mydemo.BaseAct;
import com.carrey.mydemo.R;

/**
 * 类描述：
 * 创建人：carrey
 * 创建时间：2015/12/25 11:39
 */

public class RulerAct extends BaseAct {
    private RulerView rulerView;

    private TextView input, result, btn;


    @Override
    protected void initViews() {
        setContentView(R.layout.act_ruler);
        rulerView = (RulerView) findViewById(R.id.ruler);

        input = (TextView) findViewById(R.id.input);
        result = (TextView) findViewById(R.id.result);

        btn = (Button) findViewById(R.id.btn_input);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulerView.setScale(Float.parseFloat(input.getText().toString()));
            }
        });


        rulerView.setmOnScaleChangeListener(new RulerView.OnScaleChangeListener() {
            @Override
            public void OnScaleChange(int scale) {
                result.setText(scale+"");
//                Toast.makeText(RulerAct.this,"onscalechange_int"+scale,0).show();
            }

            @Override
            public void OnScaleChange(float scale) {
//                Toast.makeText(RulerAct.this,"OnScaleChange_float"+scale,0).show();
            }
        });
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
