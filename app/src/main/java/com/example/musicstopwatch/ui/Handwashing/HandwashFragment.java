package com.example.musicstopwatch.ui.Handwashing;

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

public class HandwashFragment extends Fragment {

    private CountDownTimer timer;
    private long timerLengthSeconds = 0;
    private TimerState timerState = TimerState.Stopped;
    private long secondsRemaining = 0;
    private Button startButton, resetButton;
    private MaterialProgressBar progressCountdown;
    private TextView textView;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_hadwash, container, false);
        startButton = root.findViewById(R.id.startButton);
        startButton.setVisibility(View.VISIBLE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                timerState = TimerState.Running;
                updateButton();
            }
        });

        resetButton = root.findViewById(R.id.resetButton);
        resetButton.setVisibility(View.GONE);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                onTimerFinished();
            }
        });

        progressCountdown = root.findViewById(R.id.progress_countdown);
        textView = root.findViewById(R.id.textTime);
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

        secondsRemaining = timerState.equals(TimerState.Running) ?
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
        progressCountdown.setProgress(0);

        PrefUtil.setSecondsRemaining(timerLengthSeconds, getActivity());
        secondsRemaining = timerLengthSeconds;

        updateButton();
        updateCountdownUI();
    }

    public void startTimer(){

        timerState = TimerState.Running;

        timer = new CountDownTimer( secondsRemaining * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = millisUntilFinished / 1000;
                updateCountdownUI();
            }

            @Override
            public void onFinish() {
                onTimerFinished();
            }
        }.start();
    }

    private void setNewTimerLength(){
        double lengthInMinutes = PrefUtil.getTimerLength(getActivity(), true);
        timerLengthSeconds = (long) (lengthInMinutes * 60);
        progressCountdown.setMax(Math.toIntExact(timerLengthSeconds));
    }

    private void setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(getActivity());
        progressCountdown.setMax(Math.toIntExact(timerLengthSeconds));
    }

    private void updateCountdownUI(){
        String secondsStr = String.valueOf(secondsRemaining);
        textView.setText(secondsStr);
        progressCountdown.setProgress(Math.toIntExact((timerLengthSeconds - secondsRemaining)));
    }

    public void updateButton(){
        if(timerState.equals(TimerState.Running)){
            startButton.setVisibility(View.GONE);
            resetButton.setVisibility(View.VISIBLE);
        }else if(timerState.equals(TimerState.Stopped)){
            startButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.GONE);
        }
    }
}
