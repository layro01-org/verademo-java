package com.veracode.verademo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Constants {
	private final String JDBC_DRIVER = "mysql";
	private final String JDBC_HOSTNAME = "localhost";
	private final String JDBC_PORT = "3306";
	private final String JDBC_DATABASE = "blab";
	private final String JDBC_USER = "blab";

	private String hostname;
	private String port;
	private String dbname;
	private String username;
	private String password;

	/**
	 * Pull info from environment variables or system properties as an override,
	 * otherwise fall back to hardcoded (non-secret) defaults.
	 *
	 * Supported keys (checked in this order):
	 * - BLAB_DB_* (local/dev)
	 * - RDS_* (AWS-style)
	 */
	public Constants() {
		this.dbname = firstNonEmpty(
				System.getenv("BLAB_DB_NAME"),
				System.getenv("RDS_DB_NAME"),
				System.getProperty("BLAB_DB_NAME"),
				System.getProperty("RDS_DB_NAME"),
				JDBC_DATABASE
		);

		this.hostname = firstNonEmpty(
				System.getenv("BLAB_DB_HOSTNAME"),
				System.getenv("RDS_HOSTNAME"),
				System.getProperty("BLAB_DB_HOSTNAME"),
				System.getProperty("RDS_HOSTNAME"),
				JDBC_HOSTNAME
		);

		this.port = firstNonEmpty(
				System.getenv("BLAB_DB_PORT"),
				System.getenv("RDS_PORT"),
				System.getProperty("BLAB_DB_PORT"),
				System.getProperty("RDS_PORT"),
				JDBC_PORT
		);

		this.username = firstNonEmpty(
				System.getenv("BLAB_DB_USERNAME"),
				System.getenv("RDS_USERNAME"),
				System.getProperty("BLAB_DB_USERNAME"),
				System.getProperty("RDS_USERNAME"),
				JDBC_USER
		);

		this.password = firstNonEmpty(
				System.getenv("BLAB_DB_PASSWORD"),
				System.getenv("RDS_PASSWORD"),
				System.getProperty("BLAB_DB_PASSWORD"),
				System.getProperty("RDS_PASSWORD"),
				""
		);
	}

	public static final Constants create() {
		return new Constants();
	}

	public final String getJdbcConnectionString() {
		if (password == null) {
			throw new IllegalStateException(
					"Database password is not configured. Set BLAB_DB_PASSWORD (or RDS_PASSWORD)."
			);
		}

		String connection = null;
		try {
			connection = String.format(
					"jdbc:%s://%s:%s/%s?user=%s&password=%s",
					JDBC_DRIVER,
					hostname,
					port,
					dbname,
					URLEncoder.encode(username, "UTF-8"),
					URLEncoder.encode(password, "UTF-8")
			);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return connection;
	}

	private static String firstNonEmpty(String... values) {
		if (values == null) {
			return null;
		}

		for (String value : values) {
			if (value != null && !value.trim().isEmpty()) {
				return value;
			}
		}

		return null;
	}
}
