package org.twuni.common.persistence.android;

import java.util.ArrayList;
import java.util.List;

import org.twuni.common.Adapter;
import org.twuni.common.persistence.Parameters;
import org.twuni.common.persistence.Record;
import org.twuni.common.persistence.exception.NonUniqueObjectException;
import org.twuni.common.persistence.exception.ObjectNotFoundException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Session implements org.twuni.common.persistence.Session {

	private final SQLiteDatabase database;

	public Session( SQLiteDatabase database ) {
		this.database = database;
	}

	@Override
	public <T> List<T> query( String sql, Parameters parameters, Adapter<Record, T> adapter, int limit ) {
		ParameterizedList list = new ParameterizedList();
		parameters.apply( list );
		Cursor cursor = database.rawQuery( sql, list.toArray( new String [0] ) );
		List<T> results = new ArrayList<T>();
		for( int i = 0; cursor.moveToNext() && i < limit; i++ ) {
			results.add( adapter.adapt( new CursorRecord( cursor ) ) );
		}
		return results;
	}

	@Override
	public <T> List<T> query( String sql, Parameters parameters, Adapter<Record, T> adapter ) {
		return query( sql, parameters, adapter, Integer.MAX_VALUE );
	}

	@Override
	public <T> List<T> query( String sql, Adapter<Record, T> adapter, int limit ) {
		return query( sql, Parameters.NONE, adapter, limit );
	}

	@Override
	public <T> List<T> query( String sql, Adapter<Record, T> adapter ) {
		return query( sql, adapter, Integer.MAX_VALUE );
	}

	@Override
	public void query( String sql, Parameters parameters ) {
		ParameterizedList list = new ParameterizedList();
		parameters.apply( list );
		database.execSQL( sql, list.toArray() );
	}

	@Override
	public void query( String sql ) {
		query( sql, Parameters.NONE );
	}

	@Override
	public <T> T unique( String sql, Parameters parameters, Adapter<Record, T> adapter ) {
		List<T> results = query( sql, parameters, adapter, 2 );
		if( results.isEmpty() ) {
			throw new ObjectNotFoundException( sql );
		}
		if( results.size() > 1 ) {
			throw new NonUniqueObjectException();
		}
		return results.get( 0 );
	}

	@Override
	public <T> T unique( String sql, Adapter<Record, T> adapter ) {
		return unique( sql, Parameters.NONE, adapter );
	}

}
