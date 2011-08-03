package org.twuni.common.persistence.android;

import java.util.List;

import org.twuni.common.persistence.Migration;
import org.twuni.common.persistence.Transaction;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private final Migration [] history;
	private final int version;

	public Database( Context context, String name, int version, Migration... history ) {
		super( context, name, null, version );
		this.version = version;
		this.history = history;
	}

	public Connection getReadableConnection() {
		try {
			return getWritableConnection();
		} catch( SQLException exception ) {
			return new Connection( getReadableDatabase() );
		}
	}

	public Connection getWritableConnection() {
		return new Connection( getWritableDatabase() );
	}

	@Override
	public void onCreate( SQLiteDatabase database ) {
		Connection connection = new Connection( database );
		for( Migration migration : history ) {
			if( migration.getSequence() <= version ) {
				connection.run( migration.forward() );
			}
		}
	}

	@Override
	public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {
		List<Transaction> transactions = Migration.migrate( oldVersion, newVersion, history );
		if( transactions.isEmpty() ) {
			return;
		}
		Connection connection = new Connection( database );
		for( Transaction transaction : transactions ) {
			connection.run( transaction );
		}
	}

}
