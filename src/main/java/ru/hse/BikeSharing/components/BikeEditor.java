package ru.hse.BikeSharing.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import ru.hse.BikeSharing.Services.QRCodeGenerator;
import ru.hse.BikeSharing.domain.Bike;
import ru.hse.BikeSharing.repo.BikeRepo;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class BikeEditor extends VerticalLayout implements KeyNotifier {

    private final BikeRepo repository;

    /**
     * The currently edited bike
     */
    private Bike bike;

    /* Fields to edit properties in Customer entity */
    TextField name = new TextField("Bike name");
    TextField locationX = new TextField("Location X");
    TextField locationY = new TextField("Location Y");
    Image qrImage = new Image();

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    Button qrcode = new Button("QR Code");
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete, qrcode);
    VerticalLayout verticalLayout = new VerticalLayout(name, locationX, locationY);
    HorizontalLayout qrHorizontal = new HorizontalLayout(verticalLayout, qrImage);

    Binder<Bike> binder = new Binder<>(Bike.class);
    @Setter
    private ChangeHandler changeHandler;

    QRCodeGenerator qrCodeGenerator;

    @Autowired
    public BikeEditor(BikeRepo repository, QRCodeGenerator qrCodeGenerator) {
        this.repository = repository;
        this.qrCodeGenerator = qrCodeGenerator;

        add(qrHorizontal, actions);

        binder.bind(locationX, bike -> {
            if (bike.getLocation() != null) {
                return String.valueOf(bike.getLocation().getX());
            } else {
                return "";
            }
        }, (bike, title) -> {
            Point point;
            if (bike.getLocation() != null) {
                point = new Point(Double.parseDouble(title), bike.getLocation().getY());
            } else {
                point = new Point(Double.parseDouble(title), Double.parseDouble(title));
            }
            bike.setLocation(point);
        });

        binder.bind(locationY, bike -> {
            if (bike.getLocation() != null) {
                return String.valueOf(bike.getLocation().getY());
            } else {
                return "";
            }
        }, (bike, title) -> {
            Point point;
            if (bike.getLocation() != null) {
                point = new Point(bike.getLocation().getX(), Double.parseDouble(title));
            } else {
                point = new Point(Double.parseDouble(title), Double.parseDouble(title));
            }
            bike.setLocation(point);
        });
        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editBike(bike));

        qrcode.addClickListener(e -> setQrcode());
        qrImage.setWidth("200px");
        qrImage.setHeight("200px");

        setVisible(false);
    }

    void delete() {
        repository.delete(bike);
        changeHandler.onChange();
    }

    void save() {
        repository.save(bike);
        changeHandler.onChange();
    }

    void setQrcode() {
        try {
            bike.setQrcodeURL(qrCodeGenerator.generateQRCodeImage(bike.getId().toString(), 200, 200, "bike" + bike.getName()));
            repository.save(bike);
            //changeHandler.onChange();
            qrImage.setSrc(bike.getQrcodeURL());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editBike(Bike newBike) {
        if (newBike == null) {
            setVisible(false);
            return;
        }
        if (newBike.getId() != null) {
            bike = repository.findById(newBike.getId()).orElse(newBike);
        }
        else {
            bike = newBike;
        }

        if (bike.getQrcodeURL() != null) {
            qrImage.setSrc(bike.getQrcodeURL());
            qrImage.setSizeFull();
        } else {
            qrImage.setSrc("");
        }

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(bike);

        setVisible(true);

        // Focus first name initially
        name.focus();
    }

}
