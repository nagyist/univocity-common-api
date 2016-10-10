/*
 * Copyright (c) 2013 uniVocity Software Pty Ltd. All rights reserved.
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE.txt', which is part of this source code package.
 */

package com.univocity.api.common;

import java.io.*;
import java.util.*;

import static java.sql.Connection.*;

/**
 * Utility class used to validate arguments and configuration options passed to objects in uniVocity's API.
 *
 * @author uniVocity Software Pty Ltd - <a href="mailto:dev@univocity.com">dev@univocity.com</a>
 */
public class Args {
	protected Args() {

	}

	public static void notNull(Object o, String fieldName) {
		if (o == null) {
			throw new IllegalArgumentException(fieldName + " cannot be null");
		}
	}

	public static void positive(Integer o, String fieldName) {
		notNull(o, fieldName);
		if (o.compareTo(0) <= 0) {
			throw new IllegalArgumentException(fieldName + " must be positive. Got " + o);
		}
	}

	public static void positiveOrZero(Integer o, String fieldName) {
		notNull(o, fieldName);
		if (o.compareTo(0) < 0) {
			throw new IllegalArgumentException(fieldName + " must be a positive number or zero. Got " + o);
		}
	}

	public static <T> void notEmpty(T[] sequence, String fieldName) {
		notNull(sequence, fieldName);
		if (sequence.length == 0) {
			throw new IllegalArgumentException(fieldName + " cannot be empty");
		}
		for (T element : sequence) {
			if (element == null) {
				throw new IllegalArgumentException("Illegal " + fieldName + " list. Null elements are not allowed. Got " + Arrays.toString(sequence));
			} else if (element instanceof String && element.toString().trim().isEmpty()) {
				throw new IllegalArgumentException("Illegal " + fieldName + " list. Blank elements are not allowed. Got " + Arrays.toString(sequence));
			}
		}
	}

	public static <T> void notEmpty(Collection<T> elements, String fieldName) {
		notNull(elements, fieldName);
		if (elements.isEmpty()) {
			throw new IllegalArgumentException(fieldName + " cannot be empty");
		}
		for (T element : elements) {
			if (element == null) {
				throw new IllegalArgumentException("Illegal " + fieldName + " list. Null elements are not allowed. Got " + elements);
			} else if (element instanceof String && element.toString().trim().isEmpty()) {
				throw new IllegalArgumentException("Illegal " + fieldName + " list. Blank elements are not allowed. Got " + elements);
			}
		}
	}

	public static <T> void notEmpty(int[] field, String fieldName) {
		notNull(field, fieldName);
		if (field.length == 0) {
			throw new IllegalArgumentException(fieldName + " cannot be empty");
		}
	}

	public static void notEmpty(char[] field, String fieldName) {
		notNull(field, fieldName);
		if (field.length == 0) {
			throw new IllegalArgumentException(fieldName + " cannot be empty");
		}
	}

	public static void notEmpty(CharSequence o, String fieldName) {
		notNull(o, fieldName);
		if (o.length() == 0) {
			throw new IllegalArgumentException(fieldName + " cannot be empty");
		}
	}

	public static void notBlank(CharSequence o, String fieldName) {
		notNull(o, fieldName);
		if (o.toString().trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " cannot be blank");
		}
	}

	public static void validFile(File file, String fieldName) {
		notNull(file, fieldName);
		if (!file.exists()) {
			throw new IllegalArgumentException("Illegal " + fieldName + ": '" + file.getAbsolutePath() + "' it does not exist.");
		}
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Illegal " + fieldName + ": '" + file.getAbsolutePath() + "' it cannot be a directory.");
		}
	}

	public static void validTransactionIsolationLevel(int transactionIsolationLevel) {
		List<Integer> levels = Arrays.asList(TRANSACTION_NONE, TRANSACTION_READ_COMMITTED, TRANSACTION_READ_UNCOMMITTED, TRANSACTION_REPEATABLE_READ, TRANSACTION_SERIALIZABLE);
		if (!levels.contains(transactionIsolationLevel)) {
			throw new IllegalArgumentException("Illegal transaction isolation level: " + transactionIsolationLevel + ". Accepted isolation levels are: " + levels + " (from java.sql.Connection)");
		}
	}

	public static String guessAndValidateName(String name, File file, String fieldName) {
		if (name != null) {
			notBlank(name, fieldName);
			return name;
		}
		validFile(file, fieldName);

		name = file.getName();
		if (name.lastIndexOf('.') != -1) {
			name = name.substring(0, name.lastIndexOf('.'));
		}

		if (name.trim().isEmpty()) {
			throw new IllegalArgumentException("Cannot derive " + fieldName + " from file " + file.getAbsolutePath());
		}

		return name;
	}

	public static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

	public static String replaceSystemProperties(String filePath) {
		int offset = 0;
		int braceOpen = 0;
		while (braceOpen >= 0) {
			braceOpen = filePath.indexOf('{', offset);
			if (braceOpen >= 0) {
				offset = braceOpen;
				int braceClose = filePath.indexOf('}');
				if (braceClose > braceOpen) {
					offset = braceClose;
					String property = filePath.substring(braceOpen + 1, braceClose);
					String value = System.getProperty(property);
					if (value != null) {
						String beforeProperty = filePath.substring(0, braceOpen);
						String afterProperty = "";
						if (braceClose < filePath.length()) {
							afterProperty = filePath.substring(braceClose + 1, filePath.length());
						}
						filePath = beforeProperty + value + afterProperty;
					}
				}

			} else {
				break;
			}
		}
		return filePath;
	}
}
