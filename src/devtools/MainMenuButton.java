package devtools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.*;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Tools", id = "devtools.MainMenuButton")
@ActionRegistration(iconBase = "res/ico16.png", displayName = "DevTools")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 0, name = "DevTools"),
    @ActionReference(path = "Shortcuts", name = "F12")
})
@Messages("CTL_MainMenuButton=DevTools")
public final class MainMenuButton implements ActionListener {

    public MainMenuButton() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            WebKit webkit = new WebKit();
            webkit.open();
            webkit.requestActive();
        } catch (Throwable ex) {
            PopupBuilder.onError(ex);
        }

    }

}
