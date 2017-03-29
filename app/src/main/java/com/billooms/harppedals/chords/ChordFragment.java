package com.billooms.harppedals.chords;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.billooms.harppedals.R;
import com.billooms.harppedals.notes.BasicNote;
import com.billooms.harppedals.notes.Note;
import com.billooms.harppedals.notes.NotePlayer;
import com.billooms.harppedals.notes.SharpFlat;

import java.util.ArrayList;
import java.util.List;

/**
 * The Fragment for controlling chords.
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
public class ChordFragment extends Fragment {
	
	/** Keys for savedInstanceState bundle. */
	public final static String ARG_CHORD = "chord";
	public final static String ARG_CHORDADD = "chordAdd";

	/** Pointer to the PedalChangeListener. */
	private OnChordChangeListener chordChangeListener;

	/** Pointers to all of the controls that are displayed. */
	private Spinner rootSpinner, sharpFlatSpinner, triadSpinner;
	private TextView chordNameText, notesText;
	private CheckBox add2Button, add4Button, sus4Button, add6Button;
	
	/** NotePlayer for playing notes. */
	private NotePlayer player;
	
	/** Chord being edited. */
	private Chord chord;
	/** Root note of chord. */
	private Note root;

	/**
	 * Required empty public constructor.
	 */
	public ChordFragment() {}

	/**
	 * Container Activity must implement this interface.
	 */
	public interface OnChordChangeListener {
		/**
		 * Change the pedals based on the given pitchMask and save the given chord information.
		 *
		 * @param pitchMask Pitch mask
		 * @param rootNote First note to play when playing a glissando
		 * @param chordArray integer array representing position of spinners
		 * @param addArray boolean array representing status of check boxes
		 */
		void onChordChange(int pitchMask, Note rootNote, int[] chordArray, boolean[] addArray);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.d(TAG, "ChordFragment.onCreate");
		super.onCreate(savedInstanceState);

		chord = new Chord();
		player = new NotePlayer(this.getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.d(TAG, "ChordFragment.onCreateView");
		View view = inflater.inflate(R.layout.fragment_chord, container, false);

		rootSpinner = (Spinner) view.findViewById(R.id.rootSpinner);
		List<String> rootList = new ArrayList<>();
		for (BasicNote n : BasicNote.values()) {
			rootList.add(n.name());
		}
		ArrayAdapter rootAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, rootList);
		rootAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rootSpinner.setAdapter(rootAdapter);
		rootSpinner.setSelection(BasicNote.valueOf("C").ordinal());
		rootSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateForm();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		sharpFlatSpinner = (Spinner) view.findViewById(R.id.sharpFlatSpinner);
		List<String> sfList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {   // only use the first 3 or you'll get DOUBLESHARP
			sfList.add(SharpFlat.values()[i].getSuffix());
		}
		MyArrayAdapter sfAdapter = new MyArrayAdapter(this.getContext(), R.layout.spinner_item, sfList);
		sfAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sharpFlatSpinner.setAdapter(sfAdapter);
		sharpFlatSpinner.setSelection(SharpFlat.valueOf("NATURAL").ordinal());
		sharpFlatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateForm();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		triadSpinner = (Spinner) view.findViewById(R.id.triadSpinner);
		List<String> triadList = new ArrayList<>();
		for (Triad t : Triad.values()) {
			triadList.add(t.getName());
		}
		for (Seventh s : Seventh.values()) {
			triadList.add(s.getName());
		}
		for (Ninth n : Ninth.values()) {
			triadList.add(n.getName());
		}
		ArrayAdapter triadAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, triadList);
		triadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		triadSpinner.setAdapter(triadAdapter);
		triadSpinner.setSelection(Triad.valueOf("MAJOR").ordinal());
		triadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateForm();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		chordNameText = (TextView) view.findViewById(R.id.chordNameText);
		notesText = (TextView) view.findViewById(R.id.notesText);

		add2Button = (CheckBox) view.findViewById(R.id.add2Button);
		add2Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateForm();
			}
		});

		add4Button = (CheckBox) view.findViewById(R.id.add4Button);
		add4Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateForm();
			}
		});

		sus4Button = (CheckBox) view.findViewById(R.id.sus4Button);
		sus4Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateForm();
			}
		});

		add6Button = (CheckBox) view.findViewById(R.id.add6Button);
		add6Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateForm();
			}
		});
		
		Button playButton = (Button) view.findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.play2(chord.getChordMask(), root.getNumber());
			}
		});
		
		Button setChordButton = (Button) view.findViewById(R.id.setChordButton);
		setChordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chordChangeListener.onChordChange(chord.getPitchMask(getRootNote()), getRootNote(), getChordArray(), getChordAddArray());
			}
		});
		
		restoreInstanceState(savedInstanceState);
		
		return view;
	}

	@Override
	public void onStart() {
//		Log.d(TAG, "ChordFragment.onStart");
		super.onStart();
		
		// During startup, check if there are arguments passed to the fragment.
		// Note: arguments will over-ride any savedInstanceState that was given to onCreateView
		restoreInstanceState(getArguments());
	}

	@Override
	public void onResume() {
//		Log.d(TAG, "ChordFragment.onResume");
		super.onResume();
		
		updateForm();
	}
	
