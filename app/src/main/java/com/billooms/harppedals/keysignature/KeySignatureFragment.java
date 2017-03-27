package com.billooms.harppedals.keysignature;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.billooms.harppedals.R;
import com.billooms.harppedals.notes.Note;
import com.billooms.harppedals.notes.NotePlayer;
import com.billooms.harppedals.notes.SharpFlat;
import com.billooms.harppedals.pedals.PedalPosition;
import com.billooms.harppedals.pedals.Pedals;

import java.util.ArrayList;
import java.util.List;

import static com.billooms.harppedals.notes.SharpFlat.DOUBLESHARP;

/**
 * Fragment for setting the Key Signature.
 * Copyright 2017 Bill Ooms
 * Activities that contain this fragment must implement the
 * {@link KeySignatureFragment.OnKeySigChangeListener} interface
 * to handle interaction events.
 */
public class KeySignatureFragment extends Fragment {

	/** Pointer to the PedalChangeListener. */
	private OnKeySigChangeListener keySigChangeListener;

	/** Pointers to all of the controls that are displayed. */
	private Spinner keySpinner, scaleSpinner;
	private TextView scaleText, keyText;
	private ImageView keySigIcon;
	private ArrayAdapter<String> keySpinnerAdapter, scaleSpinnerAdapter;
	
	/** NotePlayer for playing notes. */
	private NotePlayer player;
	
	/** Key. */
	private Key key;

	/**
	 * Required empty public constructor.
	 */
	public KeySignatureFragment() {}

	/**
	 * Container Activity must implement this interface.
	 */
	public interface OnKeySigChangeListener {
		/**
		 * Change the pedals based on the given PedalPosition and save the given Key.
		 *
		 * @param pedPos PedalPosition
		 * @param key Key
		 * @param firstNote first note to play on the scale
		 */
		void onKeySigChange(PedalPosition pedPos, Key key, Note firstNote);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.d(TAG, "KeySignatureFragment.onCreate " + this.toString());
		super.onCreate(savedInstanceState);

		key = new Key();    // default is C major
		player = new NotePlayer(this.getContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.d(TAG, "KeySignatureFragment.onCreateView " + this.toString());
		View view = inflater.inflate(R.layout.fragment_key_signature, container, false);

		keySpinner = (Spinner) view.findViewById(R.id.keySpinner);
		List<String> keySigList = new ArrayList<>();
		for (KeySignature k : KeySignature.values()) {
			keySigList.add(k.getText());
		}
		keySpinnerAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, keySigList);
		keySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		keySpinner.setAdapter(keySpinnerAdapter);
		keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					key.setKeySignature(KeySignature.values()[keySpinner.getPositionForView(view)]);
				}
				updateForm();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		scaleSpinner = (Spinner) view.findViewById(R.id.scaleSpinner);
		List<String> scaleList = new ArrayList<>();
		for (Scale s : Scale.values()) {
			scaleList.add(s.name());
		}
		scaleSpinnerAdapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, scaleList);
		scaleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scaleSpinner.setAdapter(scaleSpinnerAdapter);
		scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (view != null) {
					key.setScale(Scale.values()[scaleSpinner.getPositionForView(view)]);
				}
				updateForm();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		scaleText = (TextView) view.findViewById(R.id.scaleText);

		keyText = (TextView) view.findViewById(R.id.keyText);

		keySigIcon = (ImageView) view.findViewById(R.id.keySigIcon);
		
		Button playButton = (Button) view.findViewById(R.id.playScaleButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playScale();
			}
		});
		
		Button setKeyButton = (Button) view.findViewById(R.id.setKeyButton);
		setKeyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPedalsForKey();
			}
		});
		
		Button setTonicButton = (Button) view.findViewById(R.id.setTonicButton);
		setTonicButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPedalsForTonic();
			}
		});
		
		Button setV7Button = (Button) view.findViewById(R.id.setV7Button);
		setV7Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setPedalsForV7();
			}
		});
		
		restoreInstanceState(savedInstanceState);
		
		return view;
	}

	@Override
	public void onStart() {
//		Log.d(TAG, "KeySignatureFragment.onStart " + this.toString());
		super.onStart();
		
		// During startup, check if there are arguments passed to the fragment.
		// Note: arguments will over-ride any savedInstanceState that was given to onCreateView
		restoreInstanceState(getArguments());
	}

	@Override
	public void onResume() {
//		Log.d(TAG, "KeySignatureFragment.onResume " + this.toString());
		super.onResume();
		
		updateForm();
	}
	
