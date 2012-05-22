package co.jp.feng.android.chimealarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ChimeAlarmActivity extends Activity
{
	private ToggleButton mBtnChimeSet;
	private TextView mTvVolume;
	private SeekBar mSeekBarVolume;
	private CheckBox mCheckBoxBootStart;
	private SharedPreferencesBundle mPrefs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		mPrefs = new SharedPreferencesBundle(this, R.string.shared_prefs_file_name, MODE_PRIVATE);

		mBtnChimeSet = (ToggleButton) findViewById(R.id.toggleButton1);
		mBtnChimeSet.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				PendingIntent pendingIntent;
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				StringBuilder resultStrBuf = new StringBuilder(32);
				Intent intent = new Intent(ChimeAlarmActivity.this, AlarmReceiver.class);
				intent.setAction(getString(R.string.ACTION_RING_ALARM));

				if (mBtnChimeSet.isChecked())
				{
					NextChimeEntity nextChimeEntity = NextChimeTimingUtil.getNextChime();
					intent.putExtra(getString(R.string.intent_extra_chime_type), nextChimeEntity.chimeType);
					pendingIntent = PendingIntent.getBroadcast(ChimeAlarmActivity.this, 0, intent, 0);
					am.set(AlarmManager.RTC_WAKEUP, nextChimeEntity.nextTime, pendingIntent);

					int nextSec = (int) (nextChimeEntity.nextTime - System.currentTimeMillis()) / 1000;
					resultStrBuf.append("チャイムをセットしました。 " + nextSec + "秒後に鳴ります。");
					mPrefs.putBooleanByStringResourceId(R.string.shared_prefs_key_name_chime_set, true);

					int barVolume = mSeekBarVolume.getProgress();
					mPrefs.putIntByStringResourceId(R.string.shared_prefs_key_name_chime_volume, barVolume).commit();
				}
				else
				{
					pendingIntent = PendingIntent.getBroadcast(ChimeAlarmActivity.this, 0, intent, 0);
					am.cancel(pendingIntent);

					resultStrBuf.append("チャイムを解除しました。");
					mPrefs.putBooleanByStringResourceId(R.string.shared_prefs_key_name_chime_set, false).commit();

					intent = new Intent(ChimeAlarmActivity.this, AlarmReceiver.class);
					intent.setAction(getString(R.string.ACTION_CANCEL_ALARM));
					sendBroadcast(intent);
				}

				Toast.makeText(ChimeAlarmActivity.this, resultStrBuf, Toast.LENGTH_SHORT).show();
			}
		});

		mTvVolume = (TextView) findViewById(R.id.textViewVolume);
		mSeekBarVolume = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBarVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				int barVolume = seekBar.getProgress();
				mPrefs.putIntByStringResourceId(R.string.shared_prefs_key_name_chime_volume, barVolume).commit();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if (!fromUser)
					return;

				mTvVolume.setText("Volume:" + String.valueOf(progress));
			}
		});

		mCheckBoxBootStart = (CheckBox) findViewById(R.id.checkBox1);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		boolean isChimeSet = mPrefs.getBooleanByStringResourceId(R.string.shared_prefs_key_name_chime_set, false);
		mBtnChimeSet.setChecked(isChimeSet);

		boolean isBootStart = mPrefs.getBooleanByStringResourceId(R.string.shared_prefs_key_name_boot_start, false);
		mCheckBoxBootStart.setChecked(isBootStart);

		int volume = mPrefs.getIntByStringResourceId(R.string.shared_prefs_key_name_chime_volume, 100);
		mSeekBarVolume.setProgress(volume);

		mTvVolume.setText("Volume:" + String.valueOf(volume));
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		boolean isBootStart = mCheckBoxBootStart.isChecked();
		mPrefs.putBooleanByStringResourceId(R.string.shared_prefs_key_name_boot_start, isBootStart).commit();

		//int barVolume = mSeekBarVolume.getProgress();
		//mPrefs.putIntByStringResourceId(R.string.shared_prefs_key_name_chime_volume, barVolume).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		MenuItem item1 = menu.add(0, 0, 0, "音テスト");
		item1.setIcon(android.R.drawable.ic_popup_reminder);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == 0)
		{
			Intent intent = new Intent(ChimeAlarmActivity.this, AlarmReceiver.class);
			intent.setAction(getString(R.string.ACTION_TEST_RING_ALARM));
			sendBroadcast(intent);
		}

		return true;
	}
}