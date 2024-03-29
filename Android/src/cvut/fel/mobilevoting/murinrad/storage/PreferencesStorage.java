/*
Copyright 2011 Radovan Murin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package cvut.fel.mobilevoting.murinrad.storage;

import cvut.fel.mobilevoting.murinrad.main;
import android.content.SharedPreferences;
/**
 * A class for simple data storage - password HASH
 * @author Radovan Murin
 *
 */
public class PreferencesStorage {
	public static int PASSWORD_HASH = 0;
	private final String shadowFile = "shadow";
	private SharedPreferences shadow = null;
	public static PreferencesStorage store = new PreferencesStorage();

	private PreferencesStorage() {
		shadow = main.c.getSharedPreferences(shadowFile, 0);
	}
/**
 * Adds an entry
 * @param key the key to the entry
 * @param value the entry value
 */
	public void addEntry(int key, String value) {
		SharedPreferences.Editor editor = shadow.edit();
		switch (key) {
		case 0:
			editor.putString(resolveKeyID(key), value);
			break;
		default:
			break;
		}
		editor.commit();
	}

	public String getEntry(int key) {
		String entry = shadow.getString(resolveKeyID(key), "");
		return entry;
	}

	private String resolveKeyID(int key) {
		switch (key) {
		case 0:
			return "pHash";
		default:
			return null;
		}

	}

}
