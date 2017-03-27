package com.billooms.harppedals.keysignature;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import com.billooms.harppedals.R;
import com.billooms.harppedals.notes.BasicNote;
import com.billooms.harppedals.notes.Note;

import java.util.ArrayList;
import java.util.Arrays;

import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES1;
import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES2;
import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES3;
import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES4;
import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES5;
import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES6;
import static com.billooms.harppedals.keysignature.KeyConstants.FLAT_NOTES7;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES1;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES2;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES3;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES4;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES5;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES6;
import static com.billooms.harppedals.keysignature.KeyConstants.SHARP_NOTES7;
import static com.billooms.harppedals.notes.BasicNote.A;
import static com.billooms.harppedals.notes.BasicNote.B;
import static com.billooms.harppedals.notes.BasicNote.C;
import static com.billooms.harppedals.notes.BasicNote.D;
import static com.billooms.harppedals.notes.BasicNote.E;
import static com.billooms.harppedals.notes.BasicNote.F;
import static com.billooms.harppedals.notes.BasicNote.G;
import static com.billooms.harppedals.notes.SharpFlat.FLAT;
import static com.billooms.harppedals.notes.SharpFlat.NATURAL;
import static com.billooms.harppedals.notes.SharpFlat.SHARP;

/**
 * Music key signature.
 * Copyright 2017 Bill Ooms
 */

public enum KeySignature {

	/** Define each key signature with icon, text, major key tonic, minor key tonic, array of sharps/flats. */
	FLAT7 (R.drawable.flat7,  "7 flats",  new Note(C, FLAT),    new Note(A, FLAT),    FLAT_NOTES7),
	FLAT6 (R.drawable.flat6,  "6 flats",  new Note(G, FLAT),    new Note(E, FLAT),    FLAT_NOTES6),
	FLAT5 (R.drawable.flat5,  "5 flats",  new Note(D, FLAT),    new Note(B, FLAT),    FLAT_NOTES5),
	FLAT4 (R.drawable.flat4,  "4 flats",  new Note(A, FLAT),    new Note(F, NATURAL), FLAT_NOTES4),
	FLAT3 (R.drawable.flat3,  "3 flats",  new Note(E, FLAT),    new Note(C, NATURAL), FLAT_NOTES3),
	FLAT2 (R.drawable.flat2,  "2 flats",  new Note(B, FLAT),    new Note(G, NATURAL), FLAT_NOTES2),
	FLAT1 (R.drawable.flat1,  "1 flats",  new Note(F, NATURAL), new Note(D, NATURAL), FLAT_NOTES1),
	NONE  (R.drawable.keyc,   "(none)",   new Note(C, NATURAL), new Note(A, NATURAL), new BasicNote[0]),
	SHARP1(R.drawable.sharp1, "1 sharps", new Note(G, NATURAL), new Note(E, NATURAL), SHARP_NOTES1),
	SHARP2(R.drawable.sharp2, "2 sharps", new Note(D, NATURAL), new Note(B, NATURAL), SHARP_NOTES2),
	SHARP3(R.drawable.sharp3, "3 sharps", new Note(A, NATURAL), new Note(F, SHARP),   SHARP_NOTES3),
	SHARP4(R.drawable.sharp4, "4 sharps", new Note(E, NATURAL), new Note(C, SHARP),   SHARP_NOTES4),
	SHARP5(R.drawable.sharp5, "5 sharps", new Note(B, NATURAL), new Note(G, SHARP),   SHARP_NOTES5),
	SHARP6(R.drawable.sharp6, "6 sharps", new Note(F, SHARP),   new Note(D, SHARP),   SHARP_NOTES6),
	SHARP7(R.drawable.sharp7, "7 sharps", new Note(C, SHARP),   new Note(A, SHARP),   SHARP_NOTES7);

	/** The number of key signatures. */
	public final static int SIZE = KeySignature.values().length;
	/** Default key signature. */
	public final static KeySignature DEFAULT = NONE;

	/** Resource for the icon. */
	private final int resource;
	/** Textual description. */
	private final String text;
	/** Starting note for major key. */
	private final Note major;
	/** Starting note for minor key. */
	private final Note minor;
	/** Array of sharps or flats for this key. */
	private final BasicNote[] sharpFlats;

	/**
	 * Create a new key signature.
	 * A key signature is the notation on the staff line -- that is, the number of
	 * sharps and/or flats.
	 *
	 * @param resource name of the resource containing the icon
	 * @param text text that can be used on buttons or selectors
	 * @param major starting note for major key
	 * @param minor starting note for minor key
	 * @param sharpFlats array of notes that are sharp or flat
	 */
	KeySignature(int resource, String text, Note major, Note minor, BasicNote[] sharpFlats) {
		this.resource = resource;
		this.text = text;
		this.major = major;
		this.minor = minor;
		this.sharpFlats = sharpFlats;
	}

	/**
	 * Get the display icon.
	 *
	 * @param context Context
	 * @return display icon
	 */
	public BitmapDrawable getIcon(Context context) {
		return (BitmapDrawable) context.getResources().getDrawable(resource);
	}

	/**
	 * Get the text description of the key signature such as "4 flats".
	 *
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Get first note of the major scale.
	 *
	 * @return first note of the major scale
	 */
	public Note getMajorNote() {
		return major;
	}

	/**
	 * Get first note of the minor scale.
	 *
	 * @return first note of the minor scale
	 */
	public Note getMinorNote() {
		return minor;
	}

	/**
	 * Get the array of sharps or flats for this key.
	 * Note that this is the sharps and/or flats for the signature and might
	 * not be the same as that used in a scale (such as melodic or harmonic minor).
	 *
	 * @return array of sharps or flats
	 */
	public ArrayList<BasicNote> getSharpFlats() {
		return new ArrayList<>(Arrays.asList(sharpFlats));
	}

	/**
	 * Determine if this key has any sharps in the signature.
	 * Note that this is the sharps for the signature and might not be the
	 * same as that used in a scale (such as melodic or harmonic minor).
	 *
	 * @return true: has some sharps
	 */
	public boolean hasSharps() {
		switch (this) {
			case SHARP7:
			case SHARP6:
			case SHARP5:
			case SHARP4:
			case SHARP3:
			case SHARP2:
			case SHARP1:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Determine if this key has any flats in the signature.
	 * Note that this is the flats for the signature and might not be the
	 * same as that used in a scale (such as melodic or harmonic minor).
	 *
	 * @return true: has some flats
	 */
	public boolean hasFlats() {
		switch (this) {
			case FLAT7:
			case FLAT6:
			case FLAT5:
			case FLAT4:
			case FLAT3:
			case FLAT2:
			case FLAT1:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Get the key signature(s) given the first note of the scale.
	 *
	 * @param note Note number
	 * @param major indicate if you want a major or minor scale
	 * @return matching key signature (could be two for keys with 5-7 sharps/flats)
	 */
	public static ArrayList<KeySignature> getKeyByNote(int note, boolean major) {
		ArrayList<KeySignature> list = new ArrayList<>();
		for (KeySignature keySig : KeySignature.values()) {
			if (major) {
				if (keySig.getMajorNote().getNumber() == note) {
					list.add(keySig);
				}
			} else {
				if (keySig.getMinorNote().getNumber() == note) {
					list.add(keySig);
				}
			}
		}
		return list;
	}
}
