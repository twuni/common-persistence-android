package org.twuni.common.persistence.android;

import org.twuni.common.persistence.Migration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private final Migration [] history;

	public Database( Context context, String name, int version, Migration... history ) {
		super( context, name, null, version );
		this.history = history;
	}

	public Connection getReadableConnection() {
		return new Connection( getReadableDatabase() );
	}

	public Connection getWritableConnection() {
		return new Connection( getWritableDatabase() );
	}

	@Override
	public void onCreate( SQLiteDatabase database ) {
		Connection connection = new Connection( database );
		for( Migration migration : history ) {
			if( migration.getSequence() <= database.getVersion() ) {
				connection.run( migration.forward() );
			}
		}
	}

	@Override
	public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {

		if( oldVersion == newVersion ) {
			return;
		}

		Connection connection = new Connection( database );

		if( oldVersion < newVersion ) {

			for( Migration migration : history ) {

				int sequence = migration.getSequence();

				if( oldVersion < sequence && sequence <= newVersion ) {
					connection.run( migration.forward() );
				}

			}

		} else if( oldVersion > newVersion ) {

			for( int i = 0; i < history.length; i++ ) {

				Migration migration = history[history.length - 1 - i];

				int sequence = migration.getSequence();

				if( newVersion < sequence && sequence <= oldVersion ) {
					connection.run( migration.reverse() );
				}

			}

		}

	}

}
