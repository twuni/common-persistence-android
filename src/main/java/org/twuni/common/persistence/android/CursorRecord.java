package org.twuni.common.persistence.android;

import org.twuni.common.persistence.Record;

import android.database.Cursor;

public class CursorRecord implements Record {

	private final Cursor cursor;

	public CursorRecord( Cursor cursor ) {
		this.cursor = cursor;
	}

	@Override
	public String getString( String key ) {
		return cursor.getString( cursor.getColumnIndex( key ) );
	}

	@Override
	public byte getByte( String key ) {
		return (byte) ( 0xFF & getShort( key ) );
	}

	@Override
	public int getInt( String key ) {
		return cursor.getInt( cursor.getColumnIndex( key ) );
	}

	@Override
	public short getShort( String key ) {
		return cursor.getShort( cursor.getColumnIndex( key ) );
	}

	@Override
	public long getLong( String key ) {
		return cursor.getLong( cursor.getColumnIndex( key ) );
	}

	@Override
	public double getDouble( String key ) {
		return cursor.getDouble( cursor.getColumnIndex( key ) );
	}

	@Override
	public float getFloat( String key ) {
		return cursor.getFloat( cursor.getColumnIndex( key ) );
	}

	@Override
	public byte [] getBytes( String key ) {
		return cursor.getBlob( cursor.getColumnIndex( key ) );
	}

}
