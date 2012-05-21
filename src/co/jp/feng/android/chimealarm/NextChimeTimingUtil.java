package co.jp.feng.android.chimealarm;

import java.util.Calendar;

import android.util.Log;

public class NextChimeTimingUtil
{
	public static final int TIME_MS_0930 = ((60 * 30) + (60 * 60 * 9)) * 1000;
	public static final int TIME_MS_1020 = ((60 * 20) + (60 * 60 * 10)) * 1000;
	public static final int TIME_MS_1030 = TIME_MS_1020 + (1000 * 60 * 10);
	public static final int TIME_MS_1120 = ((60 * 20) + (60 * 60 * 11)) * 1000;
	public static final int TIME_MS_1130 = TIME_MS_1120 + (1000 * 60 * 10);
	public static final int TIME_MS_1220 = ((60 * 20) + (60 * 60 * 12)) * 1000;
	public static final int TIME_MS_1330 = ((60 * 30) + (60 * 60 * 13)) * 1000;
	public static final int TIME_MS_1410 = ((60 * 10) + (60 * 60 * 14)) * 1000;
	public static final int TIME_MS_1420 = TIME_MS_1410 + (1000 * 60 * 10);
	public static final int TIME_MS_1510 = ((60 * 10) + (60 * 60 * 15)) * 1000;
	public static final int TIME_MS_1520 = TIME_MS_1510 + (1000 * 60 * 10);
	public static final int TIME_MS_1610 = ((60 * 10) + (60 * 60 * 16)) * 1000;

	private static final String TAG = NextChimeTimingUtil.class.getSimpleName();

	private static final String[][] ARY_CHIME_SETTINGS = {
		{"0930", "0"},
		{"1020", "0"},
		{"1030", "0"},
		{"1120", "0"},
		{"1130", "0"},
		{"1220", "0"},
		{"1330", "0"},
		{"1410", "0"},
		{"1420", "0"},
		{"1510", "0"},
		{"1520", "0"},
		{"1610", "0"},
	};

	static
	{
		initialize();
	}

	static private void initialize()
	{
		Log.v(TAG, "initialize");
	}

	static public NextChimeEntity getNextChime()
	{
//		if (true)
//		{
//			return new NextChimeEntity(System.currentTimeMillis() + 15000, 0);
//		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 5); // 念のため

		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int ms = cal.get(Calendar.MILLISECOND);
		int hmm = (hour * 60 * 60 + min * 60) * 1000 + ms;
		int targetMs = 0;

		if (hmm < TIME_MS_0930)
		{
			targetMs = TIME_MS_0930;
		}
		else if (hmm > TIME_MS_1610)
		{
			targetMs = TIME_MS_0930;
			cal.add(Calendar.DATE, 1); // 翌日
		}
		else if (hmm > TIME_MS_1520)
		{
			targetMs = TIME_MS_1610;
		}
		else if (hmm > TIME_MS_1510)
		{
			targetMs = TIME_MS_1520;
		}
		else if (hmm > TIME_MS_1420)
		{
			targetMs = TIME_MS_1510;
		}
		else if (hmm > TIME_MS_1410)
		{
			targetMs = TIME_MS_1420;
		}
		else if (hmm > TIME_MS_1330)
		{
			targetMs = TIME_MS_1410;
		}
		else if (hmm > TIME_MS_1220)
		{
			targetMs = TIME_MS_1330;
		}
		else if (hmm > TIME_MS_1130)
		{
			targetMs = TIME_MS_1220;
		}
		else if (hmm > TIME_MS_1120)
		{
			targetMs = TIME_MS_1130;
		}
		else if (hmm > TIME_MS_1030)
		{
			targetMs = TIME_MS_1120;
		}
		else if (hmm > TIME_MS_1020)
		{
			targetMs = TIME_MS_1030;
		}
		else if (hmm > TIME_MS_0930)
		{
			targetMs = TIME_MS_1020;
		}


		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		cal.add(Calendar.MILLISECOND, targetMs);


		return new NextChimeEntity(cal.getTimeInMillis(), 0);
	}
}
