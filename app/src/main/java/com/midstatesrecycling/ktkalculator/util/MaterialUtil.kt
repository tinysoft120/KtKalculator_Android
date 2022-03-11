package com.midstatesrecycling.ktkalculator.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.midstatesrecycling.ktkalculator.R

object MaterialUtil {

    @JvmOverloads
    @JvmStatic
    fun setTint(
        button: MaterialButton, background: Boolean = true,
        color: Int = accentColor(button.context)
    ) {

        button.isAllCaps = false
        val context = button.context
        val colorState = ColorStateList.valueOf(color)
        val textColor =
            ColorStateList.valueOf(
                getPrimaryTextColor(
                    context,
                    ColorUtil.isColorLight(color)
                )
            )


        if (background) {
            button.backgroundTintList = colorState
            button.setTextColor(textColor)
            button.iconTint = textColor
        } else {
            button.setTextColor(colorState)
            button.iconTint = colorState
        }
    }

    @JvmOverloads
    @JvmStatic
    fun tintColor(
        button: MaterialButton,
        textColor: Int = Color.WHITE,
        backgroundColor: Int = Color.BLACK
    ) {
        val backgroundColorStateList = ColorStateList.valueOf(backgroundColor)
        val textColorColorStateList = ColorStateList.valueOf(textColor)
        button.backgroundTintList = backgroundColorStateList
        button.setTextColor(textColorColorStateList)
        button.iconTint = textColorColorStateList
    }

    @JvmOverloads
    @JvmStatic
    fun setTint(textInputLayout: TextInputLayout, background: Boolean = true) {
        val context = textInputLayout.context
        val accentColor = accentColor(context)
        val colorState = ColorStateList.valueOf(accentColor)

        if (background) {
            textInputLayout.backgroundTintList = colorState
            textInputLayout.defaultHintTextColor = colorState
        } else {
            textInputLayout.boxStrokeColor = accentColor
            textInputLayout.defaultHintTextColor = colorState
            textInputLayout.isHintAnimationEnabled = true
        }
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getPrimaryTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.primary_text_default_material_light)
        } else ContextCompat.getColor(context!!, R.color.primary_text_default_material_dark)
    }

    @CheckResult
    @ColorInt
    fun accentColor(context: Context): Int {
        val color = ContextCompat.getColor(context, R.color.blue_500)
        return if (isWindowBackgroundDark(context)) ColorUtil.desaturateColor(
            color,
            0.4f
        ) else color
    }

    private fun isWindowBackgroundDark(context: Context): Boolean {
        return !ColorUtil.isColorLight(resolveColor(context, android.R.attr.windowBackground))
    }

    @JvmOverloads
    fun resolveColor(context: Context, @AttrRes attr: Int, fallback: Int = 0): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColor(0, fallback)
        } finally {
            a.recycle()
        }
    }

    @JvmStatic
    fun createTintedDrawable(context: Context, @DrawableRes res: Int, @ColorInt color: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(context, res)
        return createTintedDrawable(drawable, color)
    }

    // This returns a NEW Drawable because of the mutate() call. The mutate() call is necessary because Drawables with the same resource have shared states otherwise.
    @JvmStatic
    fun createTintedDrawable(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        if (drawable == null) {
            return null
        }
        val result = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintMode(result, PorterDuff.Mode.SRC_IN)
        DrawableCompat.setTint(result, color)
        return result
    }

    // This returns a NEW Drawable because of the mutate() call. The mutate() call is necessary because Drawables with the same resource have shared states otherwise.
    @JvmStatic
    fun createTintedDrawable(drawable: Drawable?, sl: ColorStateList): Drawable? {
        if (drawable == null) {
            return null
        }
        val temp = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTintList(temp, sl)
        return temp
    }
}
