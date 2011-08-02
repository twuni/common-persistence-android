package org.twuni.common.persistence.android;

import org.twuni.common.persistence.Transaction;
import org.twuni.common.persistence.exception.RollbackException;

import android.database.sqlite.SQLiteDatabase;

public class Connection implements org.twuni.common.persistence.Connection {

	private final SQLiteDatabase database;

	public Connection( SQLiteDatabase database ) {
		this.database = database;
	}

	@Override
	public void run( Transaction transaction ) {
		database.beginTransaction();
		try {
			transaction.perform( new Session( database ) );
			database.setTransactionSuccessful();
		} catch( Exception exception ) {
			throw new RollbackException( exception );
		} finally {
			database.endTransaction();
		}
	}

}
