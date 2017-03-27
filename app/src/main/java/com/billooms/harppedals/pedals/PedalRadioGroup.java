package com.billooms.harppedals.pedals;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.billooms.harppedals.R;

/**
 * Based on:
 * http://stackoverflow.com/questions/10289371/is-there-a-ui-widget-that-allows-the-user-to-select-between-two-options-left-or
 * https://github.com/vinc3m1/android-segmentedradiobutton
 */
public class PedalRadioGroup extends RadioGroup {

	private RadioButton[] buttons;

	public PedalRadioGroup(Context context) {
		super(context);
	}

	public PedalRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		changeButtonsImages();
	}

	private void changeButtonsImages() {
		// Use this font because it contains the NATURAL unicode symbol.
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/FreeSansBold.ttf");

		final int count = super.getChildCount();
		buttons = new RadioButton[super.getChildCount()];
		for (int i = 0; i < count ; i++) {
			buttons[i] = (RadioButton) super.getChildAt(i);
		}
		// The order of buttons is 0:top 1:middle 2:bottom

		// This assumes count > 1
		if (count > 1) {
			for (int i = 0; i < count ; i++) {
				buttons[i] = (RadioButton) super.getChildAt(i);
				buttons[i].setTypeface(font);
			}
			buttons[0].setBackgroundResource(R.drawable.segment_pedal_top);
			for (int i = 1; i < count - 1; i++) {
				buttons[i].setBackgroundResource(R.drawable.segment_pedal_middle);
			}
			buttons[count - 1].setBackgroundResource(R.drawable.segment_pedal_bottom);
		} else {
			// simple middle button in case count=1
			buttons[0].setBackgroundResource(R.drawable.pedal_white_middle);
		}
	}

	/**
	 * Get the position of the PedalRadioGroup.
	 *
	 * @return 0 (bottom sharp); 1 (middle natural); 2 (top flat)
	 */
	public int getPosition() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isChecked()) {
				return buttons.length - 1 - i;		// reverse the order so that 0 is bottom
			}
		}
		return 1;		// should never happen with a RadioGroup
	}

	/**
	 * Set the RadioButtons in this group for the given number.
	 *
	 * @param pos 0 (bottom sharp); 1 (middle natural); 2 (top flat)
	 */
	public void setPosition(int pos) {
		if (pos < 0 || pos > 2) {
			return;			// should not happen
		}
//		Log.d(TAG, "  PedalRadioGroup.setPosition:" + getPosition() + "->" + pos);
		buttons[buttons.length - pos - 1].setChecked(true);		// reverse the order so that 0 is bottom
	}
}