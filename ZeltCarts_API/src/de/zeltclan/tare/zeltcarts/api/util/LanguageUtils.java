package de.zeltclan.tare.zeltcarts.api.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helps with localization
 * Handles the ResourceBundle with given language
 * Localizes numbers to Locale-specific String
 * Defaults to Locale.ENGLISH
 * @author Mario
 *
 */
public class LanguageUtils {

	private final ResourceBundle bundle;

	/**
	 * Constructor initialize ResourceBundle
	 * @param p_resource - Name of the ResourceBundle to use
	 * @param p_language - Code of the language to use
	 */
	public LanguageUtils (String p_resource, String p_language) {
		Locale.setDefault(Locale.ENGLISH);
		bundle = ResourceBundle.getBundle(p_resource, new Locale(p_language.toLowerCase()));
	}

	/**
	 * Simple method to get localized String
	 * @param p_key - String of the key that identify needed localized String
	 * @return Localized String
	 */
	public String getString(String p_key) {
		return bundle.getString(p_key);
	}

	/**
	 * Method to get localized String
	 * @param p_key - String of the key that identify needed localized String
	 * @param p_args - Array of type Object that contains parameters for localized Strings. Numbers are localized
	 * @return Localized SString
	 */
	public String getString(String p_key, Object[] p_args) {
		for (int i = 0; i < p_args.length; i++) {
			if (p_args[i] instanceof Date) {
				p_args[i] = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, bundle.getLocale()).format(p_args[i]);
			} else if (p_args[i] instanceof Number) {
				p_args[i] = NumberFormat.getNumberInstance(bundle.getLocale()).format(p_args[i]);
			}
		}
		MessageFormat formatter = new MessageFormat(bundle.getString(p_key),bundle.getLocale());
		return formatter.format(p_args);
	}
}
