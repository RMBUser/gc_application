package com.rfu.gc.platform.pub.extend;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.rfu.gc.platform.pub.util.ReflectUtil;

/**
 * Use on source model's field to mapping another model's field.<br/>
 * It can apply to {@link ReflectUtil}.
 * @see ReflectUtil
 * @author Cris
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface FieldMapping {
	
	/**
	 * Use to match another model's field.<br/>
	 * If the mappingName field is not fond in the target model,it will skip mapping this field.<br/>
	 * If you want to skip any field,it's a good idea to sign it with the empty string mappingName("")
	 * @return String[]
	 */
	String[] mappingName();
}
