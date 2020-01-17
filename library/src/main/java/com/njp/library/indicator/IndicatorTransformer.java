package com.njp.library.indicator;

import android.view.View;

@FunctionalInterface
public interface IndicatorTransformer {
    void transformIndicator(View indicator, float offset);
}
