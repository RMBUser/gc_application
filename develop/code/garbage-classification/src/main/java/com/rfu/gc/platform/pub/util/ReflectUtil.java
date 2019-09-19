package com.rfu.gc.platform.pub.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.rfu.gc.platform.pub.extend.FieldMapping;

/**
 * ReflectUtil will do some field mapping job and so on.
 * 
 * @author Cris
 *
 */
public class ReflectUtil {

	/**
	 * Copy the field value,which's name and type are same,from "U" instance to "T"
	 * instance.</br>
	 * "U" should be a customer entity type and "T" will also be.Method will return
	 * an instance of "T" after copying the same field from the instance of
	 * "U".</br>
	 * <strong>ATTENTION:</strong></br>
	 * 1.parameter flag is a switch of the {@link FieldMapping}.</br>
	 * By the way,which field in "U" is marked with {@link FieldMapping},will match
	 * the field named mappingName in the tClass. 2.tClazz must have a non-parameter
	 * constructor.Or it will throw RuntimeException.</br>
	 * 
	 * @param <U>
	 * @param <T>
	 * @param u
	 * @param tClazz
	 * @param flag
	 * @return
	 */
	public static <U, T> T copyU2T(U u, Class<T> tClazz, boolean flag) {
		if (u == null || tClazz == null)
			throw new IllegalArgumentException("source instance u of " + u.getClass().getName() + " or target class:"
					+ tClazz.getName() + " can not be null");
		T t = null;
		try {
			t = tClazz.newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException("target class:" + tClazz.getName() + " without a non-parameter constructor.");
		}
		Class<?> uClazz = u.getClass();
		Field[] uFields = uClazz.getDeclaredFields();
		for (Field uField : uFields) {
			if (flag == true && uField.isAnnotationPresent(FieldMapping.class)) {
				String[] tFieldNames = uField.getAnnotation(FieldMapping.class).mappingName();
				if (tFieldNames == null)
					continue;
				for (String tFieldName : tFieldNames) {
					copyValue2T(t, tFieldName, getValueFromU(u, uField.getName()));
				}
			} else {
				copyValue2T(t, uField.getName(), getValueFromU(u, uField.getName()));
			}
		}
		return t;
	}

	/**
	 * Copy the field value,which's name and type are same,from "U" instance to "T"
	 * instance.</br>
	 * "U" should be a customer entity type and "T" will also be.
	 * <strong>ATTENTION:</strong></br>
	 * parameter "flag" is a switch of the {@link FieldMapping}.</br>
	 * By the way,which field in "U" is marked with {@link FieldMapping},will match
	 * the field named mappingName in the tClass.</br>
	 * <strong>TIPS:</strong></br>
	 * if a field of u is null,but it matches a no null field of t,the value of this
	 * t's field won't be replaced.
	 * 
	 * @param <U>
	 * @param <T>
	 * @param u
	 * @param t
	 * @param flag
	 */
	public static <U, T> void copyU2T(U u, T t, boolean flag) {
		if (u == null || t == null)
			throw new IllegalArgumentException("source instance or target instance must no be null");
		Field[] uFields = u.getClass().getDeclaredFields();
		for (Field uField : uFields) {
			if (flag == true && uField.isAnnotationPresent(FieldMapping.class)) {
				String[] tFieldNames = uField.getAnnotation(FieldMapping.class).mappingName();
				if (tFieldNames == null)
					continue;
				Arrays.stream(tFieldNames)
						.forEach((tFieldName) -> copyValue2T(t, tFieldName, getValueFromU(u, uField.getName())));
			} else {
				copyValue2T(t, uField.getName(), getValueFromU(u, uField.getName()));
			}
		}
	}

