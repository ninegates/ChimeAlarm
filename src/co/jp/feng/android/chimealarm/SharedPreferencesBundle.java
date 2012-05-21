package co.jp.feng.android.chimealarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesBundle
{

	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	private Context mContext;

	public Editor getEditor()
	{
		if (mEditor == null) mEditor = mSharedPreferences.edit();

		return mEditor;
	}

	public SharedPreferencesBundle(Context context, String name, int mode)
	{
		mSharedPreferences = context.getSharedPreferences(name, mode);
		mContext = context;
	}

	public SharedPreferencesBundle(Context context, int stringResourceId, int mode)
	{
		String name = context.getString(stringResourceId);
		mSharedPreferences = context.getSharedPreferences(name, mode);
		mContext = context;
	}

	public boolean contains(int key)
	{
		String strKey = String.valueOf(key);
		return mSharedPreferences.contains(strKey);
	}

	public boolean getBoolean(int key, boolean defValue)
	{
		String strKey = String.valueOf(key);
		return mSharedPreferences.getBoolean(strKey, defValue);
	}

	public float getFloat(int key, float defValue)
	{
		String strKey = String.valueOf(key);
		return mSharedPreferences.getFloat(strKey, defValue);
	}

	public int getInt(int key, int defValue)
	{
		String strKey = String.valueOf(key);
		return mSharedPreferences.getInt(strKey, defValue);
	}

	public long getLong(int key, long defValue)
	{
		String strKey = String.valueOf(key);
		return mSharedPreferences.getLong(strKey, defValue);
	}

	public String getString(int key, String defValue)
	{
		String strKey = String.valueOf(key);
		return mSharedPreferences.getString(strKey, defValue);
	}


	public boolean getBooleanByStringResourceId(int stringResourceId, boolean defValue)
	{
		String strKey = mContext.getString(stringResourceId);
		return mSharedPreferences.getBoolean(strKey, defValue);
	}

	public float getFloatByStringResourceId(int stringResourceId, float defValue)
	{
		String strKey = mContext.getString(stringResourceId);
		return mSharedPreferences.getFloat(strKey, defValue);
	}

	public int getIntByStringResourceId(int stringResourceId, int defValue)
	{
		String strKey = mContext.getString(stringResourceId);
		return mSharedPreferences.getInt(strKey, defValue);
	}

	public long getLongByStringResourceId(int stringResourceId, long defValue)
	{
		String strKey = mContext.getString(stringResourceId);
		return mSharedPreferences.getLong(strKey, defValue);
	}

	public String getStringByStringResourceId(int stringResourceId, String defValue)
	{
		String strKey = mContext.getString(stringResourceId);
		return mSharedPreferences.getString(strKey, defValue);
	}

	public boolean containsByStringResourceId(int stringResourceId)
	{
		String strKey = mContext.getString(stringResourceId);
		return mSharedPreferences.contains(strKey);
	}

	public boolean contains(String key)
	{
		return mSharedPreferences.contains(key);
	}


	// Editor
	public Editor putString(int key, String value)
	{
		String strKey = String.valueOf(key);
		return getEditor().putString(strKey, value);
	}
	public Editor putInt(int key, int value)
	{
		String strKey = String.valueOf(key);
		return getEditor().putInt(strKey, value);
	}
	public Editor putLong(int key, long value)
	{
		String strKey = String.valueOf(key);
		return getEditor().putLong(strKey, value);
	}
	public Editor putFloat(int key, float value)
	{
		String strKey = String.valueOf(key);
		return getEditor().putFloat(strKey, value);
	}
	public Editor putBoolean(int key, boolean value)
	{
		String strKey = String.valueOf(key);
		return getEditor().putBoolean(strKey, value);
	}

	public Editor putStringByStringResourceId(int stringResourceId, String value)
	{
		String strKey = mContext.getString(stringResourceId);
		return getEditor().putString(strKey, value);
	}
	public Editor putIntByStringResourceId(int stringResourceId, int value)
	{
		String strKey = mContext.getString(stringResourceId);
		return getEditor().putInt(strKey, value);
	}
	public Editor putLongByStringResourceId(int stringResourceId, long value)
	{
		String strKey = mContext.getString(stringResourceId);
		return getEditor().putLong(strKey, value);
	}
	public Editor putFloatByStringResourceId(int stringResourceId, float value)
	{
		String strKey = mContext.getString(stringResourceId);
		return getEditor().putFloat(strKey, value);
	}
	public Editor putBooleanByStringResourceId(int stringResourceId, boolean value)
	{
		String strKey = mContext.getString(stringResourceId);
		return getEditor().putBoolean(strKey, value);
	}

	public Editor putString(String key, String value)
	{
		return getEditor().putString(key, value);
	}
	public Editor putInt(String key, int value)
	{
		return getEditor().putInt(key, value);
	}
	public Editor putLong(String key, long value)
	{
		return getEditor().putLong(key, value);
	}
	public Editor putFloat(String key, float value)
	{
		return getEditor().putFloat(key, value);
	}
	public Editor putBoolean(String key, boolean value)
	{
		return getEditor().putBoolean(key, value);
	}
	public Editor remove(String key)
	{
		return getEditor().remove(key);
	}
	public Editor clear()
	{
		return getEditor().clear();
	}
	public boolean commit()
	{
		return getEditor().commit();
	}


}
