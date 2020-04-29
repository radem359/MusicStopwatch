package com.example.musicstopwatch.ui.Toothbrush;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicstopwatch.R;
import com.example.musicstopwatch.TimerState;
import com.example.musicstopwatch.util.PrefUtil;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ToothbrushFragment extends Fragment {

    private CountDownTimer timer1;
    private long timerLengthSeconds = 0;
    private TimerState timerState = TimerState.Stopped;
    private long secondsRemaining1 = 0;
    private Button startButton120, resetButton120;
    private MaterialProgressBar progressCountdown120;
    private TextView textView120;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_toothbrush, container, false);
        startButton120 = root.findViewById(R.id.startButton120);
        startButton120.setVisibility(View.VISIBLE);
        startButton120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                timerState = TimerState.Running;
                updateButton();
            }
        });

        resetButton120 = root.findViewById(R.id.resetButton120);
        resetButton120.setVisibility(View.GONE);
        resetButton120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer1.cancel();
                onTimerFinished();
            }
        });

        progressCountdown120 = root.findViewById(R.id.progress_countdown_120);
        textView120 = root.findViewById(R.id.textTime120);
        initTimer();
        return root;
    }

    public void initTimer(){

        timerState = PrefUtil.getTimerState(getActivity());
        if(timerState.equals(TimerState.Stopped)){
            setNewTimerLength();
        }else{
            setPreviousTimerLength();
        }

        secondsRemaining1 = timerState.equals(TimerState.Running) ?
                PrefUtil.getSecondsRemaining(getActivity()) : timerLengthSeconds;

        //TODO: change secondsRemaining according to where the background timer stopped

        // resume where we left off
        if(timerState.equals(TimerState.Running))
            startTimer();

        updateButton();
        updateCountdownUI();
    }

    public void onTimerFinished(){
        timerState = TimerState.Stopped;

        setNewTimerLength();
        progressCountdown120.setProgress(0);

        PrefUtil.setSecondsRemaining(timerLengthSeconds, getActivity());
        secondsRemaining1 = timerLengthSeconds;

        updateButton();
        updateCountdownUI();
    }

    public void startTimer(){

        timerState = TimerState.Running;

        timer1 = new CountDownTimer( secondsRemaining1 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining1 = millisUntilFinished / 1000;
                updateCountdownUI();
            }

            @Override
            public void onFinish() {
                onTimerFinished();
            }
        }.start();
    }

    private void setNewTimerLength(){
        double lengthInMinutes = PrefUtil.getTimerLength(getActivity(), false);
        timerLengthSeconds = (long) (lengthInMinutes * 60);
        progressCountdown120.setMax(Math.toIntExact(timerLengthSeconds));
    }

    private void setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(getActivity());
        progressCountdown120.setMax(Math.toIntExact(timerLengthSeconds));
    }

    private void updateCountdownUI(){
        String secondsStr = String.valueOf(secondsRemaining1);
        textView120.setText(secondsStr);
        progressCountdown120.setProgress(Math.toIntExact((timerLengthSeconds - secondsRemaining1)));
    }

    public void updateButton(){
        if(timerState.equals(TimerState.Running)){
            startButton120.setVisibility(View.GONE);
            resetButton120.setVisibility(View.VISIBLE);
        }else if(timerState.equals(TimerState.Stopped)){
            startButton120.setVisibility(View.VISIBLE);
            resetButton120.setVisibility(View.GONE);
        }
    }
}
