package com.billooms.harppedals.notes;

import android.content.Context;
import android.media.MediaPlayer;

import com.billooms.harppedals.R;

import java.util.ArrayList;
import java.util.TimerTask;


/**
 * Plays sound for notes on the harp based on chords.
 *
 * @author Bill Ooms. Copyright 2016 Studio of Bill Ooms. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class NotePlayer {

	/**
	 * Files which contain the mp3 of each string.
	 * The range is 3 octaves from A2 to G#5.
	 * Note that traditional piano pitch names are used here -- not harp string
	 * names.
	 */
	private final static int[] FILES = {
			R.raw.a2, R.raw.a2s, R.raw.b2, R.raw.c3, R.raw.c3s, R.raw.d3,
			R.raw.d3s, R.raw.e3, R.raw.f3, R.raw.f3s, R.raw.g3, R.raw.g3s,
			R.raw.a3, R.raw.a3s, R.raw.b3, R.raw.c4, R.raw.c4s, R.raw.d4,
			R.raw.d4s, R.raw.e4, R.raw.f4, R.raw.f4s, R.raw.g4, R.raw.g4s,
			R.raw.a4, R.raw.a4s, R.raw.b4, R.raw.c5, R.raw.c5s, R.raw.d5,
			R.raw.d5s, R.raw.e5, R.raw.f5, R.raw.f5s, R.raw.g5, R.raw.g5s,
			R.raw.a5, R.raw.a5s, R.raw.b5, R.raw.c6, R.raw.c6s, R.raw.d6,
			R.raw.d6s, R.raw.e6, R.raw.f6, R.raw.f6s, R.raw.g6, R.raw.g6s,
			R.raw.a6
	};
	
	/** Delay between arpeggiated notes in milliseconds. */
	private final static int SLOW_DELAY = 250;
	/** Delay between glissando notes in milliseconds. */
	private final static int FAST_DELAY = 50;
	/** Mask to find the 24th bit. */
	private final static int MASK24 = 0b100000000000000000000000;
	
	/** Save the context for retrieving the samples. */
	private final Context context;

	/**
	 * Construct a new ChordPlayer.
	 *
	 * @param context Context
	 */
	public NotePlayer(Context context) {
		this.context = context;
	}

	/**
	 * Play the chord with the given mask starting at the given note.
	 * Play twice adding an octave the 2nd time.
	 *
	 * @param mask 12 bit mask which is repeated (two octaves played)
	 * @param start starting note
	 */
	public void play2(int mask, int start) {
		mask = (mask << 12) | mask;
		int delay = 0;
		for (int i = start; i < start + 24; i++) {
			if (i >= FILES.length) {    // just to make sure we don't go too far
				break;
			}
			if ((mask & MASK24) != 0) {
				MediaPlayer mp = MediaPlayer.create(context, FILES[i]);
				mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
						mp.setOnCompletionListener(null);
						mp = null;		// for garbage collection
					}
				});
				playDelay(mp, delay);
				delay += SLOW_DELAY;
			}
			mask = mask << 1;   // shift right
		}
	}

	/**
	 * Play the chord with the given list of notes.
	 * Slow play speed is used.
	 *
	 * @param notes list of notes
	 */
	public void play(ArrayList<Note> notes) {
		play(notes, SLOW_DELAY);
	}

	/**
	 * Play the chord with the given list of notes.
	 * Fast play speed is used.
	 *
	 * @param notes list of notes
	 */
	public void playGliss(ArrayList<Note> notes) {
		play(notes, FAST_DELAY);
	}

	/**
	 * Play the chord with the given list of notes.
	 *
	 * @param notes list of notes
	 * @param speed delay in milliseconds between notes
	 */
	private void play(ArrayList<Note> notes, int speed) {
		int delay = 0;
		for (Note note : notes) {
			MediaPlayer mp = MediaPlayer.create(context, FILES[note.getNumber()]);
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					mp.setOnCompletionListener(null);
					mp = null;		// for garbage collection
				}
			});
			playDelay(mp, delay);
			delay += speed;
		}
	}

	/**
	 * Play the given clip after the specified time delay.
	 *
	 * @param clip clip
	 * @param delay delay in milliseconds
	 */
	private void playDelay(final MediaPlayer clip, int delay) {
		java.util.Timer t = new java.util.Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				clip.start();
			}
		}, delay);
	}

}
