package com.billooms.harppedals.keysignature;

import com.billooms.harppedals.notes.BasicNote;

import static com.billooms.harppedals.notes.BasicNote.*;

/**
 * Various constants for music keys.
 * Copyright 2017 Bill Ooms
 */

class KeyConstants {

	/** Lists of notes that are sharped (or flatted) for each key signature. */
	protected final static BasicNote[] FLAT_NOTES1 = {B};
	protected final static BasicNote[] FLAT_NOTES2 = {B, E};
	protected final static BasicNote[] FLAT_NOTES3 = {B, E, A};
	protected final static BasicNote[] FLAT_NOTES4 = {B, E, A, D};
	protected final static BasicNote[] FLAT_NOTES5 = {B, E, A, D, G};
	protected final static BasicNote[] FLAT_NOTES6 = {B, E, A, D, G, C};
	protected final static BasicNote[] FLAT_NOTES7 = {B, E, A, D, G, C, F};
	protected final static BasicNote[] SHARP_NOTES1 = {F};
	protected final static BasicNote[] SHARP_NOTES2 = {F, C};
	protected final static BasicNote[] SHARP_NOTES3 = {F, C, G};
	protected final static BasicNote[] SHARP_NOTES4 = {F, C, G, D};
	protected final static BasicNote[] SHARP_NOTES5 = {F, C, G, D, A};
	protected final static BasicNote[] SHARP_NOTES6 = {F, C, G, D, A, E};
	protected final static BasicNote[] SHARP_NOTES7 = {F, C, G, D, A, E, B};

}
