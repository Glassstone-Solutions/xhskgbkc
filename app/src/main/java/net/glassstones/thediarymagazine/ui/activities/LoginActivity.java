package net.glassstones.thediarymagazine.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.glassstones.thediarymagazine.Common;
import net.glassstones.thediarymagazine.R;
import net.glassstones.thediarymagazine.ui.widgets.CustomTextView;
import net.glassstones.thediarymagazine.utils.HelperSharedPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.topSwirl)
    ImageView topSwirl;
    @InjectView(R.id.bottomSwirl)
    ImageView bottomSwirl;
    @InjectView(R.id.divider)
    View divider;
    @InjectView(R.id.topText)
    LinearLayout topText;
    @InjectView(R.id.divider2)
    View divider2;
    @InjectView(R.id.bottomText)
    LinearLayout bottomText;
    @InjectView(R.id.exist)
    CustomTextView exist;
    @InjectView(R.id.skip)
    CustomTextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        Bitmap splash = BitmapFactory.decodeResource(getResources(), R.drawable.ic_top_swirl);
        Bitmap dash = BitmapFactory.decodeResource(getResources(), R.drawable.tdm_splash);

        colorize(splash, dash);

        logo.setScaleX(0f);
        logo.setScaleY(0f);
        topSwirl.setAlpha(0f);
        bottomSwirl.setAlpha(0f);

        topText.setTranslationY(-150);
        bottomText.setTranslationY(150);

        animate();

        skip.setOnClickListener(this);

    }

    private void colorize(Bitmap splash, Bitmap dash) {
        Palette.from(splash).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    LoginActivity.this.getWindow().setStatusBarColor(palette.getDarkVibrantColor(mutedColor));
                }
                divider.setBackgroundColor(palette.getDarkVibrantColor(mutedColor));
                exist.setTextColor(palette.getDarkVibrantColor(mutedColor));
            }
        });

        Palette.from(dash).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getLightMutedColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                divider2.setBackgroundColor(palette.getDarkVibrantColor(mutedColor));
            }
        });
    }

    private void animate() {
        // Icon Animation
        ObjectAnimator lScaleX = ObjectAnimator.ofFloat(logo, View.SCALE_X, 1f);
        ObjectAnimator lScaleY = ObjectAnimator.ofFloat(logo, View.SCALE_Y, 1f);

        AnimatorSet logoScaleAnim = new AnimatorSet();

        logoScaleAnim.playTogether(lScaleX, lScaleY);
        logoScaleAnim.setDuration(300);
        logoScaleAnim.setInterpolator(new LinearInterpolator());

        logoScaleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator topSwirlAnim = ObjectAnimator.ofFloat(topSwirl, View.ALPHA, 1f);
        topSwirlAnim.setDuration(200);
        ObjectAnimator bottomSwirlAnim = ObjectAnimator.ofFloat(bottomSwirl, View.ALPHA, 1f);
        bottomSwirlAnim.setDuration(200);

        AnimatorSet swirlAnim = new AnimatorSet();
        swirlAnim.playSequentially(topSwirlAnim, bottomSwirlAnim);

        ObjectAnimator topTextAnim = ObjectAnimator.ofFloat(topText, View.TRANSLATION_Y, 0);
        topTextAnim.setDuration(300);
        topTextAnim.setInterpolator(new LinearInterpolator());

        ObjectAnimator bottomTextAnim = ObjectAnimator.ofFloat(bottomText, View.TRANSLATION_Y, -20);
        bottomTextAnim.setDuration(300);

        bottomTextAnim.setInterpolator(new LinearInterpolator());

        AnimatorSet textAnim = new AnimatorSet();

        textAnim.playSequentially(topTextAnim, bottomTextAnim);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playSequentially(logoScaleAnim, swirlAnim, textAnim);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.skip){
            HelperSharedPreferences.putSharedPreferencesBoolean(this, Common.KEY_FIRST_RUN, false);
            startActivity(new Intent(LoginActivity.this, NewsFeedActivity.class));
            finish();
        }
    }
}
