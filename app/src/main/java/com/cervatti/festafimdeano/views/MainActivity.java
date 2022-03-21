package com.cervatti.festafimdeano.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cervatti.festafimdeano.R;
import com.cervatti.festafimdeano.constant.FimDeAnoConstants;
import com.cervatti.festafimdeano.data.SecurityPreferences;

import java.util.Calendar;
@RequiresApi(api = Build.VERSION_CODES.N)

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewHolder mViewHolder = new ViewHolder();
    private SecurityPreferences mSecurityPreferences;
    private static SimpleDateFormat SIMPLE_DATE_FORMATE = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSecurityPreferences = new SecurityPreferences(this);

        this.mViewHolder.textDayLeft = findViewById(R.id.days_left);
        this.mViewHolder.textToday = findViewById(R.id.text_today);
        this.mViewHolder.buttonConfirm = findViewById(R.id.button_confirm);

        this.mViewHolder.buttonConfirm.setOnClickListener(this);

        this.mViewHolder.textToday.setText(SIMPLE_DATE_FORMATE.format(Calendar.getInstance().getTime()));
        String daysleft = String.format("%s %s", String.valueOf(this.getDayLeft()), getString(R.string.dias));
        this.mViewHolder.textDayLeft.setText(daysleft);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.verifyPresence();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_confirm){

            String presence = this.mSecurityPreferences.getStoredString(FimDeAnoConstants.PRESENCE_KEY);

            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra(FimDeAnoConstants.PRESENCE_KEY, presence);
            startActivity(intent);
        }
    }

    public void verifyPresence(){
        String presence = this.mSecurityPreferences.getStoredString(FimDeAnoConstants.PRESENCE_KEY);
        if(presence.equals("")){
            this.mViewHolder.buttonConfirm.setText(getString(R.string.nao_confirmado));
        }else if (presence.equals(FimDeAnoConstants.CONFIRMATION_YES)){
            this.mViewHolder.buttonConfirm.setText(getString(R.string.sim));
        }else if(presence.equals(FimDeAnoConstants.CONFIRMATION_NO) ){
            this.mViewHolder.buttonConfirm.setText(getString(R.string.nao));
        }
    }

    private int getDayLeft(){
        Calendar calendarToday = Calendar.getInstance();
        int today = calendarToday.get(Calendar.DAY_OF_YEAR);

        Calendar lastDay = Calendar.getInstance();
        int dayMax = calendarToday.getLeastMaximum(Calendar.DAY_OF_YEAR);

        return dayMax - today;
    }

    private static class ViewHolder{
        TextView textToday;
        TextView textDayLeft;
        Button buttonConfirm;
    }

}