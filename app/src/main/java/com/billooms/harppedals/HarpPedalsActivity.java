package com.billooms.harppedals;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.billooms.harppedals.chords.ChordFragment;
import com.billooms.harppedals.keysignature.Key;
import com.billooms.harppedals.keysignature.KeySignature;
import com.billooms.harppedals.keysignature.KeySignatureFragment;
import com.billooms.harppedals.keysignature.Scale;
import com.billooms.harppedals.notes.Note;
import com.billooms.harppedals.pedals.PedalFragment;
import com.billooms.harppedals.pedals.PedalPosition;
import com.billooms.harppedals.pedals.Pedals;

import java.util.ArrayList;

import static com.billooms.harppedals.chords.ChordFragment.ARG_CHORD;
import static com.billooms.harppedals.chords.ChordFragment.ARG_CHORDADD;
import static com.billooms.harppedals.keysignature.Key.ARG_KEY;
import static com.billooms.harppedals.keysignature.Key.ARG_SCALE;

/**
 * This is the Main Activity for HarpPedals.
 * Copyright 2017 Bill Ooms
 */
public class HarpPedalsActivity extends FragmentActivity
		implements ChordFragment.OnChordChangeListener, KeySignatureFragment.OnKeySigChangeListener {
	
	/** Tag for the PedalFragment which is retained (not destroyed on orientation changes).
	 * Note: The tag is only used on one-pane layouts.
	 * Multi-pane layouts should retrieve by ID. */
	private final static String TAG_PEDAL_FRAG = "PedalFrag";

	/** Tag used to identify fragments that should not respond to horizontal swipes. */
	private final static String NO_SWIPE = "NO_SWIPE";

	/** Gesture Detector used to navigate between fragments on small screens. */
	private GestureDetector gDetector = null;
	/** Simple alert dialog with OK button to dismiss. */
	private AlertDialog.Builder dialogBuilder;
	
	/** Saved from fragments when in a one-pane layout */
	private Key savedKey;
	private int[] savedChordArray;
	private boolean[] savedChordAddArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		Log.d(TAG, "HarpPedalsActivity.onCreate");
		super.onCreate(savedInstanceState);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// don't do anything except close the dialog
			}
		});
		
		// This will be either the standard (small) layout or the large layout
		setContentView(R.layout.activity_main);

		// Check that the activity is using the standard (small) layout version with the 'fragment_container' FrameLayout
		if (findViewById(R.id.fragment_container) != null) {
			// Gesture Detector to detect horizontal swipes
			if (gDetector == null) {
				gDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent start, MotionEvent finish, float xVelocity, float yVelocity) {
						if ((finish == null) || (start == null)) {  // not sure why, but the float dx = gives null pointer error sometimes
							return false;
						}
						Fragment swipeFragment = getSupportFragmentManager().findFragmentByTag(NO_SWIPE);
						if (swipeFragment == null) {	// only process swipes in one-pane PedalFragments
							float dx = finish.getRawX() - start.getRawX();
							float dy = finish.getRawY() - start.getRawY();
							if (Math.abs(dy) < Math.abs(dx)) {    // horizontal
								if (dx < 0) {      // swipe toward the left for KeySignature
									launchKeyFragment();
								} else {          // swipe toward the right for Chord
									launchChordFragment();
								}
							}
							return true;	// gesture has been consumed
						}
						return false;   // don't process swipes if we're not in a one-pane PedalFragment
					}
				});
			}

			// If we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create a new PedalFragment to be placed in the activity layout
			PedalFragment pedalFragment = new PedalFragment();
			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pedalFragment, TAG_PEDAL_FRAG).commit();
		}
	}
	
	/**
	 * Launch a new KeyFragment.
	 */
	private void launchKeyFragment() {
		// Create fragment
		KeySignatureFragment keyFragment = new KeySignatureFragment();
		// set arguments for any previously saved KeySignature
		if (savedKey != null) {
			keyFragment.setArguments(savedKey.saveToBundle(new Bundle()));
		}
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		// Replace whatever is in the fragment_container view with this fragment
		transaction.replace(R.id.fragment_container, keyFragment, NO_SWIPE);
		// and add the transaction to the back stack so the user can navigate back
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}
	
	/**
	 * Launch a new ChordFragment.
	 */
	private void launchChordFragment() {
		// Create fragment
		ChordFragment chordFragment = new ChordFragment();
		// set arguments for any previously saved Chord
		if (savedChordArray != null) {
			Bundle args = new Bundle();
			args.putIntArray(ARG_CHORD, savedChordArray);
			args.putBooleanArray(ARG_CHORDADD, savedChordAddArray);
			chordFragment.setArguments(args);
		}
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		// Replace whatever is in the fragment_container view with this fragment
		transaction.replace(R.id.fragment_container, chordFragment, NO_SWIPE);
		// and add the transaction to the back stack so the user can navigate back
		transaction.addToBackStack(null);
		// Commit the transaction
		transaction.commit();
	}

	@Override
	public void onStop() {
//		Log.d(TAG, "HarpPedalsActivity.onStop");
		super.onStop();
	}
	
