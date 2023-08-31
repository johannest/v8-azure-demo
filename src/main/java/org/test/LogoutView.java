package org.test;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LogoutView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        View.super.enter(event);

        UI ui = event.getNavigator().getUI();
        ui.getSession().getSession().invalidate();
        ui.getPage().setLocation("/logout");
    }
}
