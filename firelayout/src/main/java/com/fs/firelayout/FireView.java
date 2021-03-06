package com.fs.firelayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fs.firelayout.utils.FireViewUtils;

import java.util.HashMap;

/**
 * Created by Francesco on 22/01/17.
 */

public abstract class FireView {
    protected HashMap<String, Object> attributesMap;
    View view;

    public void init(Context context, HashMap<String, Object> map) {
        this.attributesMap = map;
        view = generateView(context);

        resolveLayoutParams();

        checkTag();
        checkPadding();

        String color = FireViewUtils.getValue(attributesMap.get("background"), null);
        if (color != null)
            view.setBackgroundColor(Color.parseColor(color));

        view.setEnabled(FireViewUtils.getValue(attributesMap.get("enable"), true));

        view.setVisibility(getVisibility());
    }

    private void checkTag() {
        String tag = FireViewUtils.getValue(attributesMap.get("tag"), "");

        if (!TextUtils.isEmpty(tag))
            view.setTag(tag);
    }

    private void checkPadding() {
        if (attributesMap.get("padding") != null) {
            int padding = FireViewUtils.getValue(attributesMap.get("padding"), 0).intValue();
            view.setPadding(padding, padding, padding, padding);
        } else
            view.setPadding(FireViewUtils.getValue(attributesMap.get("padding_left"), 0).intValue(), FireViewUtils.getValue(attributesMap.get("padding_top"), 0).intValue(), FireViewUtils.getValue(attributesMap.get("padding_right"), 0).intValue(), FireViewUtils.getValue(attributesMap.get("padding_bottom"), 0).intValue());
    }

    private void resolveLayoutParams() {
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(getSize(attributesMap.get("layout_width")), getSize(attributesMap.get("layout_height")));

        if (attributesMap.get("margin") != null) {
            int margin = FireViewUtils.getValue(attributesMap.get("margin"), 0).intValue();
            marginLayoutParams.setMargins(margin, margin, margin, margin);
        } else
            marginLayoutParams.setMargins(FireViewUtils.getValue(attributesMap.get("margin_left"), 0).intValue(), FireViewUtils.getValue(attributesMap.get("margin_top"), 0).intValue(), FireViewUtils.getValue(attributesMap.get("margin_right"), 0).intValue(), FireViewUtils.getValue(attributesMap.get("margin_bottom"), 0).intValue());

        view.setLayoutParams(marginLayoutParams);
    }

    private int getSize(Object size) {
        if (size != null) {
            if (size instanceof Number)
                return ((Number) size).intValue();
            else if (size instanceof String) {
                switch ((String) size) {
                    case "wrap_content":
                        return ViewGroup.LayoutParams.WRAP_CONTENT;
                    case "match_parent":
                        return ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }
        }

        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    int getVisibility() {
        Object visibility = attributesMap.get("visibility");
        if (visibility != null && visibility instanceof String && !TextUtils.isEmpty((String) visibility)) {
            switch ((String) visibility) {
                case "gone":
                    return View.GONE;
                case "invisible":
                    return View.INVISIBLE;
            }
        }

        return View.VISIBLE;
    }

    //TODO: MORE KINDS OF GRAVITY
    protected int getGravity(String key) {
        Object gravity = attributesMap.get(key);
        if (gravity != null && gravity instanceof String && !TextUtils.isEmpty((String) gravity)) {
            switch ((String) gravity) {
                case "center":
                    return Gravity.CENTER;
                case "center_horizontal":
                    return Gravity.CENTER_HORIZONTAL;
                case "center_vertical":
                    return Gravity.CENTER_VERTICAL;
                case "top":
                    return Gravity.TOP;
                case "right":
                    return Gravity.RIGHT;
                case "bottom":
                    return Gravity.BOTTOM;
                case "left":
                    return Gravity.LEFT;
            }
        }

        return Gravity.NO_GRAVITY;
    }

    public void setEventsListener(final FireLayout.EventsListener listener) {
        if (FireViewUtils.getValue(attributesMap.get("onClick"), false))
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFireLayoutChildClicked(view);
                }
            });

        if (FireViewUtils.getValue(attributesMap.get("onLongClick"), false))
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onFireLayoutChildLongClicked(view);
                    return true;
                }
            });
    }

    public abstract View generateView(Context mContext);

    public View getView() {
        return view;
    }
}
