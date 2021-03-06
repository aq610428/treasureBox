package com.treasure.million.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.treasure.million.R;


public class EmptyDataView extends RelativeLayout {
    public TextView textView, text_refresh;


    public EmptyDataView(Context context) {
        super(context);
        init();
    }

    public EmptyDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.empty_data_view, this);
        textView = view.findViewById(R.id.text_name);
    }

    public void setData(String data) {
        textView.setText(data + "");
    }
}
