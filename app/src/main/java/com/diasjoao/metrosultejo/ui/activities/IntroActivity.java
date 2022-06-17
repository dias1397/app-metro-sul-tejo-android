package com.diasjoao.metrosultejo.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.diasjoao.metrosultejo.R;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Olá", getResources().getString(R.string.intro_1), R.drawable.intro_1, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntroFragment.newInstance("Próximo", getResources().getString(R.string.intro_2), R.drawable.intro_2, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntroFragment.newInstance("Horários", getResources().getString(R.string.intro_3), R.drawable.intro_3, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntroFragment.newInstance("Mapa", getResources().getString(R.string.intro_4), R.drawable.intro_4, getResources().getColor(R.color.colorPrimaryDark)));
        addSlide(AppIntroFragment.newInstance("Informação", getResources().getString(R.string.intro_5), R.drawable.intro_5, getResources().getColor(R.color.colorPrimaryDark)));

        showSkipButton(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, MenuActivityOld.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, MenuActivityOld.class));
        finish();
    }
}
