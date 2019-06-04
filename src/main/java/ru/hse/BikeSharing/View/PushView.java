package ru.hse.BikeSharing.View;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.BikeSharing.Services.NotificationService;


@Route(value = "pushs", layout = MainLayout.class)
public class PushView extends VerticalLayout {

    @Autowired
    NotificationService service;

    TextField userIDField = new TextField("User ID", "User ID");
    TextField textField = new TextField("Текст", "Test", "Текст пуша");

    Button sendButton = new Button("Отправить");

    Label label = new Label();

    public PushView() {
        add(userIDField, textField, sendButton, label);

        sendButton.addClickListener(e -> send());
    }

    public void send() {
        try {
            if (!userIDField.getValue().isEmpty()) {
                service.sendPush(Long.parseLong(userIDField.getValue()), textField.getValue());
            } else {
                service.sendPush(null, textField.getValue());
            }
            label.setText("Успех");
        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }
}
