package com.templarknightsmc.tkmccontacts;

import android.provider.BaseColumns;

public class TkmcDbContract {
	private TkmcDbContract() {}
	
	public static abstract class TkmcDbContacts implements BaseColumns {
		public static final String TABLE_NAME = "contacts";
		public static final String COLUMN_NAME_FIRST_NAME = "firstname";
		public static final String COLUMN_NAME_LAST_NAME = "lastname";
		public static final String COLUMN_NAME_NICKNAME = "nickname";
		public static final String COLUMN_NAME_RANK = "rank";
		public static final String COLUMN_NAME_PHONE = "phone";
		public static final String COLUMN_NAME_EMAIL = "email";
		public static final String COLUMN_NAME_LODGE = "lodge";
		public static final String COLUMN_NAME_RECORD_NUM = "recordnum";
	}

	public static abstract class TkmcDbLodges implements BaseColumns {
		public static final String TABLE_NAME = "lodges";
		public static final String COLUMN_NAME_LODGE_NUM = "lodgenum";
		public static final String COLUMN_NAME_LODGE_NAME = "lodgename";
	}
	
	public static abstract class TkmcDbVersions implements BaseColumns {
		public static final String TABLE_NAME = "versions";
		public static final String COLUMN_NAME_VERSION_NUM = "versionnum";
		public static final String COLUMN_NAME_VERSION_TYPE = "versiontype";
		public static final String COLUMN_NAME_APP_NAME = "appname";
	}

}
