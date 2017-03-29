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
 * @author Bill Ooms. Copyright 2017 Studio of Bill Ooms. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
