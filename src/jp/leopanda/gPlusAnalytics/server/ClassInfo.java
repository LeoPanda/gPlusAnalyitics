package jp.leopanda.gPlusAnalytics.server;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * データオブジェクトのクラス情報を取得するメソッドを提供する
 * 
 * @author LeoPanda
 *
 */
public class ClassInfo {
	/**
	 * データオブジェクトのクラスからGoogle+ APIのFieldパラメータを作成する
	 * 
	 * @author LeoPanda
	 *
	 */
	public static String getFiledNames(Class<?> klazz) {
		String fieldNames = "";
		for (Field field : klazz.getFields()) {
			Class<?> type = getFieldType(field);
			if (!fieldNames.isEmpty())
				fieldNames += ",";
			fieldNames += field.getName();
			if (type.getName().contains("jp.leopanda")) {//ユーザ作成クラスの場合はネストする
				fieldNames += "(" + getFiledNames(type) + ")";
			}
		}
		return fieldNames;
	}
	/**
	 * フィールドのクラスを同定する
	 */
	private static Class<?> getFieldType(Field field) {
		Class<?> type = arraiedFieldFilter(field.getType());
		if (type.equals(List.class)) {// Listクラスの場合は総称クラスを獲得する
			ParameterizedType paraType = (ParameterizedType) field
					.getGenericType();
			return (Class<?>) paraType.getActualTypeArguments()[0];
		} else {
			return type;
		}
	}
	/**
	 * クラスが配列の場合の処理
	 */
	private static Class<?> arraiedFieldFilter(Class<?> type) {
		if (type.isArray()) {
			Class<?> nextType = type.getComponentType();
			return arraiedFieldFilter(nextType);
		} else {
			return type;
		}
	}
}
