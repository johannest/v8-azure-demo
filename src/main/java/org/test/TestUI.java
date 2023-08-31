package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@Theme("mytheme")
@SpringUI
public class TestUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this, this);
        navigator.addView("", MainView.class);
        navigator.addView("ssologout", LogoutView.class);
	}
}