	/**
	 * Change a map into an entity.This method will match each element with the
	 * field in "T" entity,then do the mapping job.</br>
	 * "T" should be a customer entity type. </br>
	 * <strong>TIPS:</strong></br>
	 * If the element,matches an <strong>entity type field</strong>,is an instance
	 * of {@link Map} in the map,this method will try to change {@link Map} to
	 * <strong>entity type</strong>.
	 * 
	 * @param <T>
	 * @param map
	 * @param t
	 */
	@SuppressWarnings("unchecked")
	public static <T> void map2Entity(Map<String, ?> map, T t) {
		if (ObjNullUtil.emptyOrNull(map) || t == null)
			throw new IllegalArgumentException(
					"source map or " + t.getClass().getName() + " instance t can not be null or empty.");
		Class<?> targetClazz = t.getClass();
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value != null) {
				try {
					Field field = targetClazz.getDeclaredField(key);
					if (value instanceof Map && Map.class != field.getType()) {
						Object fieldValue = map2Entity((Map<String, ?>) value, field.getType());
						copyValue2T(t, key, fieldValue);
					} else {
						copyValue2T(t, key, value);
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
				}
			}
		}
	}

	/**
	 * Change a map into an entity.This method will match each element with the
	 * field in "T" entity,then do the mapping job.</br>
	 * "T" should be a customer entity type.The method will return an instance of
	 * "T"(tClazz).</br>
	 * <strong>ATTENTION:</strong></br>
	 * tClazz must have a non-parameter constructor.Or it will throw
	 * RuntimeException.</br>
	 * <strong>TIPS:</strong></br>
	 * If the element,matches an <strong>entity type field</strong>,is an instance
	 * of {@link Map} in the map,this method will try to change {@link Map} to
	 * <strong>entity type</strong>.
	 * 
	 * @param <T>
	 * @param map
	 * @param tClazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T map2Entity(Map<String, ?> map, Class<T> tClazz) {
		if (ObjNullUtil.emptyOrNull(map) || tClazz == null)
			throw new IllegalArgumentException(
					"source map or target class:" + tClazz.getName() + " can not be null or empty.");
		T target;
		try {
			target = tClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("target class:" + tClazz.getName() + " without a non-parameter constructor.");
		}
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value != null) {
				try {
					Field field = tClazz.getDeclaredField(key);
					if (value instanceof Map && Map.class != field.getType()) {
						Object fieldValue = map2Entity((Map<String, ?>) value, field.getType());
						copyValue2T(target, key, fieldValue);
					} else {
						copyValue2T(target, key, value);
					}
				} catch (NoSuchFieldException | SecurityException e) {
				}

			}
		}
		return target;
	}

	/**
	 * This method will change u into a map.Each field name will be the key of the
	 * map.</br>
	 * "U" should be a customer entity type.</br>
	 * <strong>ATTENTION:</strong></br>
	 * parameter "flag" is a switch of the {@link FieldMapping}.</br>
	 * By the way,which field in "U" is marked with {@link FieldMapping},will change
	 * the name to mappingName as a key.</br>
	 * <strong>TIPS:</strong></br>
	 * 1.If the parameter "map" is null,it will instantiate a {@link HashMap}
	 * instead.</br>
	 * 2.Null field of u is also put into the map</br>
	 * 
	 * @param <U>
	 * @param u
	 * @param map
	 * @return
	 */
	public static <U> Map<String, Object> entity2Map(U u, Map<String, Object> map, boolean flage) {
		if (u == null)
			throw new IllegalArgumentException("entity can not be null.");
		Class<?> sourceClazz = u.getClass();
		Field[] fields = sourceClazz.getDeclaredFields();
		if (ObjNullUtil.emptyOrNull(map))
			map = new HashMap<String, Object>();
		for (Field field : fields) {
			Object value = null;
			try {
				Method method = getGetter(sourceClazz, field.getName());
				value = method.invoke(u);
			} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				try {
					field.setAccessible(true);
					value = field.get(u);
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e1) {
				}
			}
			if (flage == true && field.isAnnotationPresent(FieldMapping.class)) {
				String[] keys = field.getAnnotation(FieldMapping.class).mappingName();
				for (String key : keys)
					map.put(key, value);

			} else {
				map.put(field.getName(), value);
			}
		}
		return map;
	}

	private static Method getGetter(Class<?> clazz, String fieldName)
			throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		String suffix = fieldName;
		if (fieldName.length() > 0) {
			char firstLetter = fieldName.charAt(0);
			if (firstLetter >= 'a' && firstLetter <= 'z')
				suffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			else if (firstLetter >= 'A' && firstLetter <= 'Z')
				suffix = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
		}
		String getter = "get" + suffix;
		return clazz.getMethod(getter);
	}

	private static Method getSetter(Class<?> clazz, String fieldName)
			throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		String suffix = fieldName;
		if (fieldName.length() > 0) {
			char firstLetter = fieldName.charAt(0);
			if (firstLetter >= 'a' && firstLetter <= 'z')
				suffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			else if (firstLetter >= 'A' && firstLetter <= 'Z')
				suffix = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
		}
		String setter = "set" + suffix;
		Field field = clazz.getDeclaredField(fieldName);
		return clazz.getMethod(setter, field.getType());
	}

	private static <U> Object getValueFromU(U u, String fieldName) {
		Class<?> clazz = u.getClass();
		Object obj = null;
		try {
			Method method = getGetter(clazz, fieldName);
			obj = method.invoke(u);
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				obj = field.get(u);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			}
		}
		return obj;
	}

	private static <T> Object copyValue2T(T t, String fieldName, Object value) {
		Class<?> tClazz = t.getClass();
		Object obj = null;
		if (value != null && ObjNullUtil.noEmptyOrNull(fieldName)) {
			try {
				Method setter = getSetter(tClazz, fieldName);
				obj = setter.invoke(t, value);
			} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				try {
					Field field = tClazz.getDeclaredField(fieldName);
					field.setAccessible(true);
					field.set(t, value);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e1) {
				}
			}
		}
		return obj;
	}
}
