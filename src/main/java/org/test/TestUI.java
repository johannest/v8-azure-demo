package org.test;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
@SpringUI
public class TestUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		CssLayout root = new CssLayout();
		root.addStyleName("flexbox");
		root.setSizeFull();

        Grid<SamplePerson> table = new Grid<>(SamplePerson.class);
        table.setItems(createPersons());
        table.setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(false);
        layout.addComponents(table);

        CssLayout wrapper = new CssLayout(layout);
        wrapper.addStyleName("flexbox");
        wrapper.addStyleName("flexgrow1");
        wrapper.setWidth("100%");

        CssLayout cssLayout = new CssLayout();
        cssLayout.addStyleName("flexbox");
        cssLayout.addStyleName("flexgrow1");
        cssLayout.addStyleName("gray");
        cssLayout.setWidth("100%");
        cssLayout.addComponent(wrapper);

        layout.setExpandRatio(table, 1);
        root.addComponent(cssLayout);
 
        setSizeFull();
		setContent(root);
	}

    private List<SamplePerson> createPersons() {
        List<SamplePerson> people = new ArrayList<>();
        for (int i=0; i<50; i++) {
            people.add(new SamplePerson("Person_"+i, "Foo"+i, "test"+i+"@gmail.com"));
        }
        return people;
    }
}
