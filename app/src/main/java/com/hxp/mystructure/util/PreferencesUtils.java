package com.hxp.mystructure.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreferencesUtils {
	private static SharedPreferences mPrefs;
	private static Gson gson;

	/**
	 * Initialize the Prefs helper class to keep a reference to the
	 * SharedPreference for this application the SharedPreference will use the
	 * package name of the application as the Key.
	 *
	 * @param context the Application context.
	 */
	public static void initPrefs(Context context) {
		if (mPrefs == null) {
			String key = context.getPackageName();
			if (key == null) {
				throw new NullPointerException("Prefs key may not be null");
			}
			if (gson == null)
				gson = new Gson();
			mPrefs = context.getSharedPreferences(key, Context.MODE_PRIVATE);
		}
	}

	/**
	 * Returns an instance of the shared preference for this app.
	 *
	 * @return an Instance of the SharedPreference
	 */
	public static SharedPreferences getPreferences() {
		if (mPrefs != null) {
			return mPrefs;
		}
		throw new RuntimeException(
				"Prefs class not correctly instantiated please call Prefs.iniPrefs(context) in the Application class onCreate.");
	}

	/**
	 * @return Returns a map containing a list of pairs key/value representing
	 * the preferences.
	 * @see SharedPreferences#getAll()
	 */
	public static Map<String, ?> getAll() {
		return getPreferences().getAll();
	}


	/**
	 * 将实体bean存入
	 */
	public static void putBean(Serializable value) {
		try {
			if (value == null) return;
			String key = value.getClass().getSimpleName();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(value);
			String string64 = new String(Base64.encode(baos.toByteArray(),
					0));
			Editor editor = getPreferences().edit();
			editor.putString(key, string64);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
				editor.commit();
			else
				editor.apply();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getBean(Class<? extends Serializable> cls) {
		Serializable value = null;
		try {
			String key = cls.getSimpleName();
			String base64 = getString(key, "");
			if (base64.equals("")) {
				return null;
			}
			byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			value = (Serializable) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) value;
	}


	/**
	 * @param key      The name of the preference to retrieve.
	 * @param defValue Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 * ClassCastException if there is a preference with this name that
	 * is not an int.
	 * @see SharedPreferences#getInt(String, int)
	 */
	public static int getInt(final String key, final int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	/**
	 * @param key      The name of the preference to retrieve.
	 * @param defValue Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 * ClassCastException if there is a preference with this name that
	 * is not a boolean.
	 * @see SharedPreferences#getBoolean(String, boolean)
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	/**
	 * @param key      The name of the preference to retrieve.
	 * @param defValue Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 * ClassCastException if there is a preference with this name that
	 * is not a long.
	 * @see SharedPreferences#getLong(String, long)
	 */
	public static long getLong(final String key, final long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	/**
	 * @param key      The name of the preference to retrieve.
	 * @param defValue Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 * ClassCastException if there is a preference with this name that
	 * is not a float.
	 * @see SharedPreferences#getFloat(String, float)
	 */
	public static float getFloat(final String key, final float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	/**
	 * @param key      The name of the preference to retrieve.
	 * @param defValue Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 * ClassCastException if there is a preference with this name that
	 * is not a String.
	 * @see SharedPreferences#getString(String, String)
	 */
	public static String getString(final String key, final String defValue) {
		return getPreferences().getString(key, defValue);
	}

	public double getDouble(String key) {
		String stringValue = getString(key, "");
		try {
			double value = Double.parseDouble(stringValue);
			return value;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * @param key   The name of the preference to modify.
	 * @param value The new value for the preference.
	 * @see Editor#putLong(String, long)
	 */
	public static void putLong(final String key, final long value) {
		final Editor editor = getPreferences().edit();
		editor.putLong(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key   The name of the preference to modify.
	 * @param value The new value for the preference.
	 * @see Editor#putInt(String, int)
	 */
	public static void putInt(final String key, final int value) {
		final Editor editor = getPreferences().edit();
		editor.putInt(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key   The name of the preference to modify.
	 * @param value The new value for the preference.
	 * @see Editor#putFloat(String, float)
	 */
	public static void putFloat(final String key, final float value) {
		final Editor editor = getPreferences().edit();
		editor.putFloat(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key   The name of the preference to modify.
	 * @param value The new value for the preference.
	 * @see Editor#putBoolean(String, boolean)
	 */
	public static void putBoolean(final String key, final boolean value) {
		final Editor editor = getPreferences().edit();
		editor.putBoolean(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key   The name of the preference to modify.
	 * @param value The new value for the preference.
	 * @see Editor#putString(String, String)
	 */
	public static void putString(final String key, final String value) {
		final Editor editor = getPreferences().edit();
		editor.putString(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key   The name of the preference to modify.
	 * @param value The new value for the preference.
	 * @see Editor#putStringSet(String,
	 * Set)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putStringSet(final String key, final Set<String> value) {
		final Editor editor = getPreferences().edit();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			editor.putStringSet(key, value);
		} else {
			// Workaround for pre-HC's lack of StringSets
			int stringSetLength = 0;
			if (mPrefs.contains(key + "#LENGTH")) {
				// First read what the value was
				stringSetLength = mPrefs.getInt(key + "#LENGTH", -1);
			}
			editor.putInt(key + "#LENGTH", value.size());
			int i = 0;
			for (String aValue : value) {
				editor.putString(key + "[" + i + "]", aValue);
				i++;
			}
			for (; i < stringSetLength; i++) {
				// Remove any remaining values
				editor.remove(key + "[" + i + "]");
			}
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key      The name of the preference to retrieve.
	 * @param defValue Value to return if this preference does not exist.
	 * @return Returns the preference values if they exist, or defValues. Throws
	 * ClassCastException if there is a preference with this name that
	 * is not a Set.
	 * @see SharedPreferences#getStringSet(String,
	 * Set)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Set<String> getStringSet(final String key, final Set<String> defValue) {
		SharedPreferences prefs = getPreferences();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return prefs.getStringSet(key, defValue);
		} else {
			if (prefs.contains(key + "#LENGTH")) {
				HashSet<String> set = new HashSet<String>();
				// Workaround for pre-HC's lack of StringSets
				int stringSetLength = prefs.getInt(key + "#LENGTH", -1);
				if (stringSetLength >= 0) {
					for (int i = 0; i < stringSetLength; i++) {
						prefs.getString(key + "[" + i + "]", null);
					}
				}
				return set;
			}
		}
		return defValue;
	}

	public static void putList(String key, ArrayList<String> marray) {
		Editor editor = getPreferences().edit();
		String[] mystringlist = marray.toArray(new String[marray.size()]);
		// the comma like character used below is not a comma it is the SINGLE
		// LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
		// seprating the items in the list
		editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	public static ArrayList<String> getList(String key) {
		// the comma like character used below is not a comma it is the SINGLE
		// LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
		// seprating the items in the list
		String[] mylist = TextUtils.split(getPreferences().getString(key, ""), "‚‗‚");
		return new ArrayList<String>(Arrays.asList(mylist));
	}

	public void putListInt(String key, ArrayList<Integer> marray, Context context) {
		Editor editor = getPreferences().edit();
		Integer[] mystringlist = marray.toArray(new Integer[marray.size()]);
		// the comma like character used below is not a comma it is the SINGLE
		// LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
		// seprating the items in the list
		editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	public ArrayList<Integer> getListInt(String key, Context context) {
		// the comma like character used below is not a comma it is the SINGLE
		// LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
		// seprating the items in the list
		String[] mylist = TextUtils.split(getPreferences().getString(key, ""), "‚‗‚");
		ArrayList<String> gottenlist = new ArrayList<String>(Arrays.asList(mylist));
		ArrayList<Integer> gottenlist2 = new ArrayList<Integer>();
		for (int i = 0; i < gottenlist.size(); i++) {
			gottenlist2.add(Integer.parseInt(gottenlist.get(i)));
		}

		return gottenlist2;
	}

	public void putListBoolean(String key, ArrayList<Boolean> marray) {
		ArrayList<String> origList = new ArrayList<String>();
		for (Boolean b : marray) {
			if (b == true) {
				origList.add("true");
			} else {
				origList.add("false");
			}
		}
		putList(key, origList);
	}

	public ArrayList<Boolean> getListBoolean(String key) {
		ArrayList<String> origList = getList(key);
		ArrayList<Boolean> mBools = new ArrayList<Boolean>();
		for (String b : origList) {
			if (b.equals("true")) {
				mBools.add(true);
			} else {
				mBools.add(false);
			}
		}
		return mBools;
	}

	/**
	 * @param key The name of the preference to remove.
	 * @see Editor#remove(String)
	 */
	public static void remove(final String key) {
		SharedPreferences prefs = getPreferences();
		final Editor editor = prefs.edit();
		if (prefs.contains(key + "#LENGTH")) {
			// Workaround for pre-HC's lack of StringSets
			int stringSetLength = prefs.getInt(key + "#LENGTH", -1);
			if (stringSetLength >= 0) {
				editor.remove(key + "#LENGTH");
				for (int i = 0; i < stringSetLength; i++) {
					editor.remove(key + "[" + i + "]");
				}
			}
		}
		editor.remove(key);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key The name of the preference to check.
	 * @see SharedPreferences#contains(String)
	 */
	public static boolean contains(final String key) {
		return getPreferences().contains(key);
	}

	public void clear() {
		final Editor editor = getPreferences().edit();
		editor.clear();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
		getPreferences().registerOnSharedPreferenceChangeListener(listener);
	}

	public void unregisterOnSharedPreferenceChangeListener(
			SharedPreferences.OnSharedPreferenceChangeListener listener) {
		getPreferences().unregisterOnSharedPreferenceChangeListener(listener);
	}
}
