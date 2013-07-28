package de.zeltclan.tare.zeltcarts.test.util;

public class VersionUtils {
	public static boolean isNewer(String p_version_old, String p_version_new) {
		String[] temp = p_version_old.split("\\.");
		final int[] versionOld = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			versionOld[i] = Integer.parseInt(temp[i]);
		}
		temp = p_version_new.split("\\.");
		final int[] versionNew = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			versionNew[i] = Integer.parseInt(temp[i]);
		}
		if (versionOld[0] < versionNew[0]) {
			return true;
		} else if (versionOld[0] == versionNew[0] && versionOld[1] < versionNew[1]) {
			return true;
		} else if (versionOld[0] == versionNew[0] && versionOld[1] == versionNew[1] && versionOld[2] < versionNew[2]) {
			return true;
		}
		return false;
	}
}
