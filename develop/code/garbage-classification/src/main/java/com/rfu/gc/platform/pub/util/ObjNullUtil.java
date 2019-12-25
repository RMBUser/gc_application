package com.rfu.gc.platform.pub.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class ObjNullUtil {
	
	public static boolean emptyOrNull(String source) {
		return source == null || source.trim().isEmpty();
	}
	
	public static boolean emptyOrNullAnd(String... source) {
		return Arrays.stream(source).allMatch(ObjNullUtil::emptyOrNull);
	}
	
	public static boolean emptyOrNullOr(String... source) {
		return Arrays.stream(source).anyMatch(ObjNullUtil::emptyOrNull);
	}
	
	public static boolean noEmptyOrNull(String source) {
		return source != null && !source.trim().isEmpty();
	}
	
	public static boolean noEmptyOrNullAnd(String... source) {
		return Arrays.stream(source).allMatch(ObjNullUtil::noEmptyOrNull);
	}
	
	public static boolean noEmptyOrNullOr(String... source) {
		return Arrays.stream(source).anyMatch(ObjNullUtil::noEmptyOrNull);
	}
	
	public static boolean emptyOrNull(Collection<?> source) {
		return source == null || source.isEmpty();
	}
	
	public static boolean noEmptyOrNull(Collection<?> source) {
		return source != null && !source.isEmpty();
	}
	
	public static boolean emptyOrNull(Map<?,?> source) {
		return source == null || source.isEmpty();
	}
	
	public static boolean noEmptyOrNull(Map<?,?> source) {
		return source != null && !source.isEmpty();
	}
}
