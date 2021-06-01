/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2010 - 2021 Fiji developers.
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
package fiji.plugin.trackmate.util;

import static fiji.plugin.trackmate.gui.Fonts.SMALL_FONT;

import java.awt.Color;

import javax.swing.JLabel;

import fiji.plugin.trackmate.Logger;

public class JLabelLogger extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private final MyLogger logger;

	public JLabelLogger() {
		this.logger = new MyLogger(this);
		setFont( SMALL_FONT );
	}
	
	public Logger getLogger() {
		return logger;
	}

	
	
	
	/*
	 * INNER CLASS
	 */
	
	private class MyLogger extends Logger {

		private final JLabelLogger label;

		public MyLogger(final JLabelLogger logger) {
			this.label = logger;
		}

		@Override
		public void log(final String message, final Color color) {
			label.setText(message);
			label.setForeground(color);
		}

		@Override
		public void error(final String message) {
			log(message, Logger.ERROR_COLOR);
		}

		/** Ignored. */
		@Override
		public void setProgress(final double val) {}

		@Override
		public void setStatus(final String status) {
			log(status, Logger.BLUE_COLOR);
		}
		
	}
}
