/*
 */
package devtools;

import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//devtools//devtools//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "devtoolsTopComponent",
        iconBase = "devtools/ico16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "devtools.devtoolsTopComponent")
@ActionReference(path = "Menu/Window" /* , position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_devtoolsAction",
        preferredID = "devtoolsTopComponent"
)
@Messages({
    "CTL_devtoolsAction=devtools",
    "CTL_devtoolsTopComponent=DevTools",
    "HINT_devtoolsTopComponent=Narzędzia deweloperskie"
})
public final class WebKit extends TopComponent {

    private final JFXPanel jfxPanel = new JFXPanel();

    private WebView browser;

    public WebKit() {
        initComponents();
        setName(Bundle.CTL_devtoolsTopComponent());
        setToolTipText(Bundle.HINT_devtoolsTopComponent());

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        this.setLayout(new BorderLayout());
        this.add(jfxPanel, BorderLayout.CENTER);

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                jfxPanel.setScene(Browse(WebKit.this.getBounds().width,
                        WebKit.this.getBounds().height));
            }
        });
    }

    @Override
    public void componentClosed() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                browser.getEngine().load(null);
            }
        });
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private Scene Browse(int width, int height) {

        browser = new WebView();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(browser);

        browser.getEngine().load("http://google.pl");

        return new Scene(borderPane, width, height);
    }
}
