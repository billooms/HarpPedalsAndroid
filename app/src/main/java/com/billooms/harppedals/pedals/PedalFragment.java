package com.billooms.harppedals.pedals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.billooms.harppedals.R;
import com.billooms.harppedals.notes.BasicNote;
import com.billooms.harppedals.notes.Note;
import com.billooms.harppedals.notes.NotePlayer;
import com.billooms.harppedals.notes.SharpFlat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static com.billooms.harppedals.notes.BasicNote.A;
import static com.billooms.harppedals.notes.BasicNote.B;
import static com.billooms.harppedals.notes.BasicNote.C;
import static com.billooms.harppedals.notes.BasicNote.D;
import static com.billooms.harppedals.notes.BasicNote.E;
import static com.billooms.harppedals.notes.BasicNote.F;
import static com.billooms.harppedals.notes.BasicNote.G;
import static com.billooms.harppedals.notes.SharpFlat.NATURAL;

/**
 * The Fragment that simulates harp pedals.
 * Note that this fragment is always retained (not destroyed on orientation changes).
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
public class PedalFragment extends Fragment implements PropertyChangeListener, RadioGroup.OnCheckedChangeListener {
	
	/** Keys for savedInstanceState bundle. */
	private final static String ARG_FIRSTNOTE = "firstNote";
	
	/** Pointers to all of the controls that are displayed.
	*   The pedalPickers for each pedal */
	private PedalRadioGroup pedalPickerA, pedalPickerB, pedalPickerC, pedalPickerD, pedalPickerE, pedalPickerF, pedalPickerG;
	private final ArrayList<RadioGroup> allPickers = new ArrayList<>();
	/** Spinner for showing and selecting optional pedals to give the same chord. */
	private Spinner alternateSpinner;
	private ArrayAdapter<String> altSpinnerAdapter;
	/** Text showing the chord or scale that the pedals are set for. */
	private TextView chordText;
	
	/** NotePlayer for playing notes. */
	private NotePlayer player;
	
	/** Pedals object. */
	private Pedals pedals;
	/** List of optional pedal positions that can give the same pitches. */
	private ArrayList<PedalPosition> options = new ArrayList<>();
	/** First note to play on glissando. */
	private Note firstNote;

	/**
	 * Required empty public constructor.
	 */
	public PedalFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.d(TAG, "PedalFragment.onCreate " + this.toString());
		super.onCreate(savedInstanceState);

		pedals = new Pedals();
		player = new NotePlayer(this.getContext());
		// restore here rather than in onCreateView so that we don't over-write any changes
		restoreInstanceState(savedInstanceState);
		if (firstNote == null) {
			firstNote = new Note(C, NATURAL);
		}
		setRetainInstance(true);		// don't destroy this fragment on orientation changes
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.d(TAG, "PedalFragment.onCreateView " + this.toString());
		View view = inflater.inflate(R.layout.fragment_pedal, container, false);

		allPickers.clear();			// Empty the list so that they are not added a 2nd time
		pedalPickerA = (PedalRadioGroup) view.findViewById(R.id.pedalA);
		allPickers.add(pedalPickerA);
		pedalPickerB = (PedalRadioGroup) view.findViewById(R.id.pedalB);
		allPickers.add(pedalPickerB);
		pedalPickerC = (PedalRadioGroup) view.findViewById(R.id.pedalC);
		allPickers.add(pedalPickerC);
		pedalPickerD = (PedalRadioGroup) view.findViewById(R.id.pedalD);
		allPickers.add(pedalPickerD);
		pedalPickerE = (PedalRadioGroup) view.findViewById(R.id.pedalE);
		allPickers.add(pedalPickerE);
		pedalPickerF = (PedalRadioGroup) view.findViewById(R.id.pedalF);
		allPickers.add(pedalPickerF);
		pedalPickerG = (PedalRadioGroup) view.findViewById(R.id.pedalG);
		allPickers.add(pedalPickerG);

		chordText = (TextView) view.findViewById(R.id.chordText);

		alternateSpinner = (Spinner) view.findViewById(R.id.alternateSpinner);
		List<String> altList = new ArrayList<>();
		altSpinnerAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, altList);
		altSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		alternateSpinner.setAdapter(altSpinnerAdapter);
		alternateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					pedals.setPedals(options.get(alternateSpinner.getPositionForView(view)));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		/* Button to play a glissando. */
		Button playGlissButton = (Button) view.findViewById(R.id.playGlissButton);
		playGlissButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playPedalGliss();
			}
		});

		return view;
	}

	@Override
	public void onStart() {
//		Log.d(TAG, "PedalFragment.onStart " + this.toString());
		super.onStart();
		
		// During startup, check if there are arguments passed to the fragment.
		// Note: arguments will over-ride any savedInstanceState that was given to onCreateView
		restoreInstanceState(getArguments());
	}

	@Override
	public void onResume() {
//		Log.d(TAG, "PedalFragment.onResume " + this.toString());
		super.onResume();
		pedalPickerA.setOnCheckedChangeListener(this);
		pedalPickerB.setOnCheckedChangeListener(this);
		pedalPickerC.setOnCheckedChangeListener(this);
		pedalPickerD.setOnCheckedChangeListener(this);
		pedalPickerE.setOnCheckedChangeListener(this);
		pedalPickerF.setOnCheckedChangeListener(this);
		pedalPickerG.setOnCheckedChangeListener(this);
		
		updateForm();
		findAlternates();

		pedals.addPropertyChangeListener(this);
	}

	@Override
	public void onPause() {
//		Log.d(TAG, "PedalFragment.onPause " + this.toString());
		super.onPause();
		pedalPickerA.setOnCheckedChangeListener(null);
		pedalPickerB.setOnCheckedChangeListener(null);
		pedalPickerC.setOnCheckedChangeListener(null);
		pedalPickerD.setOnCheckedChangeListener(null);
		pedalPickerE.setOnCheckedChangeListener(null);
		pedalPickerF.setOnCheckedChangeListener(null);
		pedalPickerG.setOnCheckedChangeListener(null);

		pedals.removePropertyChangeListener(this);
	}

	@Override
	public void onDestroy() {
//		Log.d(TAG, "PedalFragment.onDestroy " + this.toString());
		super.onDestroy();
//		player.release();
		player = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
//		Log.d(TAG, "PedalFragment.onSaveInstanceState " + this.toString());
		super.onSaveInstanceState(outState);
		if (outState == null) {
			return;
		}
		
		if (firstNote != null) {
			outState.putInt(ARG_FIRSTNOTE, firstNote.getNumber());
		}
		pedals.saveToBundle(outState);
		pedals.saveToBundle(this.getArguments());		// must update any arguments sent to this fragment as well
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
	
		if (bundle.get(ARG_FIRSTNOTE) != null) {
			firstNote = new Note(bundle.getInt(ARG_FIRSTNOTE));
		}
		pedals.restoreFromBundle(bundle);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		Log.d(TAG, "PedalFragment.onCheckedChanged: " + this.toString() + " " + allPickers.indexOf(group) + " to " + ((PedalRadioGroup)group).getPosition());
		int n = allPickers.indexOf(group);
		BasicNote note = BasicNote.values()[n];
		pedals.setPosition(note, SharpFlat.values()[((PedalRadioGroup)group).getPosition()]);
		findAlternates();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
//		Log.d(TAG, "PedalsFragment.propertyChange: " + this.toString() + " " + event.getPropertyName() + " old:" + event.getOldValue() + " new:" + event.getNewValue());
		updateForm();
	}

	/**
	 * Find alternative pedal combinations that give the same pitch.
	 */
	public void findAlternates() {
		altSpinnerAdapter.clear();
		int pitchMask = pedals.getPitchMask();
		options = Pedals.pedalsForPitchMask(pitchMask);  // get other pedal options for same pitch
		if (options.isEmpty()) {      // should never be empty, but just in case
			setAltComboEnable(false);   // disable alternateCombo
		} else {
			for (PedalPosition pedPos : Pedals.pedalsForPitchMask(pitchMask)) {
				altSpinnerAdapter.add(pedPos.toString());  // add the options to alternateCombo
			}
			alternateSpinner.setSelection(altSpinnerAdapter.getPosition(pedals.getPedalPositions().toString()));    // select what matches the pedals
			setAltComboEnable(options.size() > 1);  // don't enable if there is only one
		}
	}

		/**
		 * Set the enabled state of the alternateCombo.
		 *
		 * @param tf true: enable
		 */
	public void setAltComboEnable(boolean tf) {
		alternateSpinner.setEnabled(tf);
		alternateSpinner.setAlpha(tf ? 1.0f : 0.4f);		// to make it dim when not enabled
	}

	/**
	 * Update all display fields with current data.
	 */
	private void updateForm() {
		pedalPickerA.setPosition(pedals.getPosition(A).ordinal());
		pedalPickerB.setPosition(pedals.getPosition(B).ordinal());
		pedalPickerC.setPosition(pedals.getPosition(C).ordinal());
		pedalPickerD.setPosition(pedals.getPosition(D).ordinal());
		pedalPickerE.setPosition(pedals.getPosition(E).ordinal());
		pedalPickerF.setPosition(pedals.getPosition(F).ordinal());
		pedalPickerG.setPosition(pedals.getPosition(G).ordinal());
		chordText.setText(pedals.findChordName());
	}

	/**
	 * Play a glissando based on the current pedal positions.
	 */
	private void playPedalGliss() {
		ArrayList<Note> notes = pedals.getNotes(firstNote);
		ArrayList<Note> notes2 = new ArrayList<>();
		for (Note note : notes) {
			notes2.add(new Note(note.getNumber() + 12));   // add a 2nd octave
		}
		for (Note note : notes) {
			notes2.add(new Note(note.getNumber() + 24));   // add a 3rd octave
		}
		notes.addAll(notes2);
		player.playGliss(notes);
//    player.play(notes);     // slow for debugging
	}
	
	/**
	 * This is the method to receive data from HarpPedalsActivity.onKeySigChange.
	 * @param pedPos new PedalPosition
	 */
	public void setPedals(PedalPosition pedPos) {
		pedals.setPedals(pedPos);
	}
	
	/**
	 * Set the first note to be played on a glissando.
	 *
	 * @param note first note
	 */
	public void setFirstNote(Note note) {
		this.firstNote = note;
	}
}
