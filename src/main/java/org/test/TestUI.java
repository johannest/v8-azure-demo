package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@Theme("mytheme")
@SpringUI
@PushStateNavigation
@SpringViewDisplay
public class TestUI extends UI {

    private final SpringViewProvider viewProvider;
    private final NavigationManager navigationManager;

    @Autowired
    public TestUI(SpringViewProvider viewProvider, NavigationManager navigationManager) {
        this.viewProvider = viewProvider;
        this.navigationManager = navigationManager;
    }

    @Override
	protected void init(VaadinRequest request) {
        navigationManager.navigateToDefaultView();
	}
}
