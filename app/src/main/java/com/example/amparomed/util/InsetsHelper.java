package com.example.amparomed.util;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Edge-to-edge: soma a altura das barras do sistema ao padding original da view. */
public final class InsetsHelper {

    private InsetsHelper() { }

    private static final int TAG_TOPO = -0x54321;
    private static final int TAG_BASE = -0x54322;

    public static void aplicarTopo(final View v) {
        final Object guardado = v.getTag(TAG_TOPO);
        final int base = (guardado != null) ? (int) guardado : v.getPaddingTop();
        v.setTag(TAG_TOPO, base);
        ViewCompat.setOnApplyWindowInsetsListener(v, (view, wi) -> {
            Insets b = wi.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), base + b.top, view.getPaddingRight(), view.getPaddingBottom());
            return wi;
        });
        ViewCompat.requestApplyInsets(v);
    }

    public static void aplicarBase(final View v) {
        final Object guardado = v.getTag(TAG_BASE);
        final int base = (guardado != null) ? (int) guardado : v.getPaddingBottom();
        v.setTag(TAG_BASE, base);
        ViewCompat.setOnApplyWindowInsetsListener(v, (view, wi) -> {
            Insets b = wi.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), base + b.bottom);
            return wi;
        });
        ViewCompat.requestApplyInsets(v);
    }
}
