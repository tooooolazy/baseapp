/**
 * 
 */
package com.tooooolazy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.apache.commons.lang.StringUtils;

//import com.tooooolazy.fonts.TLZFontEnum;

/**
 * @author tooooolazy
 * 
 */
public class TLZUtils {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
	public static SimpleDateFormat sdfGreece = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat sdtfGreece = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/**
	 * Creates an instance of the class represented by the given string using the default constructor.
	 * @param objectClass
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object loadObject(String objectClass) throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		Logger.getLogger("TLZUtils").info("Loading " + objectClass);
		Object obj = null;

		Class theClass = Class.forName(objectClass);
		Constructor c = theClass.getConstructor(null);

		obj = c.newInstance(null);

		return obj;
	}

	/**
	 * Creates an instance of the given Class by invoking its constructor with the given arguments and given types.
	 * @param objectClass
	 * @param types
	 * @param args
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object loadObject(String objectClass, Class[] types, Object[] args) throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {

		// Logger.getLogger("TLZUtils").info("Loading " + objectClass);
		Object obj = null;

		Class theClass = Class.forName(objectClass);
		Constructor c = theClass.getConstructor(types);

		obj = c.newInstance(args);

		return obj;
	}

	/**
	 * Creates an instance of the given Class by invoking its constructor with the given arguments.
	 * @param theClass
	 * @param args
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object loadObject(Class theClass, Object[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		// Logger.getLogger("TLZUtils").info("Loading " + objectClass);
		Object obj = null;

		Class[] types = null;
		try {
			types = new Class[args.length];
			for (int i = 0; i < types.length; i++)
				types[i] = args[i].getClass();
		} catch (NullPointerException e) {
			// no params
		}

		Constructor c = theClass.getConstructor(types);

		obj = c.newInstance(args);

		return obj;
	}

	public static Object invokeMethod(Object obj, String method, Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// System.out.println("Invoking " + obj.getClass().toString() +"."+method);
		Class[] c = null;
		try {
			c = new Class[args.length];
			for (int i = 0; i < c.length; i++)
				c[i] = args[i].getClass();
		} catch (NullPointerException e) {
			// no params
		}

		return invokeMethod(obj, method, c, args);
	}

	public static Object invokeMethod(Object obj, String method, Class[] types, Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// System.out.println("Invoking " + obj.getClass().toString() +"."+method);
		Class tc = obj.getClass();
		Object res = null;

		Method m = tc.getMethod(method, types);
		res = m.invoke(obj, args);

		return res;
	}

	/**
	 * Invokes a static method of the given class
	 * @param tc
	 * @param method
	 * @param args
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(Class tc, String method, Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Logger.getLogger("TLZUtils").info("Invoking " + tc.toString() + "." + method);
		Object res = null;

		Class[] c = null;
		try {
			c = new Class[args.length];
			for (int i = 0; i < c.length; i++)
				c[i] = args[i].getClass();
		} catch (NullPointerException e) {
			// no params
		}

		Method m = tc.getMethod(method, c);
		res = m.invoke(null, args);

		return res;
	}

	/**
	 * Helper to get the value of a public static field of a Class 
	 * @param _c
	 * @param field
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	public static Object getStaticField(Class _c, String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
		// System.out.println("Invoking " + obj.getClass().toString() +"."+method);
		Object res = null;
		Field f = _c.getDeclaredField(field);
		return f.get(loadObject(_c.getName()));
	}

	public static Object invokeMethod(String _tc, String method, Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		Class tc = Class.forName(_tc);
		
		return invokeMethod(tc, method, args);
	}

	public static Class getPropertyType(Object obj, String _vc) {
		String method = "get" + StringUtils.capitalize(_vc);
		Class c = obj.getClass();
		try {
			Method m = c.getMethod(method);
			return m.getReturnType();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Creates the method name representing the getter of the given property and invokes it on the given object.
	 * The method name creation assumes it begins with 'get' and the next letter is capitalized.
	 * @param obj
	 * @param _vc
	 * @return
	 */
	public static Object invokeGetterByProperty(Object obj, String _vc) {
		String method = "get" + StringUtils.capitalize(_vc);
		return invokeGetter(obj, method);
	}
	/**
	 * Same as {@link #invokeGetterByProperty(Object, String)} except the method name should be the exact one as defined in the object.
	 * @param obj
	 * @param method
	 * @return
	 */
	public static Object invokeGetter(Object obj, String method) {
		try {
			Object v = invokeMethod(obj, method, null);
			return v;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Method getGetterByProperty(Class c, String property) {
		try {
			return c.getMethod("get" + StringUtils.capitalize(property));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Similar to <code>invokeGetterByProperty</code>. Creates the method name representing the setter of the given property and invokes it on the given object.
	 * The method name creation assumes it begins with 'set' and the next letter is capitalized.
	 * @param obj
	 * @param _vc
	 * @return
	 */
	public static Object invokeSetterByProperty(Object obj, String _vc, Object value) {
		String method = "set" + StringUtils.capitalize(_vc);
		try {
			if (value != null)
				invokeSetter(obj, method, value.getClass(), value);
			else {
				Class c = getPropertyType(obj, _vc);
				invokeSetter(obj, method, c, value);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Not really working if value is null...
	 * @param obj
	 * @param method
	 * @param value
	 */
	public static void invokeSetter(Object obj, String method, Object value) {
		try {
			invokeMethod(obj, method, new Object[] {value});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Invokes the given setter method on the given object.
	 * To avoid <code>NoSuchMethodException</code> as much as possible, recursion is used to try superclass of value as type until the method is found and invoked.
	 * When superclass is used, the value is also casted to the same type (this avoids <code>IllegalArgumentException</code>)
	 * @param obj
	 * @param method
	 * @param type
	 * @param value
	 */
	public static void invokeSetter(Object obj, String method, Class type, Object value) {
		try {
			invokeMethod(obj, method, new Class[] {type}, new Object[] {type.cast(value)});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if (type.getSuperclass() != null)
				invokeSetter(obj, method, type.getSuperclass(), value);
			else
				e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if the property getter method of the object has the given annotation class
	 * @param dummy
	 * @param string
	 * @param class1
	 */
	public static boolean hasAnnotation(Object dummy, String property, Class cls) {
		Method m = getGetterByProperty(dummy.getClass(), property);
		if (m.getAnnotation(cls) != null) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(millisToTime(100000000));
		System.out.println(toUpperCase("Καραϊσκάκη"));
		double d = similarity("ΑΜΟΡΓΙΑΝΟΙ", "Αμόργιανοι");
		d = similarity("30009, ΜΑΧΑΙΡΑΣ, ΑΙΤΩΛΟΑΚΑΡΝΑΝΙΑΣ, ΚΥΡΙΟ, ΗΛ., ΑΣΗΜΑΚΟΠΟΥΛΟ, DHKHGORO", "300 09, ΜΑΧΑΙΡΑΣ, ΑΙΤΩΛΟΑΚΑΡΝΑΝΙΑΣ, ΚΥΡΙΟ, ΗΛΙΑ, ΑΣΗΜΑΚΟΠΟΥΛΟ");
		d = similarity("Θήβα", "Θήρα");
		d = similarity("Αθήνα", "Αθηνά");
		d = similarity("ΚΥΡΙΟ", "ΚΥΡΙΟΣ");
		d = similarity("ΚΥΡΙΟ", "ΚΥΡΙΑ");
		d = similarity("Αγιος Βλάσιος", "Αγ. Βλάσιος");
		d = similarity("ΑΓΙΟΣ ΒΛΑΣΙΟΣ", "ΑΓ. ΒΛΑΣΙΟΣ");
		d = similarity("ΑΘΗΝΑ", "ΘΗΝΑ");
		d = similarity("MR", "MR.");
		d = similarity("USA", "U.S.A.");
		d = similarity("Μακροχώρι", "Μαυροχώρι");
		d = similarity("ΑΓ. ΠΑΡΑΣΚΕΥΗ", "ΑΓ.ΠΑΡΑΣΚΕΥΗ");
		// TODO Auto-generated method stub
		Properties properties = TLZUtils.loadProperties( "config.properties" );
		try {
			TLZMail.sendEmail(properties, new String[] {"gpatou@tooooolazy.com"}, new String[] {"gpatou@tooooolazy.com"}, new String[] {"gpatou@tooooolazy.com"},"testing", "test text", false);
			TLZMail.sendEmail(properties, new String[] {"gpatou@tooooolazy.com"}, new String[] {"gpatou@tooooolazy.com"}, new String[] {"gpatou@tooooolazy.com"},"testing", "<h1>test html</h1>", true);
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			TLZUtils.invokeMethod(Class.forName("com.tooooolazy.layout.ContactSegment"), "update", null);
			TLZUtils.loadObject("dgfs");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Tries to load a Properties file.
	 * @param propertiesFile
	 */
	public static Properties loadProperties(String propertiesFile) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = getInputStream(propertiesFile);
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * @param propertiesFile
	 * @return
	 */
	public static InputStream getInputStream(String file) {
		InputStream in = ClassLoader.getSystemResourceAsStream(file);
		if (in == null) {
			in = TLZUtils.class.getResourceAsStream(file);

			if (in == null) {
				// works when deployed on Tomcat
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
			}
		}
		return in;
	}
	public static InputStream getInputStream(File file) throws FileNotFoundException {
		InputStream in = new FileInputStream(file);
		return in;
	}

	public static void playAudioFile(String fileName) {
		File soundFile = new File(fileName);
		try {
			// Create a stream from the given file.
			// Throws IOException or UnsupportedAudioFileException
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			// AudioSystem.getAudioInputStream( inputStream ); // alternate
			// audio stream from inputstream
			playAudioStream(audioInputStream);
		} catch (Exception e) {
			Logger.getLogger("TLZUtils").warning("Problem with file " + fileName + ":");
			e.printStackTrace();
		}
	} // playAudioFile

	/** Plays audio from the given audio input stream. */
	public static void playAudioStream(AudioInputStream audioInputStream) {
		// Audio format provides information like sample rate, size, channels.
		AudioFormat audioFormat = audioInputStream.getFormat();
		Logger.getLogger("TLZUtils").info("Play input audio format=" + audioFormat);
		// Open a data line to play our type of sampled audio.
		// Use SourceDataLine for play and TargetDataLine for record.
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		if (!AudioSystem.isLineSupported(info)) {
			Logger.getLogger("TLZUtils").info("Play.playAudioStream does not handle this type of audio on this system.");
			return;
		}
		try {
			// Create a SourceDataLine for play back (throws
			// LineUnavailableException).
			SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info);
			// System.out.println( "SourceDataLine class=" + dataLine.getClass()
			// );

			// The line acquires system resources (throws
			// LineAvailableException).
			dataLine.open(audioFormat);

			// Adjust the volume on the output line.
			if (dataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				FloatControl volume = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(100.0F);
			}

			// Allows the line to move data in and out to a port.
			dataLine.start();

			// Create a buffer for moving data from the audio stream to the
			// line.
			int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
			byte[] buffer = new byte[bufferSize];
			// Move the data until done or there is an error.
			try {
				int bytesRead = 0;
				while (bytesRead >= 0) {
					bytesRead = audioInputStream.read(buffer, 0, buffer.length);
					if (bytesRead >= 0) {
						// System.out.println( "Play.playAudioStream bytes
						// read=" + bytesRead +
						// ", frame size=" + audioFormat.getFrameSize() + ",
						// frames read=" + bytesRead /
						// audioFormat.getFrameSize() );
						// Odd sized sounds throw an exception if we don't write
						// the same amount.
						int framesWritten = dataLine.write(buffer, 0, bytesRead);
					}
				} // while
			} catch (IOException e) {
				e.printStackTrace();
			}

			Logger.getLogger("TLZUtils").info("Play.playAudioStream draining line.");
			// Continues data line I/O until its buffer is drained.
			dataLine.drain();

			Logger.getLogger("TLZUtils").info("Play.playAudioStream closing line.");
			// Closes the data line, freeing any resources such as the audio
			// device.
			dataLine.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	} // playAudioStream

	public static boolean loadCreatePropertiesFile(File f, Properties props) {
		boolean created = false;

		if ( f.exists() ) {
			try {
				props.load(new FileInputStream( f ));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				created = f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return created;
	}

	//////////////////////
	// array utils
	public static Object[] addFirst(Object[] data, Object value) {
		Object[] _vc = new Object[data.length+1];

		_vc[0] = value;
		int i=1;
		for (Object o : data) {
			_vc[i++] = o;
		}
		data = _vc;

		return data;
	}
	public static Object[] addLast(Object[] data, Object value) {
		Object[] _vc = new Object[data.length+1];

		int i=0;
		for (Object o : data) {
			_vc[i++] = o;
		}
		_vc[i] = value;
		data = _vc;

		return data;
	}
	public static String[] addFirst(String[] data, String value) {
		String[] _vc = new String[data.length+1];

		_vc[0] = value;
		int i=1;
		for (String o : data) {
			_vc[i++] = o;
		}
		data = _vc;

		return data;
	}
	public static String[] addLast(String[] data, String value) {
		String[] _vc = new String[data.length+1];

		int i=0;
		for (String o : data) {
			_vc[i++] = o;
		}
		_vc[i] = value;
		data = _vc;

		return data;
	}
	public static <T> T[] moveFirst(T[] data, T value) {
		int i=0;
		for (T o : data) {
			if (o.equals(value))
				break;
			i++;
		}
		if (i == 0 || i == data.length)
			return data;

		for (int ni=i; ni>0; ni--) {
			data[ni] = data[ni-1];
		}
		data[0] = value;

		return data;
	}
	public static <T> T[] moveLast(T[] data, T value) {
		int i=0;
		for (T o : data) {
			if (o.equals(value))
				break;
			i++;
		}
		if (i >= data.length-1)
			return data;

		for (int ni=i; ni<data.length-1; ni++) {
			data[ni] = data[ni+1];
		}
		data[data.length-1] = value;

		return data;
	}

	/**
	 * Checks if <code>anagram</code> is an anagram of <code>word</code>
	 * @param word
	 * @param anagram
	 * @return
	 */
	public static boolean iAnagram(String word, String anagram){
        char[] charFromWord = word.toCharArray();
        char[] charFromAnagram = anagram.toCharArray();       
        Arrays.sort(charFromWord);
        Arrays.sort(charFromAnagram);
       
        return Arrays.equals(charFromWord, charFromAnagram);
    }

	/**
	 * Compares 2 words using Levenshtein distance algorithm {@link TLZUtils#computeEditDistance computeEditDistance}
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static double similarity(String s1, String s2) {
	    if (s1.length() < s2.length()) { // s1 should always be bigger
	        String swap = s1; s1 = s2; s2 = swap;
	    }
	    int bigLen = s1.length();
	    if (bigLen == 0) { return 1.0; /* both strings are zero length */ }
	    return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
	}
	/**
	 * This is an implementation of Levenshtein distance algorithm
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int computeEditDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

	public static String toUpperCase(String str) {
		String ucStr = StringUtils.upperCase(str);
		return ucStr;
	}

	public static Object convertValue(Object value, Class c) {
		if (c.equals(Integer.class)) {
			return convertToInteger(value);
		}
		if (c.equals(String.class)) {
			return value.toString();
		}
		return value;
	}

	/**
	 * @param value
	 */
	public static Object convertToInteger(Object value) {
		if (value instanceof Integer)
			return value;
		if (value instanceof Double)
			return ((Double) value).intValue();
		if (value instanceof BigDecimal)
			return ((BigDecimal) value).intValue();
		try {
			if (value instanceof String)
				return Integer.parseInt((String) value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}
	public static String appendCurrentDate(String str) {
		return str + "_" + sdf.format(new Date());
	}
	public static String appendCurrentDateTime(String str) {
		return str + "_" + sdtf.format(new Date());
	}

	public static boolean isEmpty(String str) {
		if (str != null) {
			if (str.trim().length() > 0)
				return false;
		}
		return true;
	}
	public static boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }
	public static boolean contains(Object[] array, Object obj) {
		for (int i=0; i<array.length; i++) {
			if (array[i].equals( obj )) {
				return true;
			}
		}
		return false;
	}
	public static String toUpperCaseGreek(String str) {
		str = str.replaceAll("ά", "α");
		str = str.replaceAll("έ", "ε");
		str = str.replaceAll("ή", "η");
		str = str.replaceAll("ί", "ι");
		str = str.replaceAll("ό", "ο");
		str = str.replaceAll("ύ", "υ");
		str = str.replaceAll("ώ", "ω");
		return str.toUpperCase();
	}

	public static String millisToTime(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
		long secs = TimeUnit.MILLISECONDS.toSeconds(millis);
		if (days > 0) {
			return String.format("%d day, %d hour, %d min, %d sec", 
				days,
				hours - TimeUnit.DAYS.toHours(days),
				mins - TimeUnit.HOURS.toMinutes(hours),
			    secs - TimeUnit.MINUTES.toSeconds(mins)
			);
		} else if (hours > 0) {
			return String.format("%d hour, %d min, %d sec", 
				hours,
				mins - TimeUnit.HOURS.toMinutes(hours),
			    secs - TimeUnit.MINUTES.toSeconds(mins)
			);
		} else if (mins > 0) {
			return String.format("%d min, %d sec", 
			    mins,
			    secs - 
			    TimeUnit.MINUTES.toSeconds(mins)
			);
		} else {
			return String.format("%d sec", 
				    secs - 
				    TimeUnit.MINUTES.toSeconds(mins)
				);
		}
	}
//
//	public static Font loadFont(TLZFontEnum tlzf) {
//		try {
//			InputStream is = TLZUtils.class.getResourceAsStream(tlzf.getFontName());
//			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//			return font;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (FontFormatException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	public static boolean sameObjects( Object o1, Object o2) {
		if ( o1 == null && o2 == null 
				|| o1 != null && o2 != null && o1.equals( o2 ) )
			return true;

		return false;
	}
	public static int getCurrentWeek() {
		Calendar c = Calendar.getInstance();
	    int week = c.get(Calendar.WEEK_OF_YEAR);
	    return week;
	}
	public static int getCurrentYear() {
		Calendar c = Calendar.getInstance();
	    int week = c.get(Calendar.YEAR);
	    return week;
	}

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
}
