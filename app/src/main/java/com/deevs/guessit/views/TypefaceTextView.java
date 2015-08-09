package com.deevs.guessit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.deevs.guessit.R;

public class TypefaceTextView extends TextView {

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyFont(context, attrs);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyFont(context, attrs);
    }

    public TypefaceTextView(Context context) {
        super(context);
    }

    private void applyFont(final Context context, final AttributeSet attrs) {
        // Acquire the assets.font set in the XML declaration for this view.
        TypedArray typefaceStyle = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);

        // Extract the string value and create a typeface from assets using that as the path, since
        // fonts are stored in assets dir.
        final String fontFromXml = typefaceStyle.getString(R.styleable.TypefaceTextView_font);
        setTypeface(Typeface.createFromAsset(context.getAssets(), fontFromXml));
    }
}
