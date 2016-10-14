package com.mcbeengs.imagerdon.adapter;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mcbeengs.imagerdon.R;

import java.util.List;

/**
 * Created by McBeengs on 11/10/2016.
 */

public class AppBarBehavior extends AppBarLayout.Behavior {

    private Context mContext;

    public AppBarBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        Toast.makeText(mContext, "yes", Toast.LENGTH_SHORT).show();
        return true;
    }
}
