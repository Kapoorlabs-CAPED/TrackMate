/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2010 - 2022 Fiji developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji.plugin.trackmate.features.track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.scijava.plugin.Plugin;

import fiji.plugin.trackmate.Dimension;
import fiji.plugin.trackmate.FeatureModel;
import fiji.plugin.trackmate.Model;

@Plugin( type = TrackAnalyzer.class )
public class TrackIndexAnalyzer implements TrackAnalyzer
{

	/** The key for this analyzer. */
	public static final String KEY = "Track index";

	/** The key for the feature TRACK_INDEX */

	public static final String TRACK_INDEX = "TRACK_INDEX";
	public static final String TRACK_ID = "TRACK_ID";
	public static final List< String > FEATURES = new ArrayList< >( 2 );
	public static final Map< String, String > FEATURE_NAMES = new HashMap< >( 2 );
	public static final Map< String, String > FEATURE_SHORT_NAMES = new HashMap< >( 2 );
	public static final Map< String, Dimension > FEATURE_DIMENSIONS = new HashMap< >( 2 );
	public static final Map< String, Boolean > IS_INT = new HashMap< >( 2 );

	static
	{
		FEATURES.add( TRACK_INDEX );
		FEATURES.add( TRACK_ID );

		FEATURE_NAMES.put( TRACK_INDEX, "Track index" );
		FEATURE_NAMES.put( TRACK_ID, "Track ID" );

		FEATURE_SHORT_NAMES.put( TRACK_INDEX, "Index" );
		FEATURE_SHORT_NAMES.put( TRACK_ID, "ID" );

		FEATURE_DIMENSIONS.put( TRACK_INDEX, Dimension.NONE );
		FEATURE_DIMENSIONS.put( TRACK_ID, Dimension.NONE );

		IS_INT.put( TRACK_INDEX, Boolean.TRUE );
		IS_INT.put( TRACK_ID, Boolean.TRUE );
	}

	private long processingTime;

	/**
	 * {@link TrackIndexAnalyzer} is not local, since the indices are
	 * re-arranged according to names.
	 */
	@Override
	public boolean isLocal()
	{
		return false;
	}

	@Override
	public void process( final Collection< Integer > trackIDs, final Model model )
	{
		final long start = System.currentTimeMillis();
		final FeatureModel fm = model.getFeatureModel();
		int index = 0;
		for ( final Integer trackID : trackIDs )
		{
			fm.putTrackFeature( trackID, TRACK_INDEX, Double.valueOf( index++ ) );
			fm.putTrackFeature( trackID, TRACK_ID, Double.valueOf( trackID ) );
		}
		final long end = System.currentTimeMillis();
		processingTime = end - start;
	}

	@Override
	public long getProcessingTime()
	{
		return processingTime;
	}

	@Override
	public String getKey()
	{
		return KEY;
	}

	@Override
	public List< String > getFeatures()
	{
		return FEATURES;
	}

	@Override
	public Map< String, String > getFeatureShortNames()
	{
		return FEATURE_SHORT_NAMES;
	}

	@Override
	public Map< String, String > getFeatureNames()
	{
		return FEATURE_NAMES;
	}

	@Override
	public Map< String, Dimension > getFeatureDimensions()
	{
		return FEATURE_DIMENSIONS;
	}

	/**
	 * Ignored. This analyzer is single-threaded.
	 */
	@Override
	public void setNumThreads()
	{}

	/**
	 * Ignored. This analyzer is single-threaded.
	 */
	@Override
	public void setNumThreads( final int numThreads )
	{}

	/**
	 * Ignore. This analyzer is single-threaded.
	 */
	@Override
	public int getNumThreads()
	{
		return 1;
	}

	@Override
	public String getInfoText()
	{
		return null;
	}

	@Override
	public ImageIcon getIcon()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return KEY;
	}

	@Override
	public Map< String, Boolean > getIsIntFeature()
	{
		return IS_INT;
	}

	@Override
	public boolean isManualFeature()
	{
		return false;
	}
}
