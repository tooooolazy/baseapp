package com.tooooolazy.vaadin.components;

import com.vaadin.ui.ComboBox;

public class WeekComboBox extends ComboBox<Integer> {

	public WeekComboBox(String caption) {
		super(caption);
		
	}
	public void attach() {
		super.attach();
		Integer[] weeks = new Integer[52];
 		for (int w=1; w<=52; w++)
 			weeks[w-1] = w;
		setItems(weeks);

	}
}