//	@Override
//	public void onPause() {
//		super.onPause();
//	}
	
	@Override
	public void onDestroy() {
//		Log.d(TAG, "KeySignatureFragment.onDestroy " + this.toString());
		super.onDestroy();
//		player.release();
		player = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
//		Log.d(TAG, "KeySignatureFragment.onSaveInstanceState " + this.toString());
		super.onSaveInstanceState(outState);
		if (outState == null) {
			return;
		}
		
		key.saveToBundle(outState);
		key.saveToBundle(this.getArguments());		// must update any arguments sent to this fragment as well
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

		key.restoreFromBundle(bundle);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnKeySigChangeListener) {
			keySigChangeListener = (OnKeySigChangeListener) context;
		} else {
			throw new RuntimeException(context.toString() + " must implement OnKeySigChangeListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		keySigChangeListener = null;
	}

	/**
	 * Update all fields on the form from key signature data.
	 */
	private void updateForm() {
		if (key.getKeySignature() != null) {
			keySpinner.setSelection(keySpinnerAdapter.getPosition(key.getKeySignature().getText()));
			scaleSpinner.setSelection(scaleSpinnerAdapter.getPosition(key.getScale().name()));
			keySigIcon.setImageDrawable(key.getKeySignature().getIcon(this.getContext()));
			scaleText.setText(key.getNotes().toString());
			keyText.setText(key.toString());
		}
	}
	/**
	 * Play the current scale.
	 */
	private void playScale() {
		ArrayList<Note> notes = key.getNotes();
		ArrayList<Note> notes2 = new ArrayList<>();
		for (Note note : notes) {
			notes2.add(new Note(note.getNumber() + 12));   // add a 2nd octave
		}
		for (Note note : notes) {
			notes2.add(new Note(note.getNumber() + 24));   // add a 3rd octave
		}
		notes2.add(new Note(notes.get(0).getNumber() + 36));  // and repeat the tonic
		notes.addAll(notes2);
		player.play(notes);
	}
	
	/**
	 * Set the pedals for the current key signature.
	 */
	private void setPedalsForKey() {
		ArrayList<Note> notes = key.getNotes();
		if (hasDoubleSharps(notes)) {   // handle double sharps differently
			int pitchMask = key.getPitchMask();
			ArrayList<PedalPosition> pedalsForPitchMask = Pedals.pedalsForPitchMask(pitchMask);
			if (!pedalsForPitchMask.isEmpty()) {
				keySigChangeListener.onKeySigChange(pedalsForPitchMask.get(0), key, key.getFirstNote());
			}
		} else {
			keySigChangeListener.onKeySigChange(toPedalPosition(key.getNotes()), key, key.getFirstNote());
		}
	}
	
	/**
	 * Determine if the given list of notes contains a double sharp.
	 *
	 * @param notes list of notes
	 * @return true: contains a double sharp
	 */
	private boolean hasDoubleSharps(ArrayList<Note> notes) {
		for (Note note : notes) {
			if (note.getSharpFlat() == DOUBLESHARP) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Convert an arrayList of notes to a PedalPosition object.
	 * There must be exactly 7 notes in the list and each note must be represented.
	 *
	 * @param list ArrayList of notes
	 * @return PedalPosition object
	 */
	private PedalPosition toPedalPosition(ArrayList<Note> list) {
		if (list.size() != 7) {
			return null;
		}
		SharpFlat[] sf = new SharpFlat[7];		// array of SharpFlat for each note A,B,C,D,E,F,G
		for (Note n : list) {
			sf[n.getBaseNote().ordinal()] = n.getSharpFlat();
		}
		return new PedalPosition(sf[0], sf[1], sf[2], sf[3], sf[4], sf[5], sf[6]);
	}
	
	/**
	 * Change the pedals for a tonic glissando.
	 * This is done by replacing the 4th and 7th of the scale by other notes.
	 */
	private void setPedalsForTonic() {
		key.setScale(Scale.MAJOR);						// and use only the notes from the Major scale
		ArrayList<Note> noteList = key.getNotes();
		Note note4 = noteList.get(3);    // get the 4th note of the scale
		Note note7 = noteList.get(6);    // get the 7th note of the scale
		noteList.remove(note4);          // and remove them
		noteList.remove(note7);
		Note alt4 = Pedals.findAlternate(note4, noteList);  // find an alternate for the 4th
		Note alt7 = Pedals.findAlternate(note7, noteList);  // find an alternate for the 7th
		if ((alt4 != null) && (alt7 != null)) {
			noteList.add(alt4);
			noteList.add(alt7);
			PedalPosition pedPos = toPedalPosition(noteList);
			keySigChangeListener.onKeySigChange(pedPos, key, key.getFirstNote());
		}
	}
	
	/**
	 * Change the pedals for a V7 glissando.
	 * This is done by replacing the 1st and 3rd of the scale by other notes.
	 */
	private void setPedalsForV7() {
		key.setScale(Scale.MAJOR);						// and use only the notes from the Major scale
		ArrayList<Note> noteList = key.getNotes();
		Note note1 = noteList.get(0);    // get the 1st note of the scale
		Note note3 = noteList.get(2);    // get the 3rd note of the scale
		noteList.remove(note1);          // and remove them
		noteList.remove(note3);
		Note alt1 = Pedals.findAlternate(note1, noteList);  // find an enharmonic for the 1st
		Note alt3 = Pedals.findAlternate(note3, noteList);  // find an alternate for the 3rd
		if ((alt1 != null) && (alt3 != null)) {
			noteList.add(alt1);
			noteList.add(alt3);
			PedalPosition pedPos = toPedalPosition(noteList);
			keySigChangeListener.onKeySigChange(pedPos, key, new Note(key.getFirstNote().getNumber() + 7));
		}
	}

}