//	@Override
//	public void onPause() {
//		super.onPause();
//	}

	@Override
	public void onDestroy() {
//		Log.d(TAG, "ChordFragment.onDestroy");
		super.onDestroy();
//		player.release();
		player = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
//		Log.d(TAG, "ChordFragment.onSaveInstanceState");
		super.onSaveInstanceState(outState);
		if (outState == null) {
			return;
		}
		
		outState.putIntArray(ARG_CHORD, getChordArray());
		outState.putBooleanArray(ARG_CHORDADD, getChordAddArray());
		// must update any arguments sent to this fragment as well
		Bundle args = getArguments();
		if (args != null) {
			args.putIntArray(ARG_CHORD, getChordArray());
			args.putBooleanArray(ARG_CHORDADD, getChordAddArray());
		}
	}
	
	/**
	 * Get an array of integers representing the position of the spinners.
	 * This is useful for saving and restoring the state of the ChordFragment.
	 *
	 * @return array of integers representing the position of the spinners
	 */
	private int[] getChordArray() {
		return new int[]{rootSpinner.getSelectedItemPosition(), sharpFlatSpinner.getSelectedItemPosition(), triadSpinner.getSelectedItemPosition()};
	}
	
	/**
	 * Get a boolean array representing the position of the check boxes.
	 * This is useful for saving and restoring the state of the ChordFragment.
	 *
	 * @return boolean array representing the position of the check boxes
	 */
	private boolean[] getChordAddArray() {
		return new boolean[]{add2Button.isChecked(), add4Button.isChecked(), sus4Button.isChecked(), add6Button.isChecked()};
	}
	
	/**
	 * Restore the state of this fragment from the given savedInstanceState.
	 *
	 * @param bundle savedInstanceState
	 */
	private void restoreInstanceState(Bundle bundle) {
		if (bundle == null) {
			return;
		}
		
		int[] chordArray = bundle.getIntArray(ARG_CHORD);
		if ((chordArray != null) && (chordArray.length == 3)) {
			rootSpinner.setSelection(chordArray[0]);
			sharpFlatSpinner.setSelection(chordArray[1]);
			triadSpinner.setSelection(chordArray[2]);
		}
		boolean[] chordAddArray = bundle.getBooleanArray(ARG_CHORDADD);
		if ((chordAddArray != null) && (chordAddArray.length == 4)) {
			add2Button.setChecked(chordAddArray[0]);
			add4Button.setChecked(chordAddArray[1]);
			sus4Button.setChecked(chordAddArray[2]);
			add6Button.setChecked(chordAddArray[3]);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnChordChangeListener) {
			chordChangeListener = (OnChordChangeListener) context;
		} else {
			throw new RuntimeException(context.toString() + " must implement OnChordChangeListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		chordChangeListener = null;
	}

	private void updateForm() {
		root = new Note(BasicNote.values()[rootSpinner.getSelectedItemPosition()], SharpFlat.values()[sharpFlatSpinner.getSelectedItemPosition()]);
		String c = root.toString2();
		int idx = triadSpinner.getSelectedItemPosition();
		if (idx < Triad.SIZE) {   // for Triads
			c = c + Triad.values()[idx].getAbbreviation2();
			chord = new Chord(Triad.values()[idx]);
		} else if (idx < (Triad.SIZE + Seventh.SIZE)) {   // for Sevenths
			c = c + Seventh.values()[idx - Triad.SIZE].getAbbreviation();
			chord = new Chord(Seventh.values()[idx - Triad.SIZE]);
		} else {    // for Ninths
			c = c + Ninth.values()[idx - (Triad.SIZE + Seventh.SIZE)].getAbbreviation();
			chord = new Chord(Ninth.values()[idx - (Triad.SIZE + Seventh.SIZE)]);
		}
		if (add2Button.isChecked()) {      // add2 if needed
			chord.addInterval(Interval.MAJ2);
		}
		if (add4Button.isChecked()) {      // add4 if needed
			chord.addInterval(Interval.P4);
		}
		if (sus4Button.isChecked()) {      // add sus4 if needed
			chord.addInterval(Interval.P4);
			chord.deleteInterval(Interval.MAJ3);  // delete any 3rd in the chord
			chord.deleteInterval(Interval.MIN3);
		}
		if (add6Button.isChecked()) {      // add 6th if needed
			chord.addInterval(Interval.MAJ6);
		}
		chordNameText.setText(c);
		String notes = "";
		for (Note n : chord.getNotes(root)) {   // notes in the chord
			notes = notes + n.toString2() + " ";
		}
		notesText.setText(notes);
	}
	
	/**
	 * Get the root note of the chord.
	 *
	 * @return root note
	 */
	private Note getRootNote() {
		return new Note(BasicNote.values()[rootSpinner.getSelectedItemPosition()],
				SharpFlat.values()[sharpFlatSpinner.getSelectedItemPosition()]);
	}
}