//	@Override
//	public void onDestroy() {
//		Log.d(TAG, "HarpPedalsActivity.onDestroy");
//		super.onDestroy();
//	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//		Log.d(TAG, "HarpPedalsActivity.onSaveInstanceState");
		super.onSaveInstanceState(outState);
		
		if (outState == null) {
			return;
		}
		if (savedChordArray != null) {
			outState.putIntArray(ARG_CHORD, savedChordArray);
		}
		if (savedChordAddArray != null) {
			outState.putBooleanArray(ARG_CHORDADD, savedChordAddArray);
		}
		if (savedKey != null) {
			outState.putInt(ARG_KEY, savedKey.getKeySignature().ordinal());
			outState.putInt(ARG_SCALE, savedKey.getScale().ordinal());
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		Log.d(TAG, "HarpPedalsActivity.onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);

		savedChordArray = savedInstanceState.getIntArray(ARG_CHORD);
		savedChordAddArray = savedInstanceState.getBooleanArray(ARG_CHORDADD);
		savedKey = new Key(KeySignature.values()[savedInstanceState.getInt(ARG_KEY, KeySignature.DEFAULT.ordinal())],
				Scale.values()[savedInstanceState.getInt(ARG_SCALE, Scale.DEFAULT.ordinal())]);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (gDetector != null) {
			return gDetector.onTouchEvent(evt);
		}
		return false;		// don't consume the event if there is no GestureDetector
	}
	
	/**
	 * Receive a message from KeySignatureFragment to change the pedals.
	 * Change the pedals based on the given PedalPosition and save the given Key.
	 *
	 * @param pedPos new PedalPosition
	 * @param key Key
	 */
	public void onKeySigChange(PedalPosition pedPos, Key key, Note firstNote) {
		if (pedPos == null) {
			return;
		}
		// Find the saved PedalFragment by its tag (null in multi-pane layout)
		PedalFragment pedalFragTag = (PedalFragment) getSupportFragmentManager().findFragmentByTag(TAG_PEDAL_FRAG);
		// Capture the pedal_fragment from the activity layout (null in a one-pane layout)
		PedalFragment pedalFragId = (PedalFragment) getSupportFragmentManager().findFragmentById(R.id.pedal_fragment);
		PedalFragment pedalFrag = (pedalFragId == null) ? pedalFragTag : pedalFragId;
		
		// Call a method in the PedalFragment directly to update its content
		pedalFrag.setPedals(pedPos);
		pedalFrag.setFirstNote(firstNote);
		
		// If pedalFragId is null, we're in one-pane layout
		if (pedalFragId == null) {
			// Save the KeySignature from the KeySignatureFragment before it's destroyed
			savedKey = key;
			// Pop the original pedalFrabment back
			getSupportFragmentManager().popBackStack();
		}
	}
	
	/**
	 * Receive a message from ChordFragment to change the pedals.
	 * Change the pedals based on the given pitchMask and save the given chord information.
	 *
	 * @param pitchMask Pitch mask
	 * @param rootNote First note to play when playing a glissando
	 * @param chordArray integer array representing position of spinners
	 * @param addArray boolean array representing status of check boxes
	 */
	public void onChordChange(int pitchMask, Note rootNote, int[] chordArray, boolean[] addArray) {
		// Find the saved PedalFragment by its tag (null in multi-pane layout)
		PedalFragment pedalFragTag = (PedalFragment) getSupportFragmentManager().findFragmentByTag(TAG_PEDAL_FRAG);
		// Capture the pedal_fragment from the activity layout (null in a one-pane layout)
		PedalFragment pedalFragId = (PedalFragment) getSupportFragmentManager().findFragmentById(R.id.pedal_fragment);
		PedalFragment pedalFrag = (pedalFragId == null) ? pedalFragTag : pedalFragId;
		
		ArrayList<PedalPosition> pedalsForMask = Pedals.pedalsForPitchMask(pitchMask);
		if (!pedalsForMask.isEmpty()) {
			pedalFrag.setPedals(pedalsForMask.get(0));	// set the pedals to the first one found
			pedalFrag.setFirstNote(rootNote);
			pedalFrag.findAlternates();
		} else {		// there is no pedal setting for the chord
			dialogBuilder.setMessage("No pedals for the chord").create().show();
			pedalFrag.setAltComboEnable(false);
			return;		// Note: this will not popBackStack
		}
		
		// If pedalFragId is null, we're in one-pane layout
		if (pedalFragId == null) {
			// Save the chord information from the ChordFragment before it's destroyed
			savedChordArray = chordArray;
			savedChordAddArray = addArray;
			// Pop the original pedalFrabment back
			getSupportFragmentManager().popBackStack();
		}
	}
}
