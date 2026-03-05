package com.easyplan.shared.util;

import java.util.Objects;

public final class Require {
	public static <T> T require(T value, String name) {
		return Objects.requireNonNull(value, name + " must not be null");
	}
}
