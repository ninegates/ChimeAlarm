package co.jp.feng.android.chimealarm;

public class NextChimeEntity
{
	public long nextTime;
	public int chimeType;

	public NextChimeEntity(long nextTime, int chimeType)
	{
		this.nextTime = nextTime;
		this.chimeType = chimeType;
	}
}
