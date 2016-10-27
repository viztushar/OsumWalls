package com.viztushar.osumwalls.tasks;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;

import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.adapter.WallAdapter;


/**
 * Created by architjn on 26/06/15.
 */
public class ColorGridTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Bitmap art;
    WallAdapter.MyViewHolder holder;
    private ValueAnimator colorAnimation;

    public ColorGridTask(Context context, Bitmap art, WallAdapter.MyViewHolder holder) {
        this.context = context;
        this.art = art;
        this.holder = holder;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Palette.generateAsync(art,
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(final Palette palette) {
                        Integer colorFrom = context.getResources().getColor(android.R.color.white);
                        Integer colorTo = palette.getVibrantColor(context.getResources().getColor(R.color.card_grey_dark_theme));
                        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                holder.realBackground.setBackgroundColor((Integer) animator.getAnimatedValue());
                            }

                        });
                        colorAnimation.start();
                    }
                });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
