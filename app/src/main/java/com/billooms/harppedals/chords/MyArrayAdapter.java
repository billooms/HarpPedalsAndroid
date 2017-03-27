package com.billooms.harppedals.chords;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * ArrayAdapter for sharp/flat spinner.
 *
 * @author Bill Ooms. Copyright 2016 Studio of Bill Ooms. All rights reserved.
 */

public class MyArrayAdapter extends ArrayAdapter {

	/** Use this font because it contains the NATURAL unicode symbol. */
	private final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/FreeSansBold.ttf");

	public MyArrayAdapter(Context context, int resource, List objects) {
		super(context, resource, objects);
	}
	// Affects default (closed) state of the spinner
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
		view.setTypeface(font);
		return view;
	}

	// Affects opened state of the spinner
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getDropDownView(position, convertView, parent);
		view.setTypeface(font);
		return view;
	}
}
