package org.twuni.common.persistence.android;

import java.util.ArrayList;

import org.twuni.common.persistence.Parameterized;

public class ParameterizedList extends ArrayList<String> implements Parameterized {

	@Override
	public void setParameter( int index, String value ) {
		add( index, value );
	}

	@Override
	public void setParameter( int index, int value ) {
		add( index, Integer.valueOf( value ).toString() );
	}

	@Override
	public void setParameter( int index, double value ) {
		add( index, Double.valueOf( value ).toString() );
	}

	@Override
	public void setParameter( int index, float value ) {
		add( index, Float.valueOf( value ).toString() );
	}

	@Override
	public void setParameter( int index, boolean value ) {
		add( index, Boolean.valueOf( value ).toString() );
	}

	@Override
	public void setParameter( int index, long value ) {
		add( index, Long.valueOf( value ).toString() );
	}

	@Override
	public void setParameter( int index, short value ) {
		add( index, Short.valueOf( value ).toString() );
	}

}
