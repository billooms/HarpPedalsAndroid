package com.billooms.harppedals.keysignature;

import android.os.Bundle;

import com.billooms.harppedals.notes.BasicNote;
import com.billooms.harppedals.notes.Note;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import static com.billooms.harppedals.keysignature.Scale.HARMONIC;
import static com.billooms.harppedals.keysignature.Scale.MAJOR;
import static com.billooms.harppedals.keysignature.Scale.MELODIC;
import static com.billooms.harppedals.notes.SharpFlat.DOUBLESHARP;
import static com.billooms.harppedals.notes.SharpFlat.FLAT;
import static com.billooms.harppedals.notes.SharpFlat.NATURAL;
import static com.billooms.harppedals.notes.SharpFlat.SHARP;

/**
 * Key for music.
 * Copyright 2017 Bill Ooms
 */

public class Key {
	/** Keys for savedInstanceState bundle. */
	public final static String ARG_KEY = "key";
	public final static String ARG_SCALE = "scale";

	/** KeyPanel can fire propertyChanges. */
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/** Property name used for changing the key signature. */
	private final static String PROP_KEYSIG = "KeySig";
	/** Property name used for changing the Major/Minor flag. */
	private final static String PROP_SCALE = "Scale";

	/** Key Signature. */
	private KeySignature keySig = null;
	/** Major/Minor/Harmonic/Melodic. */
	private Scale scale = MAJOR;

	/** Create a new key with default values. */
	public Key() {
		this(KeySignature.NONE, MAJOR);    // C Major
	}

	/**
	 * Create a new key with the given key signature and scale (Major/Minor/Harmonic/Melodic).
	 *
	 * @param keySig key signature
	 * @param scale Major/Minor/Harmonic/Melodic
	 */
	public Key(KeySignature keySig, Scale scale) {
		this.keySig = keySig;
		this.scale = scale;
	}

	@Override
	public String toString() {
		if (isMajor()) {
			return keySig.getMajorNote().toString2() + " " + scale.getName();
		} else {
			// minor keys use lower case
			return keySig.getMinorNote().toString2().toLowerCase() + " " + scale.getName();
		}
	}

	/**
	 * Get the key signature.
	 *
	 * @return key signature
	 */
	public KeySignature getKeySignature() {
		return keySig;
	}

	/**
	 * Set the key signature.
	 * This fires a PROP_KEYSIG property change with the old and new values.
	 *
	 * @param keySig new key signature
	 */
	public void setKeySignature(KeySignature keySig) {
		KeySignature old = this.keySig;
		this.keySig = keySig;
		pcs.firePropertyChange(PROP_KEYSIG, old, this.keySig);
	}

	/**
	 * Determine if the key is a scale or minor.
	 *
	 * @return true: scale
	 */
	private boolean isMajor() {
		return MAJOR.equals(scale);
	}

	/**
	 * Get the scale of this key (Major/Minor/Harmonic/Melodic).
	 *
	 * @return scale (Major/Minor/Harmonic/Melodic)
	 */
	public Scale getScale() {
		return scale;
	}

	/**
	 * Set the scale (Major/Minor/Harmonic/Melodic).
	 * This fires a PROP_SCALE property change with the old and new values.
	 *
	 * @param scale scale
	 */
	public void setScale(Scale scale) {
		Scale old = this.scale;
		this.scale = scale;
		pcs.firePropertyChange(PROP_SCALE, old, this.scale);
	}

	/**
	 * Get the first note of the scale for this key.
	 *
	 * @return first note of the scale
	 */
	public Note getFirstNote() {
		if (isMajor()) {
			return keySig.getMajorNote();
		} else {
			return keySig.getMinorNote();
		}
	}

	/**
	 * Get a list of notes on this scale.
	 * This might include double sharp notes which might require extra attention.
	 *
	 * @return list of notes on this scale
	 */
	public ArrayList<Note> getNotes() {
		ArrayList<Note> notes = new ArrayList<>();
		Note firstNote = getFirstNote();
		int num = firstNote.getBaseNote().ordinal();   // number 0 thru 6 for first note
		if ((firstNote.getBaseNote() == BasicNote.A) && (firstNote.getSharpFlat() == FLAT)) {
			num = 7;          // start higher for A-flat
		}
		for (int i = 0; i < 7; i++) {
			BasicNote basicNote = BasicNote.values()[num % 7];  // mod 7: don't go out of bounds
			if (keySig.hasSharps() && keySig.getSharpFlats().contains(basicNote)) {
				if ((i == 6) && ((scale == HARMONIC) || (scale == MELODIC))) {
					notes.add(new Note(basicNote, DOUBLESHARP, num >= 7));
				} else if ((i == 5) && (scale == MELODIC)) {
					notes.add(new Note(basicNote, DOUBLESHARP, num >= 7));
				} else {
					notes.add(new Note(basicNote, SHARP, num >= 7));
				}
			} else if (keySig.hasFlats() && keySig.getSharpFlats().contains(basicNote)) {
				if ((i == 6) && ((scale == HARMONIC) || (scale == MELODIC))) {
					notes.add(new Note(basicNote, NATURAL, num >= 7));
				} else if ((i == 5) && (scale == MELODIC)) {
					notes.add(new Note(basicNote, NATURAL, num >= 7));
				} else {
					notes.add(new Note(basicNote, FLAT, num >= 7));
				}
			} else {
				if ((i == 6) && ((scale == HARMONIC) || (scale == MELODIC))) {
					notes.add(new Note(basicNote, SHARP, num >= 7));
				} else if ((i == 5) && (scale == MELODIC)) {
					notes.add(new Note(basicNote, SHARP, num >= 7));
				} else {
					notes.add(new Note(basicNote, NATURAL, num >= 7));
				}
			}
			num = num + 1;
		}
		return notes;
	}

	/**
	 * Get the pitch mask for this key.
	 *
	 * @return pitch mask
	 */
	public int getPitchMask() {
		int mask = 0;
		for (Note note : getNotes()) {
			mask = mask | note.getPitchMask();
		}
		return mask;
	}
	
	/**
	 * Add information to the given Bundle to save the state of this Key.
	 *
	 * @param bundle Given Bundle
	 * @return modified Bundle
	 */
	public Bundle saveToBundle(Bundle bundle) {
		if (bundle == null) {
			return null;
		}
		bundle.putInt(ARG_KEY, this.getKeySignature().ordinal());
		bundle.putInt(ARG_SCALE, this.getScale().ordinal());
		return bundle;
	}
	
	/**
	 * Restore this Key from the given Bundle.
	 *
	 * @param bundle Bundle
	 */
	public void restoreFromBundle(Bundle bundle) {
		if (bundle == null) {
			return;
		}
		this.setKeySignature(KeySignature.values()[bundle.getInt(ARG_KEY)]);
		this.setScale(Scale.values()[bundle.getInt(ARG_SCALE)]);
	}
}
