package co.jp.feng.android.chimealarm;

import java.io.IOException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{
	//private static MediaPlayer sMp;
	private static SharedPreferencesBundle sPrefs;
	private static SparseArray<MediaPlayer> sAryMp;
	private static MediaPlayer sPrevPlayMp;

	private final int[] mArySoundRawId = {R.raw.se_maoudamashii_chime03};
	private final String TAG = getClass().getName();

	/* (非 Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		Log.v(TAG, "onReceive action=" + action);

		if (action == null)
		{
			Log.w(TAG, "action = null!!");
			return;
		}

		else if (action.equals(context.getString(R.string.ACTION_RING_ALARM)))
		{
			int chimeType = intent.getIntExtra(context.getString(R.string.intent_extra_chime_type), -1);
			ringChime(context, chimeType);
			setNextChime(context);
		}
		else if (action.equals(context.getString(R.string.ACTION_CANCEL_ALARM)))
		{
			if (sPrevPlayMp != null && sPrevPlayMp.isPlaying())
			{
				sPrevPlayMp.stop();
				try
				{
					sPrevPlayMp.prepare();
				}
				catch (IllegalStateException e)
				{
					Log.e(TAG, "prepare IllegalStateException=" + e.toString());
					e.printStackTrace();
				}
				catch (IOException e)
				{
					Log.e(TAG, "prepare IOException. e=" + e.toString());
					e.printStackTrace();
				}
			}
		}
		else if (action.equals(context.getString(R.string.ACTION_TEST_RING_ALARM)))
		{
			ringChime(context, 0);
		}
		else if (action.equals("android.intent.action.BOOT_COMPLETED"))
		{
			SharedPreferencesBundle prefs = getmPrefs(context);
			boolean isChimeSet = prefs.getBooleanByStringResourceId(R.string.shared_prefs_key_name_chime_set, false);
			boolean isBootStart = prefs.getBooleanByStringResourceId(R.string.shared_prefs_key_name_boot_start, false);

			Log.v(TAG, "android.intent.action.BOOT_COMPLETED isChimeSet=" + isChimeSet + " isBootStart=" + isBootStart);

			if (isChimeSet)
			{
				if (isBootStart)
				{
					int afterSec = setNextChime(context);
					Toast.makeText(context, "[BOOT] チャイムをセットしました。 " + afterSec + "秒後に鳴ります。", Toast.LENGTH_SHORT).show();
					Log.v(TAG, "チャイムをセット sec=" + afterSec);
				}
				else
				{
					prefs.putBooleanByStringResourceId(R.string.shared_prefs_key_name_chime_set, false).commit();
					Log.v(TAG, "チャイムONかつ起動時セット無効だったのでチェック入れなおし");
				}
			}
		}


	}

	/**
	 * SharedPreferencesBundleクラス取得
	 * @return SharedPreferencesBundle
	 */
	private SharedPreferencesBundle getmPrefs(Context context)
	{
		if (sPrefs == null)
		{
			sPrefs = new SharedPreferencesBundle(context, R.string.shared_prefs_file_name, Context.MODE_PRIVATE);
		}
		return sPrefs;
	}

	/**
	 * 対象MediaPlayer取得
	 * @return MediaPlayer
	 */
	private MediaPlayer getMediaPlayer(Context context, int chimeType)
	{
		if (chimeType == -1)
		{
			Log.v(TAG, "chimeType == -1");
			return null;
		}

		if (sAryMp == null)
		{
			Log.v(TAG, "sAryMp == null");
			sAryMp = new SparseArray<MediaPlayer>(8); // とりあえず8
		}

		MediaPlayer targetMp = sAryMp.get(chimeType);

		if (targetMp == null)
		{
			int rawId;

			try
			{
				rawId = mArySoundRawId[chimeType];
			}
			catch (Exception e) {
				Log.e(TAG, "getMediaPlayer Exception. e=" + e.toString());
				return null;
			}

			targetMp = MediaPlayer.create(context, rawId);
			sAryMp.put(chimeType, targetMp);
		}

		Log.v(TAG, "getMediaPlayer success.");
		return targetMp;
	}

	/**
	 * チャイムを鳴らす
	 *
	 * @param context
	 */
	private void ringChime(Context context, int chimeType)
	{
		SharedPreferencesBundle prefs = getmPrefs(context);
		int prefsVolume = prefs.getIntByStringResourceId(R.string.shared_prefs_key_name_chime_volume, 100);

		MediaPlayer mp = getMediaPlayer(context, chimeType);
		if (mp == null)
		{
			Log.w(TAG, "ringChime mp == null");
			return;
		}

		// スクリーンオン
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		//PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wake");
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, context.getString(R.string.app_name));
		wl.acquire(15000);
		//wl.acquire();

		if (mp.isPlaying())
		{
			mp.stop();
			try
			{
				mp.prepare();
			}
			catch (IllegalStateException e)
			{
				Log.e(TAG, "ringChime prepare IllegalStateException e=" + e.toString());
				e.printStackTrace();
			}
			catch (IOException e)
			{
				Log.e(TAG, "ringChime prepare IOException e=" + e.toString());
				e.printStackTrace();
			}

			Log.v(TAG, "チャイムが鳴っている途中だったので停止");
		}

		try
		{
			float setVolume = prefsVolume / 100.0f;
			mp.setVolume(setVolume, setVolume);
			mp.seekTo(0);
			mp.start();
		}
		catch (IllegalStateException e)
		{
			Log.e(TAG, "ringChime start IllegalStateException e=" + e.toString());
			e.printStackTrace();
		}

		sPrevPlayMp = mp;

		Log.v(TAG, "ringChime success.");
	}

	/**
	 * 次回のチャイムを設定する
	 *
	 * @param context
	 * @return int 何秒後に鳴るか
	 */
	private int setNextChime(Context context)
	{
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		NextChimeEntity chimeEntity = NextChimeTimingUtil.getNextChime();
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(context.getString(R.string.ACTION_RING_ALARM));
		intent.putExtra(context.getString(R.string.intent_extra_chime_type), chimeEntity.chimeType);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, chimeEntity.nextTime, pendingIntent);

		int afterSeconds = (int) (chimeEntity.nextTime - System.currentTimeMillis()) / 1000;
		Log.v(TAG, "次回のチャイムを設定 " + afterSeconds + "秒後");

		return afterSeconds;
	}

}
