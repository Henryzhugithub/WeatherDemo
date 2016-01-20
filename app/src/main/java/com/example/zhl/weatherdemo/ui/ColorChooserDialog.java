package com.example.zhl.weatherdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.zhl.weatherdemo.R;

/**
 * Created by zhl on 2016/1/19.
 */
public class ColorChooserDialog extends DialogFragment implements View.OnClickListener {

    private CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6,cardView7,cardView8,cardView9,cardView10;
    private Button buttonDisagree,buttonAgree;
    private View view;
    private SharedPreferences sf;
    private int current_theme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //保存目前的主题
        sf = getActivity().getSharedPreferences("theme", Context.MODE_PRIVATE);
        current_theme = sf.getInt("theme_value",1);

        //inflate theme_dialog.xml
        view = inflater.inflate(R.layout.theme_dialog,container);

        //remove title(already defined in theme_dialog.xml
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogButtons();

        return view;
    }

    private void dialogButtons(){
        cardView1 = (CardView) view.findViewById(R.id.card_view1);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);
        cardView3 = (CardView) view.findViewById(R.id.card_view3);
        cardView4 = (CardView) view.findViewById(R.id.card_view4);
        cardView5 = (CardView) view.findViewById(R.id.card_view5);
        cardView6 = (CardView) view.findViewById(R.id.card_view6);
        cardView7 = (CardView) view.findViewById(R.id.card_view7);
        cardView8 = (CardView) view.findViewById(R.id.card_view8);
        cardView9 = (CardView) view.findViewById(R.id.card_view8);
        cardView10 = (CardView) view.findViewById(R.id.card_view10);
        buttonDisagree = (Button)view.findViewById(R.id.buttonDisagree);
        buttonAgree = (Button) view.findViewById(R.id.buttonAgree);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
        cardView7.setOnClickListener(this);
        cardView8.setOnClickListener(this);
        cardView9.setOnClickListener(this);
        cardView10.setOnClickListener(this);
        buttonDisagree.setOnClickListener(this);
        buttonAgree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_view1:

                break;
            case R.id.card_view2:

                break;
            case R.id.card_view3:

                break;
            case R.id.card_view4:

                break;
            case R.id.card_view5:

                break;
            case R.id.card_view6:

                break;
            case R.id.card_view7:

                break;
            case R.id.card_view8:

                break;
            case R.id.card_view9:

                break;
            case R.id.card_view10:

                break;
            case R.id.buttonAgree:
                int select_theme = sf.getInt("theme_value",1);
                sf.edit().putInt("theme_value",select_theme);
                //getDialog().dismiss();
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.buttonDisagree:
                sf.edit().putInt("theme_value",current_theme);
                getDialog().dismiss();
                break;
        }
    }


}
