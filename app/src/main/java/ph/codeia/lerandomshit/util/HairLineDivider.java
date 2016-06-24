package ph.codeia.lerandomshit.util;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

/**
 * This file is a part of the Le Random Shit project.
 */
public class HairLineDivider extends RecyclerView.ItemDecoration {
    @Inject
    Logging log;

    private final Drawable divider;

    @Inject
    public HairLineDivider(Activity context) {
        TypedArray a = context.obtainStyledAttributes(new int[] {android.R.attr.listDivider});
        divider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int right = parent.getWidth();
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        for (int i = 0, n = parent.getChildCount(); i < n; i++) {
            View child = parent.getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                int top = lm.getDecoratedBottom(child);
                divider.setBounds(0, top, right, top + 1);
                divider.draw(c);
            }
        }
    }
}
